import java.io.IOException;

import clients.ChatClient;
import server.ChatServer;

public class Main {

    public static void main(String[] args) throws IOException{

        // ChatServer.main(new String[]{"8989"});
        ChatClient.main(new String[]{"127.0.0.1", "8989"});
        

    }
}
