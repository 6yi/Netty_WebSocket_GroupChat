package com.lzheng.NettyWebChat.NettyWebSocket;
import com.alibaba.fastjson.JSON;
import com.lzheng.NettyWebChat.Pojo.ChatUser;
import com.lzheng.NettyWebChat.Pojo.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static ChannelGroup channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static Map<String, ChatUser>channelMap=new HashMap<>();
    public static Map<String, ChatUser> getChannelMap() {
        return channelMap;
    }
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //广播方法
        //加入Channel组
        channelGroup.add(channel);
    }
    @Override
    protected  void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        Message rtMessage = JSON.parseObject(textWebSocketFrame.text(), Message.class);
        System.out.println("接收到的信息:"+rtMessage);
        if (rtMessage.getState()==Message.SEND_MSG){
            Channel channel = channelHandlerContext.channel();
            Message message = new Message(rtMessage.getFormUser(), rtMessage.getSendUser(), Message.OK, rtMessage.getContent());
            TextWebSocketFrame textWebSocketFrame2 = new TextWebSocketFrame(JSON.toJSONString(message));
            channelGroup.writeAndFlush(textWebSocketFrame2);
       }else if (rtMessage.getState()==Message.REGIRSTER){
           Regirster(channelHandlerContext, rtMessage);
       }else if (rtMessage.getState()==Message.ADD_USER){

       }else if(rtMessage.getState()==9){
           channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(rtMessage)));
       }
    }
    private void Regirster(ChannelHandlerContext channelHandlerContext, Message rtMessage) {
        ChatUser chatUser = new ChatUser(rtMessage.getFormUser(), channelHandlerContext.channel());
        channelMap.put(rtMessage.getSendUser(),chatUser);
        Message message = new Message(rtMessage.getFormUser(),rtMessage.getSendUser(), 8888, ""+channelMap.size());
        System.out.println(chatUser.getUserName()+"注册");
        channelGroup.forEach(h->{
                h.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
        });
    }
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        closeAndRemove(ctx);
    }

//    private void sendMsg(ChannelHandlerContext channelHandlerContext, Message rtMessage) {
//        System.out.println(channelGroup.size());
//
//            Channel channel = channelHandlerContext.channel();
//            Message message = new Message(rtMessage.getFormUser(), rtMessage.getSendUser(), Message.OK, rtMessage.getContent());
//            TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(JSON.toJSONString(message));
//            channelGroup.forEach(h->{
//                if (h!=channel) {
//                    h.writeAndFlush(textWebSocketFrame);
//                }
//            });
//
//
//    }
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        closeAndRemove(ctx);
//    }

    private static void closeAndRemove(ChannelHandlerContext ctx){
        Channel channel = ctx.channel();
        channelGroup.remove(ctx.channel());
        for (Map.Entry<String, ChatUser> entry : channelMap.entrySet()) {
            if (channel==entry.getValue().getChannel()){
                System.out.println(entry.getKey()+"关闭");
                channelMap.remove(entry.getKey());
                break;
            }
        }
        Message message = new Message("","", 8888, ""+channelMap.size());
        channelGroup.forEach(h->{
            if(h!=channel){
                h.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
            }
        });
        channelGroup.remove(channel);
        channel.close();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println(ctx.channel().remoteAddress()+"异常"+cause.getMessage());
        closeAndRemove(ctx);
    }



}
