/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

import java.util.ArrayList;

import armsgame.card.util.CardActionType.Likeness;
import armsgame.impl.Player;
import armsgame.weapon.Weapon;
import armsgame.weapon.WeaponPartSpec;
import armsgame.weapon.WeaponSpec;

/**
 *
 * @author Henry
 */
public class Ammunition extends Action
{

	/**
	 *
	 */
	private static final long serialVersionUID = 9126449081012411183L;
	private final WeaponSpec[] specTargets;

	/**
	 * Constructs an ammunition with the internal typing. This is the constructor called by card creator.
	 *
	 * @param codeType the internal code sub-type of the ammnunition
	 */
	public Ammunition(String codeType)
	{
		String[] partSpecsList = getInternalProperty("parts").split("\\s*,\\s*");
		String[] partsType = getInternalProperty("parts").split("\\s*,\\s*");
		ArrayList<WeaponPartSpec> parts = new ArrayList<>(partsType.length);
		for (String element : partsType)
		{
			WeaponPartSpec part = WeaponPartSpec.getPartSpec(element);
			if (part != null)
			{
				parts.add(part);
			}
		}
		this.parts = new WeaponPartSpec[parts.size()];
	}

	@Override
	public boolean actionPlayed(Player self)
	{
		Weapon weapon = self.selectPropertyColumn("Choose your weapon to attack with.", this::isValidRent);
		return processDamage(self, isGlobal(), weapon.isEnergetic(), weapon.getAttackPoints());
	}

	@Override
	public String getCardName()
	{
		return specTargets.getColorName() + " Ammunition";
	}

	@Override
	public String getInternalType()
	{
		return "action.ammunition." + specTargets.getInternalType();
	}

	@Override
	public boolean isEnabled(Player self, Likeness action)
	{
		if (action == Likeness.Action)
		{
			return self.columnStream().parallel().anyMatch(this::isValidRent);
		}
		return true;
	}

	public boolean isGlobal()
	{
		return this.getInternalIntProperty("global", 0) != 0;
	}

	public boolean isValidRent(Weapon column)
	{
		return specTargets.compatibleWith(column.getPropertyColor()) && column.getAttackPoints() > 0;
	}

}
