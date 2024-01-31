package com.ak.spring.jms.example.pojos;

public class Message {
    private long id;
    private String title;
    private String text;

    public Message() {
        id = 0;
        title= "Default Title";
        text = "Empty message";
    }
    public Message(long messageId, String messageTitle, String messageText) {
        this.id = messageId;
        this.title = messageTitle;
        this.text = messageText;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
