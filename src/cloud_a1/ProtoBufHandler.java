package cloud_a1;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public class ProtoBufHandler extends Handler {

	public ProtoBufHandler(String defaultResponse, BinaryTree animalTree) {
		super(defaultResponse, animalTree);
	}

	public void handle(HttpExchange he) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
