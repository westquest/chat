package chat.server;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


import java.util.ArrayList;
import java.util.List;


public class MainHandler extends SimpleChannelInboundHandler<String> {
    private static final List<Channel> channels = new ArrayList<>();
    private static int newClientIndex = 1;
    private String clientName;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.print("client connect: " + ctx);
        channels.add(ctx.channel());
        clientName = "Клиент #" + newClientIndex;
        newClientIndex ++;
        broadcastMessage("SERVER","Connect client: " + clientName);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println("Входящее сообщение: " + s);
        if (s.startsWith("/")) {
            if(s.startsWith("/changenik")){
                String newNickname = s.split("\\s",2)[1];
                broadcastMessage("SERVER","Клиент " + clientName + " сменил ник на " + newNickname );
                clientName= newNickname;
            }
            return ;
        }
        broadcastMessage(clientName, s);


    }
    public void broadcastMessage(String clientName,String message){
        String out = String.format("[%s]: %s\n", clientName, message);
        for (Channel c : channels)
        {
            c.writeAndFlush(out);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Клиент " + clientName + " отключился");
        channels.remove(ctx.channel());
        broadcastMessage("SERVER","Клиент вышел из сети: " + clientName);
        ctx.close();
    }
}
