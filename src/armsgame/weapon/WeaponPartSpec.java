package armsgame.weapon;

import armsgame.impl.Player;

import static armsgame.card.util.CardDefaults.getCardDefaults;

/**
 * This class is a placeholder for specific part implements for the Weapon, somewhat bottle-necking the ability to assemble parts.
 *
 * @author Henry
 *
 */
public class WeaponPartSpec
{

	public static WeaponPartSpec getPartSpec(String codeType)
	{

	}

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

	private final String codeType;

	/**
	 * This constructs a weapon part. Note that this is package-private, because the only class that is intended on using this is the PartCard class and Weapon class
	 *
	 * @param codeType the internal coded type of this weapon part.
	 */
	private WeaponPartSpec(String codeType)
	{
		this.codeType = codeType;
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
	public String getCodeName()
	{
		return codeType;
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

	protected double getInternalDoubleProperty(String subKey)
	{
		return getCardDefaults().getDoubleProperty(getCodeName() + "." + subKey, 0.0);
	}

	protected double getInternalDoubleProperty(String subKey, double defValue)
	{
		return getCardDefaults().getDoubleProperty(getCodeName() + "." + subKey, defValue);
	}

	protected int getInternalIntProperty(String subKey)
	{
		return getCardDefaults().getIntProperty(getCodeName() + "." + subKey, 0);
	}

	protected int getInternalIntProperty(String subKey, int defValue)
	{
		return getCardDefaults().getIntProperty(getCodeName() + "." + subKey, defValue);
	}

	protected String getInternalProperty(String subKey)
	{
		return getCardDefaults().getProperty(getCodeName() + "." + subKey);
	}

	protected String getInternalProperty(String subKey, String defValue)
	{
		return getCardDefaults().getProperty(getCodeName() + "." + subKey, defValue);
	}
}