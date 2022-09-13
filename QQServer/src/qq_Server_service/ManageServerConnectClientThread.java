package qq_Server_service;

import java.util.HashMap;
import java.util.Iterator;

public class ManageServerConnectClientThread {
    private static HashMap<String,ServerConnectClientThread> hm = new HashMap<String,ServerConnectClientThread>();

    public static void add_Client_Thread(String userId,ServerConnectClientThread scct){
        hm.put(userId,scct);
    }

    public static ServerConnectClientThread get_Client_Thread(String userId){
        return hm.get(userId);
    }

    public static void remove(String userId){
        hm.remove(userId);
    }

    public static String get_online_users(){
        Iterator<String> it = hm.keySet().iterator();
        String onlineusers = "";
        while(it.hasNext()){
            onlineusers+= it.next().toString()+" ";
        }
        return onlineusers;
    }

    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }
}
