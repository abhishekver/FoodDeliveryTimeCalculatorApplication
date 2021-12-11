package modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import calculator.MiscellaneousConstants;
import enums.FoodType;
import models.Order;
import models.comparators.OrderComparator;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import javax.inject.Named;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class MainModule extends AbstractModule {

    @Override
    public void configure() {
    }

    @Provides
    @Singleton
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @Provides
    @Singleton
    @Named("COOKING_SLOT_REQUIREMENTS")
    public Map<String, BigDecimal> getCookingSlotRequirements() {
        HashMap<String, BigDecimal> cookingSlotRequirements = new HashMap<>();

        cookingSlotRequirements.put(FoodType.MAIN_COURSE.label, BigDecimal.valueOf(2));
        cookingSlotRequirements.put(FoodType.APPETIZERS.label, BigDecimal.valueOf(1));

        return cookingSlotRequirements;
    }

    @Provides
    @Singleton
    @Named("COOKING_TIME_REQUIREMENTS")
    public Map<String, BigDecimal> getCookingTimeRequirements() {
        HashMap<String, BigDecimal> cookingTimeRequirements = new HashMap<>();

        cookingTimeRequirements.put(FoodType.MAIN_COURSE.label, BigDecimal.valueOf(29));
        cookingTimeRequirements.put(FoodType.APPETIZERS.label, BigDecimal.valueOf(17));

        return cookingTimeRequirements;
    }

    @Provides
    @Singleton
    public OrderComparator getOrderQueue() {
        return new OrderComparator();
    }

    @Provides
    @Singleton
    @Named("TOTAL_SLOTS")
    public int getTotalSlots() {
        return MiscellaneousConstants.SLOTS;
    }


    @Provides
    @Singleton
    @Named("ORDER_TIME_THRESHOLD")
    public BigDecimal getOrderThreshold() {
        return MiscellaneousConstants.ORDER_THRESHOLD;
    }

    @Provides
    @Singleton
    @Named("DELIVERY_TIME_PER_KM")
    public BigDecimal getDeliveryTimePerKm() {
        return MiscellaneousConstants.DELIVERY_TIME_PER_KMS;
    }

    @Provides
    @Singleton
    public Queue<Order> getOrderQueue(OrderComparator orderComparator) {
        return new PriorityQueue<>(orderComparator);
    }
}
