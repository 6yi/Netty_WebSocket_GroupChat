package com.lzheng.NettyWebChat.Pojo;

public class Message {

    public static final int REGIRSTER=0;
    public static final int SEND_MSG=1;
    public static final int NO_USER=2;
    public static final int NO_GROUP=3;
    public static final int USER_NO_CONNECTION=4;
    public static final int OK=200;
    public static final int ADD_USER=404;

    @Override
    public String toString() {
        return "Message{" +
                "formUser='" + formUser + '\'' +
                ", sendUser='" + sendUser + '\'' +
                ", state=" + state +
                ", content='" + content + '\'' +
                '}';
    }

    private String formUser;
    private String sendUser;
    private int state;
    private String content;

    public String getFormUser() {
        return formUser;
    }

    public void setFormUser(String formUser) {
        this.formUser = formUser;
    }

    public Message(String formUser, String sendUser, int state, String content) {
        this.formUser = formUser;
        this.sendUser = sendUser;
        this.state = state;
        this.content = content;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }

}
