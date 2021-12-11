package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private Integer orderId;
    private ArrayList<String> meals;
    private BigDecimal distance;
    private OrderMetadata orderMetadata;

}
