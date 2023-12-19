package com.ak.springboot.service.jms;

import com.ak.springboot.pojos.BookOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookOrderService {

    private static final String BOOK_QUEUE = "book.order.queue";

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(BookOrder bookOrder){
        jmsTemplate.convertAndSend(BOOK_QUEUE, bookOrder);
    }
}
