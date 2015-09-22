/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

import armsgame.card.impl.Weapon;
import armsgame.card.impl.WeaponSet;
import armsgame.card.impl.CardActionType.Likeness;
import armsgame.impl.DamageReport;
import armsgame.impl.Player;

/**
 *
 * @author Henry
 */
public class ArmsAbduction extends Action
{
	/**
	 *
	 */
	private static final long serialVersionUID = 4190593954097839138L;

	@Override
	public boolean actionPlayed(Player self)
	{
		Player target = self.selectPlayer("Please select another player to take a property set from.", Player::hasFreeWeaponParts);

		if (target == null)
		{
			return false;
		}

		Weapon takeSet = self.selectPropertyColumn("Please select a property set to take.", WeaponSet::isFullSet, target);

		if (takeSet == null)
		{
			return false;
		}

		DamageReport dealBreaker = new DamageReport(self, target);
		dealBreaker.requestPropertySet(takeSet.getPropertyColor());
		dealBreaker.finishRequest();
		return true;
	}

	@Override
	public String getInternalType()
	{
		return "action.breaker";
	}

	@Override
	public boolean isEnabled(Player self, Likeness action)
	{
		if (action == Likeness.Action)
		{
			return self.getGame().playerStream().parallel().anyMatch(Player::hasMaxWeapon);
		}
		return true;
	}
}
