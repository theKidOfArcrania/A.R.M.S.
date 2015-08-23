package armsgame.card;

public abstract class Response extends SAction {

	public enum ResponseType {
		SHIELD;
	}

	public abstract ResponseType getResponseType();

}