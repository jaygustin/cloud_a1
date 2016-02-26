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
		animalTree.addRootNode(50, "Does it fly?");
			animalTree.addYesChild(50, 30, "Does it have feathers?");
				animalTree.addYesChild(30, 20, "Does it go quack?");
					animalTree.addYesChild(20, 10, "Is it a duck?");
					animalTree.addNoChild(20, 25, "Is it a chicken?");
				animalTree.addNoChild(30, 35, "Does it collect honey?");
					animalTree.addYesChild(35, 40, "Is it a bat?");
					animalTree.addNoChild(35, 28, "Is it a bee?");
			animalTree.addNoChild(50, 75, "Does it have scales?");
				animalTree.addYesChild(75, 60, "Is it armless?");
					animalTree.addYesChild(60, 5, "Is it a snake?");
					animalTree.addNoChild(60, 65, "Is it an alligator?");
				animalTree.addNoChild(75, 80, "Does it bark?");
					animalTree.addYesChild(80, 77, "Is it a dog?");
					animalTree.addNoChild(80, 85, "Is it a cow?");
		final HttpServer server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG);

		server.createContext("/gamePlayJson", new JsonHandler("Think of an animal...", animalTree));
		server.createContext("/gamePlayProtoBuf", new ProtoBufHandler("Think of an animal...", animalTree));
		server.start();
	}
}
