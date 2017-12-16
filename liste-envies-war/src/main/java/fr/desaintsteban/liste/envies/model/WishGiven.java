package fr.desaintsteban.liste.envies.model;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import fr.desaintsteban.liste.envies.dto.AppUserDto;
import fr.desaintsteban.liste.envies.util.EncodeUtils;

public class WishGiven {

	private String email;
	private String name;

	private String message;
	private String price;



	public WishGiven() { }

	public WishGiven(String email, String name) {
		this.setName(name);
		this.setEmail(email);
	}

	public WishGiven(AppUser user) {
		this.setName(user.getName());
		this.setEmail(user.getEmail());
	}

	public Key<WishGiven> getKey() {
		return Key.create(WishGiven.class, getEmail());
	}

	public String getEmail() {
		return EncodeUtils.decode(email);
	}

	public void setEmail(String email) {
		this.email = EncodeUtils.encode(email);
	}

	public String getName() {
		return EncodeUtils.decode(name);
	}

	public void setName(String name) {
		this.name = EncodeUtils.encode(name);
	}

	public String getMessage() {
		return EncodeUtils.decode(message);
	}

	public void setMessage(String message) {
		this.message = EncodeUtils.encode(message);
	}

	public String getPrice() {
		return EncodeUtils.decode(price);
	}

	public void setPrice(String price) {
		this.price = EncodeUtils.encode(price);
	}

	public AppUserDto toDto() {
		return new AppUserDto(this.getEmail(), this.getName());
	}

}