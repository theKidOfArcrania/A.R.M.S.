package armsgame.card;

import armsgame.impl.CardActionType;
import armsgame.impl.CardActionType.Likeness;
import armsgame.impl.Player;
import armsgame.impl.SupportedActions;

public final class BurstCharge extends Card implements Valuable {
	private static final long serialVersionUID = 4350231484759060230L;

	public static String moneyString(int amount, boolean shortString) {
		double figure;
		String unit;

		if (amount > 1000000000) { // billions
			figure = amount / 1000000000.0;
			unit = shortString ? "B" : " billion dollars";
		} else if (amount > 1000000) { // millions
			figure = amount / 1000000.0;
			unit = shortString ? "M" : " million dollars";
		} else if (amount > 1000) { // thousands
			figure = amount / 1000000.0;
			unit = shortString ? "K" : " thousand dollars";
		} else {
			figure = amount;
			unit = "";
		}

		return String.format("%f1%s", figure, unit);
	}

	private final int value;
	private Action convertee = null;

	public BurstCharge(int value) {
		this.value = value;
	}

	public BurstCharge(String internalType) {
		if (internalType.startsWith("$")) {
			this.value = Integer.parseInt(internalType.substring(1)) * StandardCardDefaults.getCardDefaults()
					.getScale();
		} else {
			this.value = Integer.parseInt(internalType);
		}
	}

	BurstCharge(Action convertee) {
		this.value = convertee.getEnergyValue();
		if (this.value == 0) {
			throw new IllegalArgumentException();
		}
		this.convertee = convertee;
	}

	@Override
	public boolean actionPlayed(Player self) {
		self.addBill(this);
		return true;
	}

	@Override
	public String getCardName() {
		if (convertee != null) {
			return convertee.getCardName() + " (cash)";
		}
		return moneyString(value, false);
	}

	@Override
	public int getEnergyValue() {
		return 0;
	}

	@Override
	public String getInternalType() {
		return "cash." + value;
	}

	@Override
	public SupportedActions getSupportedTypes() {
		SupportedActions actions = new SupportedActions();
		actions.addAction(new CardActionType("Add to bank", "move.cash"));
		actions.addAction(new CardActionType("Discard", "move.discard"));
		return actions;
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
