package domain.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("address")
public class Address {
	private int id;
	private String city;
	private String street;
	private int number;
	
	public Address() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Address(int id, String city, String street, int number) {
		super();
		this.id = id;
		this.city = city;
		this.street = street;
		this.number = number;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "Address [id=" + id + ", city=" + city + ", street=" + street + ", number=" + number + "]";
	}

	
}
