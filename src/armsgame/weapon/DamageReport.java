package armsgame.weapon;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

import armsgame.impl.Player;

import static armsgame.card.util.CardDefaults.getCardDefaults;

public class DamageReport
{

	private static void damage0(Player attacker, Player victim, double damage, boolean vampiric)
	{
		// TO DO: change health to double
		double totalHealth = victim.getEnergyLevel() + victim.getShieldLevel();
		double fixedDamage = Math.min(damage, totalHealth); // fix for overflow
		victim.damageShield(fixedDamage);

		if (vampiric)
		{
			attacker.heal(fixedDamage);
		}
	}

	private final String internalType;
	private DamageReport[] combo = new DamageReport[0];

	/**
	 * Constructs a new DamageReport object from a combo of damage reports.
	 *
	 * @param combo a list of damage reports
	 */
	public DamageReport(DamageReport... combo)
	{
		ArrayList<DamageReport> comboList = new ArrayList<>(combo.length * 2);
		LinkedList<DamageReport> reportFlatten = new LinkedList<>();

		// Flatten combo list.
		// TO DO: do this only if tree is thick enough.
		this.combo = combo;
		while (reportFlatten.size() > 0)
		{
			DamageReport dmg = reportFlatten.pop();
			if (dmg.internalType == null)
			{
				for (DamageReport child : dmg.combo)
				{
					reportFlatten.push(child);
				}
			} else
			{
				comboList.add(dmg);
			}
		}

		this.combo = comboList.toArray(new DamageReport[comboList.size()]);
		this.internalType = null;
	}

	/**
	 * Constructs a new DamageReport object from an internal type pointer.
	 *
	 * @param internalType property name internal type that this damage report points to.
	 */
	public DamageReport(String internalType)
	{
		Objects.requireNonNull(internalType);
		this.internalType = internalType;
	}

	public void damage(Player attacker, Player victim, boolean vampiric, double efficiency)
	{
		if (efficiency <= 0)
		{
			return;
		}

		double singleDamage = getSingleTargetDamage() * efficiency;
		double multiDamage = getMultiTargetDamage() * efficiency;
		boolean effectiveVampiric = vampiric && isVampiric();

		if (singleDamage > 0)
		{
			damage0(attacker, victim, singleDamage, effectiveVampiric);
		}

		if (multiDamage > 0)
		{
			attacker.getGame().playerStream().forEach(player -> damage0(attacker, player, multiDamage, effectiveVampiric));
		}
	}

	/**
	 * This retrieves the accuracy rate increase of this weapon part The accuracy rate is implemented as a bar, and this accuracy rate guarantees 100% efficiency rate xx% of the time
	 *
	 * @return a value from 0 to 1
	 */
	public double getAccuracy()
	{

		if (internalType == null)
		{
			double accuracy = 0.0;
			for (DamageReport report : combo)
			{
				accuracy += report.getAccuracy();
			}
			return accuracy;
		} else
		{
			return getInternalDoubleProperty("accuracy");
		}

	}

	/**
	 * This retrieves the internal typing of this damage report.
	 *
	 * @return the internal type.
	 */
	public String getInternalType()
	{
		return internalType;
	}

	/**
	 * Retrieves the damage dealt to multiple targets at 100% efficiency
	 *
	 * @return a positive damage amount.
	 */
	public double getMultiTargetDamage()
	{
		if (internalType == null)
		{
			double dmg = 0.0;
			for (DamageReport report : combo)
			{
				dmg += report.getMultiTargetDamage();
			}
			return dmg;
		} else
		{
			return getInternalDoubleProperty("damage.multi");
		}
	}

	/**
	 * Retrieves the damage dealt to a single targets at 100% efficiency
	 *
	 * @return a positive damage amount.
	 */
	public double getSingleTargetDamage()
	{
		if (internalType == null)
		{
			double dmg = 0.0;
			for (DamageReport report : combo)
			{
				dmg += report.getSingleTargetDamage();
			}
			return dmg;
		} else
		{
			return getInternalDoubleProperty("damage.single");
		}
	}

	/**
	 * Identifies whether if this weapon part is vampiric by default. This is defined by having the energy from the victim being transferred to the damager.
	 *
	 * @return true if it is vampiric, false otherwise.
	 */
	public boolean isVampiric()
	{
		if (internalType == null)
		{
			for (DamageReport report : combo)
			{
				if (report.isVampiric())
				{
					return true;
				}
			}
			return false;
		} else
		{
			return getInternalIntProperty("vampiric", 0) != 0;
		}
	}

	protected double getInternalDoubleProperty(String subKey)
	{
		return getCardDefaults().getDoubleProperty(getInternalType() + "." + subKey, 0.0);
	}

	protected double getInternalDoubleProperty(String subKey, double defValue)
	{
		return getCardDefaults().getDoubleProperty(getInternalType() + "." + subKey, defValue);
	}

	protected int getInternalIntProperty(String subKey)
	{
		return getCardDefaults().getIntProperty(getInternalType() + "." + subKey, 0);
	}

	protected int getInternalIntProperty(String subKey, int defValue)
	{
		return getCardDefaults().getIntProperty(getInternalType() + "." + subKey, defValue);
	}

	protected String getInternalProperty(String subKey)
	{
		return getCardDefaults().getProperty(getInternalType() + "." + subKey);
	}

	protected String getInternalProperty(String subKey, String defValue)
	{
		return getCardDefaults().getProperty(getInternalType() + "." + subKey, defValue);
	}
}
