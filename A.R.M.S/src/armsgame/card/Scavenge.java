/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

import armsgame.card.util.CardActionType.Likeness;
import armsgame.impl.Player;

/**
 *
 * @author Henry
 */
public class Scavenge extends Action {
	/**
	 *
	 */
	private static final long serialVersionUID = -5079013509619766826L;

	@Override
	public boolean actionPlayed(Player self) {
		self.drawCards();
		return true;
	}

	@Override
	public String getInternalType() {
		return "action.go";
	}

	@Override
	public boolean isEnabled(Player self, Likeness action) {
		return true;
	}
}
