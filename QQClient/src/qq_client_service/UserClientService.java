package qq_client_service;

import qqcommon.Message;
import qqcommon.MessageType;
import qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

//该类完成用户登陆验证注册等功能
public class UserClientService {
    //因为别的地方可能用到socket所以设置为属性
    private User u = new User();
    private Socket socket;

    //查看是否合法,合法则上号，开始线程
    public boolean checkUser(String userId,String pwd)  {
        boolean res = false;
        u.setUserId(userId);
        u.setPasswd(pwd);
        try {
            socket = new Socket(InetAddress.getLocalHost(), 9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u); //发送user对象给服务器

            //读取服务器发送回的信息
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) ois.readObject();
            if(message.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)){
                //创建线程
                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                clientConnectServerThread.start();
                //把线程放入集合
                ManageClientServerThread.add_ClientConnectServerThread(userId,clientConnectServerThread);
                res = true;
            }else{
                //不能启动和服务器链接的线程
                socket.close();

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    //向服务器端请求在线列表
    public void online_friend_list(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserId());
        //发送给服务器
        try {
            ClientConnectServerThread ccst = ManageClientServerThread.get_ClientConnectServerThread(u.getUserId());
            ObjectOutputStream oos = new ObjectOutputStream(ccst.getSocket().getOutputStream());
            oos.writeObject(message);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }

    }

    public void logout(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserId());

        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            System.out.printf(u.getUserId() + "退出系统");
            System.exit(0);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public User getU() {
        return u;
    }

    public void setU(User u) {
        this.u = u;
    }
}
