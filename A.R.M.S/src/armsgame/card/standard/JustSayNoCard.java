package armsgame.card.standard;

import armsgame.card.Response;
import armsgame.impl.CardActionType;
import armsgame.impl.Player;
import armsgame.impl.SupportedActions;
import armsgame.impl.CardActionType.Likeness;

public class JustSayNoCard extends ActionCard implements Response {
	private static final long serialVersionUID = -1356787074424595137L;

	@Override
	public String getInternalType() {
		return "action.justSayNo";
	}

	@Override
	public ResponseType getResponseType() {
		return ResponseType.JustSayNo;
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
