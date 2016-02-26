package cloud_a1;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class AnimalClient {
	private static final String URL = "http://localhost:8080";

	public static void main(final String... args) throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		System.out.println("Let's play a game! Please choose the method of serialization "
				+ "by entering the corresponding number" + " 1. json or 2. protobuf");
		int choice = System.in.read();
		boolean json = choice == 1;
		String message = json ? "You have chosen to use text serialization (json)"
				: "You have chosen to use binary serialization (protobuf)";
		message += " Please respond to each question by entering yes or no";

		System.out.println(message);

		HttpGet httpGet;
		if (json) {
			httpGet = new HttpGet(URL + "/gamePlayJson");
			CloseableHttpResponse response1 = httpclient.execute(httpGet);
			HttpEntity entity = response1.getEntity();
			if (entity != null) {
				System.out.println(entity.getContent().toString());
			}
		} else {
			// do nothing
		}
		for (int i = 0; i < 5; i++) {

		}
	}
}
