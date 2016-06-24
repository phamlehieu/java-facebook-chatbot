package model;

/**
 * Address in receipt message
 */
public class Address {
	private String street1;
	private String street2;
	private String city;
	private String postalCode;
	private String state;
	private String country;

	public Address(String street1, String city, String postalCode, String state, String country) {
		this.street1 = street1;
		this.city = city;
		this.postalCode = postalCode;
		this.state = state;
		this.country = country;
	}

	public Address(String street1, String street2, String city, String postalCode, String state, String country) {
		this.street1 = street1;
		this.street2 = street2;
		this.city = city;
		this.postalCode = postalCode;
		this.state = state;
		this.country = country;
	}

	public String getStreet1() {
		return street1;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
