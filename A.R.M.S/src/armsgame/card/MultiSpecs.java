/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

//TO DO: rewrite comments

import java.io.Serializable;

import static java.util.Objects.requireNonNull;

/**
 *
 * @author HW
 */
public class MultiSpecs implements Serializable {

	private static final long serialVersionUID = -2188904758680736571L;

	private static String colorString(WeaponSpec propertyType) {
		String propName = propertyType.name();
		return propName.substring(0, 1)
				.toLowerCase() + propName.substring(1);
	}

	private final String colorName;
	private final WeaponSpec weaponSpec;

	// this is only used if this is a wild card.
	private final WeaponSpec propertyColor2;

	/**
	 * Constructs an all-color wild card (except gold, if applicable) MultiSpecs.
	 */
	public MultiSpecs() {
		this.colorName = "Rainbow";
		this.weaponSpec = null;
		this.propertyColor2 = null;
	}

	public MultiSpecs(String internalType) {
		MultiSpecs colors = internalTypingColors.get(internalType);
		if (colors == null) {
			throw new IllegalArgumentException();
		}

		this.weaponSpec = colors.weaponSpec;
		this.propertyColor2 = colors.propertyColor2;
		this.colorName = colors.colorName;
	}

	/**
	 * Constructs a single color MultiSpecs.
	 * <p>
	 *
	 * @param weaponSpec
	 *            What color this property card represents.
	 */
	public MultiSpecs(WeaponSpec weaponSpec) {
		// all parameters MUST be non-null.
		requireNonNull(weaponSpec);
		this.colorName = weaponSpec.getCodeName();
		this.weaponSpec = weaponSpec;
		// set it to null because this is a single color property.
		propertyColor2 = null;
	}

	/**
	 * Constructs a bi-color wild card property card. Note that the property name IS "Wild Card"
	 * <p>
	 *
	 * @param propertyColor1
	 *            The first color of this wild card.
	 * @param propertyColor2
	 *            The second color of this wild card.
	 */
	@SuppressWarnings("AssignmentToMethodParameter")
	public MultiSpecs(WeaponSpec propertyColor1, WeaponSpec propertyColor2) {
		// all parameters MUST be non-null;
		requireNonNull(propertyColor1);
		requireNonNull(propertyColor2);

		if (propertyColor1 == WeaponSpec.Nuke || propertyColor2 == WeaponSpec.Nuke) {
			throw new IllegalArgumentException("Gold cannot be included in wild card");
		}

		// always have same ordering, as how the colors were sorted in the first place.
		if (propertyColor1.compareTo(propertyColor2) > 0) {
			this.weaponSpec = propertyColor1;
			this.propertyColor2 = propertyColor2;
		} else if (propertyColor1 == propertyColor2) {
			throw new IllegalArgumentException("Must have two DIFFERENT property colors");
		} else {
			this.weaponSpec = propertyColor1;
			this.propertyColor2 = propertyColor2;
		}
		this.colorName = propertyColor1 + " -OR- " + propertyColor2;
	}

	/**
	 * This determines whether if this property card and the <code>other</code> property card can be put together under one property column.
	 * <p>
	 *
	 * @param other
	 *            the other dual color to compare against.
	 * @return true if this is compatible, false otherwise.
	 */
	public boolean compatibleWith(MultiSpecs other) {
		if (this.isAllWildCard()) {
			return other.isWildCard() || other.getPropertyColor() != WeaponSpec.Nuke;
		} else if (other.isAllWildCard()) {
			return isWildCard() || getPropertyColor() != WeaponSpec.Nuke;
		} else {
			boolean compatible = getPropertyColor() == other.getPropertyColor() || getPropertyColor() == other.getPropertyColor2();
			boolean compatible2 = getPropertyColor2() == other.getPropertyColor() || getPropertyColor2() == other.getPropertyColor2();
			return compatible || (getPropertyColor2() != null && compatible2);
		}
	}

	/**
	 * This determines whether if this property card and the <code>other</code> color can be put together under one property column.
	 * <p>
	 *
	 * @param other
	 *            the other color to compare against.
	 * @return true if this is compatible, false otherwise.
	 */
	public boolean compatibleWith(WeaponSpec other) {
		if (this.isAllWildCard()) {
			return other != WeaponSpec.Nuke;
		} else {
			boolean compatible = getPropertyColor() == other;
			boolean compatible2 = getPropertyColor2() == other;
			return compatible || (getPropertyColor2() != null && compatible2);
		}
	}

	/**
	 * Getter of the property name for this card.
	 * <p>
	 *
	 * @return the value of colorName
	 */
	public String getColorName() {
		return colorName;
	}

	public String getInternalType() {
		if (isWildCard()) {
			if (isAllWildCard()) {
				return "wild.rainbow";
			} else {
				return "wild." + colorString(weaponSpec) + propertyColor2.name();
			}
		} else {
			return colorString(weaponSpec);
		}
	}

	/**
	 * This retrieves the color of the property color 1. If this is null, then the card is an rainbow wild-card.
	 * <p>
	 *
	 * @return property color 1
	 */
	public WeaponSpec getPropertyColor() {
		return weaponSpec;
	}

	/**
	 * This retrieves the color of the property color 2. If this is null, then the card is a single-color card if color 1 is not null. If this is non-null, then the card is a dual color wild.
	 * <p>
	 *
	 * @return property color 2
	 */
	public WeaponSpec getPropertyColor2() {
		return propertyColor2;
	}

	/**
	 * Describes whether if this MultiSpecs is a all-color wild.
	 * <p>
	 *
	 * @return true if this is all-color wild, false otherwise.
	 */
	@SuppressWarnings("FinalMethod")
	public final boolean isAllWildCard() {
		return propertyColor2 == null && weaponSpec == null;
	}

	/**
	 * Describes whether if this MultiSpecs is a wild color.
	 * <p>
	 *
	 * @return true if this is wild, false if this is regular.
	 */
	@SuppressWarnings("FinalMethod")
	public final boolean isWildCard() {
		return propertyColor2 != null || weaponSpec == null;
	}

}
