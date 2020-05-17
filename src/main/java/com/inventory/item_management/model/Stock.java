package com.inventory.item_management.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Stock {
    private Map<Item, Integer> itemsAvailability;

    public Stock(Map<Item, Integer> itemsAvailability) {
        this();
        this.itemsAvailability = itemsAvailability;
    }

    public Stock(){
        this.itemsAvailability = new HashMap<>();
    }

    public Map<Item, Integer> getItemsAvailability() {
        return itemsAvailability;
    }

    public JSONArray getJson() {
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<Item, Integer> entry : itemsAvailability.entrySet()) {
            JSONObject jsonObject = new JSONObject(entry.getKey());
            jsonObject.put("availability", entry.getValue());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public void setItemsAvailability(Map<Item, Integer> itemsAvailability) {
        this.itemsAvailability = itemsAvailability;
    }

    public void setItemsAvailability(Item item, int number) {
        assert number >= 0;
        assert item != null;
        itemsAvailability.putIfAbsent(item, number);
    }



}
