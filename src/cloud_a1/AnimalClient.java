package cloud_a1;

import java.io.IOException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class AnimalClient {

    public static void main(final String... args) throws IOException {
    	CloseableHttpClient httpclient = HttpClients.createDefault();
    	
    	
    	System.out.println("Let's play a game! Please choose the method of serialization "
    			+ "by entering the corresponding number"
    			+ " 1. json or 2. protobuf");
    	int choice = System.in.read();
    	String message = choice == 1 ? "You have chosen to use text serialization (json)" : "You have chosen to use binary serialization (protobuf)";
    	message += " Please respond to each question by entering yes or no";
    	
    	System.out.println(message);
    	
    	for (int i = 0; i<5; i++) {
    		
    	}
    	
    }
}
