package threads;

import java.net.*;

import clients.ChatClient;

import java.io.*;

/**
 * This class is responsible to read input from the client and send it to the server 
 * and continues to do so, by and infinite loop, until the clients types "bye"
 */
public class WriteThread extends Thread{

    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;

    public WriteThread (Socket socket, ChatClient client){

        this.socket = socket;
        this.client = client;

        try{

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

        } catch (IOException e){
            System.out.println("Error getting output streams: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void run(){
        
        Console console = System.console();

        String userName = console.readLine("\nEnter your name: ");
        client.setUserName(userName);
        writer.println(userName);

        String text;

        do{

            text = console.readLine(" [" + client.getUserName() + "]: ");
            writer.println(text);

        } while (!text.equals("bye"));

        try {
            socket.close();
        } catch (IOException e){
            System.out.println("Error writing to server: " + e.getMessage());
        }
    }
    
}
