package com.ak.springboot;

//import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Reciever {

    /*    @JmsListener(destination="order-queue", containerFactory = "warehouseFactory")
    public void receiveMessage(String message) {
        System.out.println("Received message is: " + message);
    }*/
}
