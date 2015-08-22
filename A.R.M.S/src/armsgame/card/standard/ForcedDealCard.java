/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card.standard;

import armsgame.card.WeaponSet;
import armsgame.impl.Payment;
import armsgame.impl.Player;
import armsgame.impl.CardActionType.Likeness;

/**
 *
 * @author Henry
 */
public class ForcedDealCard extends ActionCard {
	/**
	 *
	 */
	private static final long serialVersionUID = -7696673622785225864L;

	@Override
	public boolean actionPlayed(Player self) {
		Player target = self.selectPlayer("Please select another player to take a property set from.", Player::hasIncompleteSet);

		if (target == null) {
			return false;
		}

		SWeaponPart take = (SWeaponPart) self.selectProperty("Please select a property to take.", (card) -> {
			// Has to not be part of the full set.
			WeaponSet column = target.getPropertyColumn(card);
			column.sort();
			return !column.isFullSet() || column.indexOf(card) >= column.getFullSet();
		} , target);

		if (take == null) {
			return false;
		}

		SWeaponPart give = (SWeaponPart) self.selectProperty("Please select a property to give", (card) -> true);

		Payment forceDeal = new Payment(self, target);
		forceDeal.requestProperty(take);
		forceDeal.giveProperty(give);
		forceDeal.finishRequest();
		return true;
	}

	@Override
	public String getInternalType() {
		return "action.forced";
	}

	@Override
	public boolean isEnabled(Player self, Likeness action) {
		if (action == Likeness.Action) {
			self.getGame()
					.playerStream()
					.parallel()
					.anyMatch(Player::hasIncompleteSet);
			return !self.columnStream()
					.allMatch(WeaponSet::isEmpty);
		}
		return true;
	}
}
