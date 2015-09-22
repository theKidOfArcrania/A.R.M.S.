/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card.impl;

import java.util.Arrays;

import static armsgame.card.impl.CardDefaults.getCardDefaults;

/**
 *
 * @author HW
 */
public class WeaponSpec
{
	static
	{
		String[] specsList = CardDefaults.getCardDefaults().getProperty("specs.$list").split("\\s*,\\s*");
		specs = Arrays.stream(specsList).parallel().map(WeaponSpec::new).toArray(WeaponSpec[]::new);
	}
	/*
	 * Knife("Kfe", 0x6B453A), Pistol("Pis", 0x101050), Mine("Mne", 0x105600), Shotgun("Shg", 0x7C2AB2), Rifle("Rfl", 0xED7423), Sniper("Snp", // 0x86E1EF), Rocket("Rkt", 0xDD0000), Nuke("Nke", 0xFFFF00);
	 */

	private static final WeaponSpec[] specs;

	public static WeaponSpec identifySpec(String codeType)
	{
		for (WeaponSpec spec : specs)
		{
			if (spec.getCodeName().equals(codeType))
			{
				return spec;
			}
		}
		return null;
	}

	private final WeaponPartSpec[] parts;
	private final String codeType;

	private WeaponSpec(String codeType)
	{
		this.codeType = codeType;
		String[] partSpecsList = getInternalProperty("parts").split("\\s*,\\s*");
		parts = Arrays.stream(partSpecsList).parallel().map(WeaponPartSpec::new).toArray(WeaponPartSpec[]::new);
	}

	public String getCodeName()
	{
		return codeType;
	}

	public String getEnergyFormName()
	{
		return getInternalProperty("energyForm");
	}

	public String getInternalType()
	{
		return "specs." + codeType;
	}

	public String getLongName()
	{
		return getInternalProperty("longName");
	}

	public WeaponPartSpec[] getPartSpecs()
	{
		String[] partsType = getInternalProperty("parts").split("\\s*,\\s*");
		return Arrays.stream(partsType).map(WeaponPartSpec::new).toArray(WeaponPartSpec[]::new);
	}

	public int getRGBColor()
	{
		return getInternalIntProperty("rgbColor");
	}

	public String getShortName()
	{
		return getInternalProperty("shortName");
	}

	@Override
	public String toString()
	{
		return getShortName() + " weapon-spec";
	}

	protected double getInternalDoubleProperty(String subKey)
	{
		return getCardDefaults().getDoubleProperty(getInternalType() + "." + subKey, 0.0);
	}

	protected double getInternalDoubleProperty(String subKey, double defValue)
	{
		return getCardDefaults().getDoubleProperty(getInternalType() + "." + subKey, defValue);
	}

	protected int getInternalIntProperty(String subKey)
	{
		return getCardDefaults().getIntProperty(getInternalType() + "." + subKey, 0);
	}

	protected int getInternalIntProperty(String subKey, int defValue)
	{
		return getCardDefaults().getIntProperty(getInternalType() + "." + subKey, defValue);
	}

	protected String getInternalProperty(String subKey)
	{
		return getCardDefaults().getProperty(getInternalType() + "." + subKey);
	}

	protected String getInternalProperty(String subKey, String defValue)
	{
		return getCardDefaults().getProperty(getInternalType() + "." + subKey, defValue);
	}
}
