/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static armsgame.card.WeaponSpec.Beige;
import static armsgame.card.WeaponSpec.Black;
import static armsgame.card.WeaponSpec.Blue;
import static armsgame.card.WeaponSpec.Brown;
import static armsgame.card.WeaponSpec.Green;
import static armsgame.card.WeaponSpec.LightBlue;
import static armsgame.card.WeaponSpec.Megenta;
import static armsgame.card.WeaponSpec.Orange;
import static armsgame.card.WeaponSpec.Red;
import static armsgame.card.WeaponSpec.Yellow;
import static java.util.Objects.requireNonNull;

/**
 *
 * @author HW
 */
public class DualColor implements Serializable {

	private static final long serialVersionUID = -2188904758680736571L;
	private static final Map<String, DualColor> internalTypingColors;

	static {
		DualColor[] availableWildColors = new DualColor[] { new DualColor(), // rainbow
				// wild rent/wild card properties
				new DualColor(LightBlue, Brown), new DualColor(Orange, Megenta), new DualColor(Red, Yellow), new DualColor(Blue, Green),
				new DualColor(Beige, Black),
				// wild-card properties only.
				new DualColor(Black, Green), new DualColor(Black, LightBlue) };
		DualColor[] singleColorEnum = Arrays.stream(WeaponSpec.class.getEnumConstants())
				.parallel()
				.map(DualColor::new)
				.toArray(DualColor[]::new);
		DualColor[] availableColors = new DualColor[availableWildColors.length + singleColorEnum.length];

		System.arraycopy(singleColorEnum, 0, availableColors, 0, singleColorEnum.length);
		System.arraycopy(availableWildColors, 0, availableColors, singleColorEnum.length, availableWildColors.length);

		internalTypingColors = Arrays.stream(availableWildColors)
				.collect(Collectors.toConcurrentMap(DualColor::getInternalType, UnaryOperator.identity()));

	}

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
	 * Constructs an all-color wild card (except gold, if applicable) DualColor.
	 */
	public DualColor() {
		this.colorName = "Rainbow";
		this.weaponSpec = null;
		this.propertyColor2 = null;
	}

	/**
	 * Constructs a single color DualColor.
	 * <p>
	 *
	 * @param weaponSpec
	 *            What color this property card represents.
	 */
	public DualColor(WeaponSpec weaponSpec) {
		// all parameters MUST be non-null.
		requireNonNull(weaponSpec);
		this.colorName = weaponSpec.getClassName();
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
	public DualColor(WeaponSpec propertyColor1, WeaponSpec propertyColor2) {
		// all parameters MUST be non-null;
		requireNonNull(propertyColor1);
		requireNonNull(propertyColor2);

		if (propertyColor1 == WeaponSpec.Gold || propertyColor2 == WeaponSpec.Gold) {
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

	public DualColor(String internalType) {
		DualColor colors = internalTypingColors.get(internalType);
		if (colors == null) {
			throw new IllegalArgumentException();
		}

		this.weaponSpec = colors.weaponSpec;
		this.propertyColor2 = colors.propertyColor2;
		this.colorName = colors.colorName;
	}

	/**
	 * This determines whether if this property card and the <code>other</code> property card can be put together under one property column.
	 * <p>
	 *
	 * @param other
	 *            the other dual color to compare against.
	 * @return true if this is compatible, false otherwise.
	 */
	public boolean compatibleWith(DualColor other) {
		if (this.isAllWildCard()) {
			return other.isWildCard() || other.getPropertyColor() != WeaponSpec.Gold;
		} else if (other.isAllWildCard()) {
			return isWildCard() || getPropertyColor() != WeaponSpec.Gold;
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
			return other != WeaponSpec.Gold;
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
	 * Describes whether if this DualColor is a all-color wild.
	 * <p>
	 *
	 * @return true if this is all-color wild, false otherwise.
	 */
	@SuppressWarnings("FinalMethod")
	public final boolean isAllWildCard() {
		return propertyColor2 == null && weaponSpec == null;
	}

	/**
	 * Describes whether if this DualColor is a wild color.
	 * <p>
	 *
	 * @return true if this is wild, false if this is regular.
	 */
	@SuppressWarnings("FinalMethod")
	public final boolean isWildCard() {
		return propertyColor2 != null || weaponSpec == null;
	}

}
