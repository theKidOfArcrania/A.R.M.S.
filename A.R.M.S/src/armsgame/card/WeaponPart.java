package armsgame.card;

import armsgame.impl.Player;

import static armsgame.card.CardDefaults.getCardDefaults;

/**
 * This class is a placeholder for specific part implements for the Weapon, somewhat bottle-necking the ability to assemble parts.
 *
 * @author Henry
 *
 */
public class WeaponPart
{

	private final String internalType;

	/**
	 * This constructs a weapon part. Note that this is package-private, because the only class that is intended on using this is the PartCard class and Weapon class
	 *
	 * @param internalType
	 *            the internal type of this weapon part.
	 */
	WeaponPart(String internalType)
	{
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
		return getInternalDoubleProperty("accuracy");
	}

	/**
	 * This retrieves the internal typing of this weapon part.
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
		return getInternalDoubleProperty("damage.multi");
	}

	/**
	 * Retrieves the damage dealt to a single targets at 100% efficiency
	 *
	 * @return a positive damage amount.
	 */
	public double getSingleTargetDamage()
	{
		return getInternalDoubleProperty("damage.single");
	}

	/**
	 * Identifies whether if this weapon part is vampiric by default. This is defined by having the energy from the victim being transferred to the damager.
	 *
	 * @return true if it is vampiric, false otherwise.
	 */
	public boolean isVampiric()
	{
		return getInternalIntProperty("vampiric", 0) != 0;
	}

	private void damage0(Player attacker, Player victim, double damage, boolean vampiric)
	{
		// TO DO: change health to double
		double totalHealth = victim.getEnergyLevel() + victim.getShieldLevel();
		double fixedDamage = Math.min(damage, totalHealth); // fix for overflow
		victim.damageShield(fixedDamage);

		if (vampiric)
		{
			// TO DO: healing.
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