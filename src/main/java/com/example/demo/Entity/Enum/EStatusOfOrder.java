package com.example.demo.Entity.Enum;

public enum EStatusOfOrder {

    ACTIVE("Активный"), INACTIVE("Неактивный");
    String status;

    EStatusOfOrder(String status){
        this.status = status;
    }
}
