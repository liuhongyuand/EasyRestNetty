package com.example;

import com.easyrest.EasyRest;
import com.example.rest.HomeRestModel;

public class Example {

    public static void main(String[] args) {
        EasyRest easyRest = new EasyRest("classpath:applicationContext.xml");
        easyRest.registerServiceAndStartup(HomeRestModel.class);
    }

}
