package models;

import enums.FoodType;
import enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderMetadata {

    private Status status;
    private Integer slotsRequired;
    private Map<FoodType, Integer> mealsQuantity;
    private BigDecimal estimatedFoodPrepTime;
    private BigDecimal estimatedDeliveryTime;
    private BigDecimal startTime;

}
