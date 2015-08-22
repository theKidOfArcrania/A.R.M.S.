package armsgame.card;

import armsgame.impl.CardActionType;
import armsgame.impl.SupportedActions;

public interface Action extends Card {

	Energy convertToCash();

	String getActionInternalType();

	String getActionName();

	@Override
	default SupportedActions getSupportedTypes() {
		SupportedActions actions = new SupportedActions();
		actions.addAction(new CardActionType(getActionName(), getActionInternalType()));
		actions.addAction(new CardActionType("Discard", "move.discard"));
		return actions;
	}
}