/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import armsgame.card.util.CardActionType.Likeness;
import armsgame.impl.Player;
import armsgame.weapon.DamageSpec;
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
		Weapon weapon = self.selectWeapon("Choose your weapon to attack with.", this::isUsableWith);
		Player victim = null;
		DamageSpec dmg = weapon.getDamageReport();
		dmg.setMultiDamageOverride(isMultiTarget());

		if (dmg.getSingleTargetDamage() != 0) {
			victim = self.selectPlayer("Select player to attack");
			if (victim == null) {
				return false;
			}
		}

		double efficiency = self.selectAccuracy(dmg.getAccuracy());
		dmg.damage(self, victim, weapon.isEnergetic(), efficiency);
		return true;
	}

	@Override
	public String getCardName() {
		String defName = Arrays.stream(specTargets).map(WeaponSpec::getShortName).collect(Collectors.joining("-"));
		return getInternalProperty("name", defName) + " Ammunition";
	}

	@Override
	public String getInternalType() {
		return "action.ammunition." + codeType;
	}

	@Override
	public boolean isEnabled(Player self, Likeness action) {
		if (action == Likeness.Action) {
			return self.setStream().parallel().anyMatch(this::isUsableWith);
		}
		return true;
	}

	public boolean isMultiTarget() {
		return this.getInternalIntProperty("multiTarget", 0) != 0;
	}

	public boolean isUsableWith(Weapon test) {
		WeaponSpec specTest = test.getSpec();
		for (WeaponSpec specTarget : specTargets) {
			if (specTarget == specTest) {
				return test.isActive();
			}
		}
		return false;
	}

}
