/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

import armsgame.impl.CardActionType.Likeness;
import armsgame.impl.Player;

/**
 *
 * @author Henry
 */
public class MUpgrade extends Upgrade {

	/**
	 *
	 */
	private static final long serialVersionUID = -8732210588821915468L;

	@Override
	public boolean actionPlayed(Player self) {
		// TO DO: implementing Hasbro rules??
		WeaponSet column = self.selectPropertyColumn("Please select a full-set property column to build on.", WeaponSet::isFullSet);
		if (column == null) {
			return false;
		}
		column.addAndSort(this);
		return true;
	}

	@Override
	public String getInternalType() {
		return "action.house";
	}

	@Override
	public int getRaiseValue() {
		return getInternalIntProperty("raiseValue");
	}

	// TO DO: implement support type for building
	@Override
	public boolean isEnabled(Player self, Likeness action) {
		if (action == Likeness.Action) {
			// must have at least a full set in order to have build action type.
			return self.columnStream()
					.anyMatch(WeaponSet::isFullSet);
		}
		return true;
	}
}
