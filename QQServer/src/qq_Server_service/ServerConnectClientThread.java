package qq_Server_service;

import qqcommon.Message;
import qqcommon.MessageType;
import qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ServerConnectClientThread extends Thread{
    private Socket socket;
    private String userId;

    public ServerConnectClientThread(Socket socket,String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        //可以一直发送接收客户端消息
        while(true){
            try {
                System.out.println("服务端和客户端保持通讯");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                //判断客户端返回的消息类型并做相应处理
                //拉取在线用户
                if(message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    System.out.printf(message.getSender()+"请求在线列表");
                    String onlineuser = ManageServerConnectClientThread.get_online_users();
                    //构建message对象返回
                    Message message2 = new Message();
                    message2.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message2.setContent(onlineuser);
                    message2.setGetter(message2.getSender());
                    //写入数据通道socket
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);


                }//私聊,判断是否在线不在则放入离线hashmap
                else if(message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){
                    HashMap<String,ServerConnectClientThread> hm = ManageServerConnectClientThread.getHm();
                    if(hm.get(message.getGetter())!=null) {
                        ObjectOutputStream oos = new ObjectOutputStream(ManageServerConnectClientThread.get_Client_Thread(message.getGetter()).socket.getOutputStream());
                        oos.writeObject(message);
                    }else{
                        System.out.println("不在线转入离线信箱...");
                        HashMap<String, ArrayList<Message>> hashMap = QQserver.getOffline_message();
                        if(hashMap.containsKey(message.getGetter())){
                            hashMap.get(message.getGetter()).add(message);
                        }else {
                            ArrayList<Message> al = new ArrayList<>();
                            al.add(message);
                            hashMap.put(message.getGetter(),al);
                        }
                    }
                }//退出
                else if(message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)){
                    System.out.println(message.getSender()+"退出");
                    ManageServerConnectClientThread.remove(message.getSender());
                    socket.close();
                    //退出线程
                    break;
                }//群发
                else if(message.getMesType().equals(MessageType.MESSAGE_TO_ALL)){
                    HashMap<String,ServerConnectClientThread> hm = ManageServerConnectClientThread.getHm();
                    Set<String> keysets = hm.keySet();
                    for(String id : keysets){
                        if(!id.equals(message.getSender())) {
                            ObjectOutputStream oos = new ObjectOutputStream(hm.get(id).getSocket().getOutputStream());
                            oos.writeObject(message);
                        }
                    }

                }//发文件
                else if(message.getMesType().equals(MessageType.MESSAGE_FILE_MES)) {
                    ObjectOutputStream oos = new ObjectOutputStream(ManageServerConnectClientThread.get_Client_Thread(message.getGetter()).getSocket().getOutputStream());
                    oos.writeObject(message);
                }else{
                    System.out.println("others");

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
