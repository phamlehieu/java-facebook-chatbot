package service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import model.Address;
import model.Adjustment;
import model.Button;
import model.Button.ButtonType;
import service.utils.MessageBuilder;
import model.Element;
import model.Receipt;
import model.ReceiptElement;
import model.Summary;

/**
 * This class take the responsibility to process message from controller and do proper action
 */
@Service
public class MessageProcessingService {
	@Autowired
	public SendMessageService sendMessageService;

	@Autowired
	public MessageBuilder messageBuilder;

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessingService.class);

	/**
	 * Forward message to suitable handler base on message's type
	 * https://developers.facebook.com/docs/messenger-platform/webhook-reference#webhook_setup
	 * 
	 * @param messagingObj
	 */
	public void process(JSONObject messagingObj) {
		if (messagingObj.has("optin")) {
			onAuthentication(messagingObj);
		} else if (messagingObj.has("message")) {
			onMessage(messagingObj);
		} else if (messagingObj.has("delivery")) {
			onDelivery(messagingObj);
		} else if (messagingObj.has("postback")) {
			onPostback(messagingObj);
		}
	}

	/**
	 * Handle authentication callbacks
	 * https://developers.facebook.com/docs/messenger-platform/webhook-reference#auth
	 * 
	 * @param object
	 *            JSON data of message
	 */
	public void onAuthentication(JSONObject object) {
		LOGGER.info("Handle authentication");
		// TODO Authentication
	}

	/**
	 * Handle message-delivered callbacks
	 * https://developers.facebook.com/docs/messenger-platform/webhook-reference#message_delivery
	 * 
	 * @param object
	 *            JSON data of message
	 */
	public void onDelivery(JSONObject object) {
		LOGGER.info("Handle delivery confirmation event");
		// TODO Delivery confirmation
	}

	/**
	 * Handle postback callbacks
	 * https://developers.facebook.com/docs/messenger-platform/webhook-reference#postback
	 * 
	 * @param object
	 *            JSON data of message
	 */
	public void onPostback(JSONObject object) {
		LOGGER.info("Handle postback event");
		// TODO handle postback
	}

	/**
	 * Handle message-received callbacks
	 * https://developers.facebook.com/docs/messenger-platform/webhook-reference#received_message
	 * 
	 * @param object
	 *            JSON data of message
	 */
	public void onMessage(JSONObject object) {
		LOGGER.info("Handle message event");

		// Get the sender id to send message back
		JSONObject sender = object.getJSONObject("sender");
		String senderId = sender.getString("id");

		if (!StringUtils.isEmpty(senderId)) {
			JSONObject message = object.getJSONObject("message");

			// Receive a text message
			if (message.has("text")) {
				// Convert escape unicode character to normal form
				String text = StringEscapeUtils.unescapeJava(message.getString("text"));
				LOGGER.info("Receive text: " + text);
				JSONObject data = null;

				// Build the data to response user input
				if (text.toLowerCase().contains("button")) {
					data = buildSampleButtonMessage(senderId);
				} else if (text.toLowerCase().contains("image")) {
					data = buildSampleImageMessage(senderId);
				} else if (text.toLowerCase().contains("generic")) {
					data = buildSampleGenericMessage(senderId);
				} else if (text.toLowerCase().contains("receipt")) {
					data = buildSampleReceiptMessage(senderId);
				} else {
					data = buildSampleTextMessage(senderId);
				}

				// Call SendMessageService to send message back
				sendMessageService.sendMessage(data);

				// Receive an attachment
			} else if (message.has("attachments")) {
				LOGGER.info("Receive attachment");
				JSONObject data = buildSampleTextMessage(senderId);
				sendMessageService.sendMessage(data);

				// For message with unknown type
			} else {
				LOGGER.info("Receive unknown type message");
			}
		}

	}

	/**
	 * This function using MessageBuilder to build sample button message
	 * https://developers.facebook.com/docs/messenger-platform/send-api-reference#examples
	 * 
	 * @param recipientId
	 *            ID of the recipient of your message
	 * @return a sample button message in JSONObject format
	 */
	private JSONObject buildSampleButtonMessage(String recipientId) {
		List<Button> buttons = new ArrayList<Button>();
		buttons.add(new Button(ButtonType.WEB_URL, "Show Website", "https://petersapparel.parseapp.com"));
		buttons.add(new Button(ButtonType.POSTBACK, "Start Chatting", "USER_DEFINED_PAYLOAD"));
		return messageBuilder.buildButtonMessage(recipientId, "What do you want to do next?", buttons);
	}

	/**
	 * This function using MessageBuilder to build sample receipt message
	 * https://developers.facebook.com/docs/messenger-platform/send-api-reference#examples
	 * 
	 * @param recipientId
	 *            ID of the recipient of your message
	 * @return a sample receipt message in JSONObject format
	 */
	private JSONObject buildSampleReceiptMessage(String recipientId) {
		List<ReceiptElement> elements = new ArrayList<ReceiptElement>();
		elements.add(new ReceiptElement("Classic White T-Shirt", "100% Soft and Luxurious Cotton", 2, 50, "USD",
				"http://petersapparel.parseapp.com/img/whiteshirt.png"));
		elements.add(new ReceiptElement("Classic Gray T-Shirt", "100% Soft and Luxurious Cotton", 1, 25, "USD",
				"http://petersapparel.parseapp.com/img/grayshirt.png"));
		List<Adjustment> adjustments = new ArrayList<Adjustment>();
		adjustments.add(new Adjustment("New Customer Discount", 20));
		adjustments.add(new Adjustment("$10 Off Coupon", 10));
		Summary summary = new Summary(75, 4.95, 6.19, 56.14);
		Address address = new Address("1 Hacker Way", "Menlo Park", "94025", "CA", "US");
		Receipt receipt = new Receipt("Stephane Crozatier", "12345678902", "USD", "Visa 2345", "1428444852",
				"http://petersapparel.parseapp.com/order?order_id=123456", summary, adjustments, elements, address);
		return messageBuilder.buildReceiptMessage(recipientId, receipt);
	}

	/**
	 * This function using MessageBuilder to build sample text message
	 * https://developers.facebook.com/docs/messenger-platform/send-api-reference#examples
	 * 
	 * @param recipientId
	 *            ID of the recipient of your message
	 * @return a sample text message in JSONObject format
	 */
	private JSONObject buildSampleTextMessage(String recipientId) {
		return messageBuilder.buildTextMessage(recipientId, "hello, world!");
	}

	/**
	 * This function using MessageBuilder to build sample text message
	 * https://developers.facebook.com/docs/messenger-platform/send-api-reference#examples
	 * 
	 * @param recipientId
	 *            ID of the recipient of your message
	 * @return a sample text message in JSONObject format
	 */
	private JSONObject buildSampleImageMessage(String recipientId) {
		return messageBuilder.buildImageMessage(recipientId, "https://petersapparel.com/img/shirt.png");
	}

	/**
	 * This function using MessageBuilder to build sample generic message
	 * https://developers.facebook.com/docs/messenger-platform/send-api-reference#examples
	 * 
	 * @param recipientId
	 *            ID of the recipient of your message
	 * @return a sample generic message in JSONObject format
	 */
	private JSONObject buildSampleGenericMessage(String recipientId) {
		List<Element> itemList = new ArrayList<Element>();
		List<Button> whiteShirtButtons = new ArrayList<Button>();
		whiteShirtButtons.add(new Button(ButtonType.WEB_URL, "View Item",
				"https://petersapparel.parseapp.com/view_item?item_id=100"));
		whiteShirtButtons.add(
				new Button(ButtonType.WEB_URL, "Buy Item", "https://petersapparel.parseapp.com/buy_item?item_id=100"));
		whiteShirtButtons.add(new Button(ButtonType.POSTBACK, "Bookmark Item", "USER_DEFINED_PAYLOAD_FOR_ITEM100"));
		itemList.add(new Element("Classic White T-Shirt", "", "http://petersapparel.parseapp.com/img/item100-thumb.png",
				"Soft white cotton t-shirt is back in style", whiteShirtButtons));

		List<Button> grayShirtButtons = new ArrayList<Button>();
		grayShirtButtons.add(new Button(ButtonType.WEB_URL, "View Item",
				"https://petersapparel.parseapp.com/view_item?item_id=101"));
		grayShirtButtons.add(
				new Button(ButtonType.WEB_URL, "Buy Item", "https://petersapparel.parseapp.com/buy_item?item_id=101"));
		grayShirtButtons.add(new Button(ButtonType.POSTBACK, "Bookmark Item", "SER_DEFINED_PAYLOAD_FOR_ITEM101"));
		itemList.add(new Element("Classic Grey T-Shirt", "", "http://petersapparel.parseapp.com/img/item101-thumb.png",
				"Soft gray cotton t-shirt is back in style", grayShirtButtons));
		return messageBuilder.buildGenericMessage(recipientId, itemList);
	}
}
