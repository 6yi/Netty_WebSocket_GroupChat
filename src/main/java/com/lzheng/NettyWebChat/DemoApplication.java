package com.lzheng.NettyWebChat;

import com.lzheng.NettyWebChat.NettyWebSocket.WebSocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class, args);
        try{
            new WebSocketServer().run();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
