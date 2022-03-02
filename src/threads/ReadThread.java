package threads;

import java.net.*;
import java.io.*;
import clients.ChatClient;

/**
 * This class that is one Thread is responsible for reading inputs from the server and printing it to
 * the consol, doing so repeatedly.
 * It runs an infinite loop until the client disconnects from the server
 * 
 * @author Adam Joseph
 * @version ReadThread 1:1
 */
public class ReadThread extends Thread {

    private BufferedReader reader;
    private Socket socket;
    private ChatClient client;

    public ReadThread(Socket socket, ChatClient client){

        this.socket = socket;
        this.client = client;

        try {
            
            /**
             * Stream to read data. 
             * getInputStream() returns an input stream for the socket
             */
            InputStream input = socket.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));

            } catch (IOException e){
                System.out.println("Error getting input stream: " + e.getMessage());
                e.printStackTrace();
            }
    }

    /**
     * Runs the logic behind the thread.
     */
    public void run(){
        
        while(true){
            try {

                /**
                 * Reads the respons from the server.
                 */
                String respons = reader.readLine();
                System.out.println("\n" + respons);

                /**
                 * Prints out the username after displaying the server's message
                 */
                if(client.getUserName() != null){
                    System.out.print(" [" + client.getUserName() + "]: ");
                }
            } catch (IOException e){
                System.out.println("Error reading from server: " + e.getMessage());
                e.printStackTrace();
                break;
            }
        }
    }
    
}
