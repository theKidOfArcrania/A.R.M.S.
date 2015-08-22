package armsgame.card;

public interface Response extends Action {

	public enum ResponseType {
		SHIELD;
	}

	ResponseType getResponseType();

}