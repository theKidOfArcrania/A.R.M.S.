package armsgame.card;

import armsgame.card.impl.CardActionType;
import armsgame.card.impl.SupportedActions;
import armsgame.card.impl.CardActionType.Likeness;
import armsgame.impl.Player;

public class EmpBarrier extends Response {
	private static final long serialVersionUID = -1356787074424595137L;

	@Override
	public String getInternalType() {
		return "action.justSayNo";
	}

	@Override
	public ResponseType getResponseType() {
		return ResponseType.SHIELD;
	}

	@Override
	public SupportedActions getSupportedTypes() {
		SupportedActions actions = super.getSupportedTypes();
		actions.removeAction(CardActionType.Likeness.Action);
		return actions;
	}

	@Override
	public boolean isEnabled(Player self, Likeness action) {
		return true;
	}

}
