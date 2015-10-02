package armsgame.card;

public abstract class Response extends Action {

	public abstract Damage getDamageResponse();

	public abstract Transfer getTransferResponse();

	public abstract boolean supportsDamageResponse();

	public abstract boolean supportsTransferResponse();

}