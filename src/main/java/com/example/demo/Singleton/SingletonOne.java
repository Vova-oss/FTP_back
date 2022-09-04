package com.example.demo.Singleton;

public class SingletonOne {

    private static SingletonOne singletonOne;

    public static synchronized SingletonOne getSingleton(){
        if(singletonOne == null){
            singletonOne = new SingletonOne();
        }
        return singletonOne;
    }

    private SingletonOne() {
    }
}
