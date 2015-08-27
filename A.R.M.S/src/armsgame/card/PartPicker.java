/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

import armsgame.impl.CardActionType.Likeness;
import armsgame.impl.DamageReport;
import armsgame.impl.Player;

/**
 *
 * @author Henry
 */
public class PartPicker extends Action {
	/**
	 *
	 */
	private static final long serialVersionUID = -8036992382008611576L;

	@Override
	public boolean actionPlayed(Player self) {
		Player target = self.selectPlayer("Please select another player to take a property from.", Player::hasIncompleteSet);

		if (target == null) {
			return false;
		}

		WeaponPart take = (WeaponPart) self.selectProperty("Please select a property to take.", (card) -> {
			// Has to not be part of the full weapon
			WeaponSet column = target.getPropertyColumn(card);
			column.sort();
			return !column.isFullSet() || column.indexOf(card) >= column.getFullSet();
		} , target);

		if (take == null) {
			return false;
		}

		DamageReport slyDeal = new DamageReport(self, target, take);
		slyDeal.finishRequest();
		return true;
	}

	@Override
	public String getInternalType() {
		return "action.sly";
	}

	@Override
	public boolean isEnabled(Player self, Likeness action) {
		if (action == Likeness.Action) {
			self.getGame()
					.playerStream()
					.parallel()
					.anyMatch(Player::hasIncompleteSet);
		}
		return true;
	}
}
