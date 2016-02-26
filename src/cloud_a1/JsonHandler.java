package cloud_a1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class JsonHandler extends Handler {

	public JsonHandler(String defaultResponse, BinaryTree animalTree) {
		super(defaultResponse, animalTree);
	}

	public void handle(HttpExchange he) throws IOException {
		try {
			final Headers headers = he.getResponseHeaders();
			final String requestMethod = he.getRequestMethod().toUpperCase();
			switch (requestMethod) {
			case METHOD_GET:
				// start game at root node
				responseBody = buildJsonForNode(animalTree.root).toString();
				headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
				final byte[] rawResponseBody = responseBody.getBytes(CHARSET);
				he.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
				he.getResponseBody().write(rawResponseBody);
				break;
			case METHOD_POST:
				handlePostResponse(he, headers);
				break;
			default:
				headers.set(HEADER_ALLOW, ALLOWED_METHODS);
				he.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
				break;
			}
		} finally {
			he.close();
		}
	}

	void handlePostResponse(HttpExchange he, Headers headers) throws IOException {
		headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
		InputStreamReader isr = new InputStreamReader(he.getRequestBody(), CHARSET);
		BufferedReader br = new BufferedReader(isr);
		String response = br.readLine();
		QuestionAnswerResponse qa = gson.fromJson(response, QuestionAnswerResponse.class);
		if (qa != null) {
			Node nextNode = animalTree.getNextNode(qa.id, qa.yes);
			if (nextNode == null) {
				sendResponse(he, buildJsonForNode(doneNode).toString());
			} else {
				sendResponse(he, buildJsonForNode(nextNode).toString());
			}
		} else {
			//something went wrong
			sendResponse(he, buildJsonForNode(doneNode).toString());
		}
	}

	JsonObject buildJsonForNode(Node node) {
		JsonObject json = new JsonObject();
		json.addProperty("id", node.id);
		json.addProperty("question", node.question);
		return json;
	}

	void sendResponse(HttpExchange he, String responseBody) throws IOException {
		final byte[] rawResponseBody = responseBody.getBytes(CHARSET);
		he.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
		he.getResponseBody().write(rawResponseBody);
	}
}
