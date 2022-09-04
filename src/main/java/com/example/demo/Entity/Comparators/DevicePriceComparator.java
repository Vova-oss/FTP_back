package com.example.demo.Entity.Comparators;

import com.example.demo.Entity.Device;

import java.util.Comparator;

public class DevicePriceComparator implements Comparator<Device> {
    @Override
    public int compare(Device o1, Device o2) {
        Integer price1 = Integer.parseInt(o1.getPrice());
        Integer price2 = Integer.parseInt(o2.getPrice());
        return price1.compareTo(price2);
    }
}
