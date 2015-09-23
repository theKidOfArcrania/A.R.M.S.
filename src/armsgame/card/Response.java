package armsgame.card;

public abstract class Response extends Action {

	public enum ResponseType {
		SHIELD;
	}

	public abstract ResponseType getResponseType();

}