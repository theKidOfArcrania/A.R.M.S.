
package armsgame.card;

/**
 * <<<<<<< Upstream, based on branch 'homemaster' of https://github.com/theKidOfArcrania/A.R.M.S. This class is a placeholder for specific part implements for the Weapon, somewhat bottle-necking the ability to assemble parts. ======= This class encapsulates a property card, used for winning, and
 * also for renting from. This also has implementation for wild cards, based on the two properties that determine the color type of a property: <code>weaponSpec</code> and <code>propertyColor2</code>. In a regular property card, you would have just <code>weaponSpec</code> filled as the color of this
 * property.
 * <p>
 * >>>>>>> 01819f1 General Implementations!
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
