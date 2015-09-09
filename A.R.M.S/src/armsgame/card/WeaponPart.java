package armsgame.card;

/**
 * This class is a placeholder for specific part implements for the Weapon, somewhat bottle-necking the ability to assemble parts.
 *
 * @author Henry
 *
 */
public class WeaponPart {

	private final String internalType;

	/**
	 * This constructs a weapon part. Note that this is package-private, because the only class that is intended on using this is the PartCard class and Weapon class
	 *
	 * @param internalType
	 *            the internal type of this weapon part.
	 */
	WeaponPart(String internalType) {
		this.internalType = internalType;
	}

	/**
	 * This retrieves the internal typing of this weapon part.
	 *
	 * @return the internal type.
	 */
	public String getInternalType() {
		return internalType;
	}

}