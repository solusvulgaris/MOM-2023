package com.ak.spring.jms.example.web;

import com.ak.spring.jms.example.service.MessageService;
import com.ak.spring.jms.example.pojos.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AppController {
    @Autowired
    private MessageService messageService;

    @GetMapping(path = "/")
    public String homePage(Model model) {
        model.addAttribute("messageNumber", messageService.getMessageCount());
        return "home-page";
    }

    @GetMapping(path = "/process/message/{messageText}/")
    public @ResponseBody String processOrder(@PathVariable("messageText") String messageText) {
        try {
            Message m = new Message();
            messageService.processNewMessage();
            m.setId(messageService.getMessageCount());
            m.setText(messageText);
            messageService.send(m);
        } catch (Exception exception) {
            messageService.rollbackLastMessage();
            return "Error occurred! " + exception.getLocalizedMessage();
        }
        return "Message: '" + messageText +
                "' was successfully processed!";
    }
}