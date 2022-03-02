package server;

import java.io.*;
import java.net.*;
import java.util.*;

import threads.UserThread;

/**
 * This class starts a server listening to a specific port. When a new client connect an instance of UserThread is created
 * to serve that client.
 * Each connection is processed in seperates thread and therefor the server is able to handle multiple clients at the same time.
 * 
 * @author Adam Joseoh
 * @version ChatServer 1:1
 */

public class ChatServer extends Thread{
    
    private int port;
    /**
     * Keeps track of connected clients usernames and threads. 
     * Set is used because it doesn't allow duplicates and the order of elements doesn't matter.
     */
    private Set<String> userNames = new HashSet<>(); 
    private Set<UserThread> userThreads = new HashSet<>();

    /**
     * Constructor for the server that takes has a port as an argument
     * @param port
     */
    public ChatServer(int port){
        
        this.port = port;
    
}
    /**
     * An operation to start a server and connect a client to that port.
     * @throws IOException
     */
    public void run(){

        try (ServerSocket serverSocket = new ServerSocket(port)){
            
                System.out.println("Server is listening to port: " + port);

                while(true){
                
                    Socket socket = serverSocket.accept();
                    System.out.println("New User is connected");

                    UserThread newUser = new UserThread(socket, this);
                    userThreads.add(newUser);
                    newUser.start();

                    }

            } catch(IOException e){
                System.out.println("Error in server: " + e.getMessage());
                e.printStackTrace();
            }
        }
    
        /**
         * Delivers a message from one user to others (broadcasting)
         * @param message being sent
         * @param excludeUser excludes the user sending a message to send the message to itself.
         */
    public void broadcast(String message, UserThread excludeUser){
        for (UserThread aUser : userThreads){
            if(aUser != excludeUser){
                aUser.sendMessage(message);
            }
        }
    }

    /**
     * Stores the username to the newly connected client 
     * @param userName takes the userName variable thats being stored.
     */
    public void addUserName(String userName){
        userNames.add(userName);
    }

    /**
     * When a client disconnects this method removes the client and UserThread association 
     * @param userName is the clients username that was stored 
     * @param aUser is the associated thread for that client
     */
    public void removeUser(String userName, UserThread aUser){
        boolean removed = userNames.remove(userName);
        if(removed){
            userThreads.remove(aUser);
            System.out.println("the user " + userName + " disconnected");
        }
    }

    /**
     * Returns a list of usernames thats been stored when clients connect.
     * @return list of usernames
     */
    public Set<String> getUserNames(){

        return this.userNames;
    }

    /**
     * Checks if the server has other connected users
     * @return true if the server has other connected users (not counting the client calling this method)
     */
    public boolean hasUsers(){

        return !this.userNames.isEmpty();

    }

    public static void main(String[] args) throws IOException{

        if (args.length < 1) {
            System.out.println("Syntax: java ChatServer <port-number>");
            System.exit(0);
        }
 
        int port = Integer.parseInt(args[0]);
 
        ChatServer server = new ChatServer(port);
        server.start();
    }


}
