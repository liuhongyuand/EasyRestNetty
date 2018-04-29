package com.example.main.impl;

import com.easyrest.ioc.remote.EasyRestServiceLookup;
import com.easyrest.model.ResponseEntity;
import com.example.api.Service1;
import com.example.api.Service2;
import com.example.model.People;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class Service1Impl implements Service1 {

    @Override
    public ResponseEntity createPeople(String name, int age, long birthday, List<String> skills, People boss) {
        Service2 service2 = EasyRestServiceLookup.lookup(Service2.class);
        return ResponseEntity.buildOkResponse(service2.getPeople(name, age, birthday, skills, boss));
    }

}