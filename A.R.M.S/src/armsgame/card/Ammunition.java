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
public class Ammunition extends Action {

	/**
	 *
	 */
	private static final long serialVersionUID = 9126449081012411183L;
	private final MultiSpecs weaponSpecs;

	/**
	 * Constructs an ammunition with the internal typing. This is the constructor called by card creator.
	 *
	 * @param internalType
	 *            the internal type of the ammnunition usages
	 */
	public Ammunition(String internalType) {
		weaponSpecs = new MultiSpecs(internalType);
	}

	@Override
	public boolean actionPlayed(Player self) {
		Weapon weapon = self.selectPropertyColumn("Choose your weapon to attack with.", this::isValidRent);
		return processDamage(self, isGlobal(), weapon.isEnergetic(), weapon.getAttackPoints());
	}

	@Override
	public String getCardName() {
		return weaponSpecs.getColorName() + " Ammunition";
	}

	@Override
	public String getInternalType() {
		return "action.rent." + weaponSpecs.getInternalType();
	}

	@Override
	public boolean isEnabled(Player self, Likeness action) {
		if (action == Likeness.Action) {
			return self.columnStream()
				.parallel()
				.anyMatch(this::isValidRent);
		}
		return true;
	}

	public boolean isGlobal() {
		return this.getInternalIntProperty("global", 0) != 0;
	}

	public boolean isValidRent(Weapon column) {
		return weaponSpecs.compatibleWith(column.getPropertyColor()) && column.getAttackPoints() > 0;
	}

}
