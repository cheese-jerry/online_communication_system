package qq_client_service;
import qqcommon.Message;
import qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

public class Client_Message {
    public void send_message_to_one(String sender,String getter,String contents){
        Message message = new Message();
        message.setSender(sender);
        message.setGetter(getter);
        message.setContent(contents);
        message.setSendTime(new Date().toString());
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        System.out.println(sender+" 发给 "+getter);
        try{
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientServerThread.get_ClientConnectServerThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);

        }catch (IOException ioe){
            ioe.printStackTrace();
        }

    }

    public void send_message_to_all(String sender,String contents){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_TO_ALL);
        message.setContent(contents);
        message.setSendTime(new Date().toString());
        message.setSender(sender);
        System.out.println(sender+" 发给所有人");
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientServerThread.get_ClientConnectServerThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
