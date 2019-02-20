package com.stucom.abou.game.model;

public class Message {

    private int Id;
    private int FromId;
    private String Text;
    private String SentAt;
    private String ReceivedAt;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getFromId() {
        return FromId;
    }

    public void setFromId(int fromId) {
        FromId = fromId;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        this.Text = text;
    }

    public String getSentAt() {
        return SentAt;
    }

    public void setSentAt(String sentAt) {
        SentAt = sentAt;
    }

    public String getReceivedAt() {
        return ReceivedAt;
    }

    public void setReceivedAt(String receivedAt) {
        ReceivedAt = receivedAt;
    }
}