/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

import armsgame.card.util.CardActionType.Likeness;
import armsgame.impl.Player;
import armsgame.weapon.DamageSpec;

/**
 *
 * @author Henry
 */
public class FocusedShot extends Action {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = -882780157673704376L;

	@Override
	public boolean actionPlayed(Player self) {
		Player victim = null;
		DamageSpec dmg = getDamageSpec();

		if (dmg.getSingleTargetDamage() > 0) {
			victim = self.selectPlayer("Select player to attack");
			if (victim == null) {
				return false;
			}
		}

		double efficiency = self.selectAccuracy(dmg.getAccuracy());
		dmg.damage(self, victim, false, efficiency);
		return true;
	}

	public DamageSpec getDamageSpec() {
		return new DamageSpec(getInternalType() + ".damage");
	}

	@Override
	public String getInternalType() {
		return "action.debtCollector";
	}

	public int getPayAmount() {
		return this.getInternalIntProperty("amount", 0);
	}

	@Override
	public boolean isEnabled(Player self, Likeness action) {
		return true;
	}

	public boolean isGlobal() {
		return this.getInternalIntProperty("global", 0) != 0;
	}
}
