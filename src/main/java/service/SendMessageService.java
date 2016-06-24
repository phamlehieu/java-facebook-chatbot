package service;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * This class take responsibility to send message back to Facebook via graph API
 */
@Service
public class SendMessageService {
	@Value("${graph.url}")
	private String SEND_API_URL;

	@Value("${page.access.token}")
	private String PAGE_ACCESS_TOKEN;
	
	@Value("${socket.timeout}")
	private int SOCKET_TIMEOUT;
	
	@Value("${connect.timeout}")
	private int CONNECT_TIMEOUT;
	
	@Value("${max.connection}")
	private int MAX_CONNECTION;

	private static final Logger LOGGER = LoggerFactory.getLogger(SendMessageService.class);
	private static CloseableHttpAsyncClient client;
	
	/**
	 * Initialize Async HTTP client
	 */
	SendMessageService() {
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();
		client = HttpAsyncClients.custom().setMaxConnTotal(MAX_CONNECTION).setDefaultRequestConfig(requestConfig).build();
		client.start();
	}
	
	/**
	 * Send message back to Facebook via graph API
	 * @param data
	 */
	public void sendMessage(JSONObject data) {
		try {
			// Build the request
			URI uri = new URIBuilder(SEND_API_URL).setParameter("access_token", PAGE_ACCESS_TOKEN).build();
			HttpPost request = new HttpPost(uri);
			request.setHeader("Content-Type", "application/json");
			String jsonData = data.toString();
			StringEntity body = new StringEntity(jsonData, "UTF-8");
			request.setEntity(body);
			
			
			// Execute request async
			LOGGER.info("Send message with data: " + data.toString());
			client.execute(request, new FutureCallback<HttpResponse>() {
				// Callback when send message completed
				public void completed(HttpResponse result) {
					LOGGER.info("Send message completed");
				}
				// Callback when send message failed
				public void failed(Exception ex) {
					LOGGER.error("Send message failed");
					LOGGER.error(ex.getMessage());
				}
				// Callback when send message cancelled
				public void cancelled() {
					LOGGER.info("Send message cancelled");
				}
			});
		} catch (URISyntaxException ex) {
			LOGGER.error(ex.getMessage());
		}
	}
}
