package sealed;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Sealed {
}

class Product {

}

class OrderService {

        public BigDecimal calculateTotalAmount(Order order) {
            BigDecimal total = BigDecimal.ZERO;
            // u can use pattern matching.

            for (OrderLine line : order.lines()) {
                if (line instanceof SaleOrderLine saleOrderLine) {
                    total = total.add(saleOrderLine.amount());
                } else if (line instanceof DiscountOrderLine discountOrderLine) {
                    total = total.subtract(discountOrderLine.amount());
                }
            }
            return total;
        }
}

sealed interface OrderLine permits SaleOrderLine, DiscountOrderLine {

}

record Order(long id, LocalDateTime dateTime, List<OrderLine> lines) {

}

record SaleOrderLine(Product product, int quantity, BigDecimal amount) implements OrderLine {

}

record DiscountOrderLine(String discountCode, BigDecimal amount) implements OrderLine {

}

