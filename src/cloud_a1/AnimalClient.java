package cloud_a1;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;

public class AnimalClient {
	private static final String URL = "http://localhost:8080";
	private static ResponseHandler<String> responseHandler = new BasicResponseHandler();
	private static Gson gson = new Gson();
	private static Scanner in = new Scanner(System.in);

	public static void main(final String... args) throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		System.out.println("Let's play a game! Please choose the method of serialization "
				+ "by entering the corresponding number" + " 1. json or 2. protobuf");

		int choice = in.nextInt();
		in.nextLine(); // throw away \n
		boolean json = choice == 1;
		String message = json ? "You have chosen to use text serialization (json)"
				: "You have chosen to use binary serialization (protobuf)";
		message += " Please respond to each question by entering yes or no";

		System.out.println(message);

		if (json) {
			runJsonVersion(in, httpclient);
		} else {
			runProtoVersion(in, httpclient);
		}

		in.close();
	}

	static void runJsonVersion(Scanner in, CloseableHttpClient httpclient) throws ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet(URL + "/gamePlayJson");
		String responseBody = httpclient.execute(httpGet, responseHandler);
		Node node = gson.fromJson(responseBody, Node.class);
		System.out.print(node.question);

		int id = node.id;
		while (true) {
			HttpPost httpPost = new HttpPost(URL + "/gamePlayJson");
			String content = gson.toJson(new QuestionAnswerResponse(id, yesOrNo(in)));
			StringEntity entity = new StringEntity(content, Handler.CHARSET);
			httpPost.setEntity(entity);
			String sResponse = doPost(httpclient, httpPost, responseHandler);
			if (sResponse == null) {
				System.out.println("Server was busy, sorry about that. Re-run the program if you would like to play again.");
				break;
			}
			node = gson.fromJson(sResponse, Node.class);

			if (node.question.equals("done")) {
				System.out.println("Well, that's it! Re-run the program if you would like to play again.");
				break;
			} else {
				id = node.id;
				System.out.print(node.question);
			}
		}
	}

	private static void runProtoVersion(Scanner in, CloseableHttpClient httpclient) throws IOException {
		HttpGet httpGet = new HttpGet(URL + "/gamePlayProtoBuf");
		HttpResponse response = httpclient.execute(httpGet);
		InputStream is = response.getEntity().getContent();
		ResponseProtos.ServerResponse sr = ResponseProtos.ServerResponse.parseFrom(is);
		System.out.print(sr.getQuestion());

		int id = sr.getId();
		while (true) {
			HttpPost httpPost = new HttpPost(URL + "/gamePlayProtoBuf");
			byte[] content = ResponseProtos.ClientResponse.newBuilder().setId(id).setYes(yesOrNo(in)).build()
					.toByteArray();
			ByteArrayEntity entity = new ByteArrayEntity(content);
			httpPost.setEntity(entity);
			response = doPost(httpclient, httpPost);
			if (response == null) {
				System.out.println("Server was busy, sorry about that. Re-run the program if you would like to play again.");
				break;
			}
			is = response.getEntity().getContent();
			sr = ResponseProtos.ServerResponse.parseFrom(is);

			if (sr.getQuestion().equals("done")) {
				System.out.println("Well, that's it! Re-run the program if you would like to play again.");
				break;
			} else {
				id = sr.getId();
				System.out.print(sr.getQuestion());
			}
		}
	}

	static private boolean yesOrNo(Scanner in) {
		while (true) {
			String yesOrNo = in.nextLine();
			if (yesOrNo.equals("yes")) {
				return true;
			} else if (yesOrNo.equals("no")) {
				return false;
			}
		}
	}

	static HttpResponse doPost(CloseableHttpClient httpclient, HttpPost httpPost)
			throws ClientProtocolException, IOException {
		try {
			return httpclient.execute(httpPost);
		} catch (NoHttpResponseException e) {
			System.out.println("No response from server. Try this request again? yes/no");
			if (in.nextLine().contains("yes")) {
				// try again
				return doPost(httpclient, httpPost);
			}
		}
		return null;
	}

	static String doPost(CloseableHttpClient httpclient, HttpPost httpPost, ResponseHandler<String> handler)
			throws ClientProtocolException, IOException {
		try {
			return httpclient.execute(httpPost, handler);
		} catch (NoHttpResponseException e) {
			System.out.println("No response from server. Try this request again? yes/no");
			if (in.nextLine().contains("yes")) {
				// try again
				return doPost(httpclient, httpPost, handler);
			}
		}
		return null;
	}
}
