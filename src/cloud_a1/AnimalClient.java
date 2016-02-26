package cloud_a1;

import java.io.IOException;
import java.util.Scanner;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;

public class AnimalClient {
	private static final String URL = "http://localhost:8080";
	private static ResponseHandler<String> responseHandler = new BasicResponseHandler();

	public static void main(final String... args) throws IOException {
		Gson gson = new Gson();
		CloseableHttpClient httpclient = HttpClients.createMinimal();

		System.out.println("Let's play a game! Please choose the method of serialization "
				+ "by entering the corresponding number" + " 1. json or 2. protobuf");

		Scanner in = new Scanner(System.in);
		int choice = in.nextInt();
		in.nextLine(); // throw away \n
		boolean json = choice == 1;
		String message = json ? "You have chosen to use text serialization (json)"
				: "You have chosen to use binary serialization (protobuf)";
		message += " Please respond to each question by entering yes or no";

		System.out.println(message);

		HttpGet httpGet;
		// if (json) {
		httpGet = new HttpGet(URL + "/gamePlayJson");
		String responseBody = httpclient.execute(httpGet, responseHandler);
		System.out.println("responseBody " + responseBody);	//remove this
		Node node = gson.fromJson(responseBody, Node.class);
		System.out.print(node.question);
		String yesOrNo = in.nextLine();
		// } else {
		// do nothing
		// }

		boolean done = false;
		int i = 5;
		int id = node.id;
		while (!done || (i > 0)) {
			HttpPost httpPost = new HttpPost(URL + "/gamePlayJson");
			if (!"".equals(yesOrNo)) {
				String content = gson.toJson(new QuestionAnswerResponse(id, yesOrNo));
//				System.out.println("Content " + content);
				StringEntity entity = new StringEntity(content, Handler.CHARSET);
				httpPost.setEntity(entity);
			}
			String sResponse = httpclient.execute(httpPost, responseHandler);
//			System.out.println(sResponse);
			node = gson.fromJson(sResponse, Node.class);

			if (node.question.equals("done")) {
				done = true;
				System.out.println("Well, that's it!");
			} else {
				id = node.id;
				System.out.print(node.question);
				yesOrNo = in.nextLine();
			}
			i--;
		}
		in.close();
	}
}
