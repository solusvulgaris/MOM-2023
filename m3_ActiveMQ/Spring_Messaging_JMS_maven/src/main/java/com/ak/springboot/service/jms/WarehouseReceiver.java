package com.ak.springboot.service.jms;

import com.ak.springboot.pojos.BookOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class WarehouseReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseReceiver.class);

    @JmsListener(destination = "book.order.queue")
    public void receive(BookOrder bookOrder){
        LOGGER.info("Message received!");
        LOGGER.info("Message is == " + bookOrder);
    }
}
