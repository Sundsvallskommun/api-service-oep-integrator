package se.sundsvall.oepintegrator.utility;

public class EncryptionException extends RuntimeException {

	public EncryptionException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public EncryptionException(final String message) {
		super(message);
	}

}
