package com.lzheng.NettyWebChat;
import com.lzheng.NettyWebChat.NettyWebSocket.MyTextWebSocketFrameHandler;
import com.lzheng.NettyWebChat.Pojo.ChatUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class UserController {
    @RequestMapping("/login/{userName}")
    @ResponseBody
    public int login(@PathVariable(name = "userName") String userName){
        Map<String, ChatUser> channelMap = MyTextWebSocketFrameHandler.getChannelMap();
        int json;
        if (channelMap.containsKey(userName)){
            json=0;
        }else{
            json=200;
        }
        return json;
    }

}
