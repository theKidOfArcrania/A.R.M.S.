/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

import armsgame.card.util.CardActionType.Likeness;
import armsgame.impl.Player;
import armsgame.impl.WeaponTransfer;
import armsgame.weapon.WeaponPartSpec;

/**
 *
 * @author Henry
 */
public class PartPicker extends Action {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = -8036992382008611576L;

	@Override
	public boolean actionPlayed(Player self) {
		Player target = self.selectPlayer("Please select another player to take a property from.", Player::hasFreeWeaponParts);

		if (target == null) {
			return false;
		}

		// FIXME: return a part and its respective weapon spec.
		WeaponPartSpec take = self.selectPartUpgrade("Please select a property to take.", (part, weapon) -> {
			// Has to not be part of the full weapon
			return !weapon.isComplete();
		} , target);

		if (take == null) {
			return false;
		}

		WeaponTransfer slyDeal = new WeaponTransfer(self, target, null, take);
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
			for (Player player : self.getGame().getPlayers()) {
				if (player != self && player.hasFreeWeaponParts()) {
					return true;
				}
			}
			return false;
		}
		return true;
	}
}
