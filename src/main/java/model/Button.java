package model;

/**
 * Button in button message
 */
public class Button {
	public enum ButtonType {
		WEB_URL, POSTBACK;
	}

	private ButtonType type;
	private String title;
	private String data;

	public Button(ButtonType type, String title, String data) {
		this.type = type;
		this.title = title;
		this.data = data;
	}

	public ButtonType getType() {
		return type;
	}

	public void setType(ButtonType type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
