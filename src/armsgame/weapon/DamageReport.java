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

	}

	public Player getAttacker() {
		return attacker;
	}

	public double getDmg() {
		return dmg;
	}

	public Player getVictim() {
		return victim;
	}

	public boolean isVampiric() {
		return vampiric;
	}
}
