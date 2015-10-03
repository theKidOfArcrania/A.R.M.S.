package armsgame.weapon;

import armsgame.impl.Player;

public class DamageReport {
	private final Player attacker;
	private final Player victim;
	private final double dmg;
	private final boolean vampiric;

	public DamageReport(Player attacker, Player victim, double dmg, boolean vampiric) {
		this.attacker = attacker;
		this.victim = victim;
		this.dmg = dmg;
		this.vampiric = vampiric;
	}

	public void execute() {
		double totalHealth = victim.getEnergyLevel() + victim.getShieldLevel();
		double fixedDamage = Math.min(dmg, totalHealth); // fix for overflow damage

		victim.selectResponse(this);
		victim.damageShield(fixedDamage);

		if (vampiric) {
			attacker.heal(fixedDamage);
		}
	}

	public Player getAttacker() {
		return attacker;
	}

	public double getDamage() {
		return dmg;
	}

	public Player getVictim() {
		return victim;
	}

	public boolean isVampiric() {
		return vampiric;
	}
}
