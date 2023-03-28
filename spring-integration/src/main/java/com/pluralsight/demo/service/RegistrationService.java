package com.pluralsight.demo.service;

import com.pluralsight.demo.config.SwagGateway;
import com.pluralsight.demo.database.*;
import com.pluralsight.demo.model.AttendeeRegistration;
import com.pluralsight.demo.model.Swag;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class RegistrationService {
    private static final Logger LOG = LoggerFactory.getLogger(RegistrationService.class);

    private final AttendeeTicketRepository attendeeTicketRepository;
    private final DiscountCodeRepository discountCodeRepository;
    private final PricingCategoryRepository pricingCategoryRepository;
    private final TicketPriceRepository ticketPriceRepository;
    private final TicketTypeRepository ticketTypeRepository;

    public RegistrationService(AttendeeTicketRepository attendeeTicketRepository,
                               DiscountCodeRepository discountCodeRepository,
                               PricingCategoryRepository pricingCategoryRepository,
                               TicketPriceRepository ticketPriceRepository,
                               TicketTypeRepository ticketTypeRepository) {
        this.attendeeTicketRepository = attendeeTicketRepository;
        this.discountCodeRepository = discountCodeRepository;
        this.pricingCategoryRepository = pricingCategoryRepository;
        this.ticketPriceRepository = ticketPriceRepository;
        this.ticketTypeRepository = ticketTypeRepository;
    }

    public void register(@Header("dateTime") OffsetDateTime dateTime,
                         @Payload AttendeeRegistration registration) {
        LOG.debug("Registration received for: {}", registration.getEmail());

        Attendee attendee = createAttendee(registration);
        TicketPrice ticketPrice = getTicketPrice(dateTime, registration);
        Optional<DiscountCode> discountCode = discountCodeRepository.findByCode(registration.getDiscountCode());

        AttendeeTicket attendeeTicket = new AttendeeTicket();
        attendeeTicket.setTicketCode(UUID.randomUUID().toString());
        attendeeTicket.setAttendee(attendee);
        attendeeTicket.setTicketPrice(ticketPrice);
        attendeeTicket.setDiscountCode(discountCode.orElse(null));
        attendeeTicket.setNetPrice(ticketPrice.getBasePrice().subtract(discountCode.map(DiscountCode::getAmount).orElse(BigDecimal.ZERO)));

        attendeeTicketRepository.save(attendeeTicket);
        LOG.debug("Registration saved, ticket code: {}", attendeeTicket.getTicketCode());
    }

    private Attendee createAttendee(AttendeeRegistration registration) {
        Attendee attendee = new Attendee();
        attendee.setFirstName(registration.getFirstName());
        attendee.setLastName(registration.getLastName());
        attendee.setEmail(registration.getEmail());
        attendee.setPhoneNumber(StringUtils.trimToNull(registration.getPhoneNumber()));
        attendee.setTitle(StringUtils.trimToNull(registration.getTitle()));
        attendee.setCompany(StringUtils.trimToNull(registration.getCompany()));
        return attendee;
    }

    private TicketPrice getTicketPrice(OffsetDateTime dateTime, AttendeeRegistration registration) {
        TicketType ticketType = ticketTypeRepository.findByCode(registration.getTicketType())
                .orElseThrow(() -> new IllegalArgumentException("Invalid ticket type: " + registration.getTicketType()));

        PricingCategory pricingCategory = pricingCategoryRepository.findByDate(dateTime.toLocalDate())
                .or(() -> pricingCategoryRepository.findByCode("L"))
                .orElseThrow(() -> new EntityNotFoundException("Cannot determine pricing category"));

        return ticketPriceRepository.findByTicketTypeAndPricingCategory(ticketType, pricingCategory)
                .orElseThrow(() -> new EntityNotFoundException("Cannot determine ticket price for ticket type '" + ticketType.getCode() + "' and pricing category '" + pricingCategory.getCode() + "'"));
    }

    @Autowired
    private SwagGateway swagGateway;

    public void commit(String userId) {
        LOG.debug("Committing registration for user: {}", userId);
        final Message<Swag> message = MessageBuilder.withPayload(new Swag("T-Shirt")).build();
        swagGateway.sendSwag(message);
    }
}
