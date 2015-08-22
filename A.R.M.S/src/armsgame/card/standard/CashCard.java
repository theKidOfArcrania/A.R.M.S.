package armsgame.card.standard;

import armsgame.card.Action;
import armsgame.card.Energy;
import armsgame.impl.Player;
import armsgame.impl.CardActionType.Likeness;

public final class CashCard extends StandardCard implements Energy {

	/**
	 *
	 */
	private static final long serialVersionUID = 4350231484759060230L;
	private final int value;
	private Action convertee = null;

	public CashCard(int value) {
		this.value = value;
	}

	public CashCard(String internalType) {
		if (internalType.startsWith("$")) {
			this.value = Integer.parseInt(internalType.substring(1)) * StandardCardDefaults.getCardDefaults()
					.getScale();
		} else {
			this.value = Integer.parseInt(internalType);
		}
	}

	CashCard(Action convertee) {
		this.value = convertee.getEnergyValue();
		if (this.value == 0) {
			throw new IllegalArgumentException();
		}
		this.convertee = convertee;
	}

	@Override
	public String getCardName() {
		if (convertee != null) {
			return convertee.getCardName() + " (cash)";
		}
		return Energy.moneyString(value, false);
	}

	@Override
	public String getInternalType() {
		return "cash." + value;
	}

	@Override
	public int getEnergyValue() {
		return 0;
	}

	@Override
	public int getValue() {
		return value;
	}

	@SuppressWarnings("unused")
	@Override
	public boolean isEnabled(Player self, Likeness action) {
		return true;
	}
}
