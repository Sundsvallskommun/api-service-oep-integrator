package se.sundsvall.oepintegrator.integration.opene.soap.model.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Poster {

	private int userID;

	private String username;

	private String firstname;

	private String lastname;

	private String email;

	public int getUserID() {
		return userID;
	}

	public void setUserID(final int userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(final String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(final String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		final Poster poster = (Poster) o;
		return userID == poster.userID && Objects.equals(username, poster.username) && Objects.equals(firstname, poster.firstname) && Objects.equals(lastname, poster.lastname) && Objects.equals(email, poster.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userID, username, firstname, lastname, email);
	}

	@Override
	public String toString() {
		return "Poster{" +
			"userID=" + userID +
			", username='" + username + '\'' +
			", firstname='" + firstname + '\'' +
			", lastname='" + lastname + '\'' +
			", email='" + email + '\'' +
			'}';
	}
}
