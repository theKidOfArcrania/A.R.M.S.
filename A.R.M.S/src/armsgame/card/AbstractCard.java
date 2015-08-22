package armsgame.card;

import java.awt.Image;
import java.io.IOException;

import armsgame.impl.Board;
import armsgame.impl.Payment;
import armsgame.impl.Player;

public abstract class AbstractCard implements Card {

	private static final long serialVersionUID = -8288322516558088995L;

	protected static boolean payRequest(Player self, boolean global, int amount) {
		return payRequest(self, global, amount, "pay");
	}

	protected static boolean payRequest(Player self, boolean global, int amount, String payType) {
		if (global) {
			Board game = self.getGame();
			for (int i = 0; i < game.getPlayerCount(); i++) {
				Player target = game.getPlayer(i);
				if (target != self) {
					Payment rentPay = new Payment(self, target, amount);
					rentPay.finishRequest();
				}
			}
			return true;
		} else {
			Player target = self.selectPlayer("Please select a player to " + payType + " you.");
			if (target == null) {
				return false;
			}

			Payment rentPay = new Payment(self, target, amount);
			rentPay.finishRequest();
			return true;
		}
	}

	int id = -1;

	public AbstractCard() {
		super();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AbstractCard other = (AbstractCard) obj;
		if (this.id < 0 || this.id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public final int getCardId() {
		return id;
	}

	@Override
	public abstract String getCardName();

	@Override
	public abstract String getDescription();

	@Override
	public abstract Image getImage() throws IOException;

	@Override
	public abstract String getInternalType();

	@Override
	public abstract int getEnergyValue();

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + this.id;
		return hash;
	}

	@Override
	public String toString() {
		return getCardName();
	}

	protected abstract int getInternalIntProperty(String subKey);

	protected abstract int getInternalIntProperty(String subKey, int defValue);

	protected abstract String getInternalProperty(String subKey);

	protected abstract String getInternalProperty(String subKey, String defValue);

}