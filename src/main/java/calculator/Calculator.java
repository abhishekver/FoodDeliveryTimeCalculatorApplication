package calculator;

import enums.Status;
import exceptions.UnrecoverableException;
import mappers.JsonToObjectMapper;
import models.Order;
import models.OrderMetadata;
import utils.OrderController;
import com.google.inject.Inject;

import javax.inject.Named;
import java.math.BigDecimal;
import java.util.List;
import java.util.Queue;

public class Calculator {

    private Queue<Order> orderQueue;

    private int slotsUsed;

    private final Integer TOTAL_SLOTS;
    private final BigDecimal ORDER_TIME_THRESHOLD;

    private final JsonToObjectMapper jsonToObjectMapper;
    private final OrderController orderController;

    @Inject
    public Calculator(@Named("TOTAL_SLOTS") Integer totalSlots,
                      @Named("ORDER_TIME_THRESHOLD") BigDecimal orderThreshold,
                      Queue<Order> orderQueue,
                      JsonToObjectMapper jsonToObjectMapper,
                      OrderController orderController) {

        this.orderQueue = orderQueue;

        this.jsonToObjectMapper = jsonToObjectMapper;
        this.orderController = orderController;

        this.TOTAL_SLOTS = totalSlots;
        this.ORDER_TIME_THRESHOLD = orderThreshold;
    }

    public void calculateDeliveryTime(String inputFile) throws UnrecoverableException {

        List<Order> orders = jsonToObjectMapper.convertRequest(inputFile).getOrderList();

        int i = processOrders(orders, 0, BigDecimal.ZERO);

        while (!orderQueue.isEmpty()) {
            Order orderFromKitchen = orderQueue.remove();
            slotsUsed = orderController.deliverOrder(orderFromKitchen, slotsUsed);
            if (i < orders.size()) {
                i = processOrders(orders, i, orderFromKitchen.getOrderMetadata().getEstimatedDeliveryTime());
            }
        }

        orderController.displayMessages(orders);
    }

    private int processOrders(List<Order> orders, int i, BigDecimal startTime) {
        while (i < orders.size()) {
            Order order = orders.get(i);
            orderController.populateOrderRequirements(order);
            if (orders.get(i).getOrderMetadata().getSlotsRequired() > TOTAL_SLOTS) {
                orderController.rejectOrder(order);
                i += 1;
            } else if (orders.get(i).getOrderMetadata().getSlotsRequired() <= TOTAL_SLOTS - slotsUsed) {
                Status status = processNextOrder(order, startTime);
                if (status.equals(Status.REJECTED)) {
                    orderController.rejectOrder(order);
                }
                i += 1;
            } else {
                break;
            }
        }
        return i;
    }

    private Status processNextOrder(Order order, BigDecimal startTime) {
        orderController.populateOrderRequirements(order);

        OrderMetadata orderMetadata = order.getOrderMetadata();
        orderMetadata.setStartTime(startTime);
        orderMetadata.setEstimatedDeliveryTime(orderMetadata.getEstimatedFoodPrepTime().add(orderController.calculateTravelTime(order).add(startTime)));

        if (orderMetadata.getEstimatedDeliveryTime().compareTo(ORDER_TIME_THRESHOLD) < 0) {
            orderQueue.add(order);
            slotsUsed += order.getOrderMetadata().getSlotsRequired();
            order.getOrderMetadata().setStatus(Status.ACCEPTED);
        } else {
            orderController.rejectOrder(order);
        }
        return orderMetadata.getStatus();
    }

}
