package qq_client_service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

public class File_Message {
    public void send_file_to_one(String sender,String getter,String src,String dest){
        Message message = new Message();
        message.setSender(sender);
        message.setSendTime(new Date().toString());
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setGetter(getter);
        message.setSrc(src);
        message.setDest(dest);

        FileInputStream fileInputStream = null;
        try {
            byte[] filebytes = new byte[1024];
            int len = 0;
            fileInputStream = new FileInputStream(src);
            while((len = fileInputStream.read(filebytes))!=-1){
                System.out.println("正在读取...");
            }
            message.setData(filebytes);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }finally {
            if(fileInputStream != null ){
                try{
                    fileInputStream.close();
                }catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }

        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientServerThread.get_ClientConnectServerThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
