/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

import armsgame.impl.Player;
import armsgame.impl.CardActionType.Likeness;

import static java.util.Objects.requireNonNull;

/**
 *
 * @author Henry
 */
public class Ammunition extends Action {

	/**
	 *
	 */
	private static final long serialVersionUID = 9126449081012411183L;
	private final DualColor propertyColors;

	/**
	 * Constructs an all-color wild rent (except gold, if applicable).
	 */
	public Ammunition() {
		propertyColors = new DualColor();
	}

	/**
	 * Constructs a one-color rent card.
	 *
	 * @param weaponSpec
	 *            What color this rent card represents.
	 */
	public Ammunition(WeaponSpec weaponSpec) {
		// all parameters MUST be non-null.
		requireNonNull(weaponSpec);
		propertyColors = new DualColor(weaponSpec);
	}

	/**
	 * Constructs a bi-color wild card rent card.
	 * <p>
	 *
	 * @param propertyColor1
	 *            The first color of this wild card.
	 * @param propertyColor2
	 *            The second color of this wild card.
	 */
	public Ammunition(WeaponSpec propertyColor1, WeaponSpec propertyColor2) {
		// all parameters MUST be non-null;
		requireNonNull(propertyColor1);
		requireNonNull(propertyColor2);
		propertyColors = new DualColor(propertyColor1, propertyColor2);
	}

	/**
	 * Constructs a Rent Card with the internal typing. This is the constructor called by card creator.
	 *
	 * @param internalType
	 *            the internal type of the rent-card color
	 */
	public Ammunition(String internalType) {
		propertyColors = new DualColor(internalType);
	}

	@Override
	public boolean actionPlayed(Player self) {
		int rent = self.columnStream()
				.parallel()
				.filter(this::isValidRent)
				.mapToInt(WeaponSet::getRent)
				.max()
				.orElse(0);
		if (rent == 0) {
			return false;
		}
		return payRequest(self, isGlobal(), rent, "rent");
	}

	@Override
	public String getCardName() {
		return propertyColors.getColorName() + " Rent";
	}

	@Override
	public String getInternalType() {
		return "action.rent." + propertyColors.getInternalType();
	}

	@Override
	public boolean isEnabled(Player self, Likeness action) {
		if (action == Likeness.Action) {
			return self.columnStream()
					.parallel()
					.anyMatch(this::isValidRent);
		}
		return true;
	}

	public boolean isGlobal() {
		return this.getInternalIntProperty("global", 0) != 0;
	}

	public boolean isValidRent(WeaponSet column) {
		return propertyColors.compatibleWith(column.getPropertyColor()) && column.getRent() > 0;
	}

}
