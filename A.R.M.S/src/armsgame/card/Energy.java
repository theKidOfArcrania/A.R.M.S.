package armsgame.card;

import armsgame.impl.CardActionType;
import armsgame.impl.Player;
import armsgame.impl.SupportedActions;

public interface Energy extends Valuable {

	static String moneyString(int amount, boolean shortString) {
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

	@Override
	default boolean actionPlayed(Player self) {
		self.addBill(this);
		return true;
	}

	@Override
	default SupportedActions getSupportedTypes() {
		SupportedActions actions = new SupportedActions();
		actions.addAction(new CardActionType("Add to bank", "move.cash"));
		actions.addAction(new CardActionType("Discard", "move.discard"));
		return actions;
	}

}