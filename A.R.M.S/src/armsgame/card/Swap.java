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
public class Swap extends Action
{
	/**
	 *
	 */
	private static final long serialVersionUID = -7696673622785225864L;

	@Override
	public boolean actionPlayed(Player self)
	{
		Player target = self.selectPlayer("Please select another player to take a property set from.", Player::hasFreeWeaponParts);

		if (target == null)
		{
			return false;
		}

		PartCard take = self.selectProperty("Please select a property to take.", (card) -> {
			// Has to not be part of the full set.
			Weapon column = target.getWeapon(card);
			column.sort();
			return !column.isFullSet() || column.indexOf(card) >= column.getFullSet();
		} , target);

		if (take == null)
		{
			return false;
		}

		PartCard give = self.selectPartUpgrade("Please select a property to give", (card) -> true);

		DamageReport forceDeal = new DamageReport(self, target);
		forceDeal.requestProperty(take);
		forceDeal.giveProperty(give);
		forceDeal.finishRequest();
		return true;
	}

	@Override
	public String getInternalType()
	{
		return "action.forced";
	}

	@Override
	public boolean isEnabled(Player self, Likeness action)
	{
		if (action == Likeness.Action)
		{
			self.getGame().playerStream().parallel().anyMatch(Player::hasFreeWeaponParts);
			return !self.columnStream().allMatch(Weapon::isEmpty);
		}
		return true;
	}
}
