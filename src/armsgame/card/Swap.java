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
import armsgame.weapon.WeaponPartSpec;

/**
 *
 * @author Henry
 */
public class Swap extends Action {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = -7696673622785225864L;

	@Override
	public boolean actionPlayed(Player self) {
		Player target = self.selectPlayer("Please select another player to take a property set from.", Player::hasFreeWeaponParts);

		if (target == null) {
			return false;
		}

		WeaponPartSpec take = self.selectPartUpgrade("Please select a weapon part to take.", (part, weapon) -> {
			// TO DO: ask the player to select where to put the part.
			// TO DO: make sure that this weapon is usable.
			return !weapon.isComplete();
		} , target);

		if (take == null) {
			return false;
		}

		WeaponPartSpec give = self.selectPartUpgrade("Please select a property to give");

		WeaponTransfer forceDeal = new WeaponTransfer(self, target);
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
			PARTCHK: {
				for (Weapon weapon : self.getWeaponSets()) {
					if (!weapon.isOriginal()) {
						break PARTCHK;
					}
				}
				return false;
			}
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
