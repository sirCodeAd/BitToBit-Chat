package threads;

import java.io.*;
import java.net.*;
import java.util.*;

import server.ChatServer;

/**
 * This class is responsible for reading messages sent by a client and then broadcasting those messages to all other clients.
 * First it sends a list of online user to the new user, then it reads the new users username and notify all other user about
 * the new user.
 * 
 * @author Adam Joseph
 * @version UserThread 1:1
 */

public class UserThread extends Thread{

    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;

    public UserThread(Socket socket, ChatServer chatServer) {

        this.socket = socket;
        this.server = chatServer;

    }

    public void run(){

        try{
            /**
             * Datastreams to send and recive data
             */
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            /**
             * Prints user to the connected client
             */
            printUsers();

            /**
             * Reads the new clients username and notify all other user that a new user has connected.
             */
            String userName = reader.readLine();
            server.addUserName(userName);

            String serverMessage = "New user connected: " + userName;
            server.broadcast(serverMessage, this);

            /**
             * A loop that reads messages from the connected user and sends it to all other connected user
             * until the user writes "bye".
             * All other user gets notified about the disconnected user
             */
            String clientMessage;

            do{
                clientMessage = reader.readLine();
                serverMessage = " [ " + userName + " ]: " + clientMessage;
                server.broadcast(serverMessage, this);
            }while (!clientMessage.equals("bye"));

            server.removeUser(userName, this);
            socket.close();

            serverMessage = userName + " has diconnected!";
            server.broadcast(serverMessage, this);
        } catch (IOException e){
            System.out.println("Error in UserThread" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Sends a list of connected users to the newly connected client.
     */
    public void printUsers(){
        if(server.hasUsers()){
            writer.println("Connected users: " + server.getUserNames());
        }
        else{
            writer.println("No users has connected.");
        }
    }

    /**
     * Sends a message to the client
     * @param message being sent
     */
    public void sendMessage(String message){
        
        writer.println(message);
    }


    
}
