package armsgame.card.impl;

import java.io.IOException;

public class DeckInitializationFailureException extends IOException {
	private static final long serialVersionUID = 3085006990278817785L;

	public DeckInitializationFailureException() {
	}

	public DeckInitializationFailureException(String message) {
		super(message);
	}

	public DeckInitializationFailureException(String message, Throwable cause) {
		super(message, cause);
	}

	public DeckInitializationFailureException(Throwable cause) {
		super(cause);
	}

}
