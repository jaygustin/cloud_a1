package cloud_a1;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Handler implements HttpHandler {

	static final String HEADER_ALLOW = "Allow";
	static final String HEADER_CONTENT_TYPE = "Content-Type";

	static final Charset CHARSET = StandardCharsets.UTF_8;

	static final int STATUS_OK = 200;
	static final int STATUS_METHOD_NOT_ALLOWED = 405;

	static final int NO_RESPONSE_LENGTH = -1;

	static final String METHOD_GET = "GET";
	static final String METHOD_POST = "POST";
	static final String METHOD_OPTIONS = "OPTIONS";
	static final String ALLOWED_METHODS = METHOD_GET + "," + METHOD_OPTIONS;

	String defaultResponse;
	String responseBody;
	Gson gson = new Gson();
	BinaryTree animalTree;

	public Handler(String defaultResponse, BinaryTree animalTree) {
		this.defaultResponse = defaultResponse;
		this.animalTree = animalTree;
	}

	@Override
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
				handlePostResponse(he);
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

	void handlePostResponse(HttpExchange he) {
		// TODO Auto-generated method stub

	}

	String getResponse(Map<String, List<String>> requestParameters) {
		return defaultResponse;
	}

	static Map<String, List<String>> getRequestParameters(final URI requestUri) {
		final Map<String, List<String>> requestParameters = new LinkedHashMap<>();
		final String requestQuery = requestUri.getRawQuery();
		if (requestQuery != null) {
			final String[] rawRequestParameters = requestQuery.split("[&;]", -1);
			for (final String rawRequestParameter : rawRequestParameters) {
				final String[] requestParameter = rawRequestParameter.split("=", 2);
				final String requestParameterName = decodeUrlComponent(requestParameter[0]);
				requestParameters.putIfAbsent(requestParameterName, new ArrayList<>());
				final String requestParameterValue = requestParameter.length > 1
						? decodeUrlComponent(requestParameter[1]) : null;
				requestParameters.get(requestParameterName).add(requestParameterValue);
			}
		}
		return requestParameters;
	}

	static String decodeUrlComponent(final String urlComponent) {
		try {
			return URLDecoder.decode(urlComponent, CHARSET.name());
		} catch (final UnsupportedEncodingException ex) {
			throw new InternalError(ex);
		}
	}
}
