package controller;

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

/**
 * Handle request from facebook
 */
@RestController
@ComponentScan(basePackages = "service")
public class WebHookController {
	@Value("${validation.token}")
	public String VALIDATION_TOKEN;

	@Autowired
	public MessageProcessingService messageProcessingService;

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
		LOGGER.info("Receive verify request");

		if (VALIDATION_TOKEN.equals(token)) {
			LOGGER.info("Verify successful. Send back hub.challenge ");
			return new ResponseEntity<String>(challenge, HttpStatus.OK);
		} else {
			LOGGER.error("Wrong validation token");
			return new ResponseEntity<String>("Error, wrong validation token", HttpStatus.FORBIDDEN);
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
		LOGGER.info("Request body: " + body);
		
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
}