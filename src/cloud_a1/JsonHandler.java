package cloud_a1;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
				final Map<String, List<String>> requestParameters = getRequestParameters(he.getRequestURI());
				// do something with the request parameters
				if (requestParameters.isEmpty()) {
					responseBody = defaultResponse;
				} else {
					responseBody = getResponse(requestParameters);
				}
				headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
				final byte[] rawResponseBody = responseBody.getBytes(CHARSET);
				he.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
				he.getResponseBody().write(rawResponseBody);
				break;
			case METHOD_POST:
				handlePostResponse(he, headers);
			case METHOD_OPTIONS:
				headers.set(HEADER_ALLOW, ALLOWED_METHODS);
				he.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
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

	void handlePostResponse(HttpExchange he, Headers headers) {
		headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
		String response = he.getResponseBody().toString();
		QuestionAnswerResponse qa = gson.fromJson(response, QuestionAnswerResponse.class);
		if (qa != null) {
			Node nextNode = animalTree.getNextNode(qa.nodeId, qa.yes);
			try {
				he.getResponseBody().write(buildJsonForNode(nextNode).getAsByte());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {

		}
	}

	JsonObject buildJsonForNode(Node node) {
		JsonObject json = new JsonObject();
		json.addProperty("id", node.key);
		json.addProperty("question", node.name);
		return json;
	}

	int extractNodeId(JsonObject json) {
		JsonObject idObject = json.getAsJsonObject("id");
		if (idObject != null) {
			return idObject.getAsInt();
		}
		return 0;
	}
}
