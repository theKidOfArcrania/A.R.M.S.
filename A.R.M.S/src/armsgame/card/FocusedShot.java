/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

import armsgame.impl.Player;
import armsgame.impl.CardActionType.Likeness;

/**
 *
 * @author Henry
 */
public class FocusedShot extends Action {
	/**
	 *
	 */
	private static final long serialVersionUID = -882780157673704376L;

	@Override
	public boolean actionPlayed(Player self) {
		return payRequest(self, isGlobal(), false, getPayAmount());
	}

	@Override
	public String getInternalType() {
		return "action.debtCollector";
	}

	public int getPayAmount() {
		return this.getInternalIntProperty("amount", 0);
	}

	@Override
	public boolean isEnabled(Player self, Likeness action) {
		return true;
	}

	public boolean isGlobal() {
		return this.getInternalIntProperty("global", 0) != 0;
	}
}
