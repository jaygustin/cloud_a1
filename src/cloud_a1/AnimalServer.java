package cloud_a1;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class AnimalServer {
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 8080;
	private static final int BACKLOG = 1;

	public static void main(final String... args) throws IOException {
		BinaryTree animalTree = new BinaryTree();
		animalTree.addNode(50, "Does it fly?");
			animalTree.addNode(30, "Does it have feathers?");
				animalTree.addNode(20, "Does it go quack?");
					animalTree.addNode(10, "Is it a duck?");
					animalTree.addNode(25, "Is it a chicken?");
				animalTree.addNode(35, "Does it collect honey?");
					animalTree.addNode(40, "Is it a bat?");
					animalTree.addNode(28, "Is it a bee?");
			animalTree.addNode(75, "Does it have scales?");
				animalTree.addNode(60, "Is it armless?");
					animalTree.addNode(5, "Is it a snake?");
					animalTree.addNode(65, "Is it an alligator?");
				animalTree.addNode(80, "Does it bark?");
					animalTree.addNode(77, "Is it a dog?");
					animalTree.addNode(85, "Is it a cow?");
		final HttpServer server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG);

		server.createContext("/", new Handler("Let's play a game", animalTree));
		server.createContext("/gamePlayJson", new JsonHandler("Think of an animal...", animalTree));
		server.createContext("/gamePlayProtoBuf", new ProtoBufHandler("Think of an animal...", animalTree));
		server.start();
	}
}
