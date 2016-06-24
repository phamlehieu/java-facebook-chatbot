package model;

/**
 * Element in receipt messgae
 */
public class ReceiptElement {
	private String title;
	private String subtitle;
	private int quantity;
	private double price;
	private String currency;
	private String imageUrl;

	public ReceiptElement(String title, String subtitle, int quantity, double price, String currency, String imageUrl) {
		super();
		this.title = title;
		this.subtitle = subtitle;
		this.quantity = quantity;
		this.price = price;
		this.currency = currency;
		this.imageUrl = imageUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
