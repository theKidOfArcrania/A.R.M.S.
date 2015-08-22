/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card.standard;

import armsgame.card.DualColor;
import armsgame.card.WeaponPart;
import armsgame.card.WeaponSpec;
import armsgame.impl.Player;
import armsgame.impl.CardActionType.Likeness;

import static java.util.Objects.requireNonNull;

/**
 * This class encapsulates a property card, used for winning, and also for renting from. This also has implementation for wild cards, based on the two properties that determine the color type of a property: <code>propertyColors</code> and <code>propertyColor2</code>. In a regular property card, you
 * would have just <code>propertyColors</code> filled as the color of this property. In a bi-color wild card, both of these properties will be filled as to which colors it can rotate to. In an all-color wild card, both of these properties are left to be NULL. Note: when determining rents, don't use
 * these two properties directly, rather, use the helper functions {@link #compatibleWith(armsgame.card.WeaponPart)} and {@link #canStandAlone()} to determine whether if this can
 * <p>
 *
 * @author HW
 */
@SuppressWarnings("FinalClass")
public final class SWeaponPart extends StandardCard implements WeaponPart {

	/**
	 *
	 */
	private static final long serialVersionUID = -4847169645075457537L;
	private int propNumber = 0;
	private final DualColor propertyColors;

	/**
	 * Constructs an all-color wild card (except gold, if applicable).
	 */
	public SWeaponPart() {
		propertyColors = new DualColor();
	}

	/**
	 * Constructs a regular property card.
	 * <p>
	 *
	 * @param propNumber
	 *            The number of the property (ie 1st Blue WeaponPart or 3rd Green WeaponPart)
	 * @param weaponSpec
	 *            What color this property card represents.
	 */
	public SWeaponPart(int propNumber, WeaponSpec weaponSpec) {
		// all parameters MUST be non-null.
		requireNonNull(propNumber);
		requireNonNull(weaponSpec);
		this.propNumber = propNumber;
		this.propertyColors = new DualColor(weaponSpec);
	}

	/**
	 * Constructs a bi-color wild card property card.
	 * <p>
	 *
	 * @param propertyColor1
	 *            The first color of this wild card.
	 * @param propertyColor2
	 *            The second color of this wild card.
	 */
	public SWeaponPart(WeaponSpec propertyColor1, WeaponSpec propertyColor2) {
		// all parameters MUST be non-null;
		requireNonNull(propertyColor1);
		requireNonNull(propertyColor2);
		propertyColors = new DualColor(propertyColor1, propertyColor2);
	}

	/**
	 * Constructs a WeaponPart Card with the internal typing. This is the constructor called by card creator.
	 *
	 * @param internalType
	 *            the internal type of the property-card color
	 */
	public SWeaponPart(String internalType) {
		if (internalType.contains("$")) {
			int propNumIndex = internalType.indexOf("$");
			propertyColors = new DualColor(internalType.substring(0, propNumIndex));
			propNumber = Integer.parseInt(internalType.substring(propNumIndex + 1));
		} else {
			propertyColors = new DualColor(internalType);
			if (!propertyColors.isWildCard()) {
				throw new IllegalArgumentException("Single property card must have '$' and prop number as a suffix.");
			}
		}
	}

	@Override
	public boolean canStandAlone() {
		return !isAllWildCard();
	}

	@Override
	public boolean compatibleWith(WeaponPart other) {

		return propertyColors.compatibleWith(other.getDualColors());
	}

	@Override
	public String getCardName() {
		return getPropertyName();
	}

	@Override
	public DualColor getDualColors() {
		return propertyColors;
	}

	@Override
	public String getInternalType() {
		return "props." + propertyColors.getInternalType() + (isWildCard() ? "" : "." + propNumber);
	}

	@Override
	public String getPropertyName() {
		if (isWildCard()) {
			return propertyColors.getColorName() + " Wild";
		}
		return getInternalProperty("name") + "#" + propNumber;
	}

	@Override
	public int getEnergyValue() {
		return 0;
	}

	@Override
	public int getValue() {
		return getInternalIntProperty("value");
	}

	@Override
	public boolean isAllWildCard() {
		return propertyColors.isAllWildCard();
	}

	@Override
	public boolean isEnabled(Player self, Likeness action) {
		return true;
	}

	@Override
	public boolean isWildCard() {
		return propertyColors.isWildCard();
	}

}
