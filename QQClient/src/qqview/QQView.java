package qqview;

import qq_client_service.Client_Message;
import qq_client_service.File_Message;
import qq_client_service.UserClientService;

import java.util.Scanner;

public class QQView {
    private boolean loop = true; //控制是否显示菜单
    private String key = "";
    Scanner scanner = new Scanner(System.in);
    private UserClientService userClientService = new UserClientService();
    private Client_Message client_message = new Client_Message();
    private File_Message file_message = new File_Message();

    public void mainMenu(){
        while(loop) {
            System.out.println("=========欢迎登陆=========");
            System.out.println("\t\t 1 登陆系统");
            System.out.println("\t\t 9 退出系统");
            System.out.println("请输入你的选择:");
            key = scanner.next();
            switch (key) {
                case "1":
                    System.out.print("请输入你的用户名:");
                    String userId = scanner.next();
                    System.out.print("请输入你的密码:");
                    String pwd = scanner.next();
                    //发到服务器看看是否合法
                    if(userClientService.checkUser(userId,pwd)){
                        System.out.println("=========欢迎 用户"+ userId +"=========");
                        //进入二级菜单
                        while(loop){
                            System.out.println("\n=========网络通讯二级菜单========");
                            System.out.println("1 显示在线用户列表:");
                            System.out.println("2 群发消息:");
                            System.out.println("3 私聊消息:");
                            System.out.println("4 发送文件:");
                            System.out.println("9 退出系统:");
                            System.out.println("请输入你的选择:");
                            key = scanner.next();
                            switch (key){
                                case "1":
                                    System.out.println("\n=========当前用户=========\n");
                                    userClientService.online_friend_list();
                                    break;
                                case "2":
                                    System.out.println("输入内容：");
                                    String contents = scanner.next();
                                    client_message.send_message_to_all(userId,contents);
                                    break;
                                case "3":
                                    System.out.println("输入要发给谁：");
                                    String towho = scanner.next();
                                    System.out.println("输入内容：");
                                    String contents2 = scanner.next();
                                    client_message.send_message_to_one(userId,towho,contents2);
                                    break;
                                case "4":
                                    System.out.println("输入要发给谁：");
                                    String towho2 = scanner.next();
                                    System.out.println("输入路径：");
                                    String src = scanner.next();
                                    System.out.println("输入接受者路径：");
                                    String dest = scanner.next();
                                    file_message.send_file_to_one(userId,towho2,src,dest);
                                    break;
                                case "9":
                                    userClientService.logout();
                                    loop = false;
                                    break;
                            }

                        }
                    }else{
                        System.out.println("=========认证失败 退出系统=========");
                    }
                    break;
                case "9":
                    loop = false;
                    System.out.println("exit ");
            }
        }
    }
}
