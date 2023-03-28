package com.pluralsight.demo.web;

import com.pluralsight.demo.model.AttendeeRegistration;
import com.pluralsight.demo.service.RegistrationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/")
public class RegistrationController {
    private static final Logger LOG = LoggerFactory.getLogger(RegistrationController.class);

//    private final MessageChannel registrationRequestChannel;

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

//    public RegistrationController(@Qualifier("registrationRequest") MessageChannel registrationRequestChannel) {
//        this.registrationRequestChannel = registrationRequestChannel;
//    }


    @GetMapping
    public String index(@ModelAttribute("registration") AttendeeRegistration registration) {
        return "index";
    }

    @PostMapping
    public String submit(@ModelAttribute("registration") @Valid AttendeeRegistration registration, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            LOG.warn("Validation failed: {}", bindingResult);
            return "index";
        }

//        var registrationMessage = MessageBuilder.withPayload(registration)
//            .setHeader("dateTime", OffsetDateTime.now())
//            .build();
//

        registrationService.commit(UUID.randomUUID().toString());
        return "success";
    }

}
