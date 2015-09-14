/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

import static armsgame.card.CardDefaults.getCardDefaults;

/**
 *
 * @author HW
 */
public class WeaponSpec
{

	/*
	 * Knife("Kfe", 0x6B453A), Pistol("Pis", 0x101050), Mine("Mne", 0x105600), Shotgun("Shg", 0x7C2AB2), Rifle("Rfl", 0xED7423), Sniper("Snp", // 0x86E1EF), Rocket("Rkt", 0xDD0000), Nuke("Nke", 0xFFFF00);
	 */

	private final String codeType;

	WeaponSpec(String codeType)
	{
		this.codeType = codeType;
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
		return name() + " weapon-spec";
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
