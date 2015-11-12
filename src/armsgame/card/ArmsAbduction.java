/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

import armsgame.card.util.CardActionType.Likeness;
import armsgame.impl.Player;
import armsgame.impl.WeaponTransfer;
import armsgame.weapon.Weapon;

/**
 *
 * @author Henry
 */
public class ArmsAbduction extends Action {
	/**
	 *
	 */
	private static final long serialVersionUID = 4190593954097839138L;

	@Override
	public boolean actionPlayed(Player self) {
		Player target = self.selectPlayer("Please select another player to take a property set from.", Player::hasFreeWeaponParts);

		if (target == null) {
			return false;
		}

		Weapon takeSet = self.selectWeapon("Please select a property set to take.", Weapon::isComplete, target);

		if (takeSet == null) {
			return false;
		}

		WeaponTransfer dealBreaker = new WeaponTransfer(self, target);
		dealBreaker.request(takeSet.getSpec());
		dealBreaker.finishRequest();
		return true;
	}

	@Override
	public String getInternalType() {
		return "action.abduction";
	}

	@Override
	public boolean isEnabled(Player self, Likeness action) {
		if (action == Likeness.Action) {
			for (Player player : self.getGame().getPlayers()) {
				if (player.hasMaxWeapon()) {
					return true;
				}
			}
		}
		return true;
	}
}
