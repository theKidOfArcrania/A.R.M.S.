package armsgame.card;

import armsgame.impl.CardActionType;
import armsgame.impl.Player;
import armsgame.impl.SupportedActions;

public interface WeaponPart extends Valuable {

	public static final CardActionType Property = new CardActionType("Add WeaponPart", "move.property");

	@Override
	default boolean actionPlayed(Player self) {
		WeaponSet column = self.getPropertyColumn(getDualColors().getPropertyColor());
		column.addAndSort(this);
		return true;
	}

	/**
	 * Determines whether if this card can stand alone by itself. Meaning, if you can rent other people with this card.
	 * <p>
	 *
	 * @return true if it can stand alone, false otherwise.
	 */
	boolean canStandAlone();

	/**
	 * This determines whether if this property card and the <code>other</code> property card can be put together under one property set.
	 * <p>
	 *
	 * @param other
	 *            the other property to compare against.
	 * @return true if this is compatible, false otherwise.
	 */
	boolean compatibleWith(WeaponPart other);

	DualColor getDualColors();

	/**
	 * Getter of the property name for this card.
	 * <p>
	 *
	 * @return the value of propertyName
	 */
	String getPropertyName();

	@Override
	default SupportedActions getSupportedTypes() {
		SupportedActions actions = new SupportedActions();
		actions.addAction(new CardActionType("Add property", "move.property"));
		actions.addAction(new CardActionType("Discard", "move.discard"));
		return actions;
	}

	/**
	 * Describes whether if this card is a all-color wild card.
	 * <p>
	 *
	 * @return true if this is all-color wild, false otherwise.
	 */
	boolean isAllWildCard();

	/**
	 * Describes whether if this card is a wild card.
	 * <p>
	 *
	 * @return true if this is wild, false if this is regular.
	 */
	boolean isWildCard();

}