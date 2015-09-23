/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

import armsgame.card.util.CardActionType.Likeness;
import armsgame.impl.WeaponTransfer;
import armsgame.impl.Player;
import armsgame.weapon.Weapon;

/**
 *
 * @author Henry
 */
public class PartPicker extends Action
{
	/**
	 *
	 */
	private static final long serialVersionUID = -8036992382008611576L;

	@Override
	public boolean actionPlayed(Player self)
	{
		Player target = self.selectPlayer("Please select another player to take a property from.", Player::hasFreeWeaponParts);

		if (target == null)
		{
			return false;
		}

		PartCard take = self.selectPartUpgrade("Please select a property to take.", (card) -> {
			// Has to not be part of the full weapon
			Weapon column = target.getWeapon(card);
			column.sort();
			return !column.isFullSet() || column.indexOf(card) >= column.getFullSet();
		} , target);

		if (take == null)
		{
			return false;
		}

		WeaponTransfer slyDeal = new WeaponTransfer(self, target, take);
		slyDeal.finishRequest();
		return true;
	}

	@Override
	public String getInternalType()
	{
		return "action.sly";
	}

	@Override
	public boolean isEnabled(Player self, Likeness action)
	{
		if (action == Likeness.Action)
		{
			self.getGame().playerStream().parallel().anyMatch(Player::hasFreeWeaponParts);
		}
		return true;
	}
}
