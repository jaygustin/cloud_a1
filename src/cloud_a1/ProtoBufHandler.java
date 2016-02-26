package cloud_a1;

import java.io.IOException;
import java.io.InputStream;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class ProtoBufHandler extends Handler {

	public ProtoBufHandler(String defaultResponse, BinaryTree animalTree) {
		super(defaultResponse, animalTree);
	}

	public void handle(HttpExchange he) throws IOException {
		try {
			final Headers headers = he.getResponseHeaders();
			final String requestMethod = he.getRequestMethod().toUpperCase();
			switch (requestMethod) {
			case METHOD_GET:
				// start game at root node
				byte[] responseBodyByte = buildByteArrayForNode(animalTree.root);
				headers.set(HEADER_CONTENT_TYPE, String.format("application/x-google-protobuf"));
				he.sendResponseHeaders(STATUS_OK, responseBodyByte.length);
				he.getResponseBody().write(responseBodyByte);
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
		headers.set(HEADER_CONTENT_TYPE, String.format("application/x-google-protobuf"));
		InputStream is = he.getRequestBody();
		ResponseProtos.ClientResponse cr;
		try {
			cr = ResponseProtos.ClientResponse.parseFrom(is);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		if (cr != null) {
			Node nextNode = animalTree.getNextNode(cr.getId(), cr.getYes());
			if (nextNode == null) {
				sendResponse(he, buildByteArrayForNode(doneNode));
			} else {
				sendResponse(he, buildByteArrayForNode(nextNode));
			}
		} else {
			//something went wrong
			sendResponse(he, buildByteArrayForNode(doneNode));
		}
	}

	private void sendResponse(HttpExchange he, byte[] rawResponseBody) throws IOException {
		he.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
		he.getResponseBody().write(rawResponseBody);
	}

	private byte[] buildByteArrayForNode(Node node) {
		return ResponseProtos.ServerResponse.newBuilder().setId(node.id).setQuestion(node.question).build().toByteArray();
	}

}
