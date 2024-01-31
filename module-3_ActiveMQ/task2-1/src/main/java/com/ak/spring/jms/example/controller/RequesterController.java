package com.ak.spring.jms.example.controller;

import lombok.extern.slf4j.Slf4j;
import com.ak.spring.jms.example.service.RequesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class RequesterController {
    private final RequesterService requesterService;
    @Autowired
    public RequesterController(RequesterService requesterService) {
        this.requesterService = requesterService;
    }

    @GetMapping("/send-request")
    @ResponseBody
    public String sendRequest(@RequestParam String request){
        log.info("Requester sent message: " + request);
        return requesterService.sendAndReceive(request);
    }
}