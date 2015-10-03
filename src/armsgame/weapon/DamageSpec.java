package armsgame.weapon;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

import armsgame.impl.Player;

import static armsgame.card.util.CardDefaults.getCardDefaults;

public class DamageSpec {

	private static void damage0(Player attacker, Player victim, double damage, boolean vampiric) {
		DamageReport rep = new DamageReport(attacker, victim, damage, vampiric);
		rep.execute();
	}

	private final String internalType;

	private final ArrayList<DoubleUnaryOperator> dmgMods = new ArrayList<>();
	private DamageSpec[] combo = new DamageSpec[0];

	private boolean multiDmgOverride;

	/**
	 * Constructs a NO-damage DamageSpec object.
	 */
	public DamageSpec() {
		this.internalType = null;
	}

	/**
	 * Constructs a new DamageSpec object from a combination of damage specs.
	 *
	 * @param first the first damage spec
	 * @param combo a rest of the damage specs
	 */
	public DamageSpec(DamageSpec first, DamageSpec... combo) {
		ArrayList<DamageSpec> comboList = new ArrayList<>(combo.length * 2 + 1);
		LinkedList<DamageSpec> specFlatten = new LinkedList<>();

		// Flatten combo list.
		// TO DO: do this only if tree is thick enough.
		this.combo = combo;
		while (specFlatten.size() > 0) {
			DamageSpec dmg = specFlatten.pop();
			if (dmg.internalType == null) {
				for (DamageSpec child : dmg.combo) {
					specFlatten.push(child);
				}
			} else {
				comboList.add(dmg);
			}
		}
		comboList.add(first);

		this.combo = comboList.toArray(new DamageSpec[comboList.size()]);
		this.internalType = null;
	}

	/**
	 * Constructs a new DamageSpec object from an internal type pointer.
	 *
	 * @param internalType property name internal type that this damage spec points to.
	 */
	public DamageSpec(String internalType) {
		Objects.requireNonNull(internalType);
		this.internalType = internalType;
	}

	/**
	 * Adds another damage modifier which will be applied in reverse order
	 *
	 * @param dmgMod the operator that modifies the damage.
	 */
	public void addDamageModifier(DoubleUnaryOperator dmgMod) {
		dmgMods.add(dmgMod);
	}

	/**
	 * Carries out the damage where the attacker deals damage onto the victim (convenience method).
	 *
	 * @param attacker the player who attacks
	 * @param victim the player who receives the damage
	 */
	public void damage(Player attacker, Player victim) {
		damage(attacker, victim, false, 1.0);
	}

	/**
	 * Carries out the damage where the attacker deals damage onto the victim (convenience method).
	 *
	 * @param attacker the player who attacks
	 * @param victim the player who receives the damage
	 * @param energetic whether if the respective weapon has an energy crystal.
	 * @param efficiency the efficiency rate of the player attacking the victim.
	 */
	public void damage(Player attacker, Player victim, boolean energetic, double efficiency) {
		damage(attacker, victim, energetic, efficiency, false);
	}

	/**
	 * Carries out the damage where the attacker deals damage onto the victim.
	 *
	 * @param attacker the player who attacks
	 * @param victim the player who receives the damage
	 * @param energetic whether if the respective weapon has an energy crystal.
	 * @param efficiency the efficiency rate of the player attacking the victim.
	 * @param multiDamageOverride determines whether if all the single target damage is converted to multi target damage.
	 */
	public void damage(Player attacker, Player victim, boolean energetic, double efficiency, boolean multiDamageOverride) {
		double singleDamage = getSingleTargetDamage() * efficiency;
		double multiDamage = getMultiTargetDamage() * efficiency;

		if (multiDamageOverride) {
			multiDamage += singleDamage;
			singleDamage = 0;
		} else {
			singleDamage = modifyDamage(singleDamage);
		}

		multiDamage = modifyDamage(multiDamage);
		boolean effectiveVampiric = energetic || isVampiric();

		if (victim != null && (singleDamage + multiDamage > 0)) {
			damage0(attacker, victim, singleDamage + multiDamage, effectiveVampiric);
		}

		if (multiDamage > 0) {
			for (Player player : attacker.getGame().getPlayers()) {
				if (player != victim) {
					damage0(attacker, player, multiDamage, effectiveVampiric);
				}
			}
		}
	}

	/**
	 * This retrieves the accuracy rate increase of this weapon part The accuracy rate is implemented as a bar, and this accuracy rate guarantees 100% efficiency rate xx% of the time
	 *
	 * @return a value xx from 0 to 1 where this accuracy rate guarantees 100% efficiency rate xx% of the time
	 */
	public double getAccuracy() {

		if (internalType == null) {
			double accuracy = 0.0;
			for (DamageSpec spec : combo) {
				accuracy += spec.getAccuracy();
			}
			return accuracy;
		} else {
			return getInternalDoubleProperty("accuracy");
		}

	}

	/**
	 * This retrieves the internal typing of this damage spec.
	 *
	 * @return the internal type.
	 */
	public String getInternalType() {
		return internalType;
	}

	/**
	 * Retrieves the damage dealt to multiple targets at 100% efficiency
	 *
	 * @return a positive damage amount.
	 */
	public double getMultiTargetDamage() {
		if (internalType == null) {
			double dmg = 0.0;
			for (DamageSpec spec : combo) {
				dmg += spec.getMultiTargetDamage();
			}

			if (multiDmgOverride) {
				return singleTargetDamage0() + dmg;
			} else {
				return dmg;
			}
		} else {
			return getInternalDoubleProperty("damage.multi");
		}
	}

	/**
	 * Retrieves the damage dealt to a single targets at 100% efficiency
	 *
	 * @return a positive damage amount.
	 */
	public double getSingleTargetDamage() {
		if (multiDmgOverride) {
			return 0;
		} else {
			return singleTargetDamage0();
		}
	}

	public boolean isMultiDamageOverride() {
		return multiDmgOverride;
	}

	/**
	 * Identifies whether if this weapon part is vampiric by default. This is defined by having the energy from the victim being transferred to the damager.
	 *
	 * @return true if it is vampiric, false otherwise.
	 */
	public boolean isVampiric() {
		if (internalType == null) {
			for (DamageSpec spec : combo) {
				if (spec.isVampiric()) {
					return true;
				}
			}
			return false;
		} else {
			return getInternalIntProperty("vampiric", 0) != 0;
		}
	}

	public void setMultiDamageOverride(boolean override) {
		this.multiDmgOverride = override;
	}

	private double modifyDamage(double dmg) {
		double modDmg = dmg;
		for (int i = dmgMods.size() - 1; i >= 0; i--) {
			modDmg = dmgMods.get(i).applyAsDouble(modDmg);
		}
		return modDmg;
	}

	private double singleTargetDamage0() {
		if (internalType == null) {
			double dmg = 0.0;
			for (DamageSpec spec : combo) {
				dmg += spec.getSingleTargetDamage();
			}
			return dmg;
		} else {
			return getInternalDoubleProperty("damage.single");
		}
	}

	protected double getInternalDoubleProperty(String subKey) {
		return getCardDefaults().getDoubleProperty(getInternalType() + "." + subKey, 0.0);
	}

	protected double getInternalDoubleProperty(String subKey, double defValue) {
		return getCardDefaults().getDoubleProperty(getInternalType() + "." + subKey, defValue);
	}

	protected int getInternalIntProperty(String subKey) {
		return getCardDefaults().getIntProperty(getInternalType() + "." + subKey, 0);
	}

	protected int getInternalIntProperty(String subKey, int defValue) {
		return getCardDefaults().getIntProperty(getInternalType() + "." + subKey, defValue);
	}

	protected String getInternalProperty(String subKey) {
		return getCardDefaults().getProperty(getInternalType() + "." + subKey);
	}

	protected String getInternalProperty(String subKey, String defValue) {
		return getCardDefaults().getProperty(getInternalType() + "." + subKey, defValue);
	}
}
