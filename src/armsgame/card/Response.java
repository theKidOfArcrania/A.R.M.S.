package armsgame.card;

import java.util.function.UnaryOperator;

import armsgame.impl.WeaponTransfer;
import armsgame.weapon.DamageSpec;

public abstract class Response extends Action {
	public interface Damage extends UnaryOperator<DamageSpec> {
	}

	public interface Transfer extends UnaryOperator<WeaponTransfer> {
	}

	public abstract Damage getDamageResponse();

	public abstract Transfer getTransferResponse();

}