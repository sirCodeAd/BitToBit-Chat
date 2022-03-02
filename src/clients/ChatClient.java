package clients;

import java.io.*;
import java.net.*;
import threads.*;

/**
 * This class starts the client program, connects to a server specified to a specific hostname/ip-adress and port number.
 * Once a connection is made, it starts two threads (ReadThread and WriteThread) 
 */
public class ChatClient {

    private String hostName;
    private String userName;
    private int port;

    public ChatClient(String hostName, int port){

        this.hostName   = hostName;
        this.port       = port;
    }

    /**
     * Here we execute the program that connects to the server.
     */
    public void execute(){

        try{

            /**
             * We establish a socket that will listen for request from clients that will connect to the server
             * specified by a hostname and port
             */
            Socket socket = new Socket(hostName, port);
            System.out.println("Connected to the server");

            /**
             * Two threads that will read and write inputs/outputs from the server and then print them to
             * the consols, and will do so while there is inputs/outputs coming and going until client disconnects.
             */
            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();   

        } catch (UnknownHostException ue){
            System.out.println("Server not found: " + ue.getMessage());
            ue.printStackTrace();
        } catch (IOException e){
            System.out.println("I/O Error: " + e.getMessage());
        }
    }

    /**
     * Setter and getters
     */
    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserName(){
        return this.userName;
    }

    public static void main(String[] args) {
        if (args.length < 2) return;
 
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
 
        ChatClient client = new ChatClient(hostname, port);
        client.execute();
    }
    
    
}
