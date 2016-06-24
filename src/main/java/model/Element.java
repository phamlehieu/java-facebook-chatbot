package model;

import java.util.List;

/**
 * Element in generic message
 */
public class Element {
	private String title;
	private String itemUrl;
	private String imageUrl;
	private String subtitle;
	private List<Button> buttons;

	public Element(String title, String itemUrl, String imageUrl, String subtitle, List<Button> buttons) {
		this.title = title;
		this.itemUrl = itemUrl;
		this.imageUrl = imageUrl;
		this.subtitle = subtitle;
		this.buttons = buttons;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getItemUrl() {
		return itemUrl;
	}

	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public List<Button> getButtons() {
		return buttons;
	}

	public void setButtons(List<Button> buttons) {
		this.buttons = buttons;
	}
}
