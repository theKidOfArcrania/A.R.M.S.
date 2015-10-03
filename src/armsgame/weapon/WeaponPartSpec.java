package armsgame.weapon;

import java.util.Arrays;
import java.util.HashMap;

import static armsgame.card.util.CardDefaults.getCardDefaults;

/**
 * This class is a placeholder for specific part implements for the Weapon, somewhat bottle-necking the ability to assemble parts.
 *
 * @author Henry
 *
 */
public class WeaponPartSpec {
	private static String[] SUPPORTED_CODES = getCardDefaults().getProperty("specs.parts").split("\\s*,\\s*");

	static {
		Arrays.sort(SUPPORTED_CODES);
	}

	private static HashMap<String, WeaponPartSpec> partSpecs = new HashMap<>();

	public static WeaponPartSpec getPartSpec(String codeType) {
		if (!partSpecs.containsKey(codeType)) {
			if (Arrays.binarySearch(SUPPORTED_CODES, codeType) >= 0) {
				partSpecs.put(codeType, new WeaponPartSpec(codeType));
			} else {
				partSpecs.put(codeType, null);
			}
		}
		return partSpecs.get(codeType);
	}

	private final String codeType;

	/**
	 * This constructs a weapon part. Note that this is package-private, because the only class that is intended on using this is the PartCard class and Weapon class
	 *
	 * @param codeType the internal coded type of this weapon part.
	 */
	private WeaponPartSpec(String codeType) {
		this.codeType = codeType;
	}

	/**
	 * This retrieves the accuracy rate increase of this weapon part The accuracy rate is implemented as a bar, and this accuracy rate guarantees 100% efficiency rate xx% of the time
	 *
	 * @return a value from 0 to 1
	 */
	public double getAccuracy() {
		return getInternalDoubleProperty("accuracy");
	}

	/**
	 * This retrieves the internal typing of this weapon part.
	 *
	 * @return the internal type.
	 */
	public String getCodeName() {
		return codeType;
	}

	public String getInternalType() {
		return "specs.part." + getCodeName();
	}

	/**
	 * Retrieves the damage dealt to multiple targets at 100% efficiency
	 *
	 * @return a positive damage amount.
	 */
	public double getMultiTargetDamage() {
		return getInternalDoubleProperty("damage.multi");
	}

	/**
	 * Retrieves a user-friendly name of this weapon part.
	 * 
	 * @return the name of this weapon
	 */
	public String getName() {
		return getInternalProperty("name");
	}

	/**
	 * Retrieves the damage dealt to a single targets at 100% efficiency
	 *
	 * @return a positive damage amount.
	 */
	public double getSingleTargetDamage() {
		return getInternalDoubleProperty("damage.single");
	}

	/**
	 * Describes whether if this part upgrade is needed to deal any damage.
	 *
	 * @return true if this is neccessary, false otherwise.
	 */
	public boolean isNecessaryUpgrade() {
		return getInternalIntProperty("necessaryUgrade") != 0;
	}

	/**
	 * Identifies whether if this weapon part is vampiric by default. This is defined by having the energy from the victim being transferred to the damager.
	 *
	 * @return true if it is vampiric, false otherwise.
	 */
	public boolean isVampiric() {
		return getInternalIntProperty("vampiric", 0) != 0;
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