package com.inventory.item_management.model;

import java.util.Map;

public class Stock {
    private Map<Item, Integer> itemsAvailability;

    public Stock(Map<Item, Integer> itemsAvailability) {
        this.itemsAvailability = itemsAvailability;
    }

    public Map<Item, Integer> getItemsAvailability() {
        return itemsAvailability;
    }

    public void setItemsAvailability(Map<Item, Integer> itemsAvailability) {
        this.itemsAvailability = itemsAvailability;
    }

    public void setItemsAvailability(Item item, int number) throws Exception{
        assert number > 0;
        assert item != null;
        itemsAvailability.put(item, number);
    }

}
