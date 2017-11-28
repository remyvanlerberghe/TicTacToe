package com.tictactoe.android.tictactoe.Models;
import java.util.Map;

public class Message {
    public String sender;
    public String content;
    public Map time;

    public Message(String sender, String content, Map time) {
        this.sender = sender;
        this.content = content;
        this.time = time;
    }

    public Message(){

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map getTime() {
        return time;
    }

    public void setTime(Map time) {
        this.time = time;
    }
}
