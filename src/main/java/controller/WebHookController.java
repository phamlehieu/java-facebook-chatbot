package controller;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import service.MessageProcessingService;
import service.utils.CryptoUtils;

/**
 * Handle request from facebook
 */
@RestController
@ComponentScan(basePackages = "service")
public class WebHookController {
	@Value("${validation.token}")
	private String VALIDATION_TOKEN;
	
	@Value("${app.secret}")
	private String APP_SECRET;

	@Autowired
	private MessageProcessingService messageProcessingService;

	private static final Logger LOGGER = LoggerFactory.getLogger(WebHookController.class);

	/**
	 * Use for setup facebook webhook
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param token
	 *            verify token (You provide this token to facebook)
	 * @param challenge
	 *            challenge string
	 * @return challenge string if verify successful, otherwise response with
	 *         HTTP status 403
	 */
	@RequestMapping(value = "/webhook", method = RequestMethod.GET)
	public ResponseEntity<String> webhookVerify(HttpServletRequest request,
			@RequestParam(name = "hub.verify_token") String token,
			@RequestParam(name = "hub.challenge") String challenge) {
		LOGGER.info("Receive GET request to /webhook");
		
		if (VALIDATION_TOKEN.equals(token)) {
			LOGGER.info("Verify successful. Send back hub.challenge ");
			return new ResponseEntity<String>(challenge, HttpStatus.OK);
		} else {
			LOGGER.error("Wrong validation token");
			return new ResponseEntity<String>("Wrong validation token", HttpStatus.FORBIDDEN);
		}
	}

	/**
	 * Controller to handler webhook callback, parse the content of request to
	 * messaging object and pass to another service to process
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param body
	 *            content of POST request
	 * @return send back a 200
	 */
	@RequestMapping(value = "/webhook", method = RequestMethod.POST)
	public ResponseEntity<String> webhookMessage(HttpServletRequest request, @RequestBody String body) {
		LOGGER.info("Receive POST request to webhook");
		LOGGER.info("Request body: " + body);
		if (request.getHeader("x-hub-signature") != null) {
			String signature = request.getHeader("x-hub-signature").split("=")[1];
			if (!verifyRequestSignature(signature, APP_SECRET, body, "HmacSHA1")) {
				LOGGER.info("Verify request signature failed");
				return new ResponseEntity<String>("",HttpStatus.UNAUTHORIZED);
			}
		} else {
			LOGGER.info("Missing x-hub-signature in request header");
			return new ResponseEntity<String>("",HttpStatus.BAD_REQUEST);
		}

		
		JSONObject object = new JSONObject(body);
		JSONArray entryArray = object.getJSONArray("entry");
		
		for (int i = 0; i < entryArray.length(); i++) {
			JSONObject entryItem = entryArray.getJSONObject(i);
			JSONArray messagingArray = entryItem.getJSONArray("messaging");
			for (int j = 0; j < messagingArray.length(); j++) {
				JSONObject messagingItem = messagingArray.getJSONObject(j);
				messageProcessingService.process(messagingItem);
			}
		}
		
		return new ResponseEntity<String>("", HttpStatus.OK);
	}
	
	private boolean verifyRequestSignature(String signature, String secret, String data, String algorithm) {
		try {
			String expectSignature = CryptoUtils.createHMAC(secret, data, algorithm);
			if (signature.equals(expectSignature)) {
				return true;
			}
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("Invalid agorithm " + e.getMessage());
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			LOGGER.error("Invalid key " + e.getMessage());
		}
		return false;
	}
}