package armsgame.card;

public interface Response extends Action {

	public enum ResponseType {
		JustSayNo;
	}

	ResponseType getResponseType();

}