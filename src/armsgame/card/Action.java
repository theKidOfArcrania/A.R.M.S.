/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

import armsgame.card.util.CardActionType;
import armsgame.card.util.SupportedActions;
import armsgame.impl.Player;

/**
 * This card contains all the default impl. for any action card.
 * <p>
 *
 * @author HW
 */
public abstract class Action extends Card {
	private static final long serialVersionUID = -8876027487042225439L;

	@SuppressWarnings("unused")
	@Override
	public boolean actionPlayed(Player self) {
		// default implementation is nothing.
		return true;
	}

	public BurstCharge convertToCash() {
		return new BurstCharge(this);
	}

	public String getActionInternalType() {
		return this.getInternalProperty("actionInternalType", "move.action");
	}

	public String getActionName() {
		return this.getInternalProperty("actionName", "Play " + getCardName());
	}

	@Override
	public String getCardName() {
		return this.getInternalProperty("name");
	}

	@Override
	public String getDescription() {
		return getInternalProperty("description", "");
	}

	@Override
	public int getEnergyValue() {
		return getInternalIntProperty("sellValue", 0);
	}

	@Override
	public SupportedActions getSupportedTypes() {
		SupportedActions actions = new SupportedActions();
		actions.addAction(new CardActionType(getActionName(), getActionInternalType()));
		actions.addAction(new CardActionType("Discard", "move.discard"));
		return actions;
	}

}
