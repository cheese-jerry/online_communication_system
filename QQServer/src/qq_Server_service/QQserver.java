package qq_Server_service;

import qqcommon.Message;
import qqcommon.MessageType;
import qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class QQserver {
    private ServerSocket serverSocket = null;
    private static HashMap<String,User> validUsers =  new HashMap<>();
    private static HashMap<String, ArrayList<Message>> offline_message = new HashMap<>();//储存离线留言
    //静态代码块初始化
    static {
        validUsers.put("yuyuemin",new User("yuyuemin","123456"));
        validUsers.put("cmx",new User("cmx","197121"));
        validUsers.put("xjp",new User("xjp","250"));
        validUsers.put("ab",new User("ab","10"));

    }

    public boolean check_User(String userId,String pwd){

        if(validUsers.get(userId) == null){
            return false;
        }else if(!validUsers.get(userId).getPasswd().equals(pwd)){
            return false;
        }
        return true;
    }

    public static HashMap<String, ArrayList<Message>> getOffline_message() {
        return offline_message;
    }

    public static void setOffline_message(HashMap<String, ArrayList<Message>> offline_message) {
        QQserver.offline_message = offline_message;
    }

    public QQserver(){
        try {
            System.out.println("服务器在9999端口监听");
            serverSocket = new ServerSocket(9999);
            Thread thread = new Thread(new News());
            thread.start();
            while (true) {
                Socket socket = serverSocket.accept();
                //得到输入流
                ObjectInputStream ois= new ObjectInputStream(socket.getInputStream());
                //得到输出流
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                User u = (User)ois.readObject();
                //new message 准备回复客户端
                Message message = new Message();
                //验证
                if(check_User(u.getUserId(),u.getPasswd())){
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    oos.writeObject(message);
                    //创建线程建立持续通讯
                    System.out.println("已连接"+u.getUserId());
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket, u.getUserId());
                    serverConnectClientThread.start();
                    //加入集合
                    ManageServerConnectClientThread.add_Client_Thread(u.getUserId(),serverConnectClientThread);

                    //看看有无离线留言,有则发送
                    if(offline_message.get(u.getUserId())!=null){
                        ArrayList<Message> arrayList = offline_message.get(u.getUserId());
                        for(Message m : arrayList){
                            ObjectOutputStream oos2 = new ObjectOutputStream(socket.getOutputStream());
                            oos2.writeObject(m);
                            arrayList.remove(m);
                        }
                    }
                }else{
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    socket.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
