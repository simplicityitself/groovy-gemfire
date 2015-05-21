package com.simplicityitself.gemfire;

import java.util.List;

public class CustomerOrder {

    private int customerId;
    private double orderTotal;
    private List<OrderItem> items;

    public CustomerOrder(int customerId, double orderTotal, List<OrderItem> items) {
        this.customerId = customerId;
        this.orderTotal = orderTotal;
        this.items = items;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
