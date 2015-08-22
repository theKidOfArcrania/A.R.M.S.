/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card.standard;

import armsgame.card.Action;
import armsgame.impl.Player;

/**
 * This card contains all the default impl. for any action card.
 * <p>
 *
 * @author HW
 */
public abstract class SAction extends StandardCard implements Action {
	/**
	 *
	 */
	private static final long serialVersionUID = -8876027487042225439L;

	SAction() {
	}

	@SuppressWarnings("unused")
	@Override
	public boolean actionPlayed(Player self) {
		// default implementation is nothing.
		return true;
	}

	@Override
	public SEnergy convertToCash() {
		return new SEnergy(this);
	}

	@Override
	public String getActionInternalType() {
		return this.getInternalProperty("actionInternalType", "move.action");
	}

	@Override
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

}
