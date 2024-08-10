package kall.core;

import kall.util.ClearConsole;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc =new Scanner(System.in);
        String input = null;
        while (true){
            //TODO:1.检测数据库是否连接，连接失败让用户输入连接信息，在当前项目的父目录

            //TODO:2.监测的文件或目录写在配置文件中，配置文件路径默认项目的父目录下。文件存在则输出已导入，不在则创建

            //主界面：
            System.out.println("------------------Welcome to DataInput Program--------------------");
            System.out.println("==================================================================");
            //TODO:如果找不到该文件，则告诉路径错误并回到主界面。输入绝对路径或相对路径。
            System.out.println("1. add new file to monitor.");

            //TODO:监测该目录下文件新加入则导入，已存在且内容有变动也导入
            System.out.println("2. monitor the specified path dir.");

            System.out.println("==================================================================");
            System.out.println("Type: 1 or 2,then type:/path/to/file.txt or ./file.txt");
            System.out.println("Type 'e' to exit, or 'r' to refresh..");
            input=inputPattern(sc);
            if(input.equalsIgnoreCase("e")){
                break;
            } else if (input.equalsIgnoreCase("r")) {
                ClearConsole.clear3();
                continue;
            }
            System.out.println("good");
        }
        sc.close();
        System.out.println("End...");
    }
    private static String inputPattern(Scanner sc){
        System.out.print("input> ");
        return sc.nextLine();
    }

}