package client.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHander extends SimpleChannelInboundHandler<String> {
    private Callback onMessagereceivedCallback;

    public ClientHander(Callback onMessagereceivedCallback) {
        this.onMessagereceivedCallback = onMessagereceivedCallback;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

        if(onMessagereceivedCallback != null){
            onMessagereceivedCallback.callback(s);
        }

    }
}
