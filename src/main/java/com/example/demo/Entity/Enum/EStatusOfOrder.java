package com.example.demo.Entity.Enum;

public enum EStatusOfOrder {

    ACTIVE("Активный"), INACTIVE("Неактивный");
    final String status;

    EStatusOfOrder(String status){
        this.status = status;
    }
}
