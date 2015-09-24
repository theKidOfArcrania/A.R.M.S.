/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

import java.util.ArrayList;

import armsgame.card.util.CardActionType.Likeness;
import armsgame.impl.Player;
import armsgame.weapon.Weapon;
import armsgame.weapon.WeaponSpec;

/**
 *
 * @author Henry
 */
public class Ammunition extends Action {

	/**
	 *
	 */
	private static final long serialVersionUID = 9126449081012411183L;
	private final WeaponSpec[] specTargets;
	private final String codeType;

	/**
	 * Constructs an ammunition with the internal typing. This is the constructor called by card creator.
	 *
	 * @param codeType the internal code sub-type of the ammnunition
	 */
	public Ammunition(String codeType) {
		this.codeType = codeType;
		String[] codeTargets = codeType.split("-");
		ArrayList<WeaponSpec> targets = new ArrayList<>(codeTargets.length);
		for (String code : codeTargets) {
			WeaponSpec target = WeaponSpec.identifySpec(code);
			if (target != null) {
				targets.add(target);
			}
		}

		this.specTargets = new WeaponSpec[targets.size()];
		targets.toArray(this.specTargets);
	}

	@Override
	public boolean actionPlayed(Player self) {
		Weapon weapon = self.selectWeapon("Choose your weapon to attack with.", Weapon::isActive);
		return processDamage(self, isGlobal(), weapon.isVampiric(), weapon.getAttackPoints());
	}

	@Override
	public String getCardName() {
		return specTargets.getColorName() + " Ammunition";
	}

	@Override
	public String getInternalType() {
		return "action.ammunition." + specTargets.getInternalType();
	}

	@Override
	public boolean isEnabled(Player self, Likeness action) {
		if (action == Likeness.Action) {
			return self.columnStream().parallel().anyMatch(this::isValidRent);
		}
		return true;
	}

	public boolean isGlobal() {
		return this.getInternalIntProperty("global", 0) != 0;
	}

	public boolean isValidRent(Weapon column) {
		return specTargets.compatibleWith(column.getPropertyColor()) && column.getAttackPoints() > 0;
	}

}
