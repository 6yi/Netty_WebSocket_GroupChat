package com.lzheng.NettyWebChat.Pojo;

import io.netty.channel.Channel;

import java.util.List;

public class ChatUser {

    private String userName;
    private List<ChatUser> friend;
    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ChatUser(String userName, Channel channel) {
        this.userName = userName;
        this.channel = channel;
    }

    public List<ChatUser> getFriend() {
        return friend;
    }

    public void setFriend(List<ChatUser> friend) {
        this.friend = friend;
    }
}
