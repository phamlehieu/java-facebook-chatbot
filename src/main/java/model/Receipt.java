package model;

import java.util.List;

/**
 * Receipt structure in receipt messgae
 */
public class Receipt {
	private String recipientName;
	private String orderNumber;
	private String currency;
	private String paymentMethod;
	private String timestamp;
	private String orderUrl;

	private Summary summary;
	private List<Adjustment> adjustments;
	private List<ReceiptElement> receiptElements;
	private Address address;

	public Receipt(String recipientName, String orderNumber, String currency, String paymentMethod, Summary summary,
			List<ReceiptElement> receiptElements) {
		this.recipientName = recipientName;
		this.orderNumber = orderNumber;
		this.currency = currency;
		this.paymentMethod = paymentMethod;
		this.summary = summary;
		this.receiptElements = receiptElements;
	}

	public Receipt(String recipientName, String orderNumber, String currency, String paymentMethod, String timestamp,
			String orderUrl, Summary summary, List<Adjustment> adjustments, List<ReceiptElement> receiptElements,
			Address address) {
		this.recipientName = recipientName;
		this.orderNumber = orderNumber;
		this.currency = currency;
		this.paymentMethod = paymentMethod;
		this.timestamp = timestamp;
		this.orderUrl = orderUrl;
		this.summary = summary;
		this.adjustments = adjustments;
		this.receiptElements = receiptElements;
		this.address = address;
	}

	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getOrderUrl() {
		return orderUrl;
	}

	public void setOrderUrl(String orderUrl) {
		this.orderUrl = orderUrl;
	}

	public Summary getSummary() {
		return summary;
	}

	public void setSummary(Summary summary) {
		this.summary = summary;
	}

	public List<Adjustment> getAdjustments() {
		return adjustments;
	}

	public void setAdjustments(List<Adjustment> adjustments) {
		this.adjustments = adjustments;
	}

	public List<ReceiptElement> getReceiptElements() {
		return receiptElements;
	}

	public void setReceiptElements(List<ReceiptElement> receiptElements) {
		this.receiptElements = receiptElements;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
