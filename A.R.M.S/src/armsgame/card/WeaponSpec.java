/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

/**
 *
 * @author HW
 */
public enum WeaponSpec {

	Beige, Black, Blue, Brown, Gold, Green, LightBlue("Light Blue"), Megenta, Orange, Red, Yellow;
	private final String className;

	WeaponSpec() {
		this.className = name();
	}

	WeaponSpec(String colorName) {
		this.className = colorName;
	}

	public String getClassName() {
		return className;
	}

	@Override
	public String toString() {
		return "class " + className;
	}

}
