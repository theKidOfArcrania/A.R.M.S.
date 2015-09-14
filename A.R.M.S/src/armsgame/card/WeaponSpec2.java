/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

import java.util.Arrays;

/**
 *
 * @author HW
 */
public enum WeaponSpec2 {

	Knife("Kfe", 0x6B453A), Pistol("Pis", 0x101050), Mine("Mne", 0x105600), Shotgun("Shg", 0x7C2AB2), Rifle("Rfl", 0xED7423), Sniper("Snp",
			0x86E1EF), Rocket("Rkt", 0xDD0000), Nuke("Nke", 0xFFFF00);

	public static WeaponSpec2 locateSpec(String internalType) {
		return Arrays.stream(WeaponSpec2.values())
			.parallel()
			.filter(spec -> spec.codeName.equals(internalType))
			.findAny()
			.orElse(null);
	}

	private final String codeName;
	private final int rgb;

	WeaponSpec2(String codeName, int rgb) {
		this.codeName = codeName;
		this.rgb = rgb;
	}

	public String getCodeName() {
		return codeName;
	}

	public String getEnergyFormName() {
		return CardDefaults.getCardDefaults()
			.getProperty("specs." + getCodeName() + ".energyForm");
	}

	public String getLongName() {
		return CardDefaults.getCardDefaults()
			.getProperty("specs." + getCodeName() + ".longName");
	}

	public int getRGBColor() {
		// TO DO: export to carddefs.
		return rgb;
	}

	@Override
	public String toString() {
		return name() + " weapon-spec";
	}
}
