package service.utils;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import model.Address;
import model.Adjustment;
import model.Button;
import model.Button.ButtonType;
import model.Element;
import model.Receipt;
import model.ReceiptElement;
import model.Summary;

/**
 * Helper class for create message
 */
@Component
public class MessageBuilder {

	/**
	 * Build the recipient part of message
	 * @param recipientId
	 * @return recipient part JSONObject
	 */
	private JSONObject buildRecipientPart(String recipientId) {
		JSONObject recepient = new JSONObject();
		recepient.put("id", recipientId);
		return recepient;
	}

	/**
	 * Build the image part of image message
	 * @param url URL of image
	 * @return image part JSONObject
	 */
	private JSONObject buildImageDataPart(String url) {
		JSONObject message = new JSONObject();
		JSONObject attachment = new JSONObject();
		JSONObject payload = new JSONObject();

		payload.put("url", url);
		attachment.put("type", "image");
		attachment.put("payload", payload);
		message.put("attachment", attachment);
		return message;
	}

	/**
	 * Build button in button message
	 * @param buttonItem structure of button
	 * @return JSONObject of a button
	 */
	private JSONObject buildButton(Button buttonItem) {
		JSONObject button = new JSONObject();
		button.put("title", buttonItem.getTitle());
		if (ButtonType.WEB_URL == buttonItem.getType()) {
			button.put("type", "web_url");
			button.put("url", buttonItem.getData());
		} else if (ButtonType.POSTBACK == buttonItem.getType()) {
			button.put("type", "postback");
			button.put("payload", buttonItem.getData());
		}
		return button;
	}

	/**
	 * Build element in generic message
	 * @param elementItem structure of element
	 * @return JSONObject of an element
	 */
	private JSONObject buildElement(Element elementItem) {
		JSONObject element = new JSONObject();
		JSONArray buttons = new JSONArray();
		for (Button buttonItem : elementItem.getButtons()) {
			buttons.put(buildButton(buttonItem));
		}

		element.put("title", elementItem.getTitle());
		element.put("subtitle", elementItem.getSubtitle());
		element.put("image_url", elementItem.getImageUrl());
		element.put("item_url", elementItem.getItemUrl());
		element.put("buttons", buttons);

		return element;
	}

	/**
	 * Build generic part in generic message
	 * @param elementList list element in generic message
	 * @return generic part JSONObject
	 */
	private JSONObject buildGenericDataPart(List<Element> elementList) {
		JSONObject message = new JSONObject();
		JSONObject attachment = new JSONObject();
		JSONObject payload = new JSONObject();
		JSONArray elements = new JSONArray();

		for (Element elementItem : elementList) {
			elements.put(buildElement(elementItem));
		}

		payload.put("template_type", "generic");
		payload.put("elements", elements);

		attachment.put("type", "template");
		attachment.put("payload", payload);

		message.put("attachment", attachment);
		return message;
	}

	/**
	 * Build receipt part in receipt message
	 * @param reicept structure of receipt
	 * @return receipt part JSONObject
	 */
	private JSONObject buildReceiptDataPart(Receipt reicept) {
		JSONObject message = new JSONObject();
		JSONObject attachment = new JSONObject();
		JSONObject payload = new JSONObject();
		
		JSONArray elements = new JSONArray();
		for (ReceiptElement elementItem : reicept.getReceiptElements()) {
			elements.put(buildReceiptElement(elementItem));
		}
		payload.put("elements", elements);
		
		payload.put("summary", buildReceiptSummary(reicept.getSummary()));
		
		if (reicept.getAddress() != null) {
			payload.put("address", buildReceiptAddress(reicept.getAddress()));
		}
		
		if (reicept.getAdjustments() != null && reicept.getAdjustments().size() > 0) {
			JSONArray adjustment = new JSONArray();
			for (Adjustment adjusmentItem : reicept.getAdjustments()) {
				adjustment.put(buildReceiptAdjustment(adjusmentItem));
			}
			payload.put("adjustments", adjustment);
		}

		payload.put("template_type", "receipt");
		payload.put("recipient_name", reicept.getRecipientName());
		payload.put("order_number", reicept.getOrderNumber());
		payload.put("currency", reicept.getCurrency());
		payload.put("payment_method", reicept.getPaymentMethod());
		payload.put("timestamp", reicept.getTimestamp());
		payload.put("order_url", reicept.getOrderUrl());

		attachment.put("type", "template");
		attachment.put("payload", payload);

		message.put("attachment", attachment);
		return message;
	}
	
	/**
	 * Build summary part of the receipt
	 * @param summaryItem structure of summary
	 * @return JSONObject of a summary
	 */
	private JSONObject buildReceiptSummary(Summary summaryItem) {
		JSONObject summary = new JSONObject();
		summary.put("subtotal", summaryItem.getSubtotal());
		summary.put("shipping_cost", summaryItem.getShippingCost());
		summary.put("total_tax", summaryItem.getTotalTax());
		summary.put("total_cost", summaryItem.getTotalCost());
		return summary;
	}

	/**
	 * Build adjustment part of the receipt
	 * @param adjustmentItem structure of adjustment
	 * @return JSONObject of a adjustment
	 */
	private JSONObject buildReceiptAdjustment(Adjustment adjustmentItem) {
		JSONObject adjustment = new JSONObject();
		adjustment.put("name", adjustmentItem.getName());
		adjustment.put("amount", adjustmentItem.getAmount());
		return adjustment;
	}
	
	/**
	 * Build address part of the receipt
	 * @param addressItem structure of address
	 * @return JSONObject of a address
	 */
	private JSONObject buildReceiptAddress(Address addressItem) {
		JSONObject address = new JSONObject();

		address.put("street_1", addressItem.getStreet1());
		address.put("street_2", addressItem.getStreet2());
		address.put("city", addressItem.getCity());
		address.put("postal_code", addressItem.getPostalCode());
		address.put("state", addressItem.getState());
		address.put("country", addressItem.getCountry());
		
		return address;
	}
	
	/**
	 * Build element in the receipt
	 * @param elementItem structure of element
	 * @return JSONObject of an receipt element
	 */
	private JSONObject buildReceiptElement(ReceiptElement elementItem) {
		JSONObject element = new JSONObject();

		element.put("title", elementItem.getTitle());
		element.put("subtitle", elementItem.getSubtitle());
		element.put("quantity", elementItem.getQuantity());
		element.put("price", elementItem.getPrice());
		element.put("currency", elementItem.getCurrency());
		element.put("image_url", elementItem.getImageUrl());
		
		return element;
	}

	/**
	 * Build button part in button message
	 * @param text content of button message
	 * @param buttons list of button in button message
	 * @return button part JSONObject
	 */
	private JSONObject buildButtonDataPart(String text, List<Button> buttons) {
		JSONObject message = new JSONObject();
		JSONObject attachment = new JSONObject();
		JSONObject payload = new JSONObject();
		JSONArray btns = new JSONArray();

		for (Button button : buttons) {
			JSONObject btn = new JSONObject();
			btn.put("title", button.getTitle());
			if (ButtonType.WEB_URL == button.getType()) {
				btn.put("type", "web_url");
				btn.put("url", button.getData());
			} else if (ButtonType.POSTBACK == button.getType()) {
				btn.put("type", "postback");
				btn.put("payload", button.getData());
			}
			btns.put(btn);
		}

		payload.put("template_type", "button");
		payload.put("text", text);
		payload.put("buttons", btns);

		attachment.put("type", "template");
		attachment.put("payload", payload);

		message.put("attachment", attachment);
		return message;
	}

	/**
	 * Build text part of a text message
	 * @param content content of text message
	 * @return text part JSONObject
	 */
	private JSONObject buildTextDataPart(String content) {
		JSONObject message = new JSONObject();
		message.put("text", content);
		return message;
	}

	/**
	 * Build an image message
	 * @param recipientId recipient id of the message
	 * @param url image url
	 * @return image message in JSONObject format
	 */
	public JSONObject buildImageMessage(String recipientId, String url) {
		JSONObject message = new JSONObject();
		message.put("recipient", buildRecipientPart(recipientId));
		message.put("message", buildImageDataPart(url));
		return message;
	}

	/**
	 * Build a text message
	 * @param recipientId recipient id of the message
	 * @param content content of text message
	 * @return text message in JSONObject format
	 */
	public JSONObject buildTextMessage(String recipientId, String content) {
		JSONObject message = new JSONObject();
		message.put("recipient", buildRecipientPart(recipientId));
		message.put("message", buildTextDataPart(content));
		return message;
	}

	/**
	 * Build a button message
	 * @param recipientId recipient id of the message
	 * @param text text content of button message
	 * @param buttons list of button in the message
	 * @return button message in JSONObject format
	 */
	public JSONObject buildButtonMessage(String recipientId, String text, List<Button> buttons) {
		JSONObject message = new JSONObject();
		message.put("recipient", buildRecipientPart(recipientId));
		message.put("message", buildButtonDataPart(text, buttons));
		return message;
	}

	/**
	 * Build a generic message
	 * @param recipientId recipient id of the message
	 * @param elements list of element in the message
	 * @return generic message in JSONObject format
	 */
	public JSONObject buildGenericMessage(String recipientId, List<Element> elements) {
		JSONObject message = new JSONObject();
		message.put("recipient", buildRecipientPart(recipientId));
		message.put("message", buildGenericDataPart(elements));
		return message;

	}

	/**
	 * Build a receipt message
	 * @param recipientId recipient id of the message
	 * @param receipt Object contain structure of receipt
	 * @return receipt message in JSONObject format
	 */
	public JSONObject buildReceiptMessage(String recipientId, Receipt receipt) {
		JSONObject message = new JSONObject();
		message.put("recipient", buildRecipientPart(recipientId));
		message.put("message", buildReceiptDataPart(receipt));
		return message;

	}
}
