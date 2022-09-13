package qq_Server_service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class News implements Runnable{
    Scanner scanner = new Scanner(System.in);
    @Override
    public void run() {
        System.out.println("输入要推送的新闻：");
        String contents = scanner.next();
        HashMap<String,ServerConnectClientThread> hm = ManageServerConnectClientThread.getHm();
        for(String key: hm.keySet()) {
            Message message = new Message();
            message.setMesType(MessageType.MESSAGE_TO_ALL);
            message.setGetter(key);
            message.setSender("服务器");
            message.setSendTime(new Date().toString());
            message.setContent(contents);
            try {
                ObjectOutputStream oos = new ObjectOutputStream(hm.get(key).getSocket().getOutputStream());
                oos.writeObject(message);
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
    }
}
