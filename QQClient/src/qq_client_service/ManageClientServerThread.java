package qq_client_service;

import java.util.HashMap;

public class ManageClientServerThread {
    private static HashMap<String,ClientConnectServerThread> hm = new HashMap<>();

    public static void add_ClientConnectServerThread(String userId,ClientConnectServerThread clientConnectServerThread){
        hm.put(userId,clientConnectServerThread);
    }

    public static ClientConnectServerThread get_ClientConnectServerThread(String userId) {
        return hm.get(userId);
    }
}
