/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

import armsgame.impl.CardActionType;
import armsgame.impl.CardActionType.Likeness;
import armsgame.impl.Player;
import armsgame.impl.SupportedActions;

import static java.util.Objects.requireNonNull;

/**
 * This class encapsulates a property card, used for winning, and also for renting from. This also has implementation for wild cards, based on the two properties that determine the color type of a property: <code>weaponSpec</code> and <code>propertyColor2</code>. In a regular property card, you
 * would have just <code>weaponSpec</code> filled as the color of this property.
 * <p>
 *
 * @author HW
 */
@SuppressWarnings("FinalClass")
public final class WeaponPart extends Card {

	/**
	 *
	 */
	private static final long serialVersionUID = -4847169645075457537L;

	private final MultiSpecs weaponSpec;

	/**
	 * Constructs a regular property card.
	 * <p>
	 *
	 * @param partNum
	 *            The index of the weapon part being referred to.
	 * @param weaponSpec
	 *            What weapon-spec this part belongs to.
	 */
	public WeaponPart(int partNum, WeaponSpec weaponSpec) {
		// all parameters MUST be non-null.
		requireNonNull(partNum);
		requireNonNull(weaponSpec);
		this.propNumber = partNum;
		this.weaponSpec = weaponSpec;
	}

	/**
	 * Constructs a WeaponPart Card with the internal typing. This is the constructor called by card creator.
	 *
	 * @param internalType
	 *            the internal type of the property-card color
	 */
	public WeaponPart(String internalType) {
		if (internalType.contains("$")) {
			int propNumIndex = internalType.indexOf("$");
			weaponSpec = WeaponSpec.locateSpec(internalType.substring(0, propNumIndex));
			propNumber = Integer.parseInt(internalType.substring(propNumIndex + 1));
		} else {
			throw new IllegalArgumentException("Single property card must have '$' and prop number as a suffix.");
		}
	}

	@Override
	public boolean actionPlayed(Player self) {
		WeaponSet column = self.getPropertyColumn(getSpec());
		column.addAndSort(this);
		return true;
	}

	@Override
	public String getCardName() {
		return getPropertyName();
	}

	public int getDamageBase() {
		return getInternalIntProperty("boostBase");
	}

	@Override
	public String getInternalType() {
		return "props." + weaponSpec.getCodeName() + "." + propNumber;
	}

	/**
	 * Getter of the property name for this card.
	 * <p>
	 *
	 * @return the value of propertyName
	 */
	public String getPropertyName() {
		return getInternalProperty("name");
	}

	public WeaponSpec getSpec() {
		return weaponSpec;
	}

	@Override
	public SupportedActions getSupportedTypes() {
		SupportedActions actions = new SupportedActions();
		actions.addAction(new CardActionType("Add property", "move.property"));
		actions.addAction(new CardActionType("Discard", "move.discard"));
		return actions;
	}

	@Override
	public boolean isEnabled(Player self, Likeness action) {
		return true;
	}

	public int modifyDamage(int base) {
		return base + getDamageBase();
	}

}
