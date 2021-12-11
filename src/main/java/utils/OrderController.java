package utils;

import enums.Status;
import models.Order;
import models.OrderMetadata;

import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderController {

    private final Map<String, BigDecimal> COOKING_SLOT_REQUIREMENTS;
    private final Map<String, BigDecimal> COOKING_TIME_REQUIREMENTS;

    private final BigDecimal DELIVERY_TIME_PER_KM;

    @Inject
    public OrderController(@Named("COOKING_SLOT_REQUIREMENTS") Map<String, BigDecimal> COOKING_SLOT_REQUIREMENTS,
                           @Named("COOKING_TIME_REQUIREMENTS") Map<String, BigDecimal> COOKING_TIME_REQUIREMENTS,
                           @Named("DELIVERY_TIME_PER_KM") BigDecimal DELIVERY_TIME_PER_KM) {

        this.COOKING_SLOT_REQUIREMENTS = COOKING_SLOT_REQUIREMENTS;
        this.COOKING_TIME_REQUIREMENTS = COOKING_TIME_REQUIREMENTS;
        this.DELIVERY_TIME_PER_KM = DELIVERY_TIME_PER_KM;
    }

    public int deliverOrder(Order order, int slotsUsed) {
        OrderMetadata orderMetadata = order.getOrderMetadata();
        orderMetadata.setStatus(Status.ACCEPTED);
        slotsUsed -= orderMetadata.getSlotsRequired();
        return slotsUsed;
    }

    public void rejectOrder(Order order) {
        OrderMetadata orderMetadata = order.getOrderMetadata();
        orderMetadata.setStatus(Status.REJECTED);
    }

    public void displayMessages(List<Order> orders) {
        orders.forEach((order) -> {
            OrderMetadata orderMetadata = order.getOrderMetadata();
            if (order.getOrderMetadata().getStatus().equals(Status.ACCEPTED)) {
                System.out.println("Order " + order.getOrderId() + " will get delivered in " + orderMetadata.getEstimatedDeliveryTime() + " minutes");
            } else {
                System.out.println("Order " + order.getOrderId() + " is denied because the restaurant cannot accommodate it.");
            }
        });
    }

    public void populateOrderRequirements(Order order) {
        order.setOrderMetadata(
                OrderMetadata.builder()
                        .status(Status.PENDING)
                        .slotsRequired(calculateOrderSlotRequirement(order.getMeals()).intValue())
                        .estimatedFoodPrepTime(calculateOrderCookingTime(order.getMeals()))
                        .build()
        );
    }

    private BigDecimal calculateOrderSlotRequirement(ArrayList<String> meals) {
        return meals.stream()
                .map(COOKING_SLOT_REQUIREMENTS::get)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateOrderCookingTime(ArrayList<String> meals) {
        return meals.stream()
                .distinct()
                .map(COOKING_TIME_REQUIREMENTS::get)
                .reduce(BigDecimal.ZERO, BigDecimal::max);
    }

    public BigDecimal calculateTravelTime(Order order) {
        return order.getDistance().multiply(DELIVERY_TIME_PER_KM);
    }

}
