/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

import armsgame.card.impl.CardActionType;
import armsgame.card.impl.SupportedActions;
import armsgame.card.impl.WeaponPartSpec;
import armsgame.card.impl.CardActionType.Likeness;
import armsgame.impl.Player;

import static java.util.Objects.requireNonNull;

/**
 * This class encapsulates a property card, used for winning, and also for renting from. This also has implementation for wild cards, based on the two properties that determine the color type of a property: <code>weaponSpec</code> and <code>propertyColor2</code>. In a regular property card, you
 * would have just <code>weaponSpec</code> filled as the color of this property.
 * <p>
 *
 * @author HW
 */
@SuppressWarnings("FinalClass")
public final class PartCard extends Action
{
	/**
	 *
	 */
	private static final String prefixes = "$*";
	private static final long serialVersionUID = -4847169645075457537L;
	private final int type;
	private final WeaponPartSpec partBuild;

	/**
	 * Constructs an all-part weapon part, with no restrictions.
	 */
	public PartCard()
	{
		this.type = 1;
		this.partBuild = null;
	}

	/**
	 * Constructs a single-part card.
	 * <p>
	 *
	 * @param weaponPartSpec
	 *            This is the weapon part that this part will build
	 */
	public PartCard(WeaponPartSpec weaponPartSpec)
	{
		// all parameters MUST be non-null.
		requireNonNull(weaponPartSpec);
		this.type = 0;
		this.partBuild = weaponPartSpec;
	}

	/**
	 * Constructs a PartCard Card with the internal typing. This is the constructor called by card creator.
	 *
	 * @param internalType
	 *            the internal type of the property-card color
	 */
	PartCard(String internalType)
	{
		char prefix = internalType.charAt(0);
		switch (prefix) {
		case '$':
			type = 0;
			partBuild = new WeaponPartSpec(internalType.substring(1));
			break;
		case '*':
			type = 1;
			partBuild = null;
			break;
		default:
			throw new IllegalArgumentException("Invalid prefix header '" + prefix + "'");
		}
	}

	@Override
	public boolean actionPlayed(Player self)
	{
		// TO DO: the action.
	}

	@Override
	public String getCardName()
	{
		return getPropertyName();
	}

	public int getDamageBase()
	{
		return getInternalIntProperty("boostBase");
	}

	@Override
	public String getInternalType()
	{
		return "action.part." + prefixes.charAt(type);
	}

	/**
	 * Getter of the property name for this card.
	 * <p>
	 *
	 * @return the value of propertyName
	 */
	public String getPropertyName()
	{
		return getInternalProperty("name");
	}

	@Override
	public SupportedActions getSupportedTypes()
	{
		SupportedActions actions = new SupportedActions();
		actions.addAction(new CardActionType("Add property", "move.property"));
		actions.addAction(new CardActionType("Discard", "move.discard"));
		return actions;
	}

	@Override
	public boolean isEnabled(Player self, Likeness action)
	{
		return true;
	}

	public int modifyDamage(int base)
	{
		return base + getDamageBase();
	}

}
