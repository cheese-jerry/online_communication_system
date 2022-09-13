package qq_client_service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientConnectServerThread extends Thread {
    private Socket socket;


    @Override
    public void run() {
        while(true){
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject(); //如果服务器没有发送message会阻塞
                //判断服务端返回的消息类型并做相应处理
                if(message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){
                    String[] onlineusers = message.getContent().split(" ");
                    for (int i = 0; i < onlineusers.length; i++) {
                        System.out.printf("用户："+onlineusers[i]);
                    }

                }
                //接收别人发的消息
                else if(message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){
                    System.out.println(message.getSender()+" 对 "+message.getGetter()+" 说 ");
                    System.out.println(message.getContent());
                }else if(message.getMesType().equals(MessageType.MESSAGE_TO_ALL)){
                    System.out.println(message.getSender()+" 对所有人说 ");
                    System.out.println(message.getContent());
                }else if(message.getMesType().equals(MessageType.MESSAGE_FILE_MES)){
                    System.out.println(message.getSender()+" 给 "+message.getGetter()+" 发送文件 ");
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                    fileOutputStream.write(message.getData());
                    System.out.println("发送成功");
                }else{
                    System.out.println("other type's message");
                }
            } catch (Exception e) {
               e.printStackTrace();
            }

        }
    }

    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }
}
