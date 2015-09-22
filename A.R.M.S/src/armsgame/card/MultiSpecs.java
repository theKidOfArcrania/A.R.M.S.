/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

//TO DO: rewrite comments

import java.io.Serializable;
import java.util.Arrays;
import java.util.EnumSet;

import armsgame.card.impl.WeaponSpec;

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

	private final EnumSet<WeaponSpec> weaponSpecs;

	/**
	 * Constructs an all-color wild card (except gold, if applicable) MultiSpecs.
	 */
	public MultiSpecs() {
		this.weaponSpecs = EnumSet.allOf(WeaponSpec.class);
		weaponSpecs.remove(WeaponSpec.Nuke);
	}

	public MultiSpecs(String internalType) {
		String[] typeSpecs = internalType.split("_");
		weaponSpecs = EnumSet.noneOf(WeaponSpec.class);

		Arrays.stream(typeSpecs)
			.parallel()
			.map(WeaponSpec::locateSpec)
			.peek(spec -> {
				if (spec == null) {
					throw new IllegalArgumentException();
				}
			})
			.forEach(weaponSpecs::add);
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
		return weaponSpecs.contains(other);
	}

	/**
	 * Getter of the property name for this card.
	 * <p>
	 *
	 * @return the value of colorName
	 */
	public String getColorName() {
		StringBuilder sb = new StringBuilder(weaponSpecs.size() * 20);
		boolean first = true;
		for (WeaponSpec spec : weaponSpecs) {
			if (first) {
				sb.append(spec.getLongName());
			} else {
				sb.append("-")
					.append(spec.getLongName());
			}
		}
		return sb.toString();
	}

	public String getInternalType() {
		StringBuilder sb = new StringBuilder(weaponSpecs.size() * 20);
		boolean first = true;
		for (WeaponSpec spec : weaponSpecs) {
			if (first) {
				sb.append(spec.getCodeName());
			} else {
				sb.append("_")
					.append(spec.getCodeName());
			}
		}
		return sb.toString();
	}
}
