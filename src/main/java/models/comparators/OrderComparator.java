package models.comparators;

import models.Order;

import java.util.Comparator;

public class OrderComparator implements Comparator<Order> {

        public int compare(Order o1, Order o2) {
            return o1.getOrderMetadata().getEstimatedDeliveryTime().compareTo(o2.getOrderMetadata().getEstimatedDeliveryTime());
        }

}
