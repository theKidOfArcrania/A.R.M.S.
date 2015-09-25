package armsgame.weapon;

import java.util.ArrayList;
import java.util.Arrays;

import armsgame.card.EnergyCrystal;
import armsgame.impl.Player;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.paint.Color;

/**
 * This class is the base for implementing different weapon classes. It also contains a list of parts required to complete this weapons set.
 *
 * @author Henry
 *
 */
public class Weapon implements Observable {
	private final WeaponSpec spec;
	private EnergyCrystal crystal = null;
	private final WeaponPartSpec[] parts;
	private final boolean[] partsBuilt;
	private final ArrayList<InvalidationListener> listeners = new ArrayList<>();

	/**
	 * Constructs a Weapon class
	 *
	 * @param spec the specifications of this weapon.
	 */
	public Weapon(WeaponSpec spec) {
		// Initialize the parts needed for this weapon.
		this.spec = spec;
		parts = spec.getPartSpecs();
		partsBuilt = new boolean[parts.length];
		Arrays.sort(parts);
	}

	@Override
	public synchronized void addListener(InvalidationListener listener) {
		listeners.add(listener);
	}

	/**
	 * Builds an energy crystal on this weapon
	 *
	 * @param crystal the energy crystal to build.
	 * @return true if it was actually built, false otherwise.
	 */
	public boolean buildCrystal(EnergyCrystal crystal) {
		if (this.crystal == null) {
			this.crystal = crystal;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This builds the the current part onto the weapon.
	 *
	 * @param part the part to implement on this weapon.
	 * @return whether if this is actually built or not.
	 */
	public boolean buildPart(WeaponPartSpec part) {
		int partIndex = Arrays.binarySearch(parts, part);
		if (partIndex < 0 || partsBuilt[partIndex]) { // Not found in parts list, or this is already built on.
			return false;
		} else {
			partsBuilt[partIndex] = true;
			listeners.forEach(list -> list.invalidated(this));
			return true;
		}
	}

	public void damage(Player attacker, Player victim) {
		DamageReport dmg = getDamageReport();
		double efficiency = attacker.selectAccuracy(dmg.getAccuracy());
		// TO DO: Damage
	}

	public Color getColorClass() {
		int colorRGB = spec.getRGBColor();
		return Color.rgb((colorRGB >> 16) & 0xFF, (colorRGB >> 8) & 0xFF, colorRGB & 0xFF);
	}

	public DamageReport getDamageReport() {
		DamageReport baseDmg = new DamageReport(getInternalType() + ".damage");
		DamageReport[] partDmg = new DamageReport[parts.length];
		int dmgIndex = 0;

		for (int i = 0; i < parts.length; i++) {
			if (partsBuilt[i]) {
				partDmg[dmgIndex] = new DamageReport(parts[i].getInternalType() + ".damage");
				dmgIndex++;
			} else if (parts[i].isNecessaryUpgrade()) { // A necessary upgrade has not been built
				return new DamageReport();
			}
		}
		return new DamageReport(baseDmg, partDmg);
	}

	/**
	 * This obtains the internal typing of this weapon spec
	 *
	 * @return the internal type.
	 */
	public String getInternalType() {
		return spec.getInternalType();
	}

	public String getName() {
		if (isEnergetic()) {
			return spec.getEnergyFormName();
		}
		return spec.getLongName();
	}

	public WeaponSpec getSpec() {
		return spec;
	}

	/**
	 * Determines whether if this weapon does any effective damage.
	 *
	 * @return true if this has effective damage, false otherwise.
	 */
	public boolean isActive() {
		DamageReport dmg = getDamageReport();
		return dmg.getMultiTargetDamage() > 0 || dmg.getSingleTargetDamage() > 0;
	}

	/**
	 * Specifies whether if this part is needed on this weapon to attain max upgrades.
	 *
	 * @param part the weapon part to test with
	 * @return true if it can be built on here, false otherwise.
	 */
	public boolean isBuildable(WeaponPartSpec part) {
		int partIndex = Arrays.binarySearch(parts, part);
		return partIndex >= 0 && !partsBuilt[partIndex];
	}

	/**
	 * Specifies if the weapon has all max upgrades (other than the energy upgrade);
	 *
	 * @return true if it is completed, false otherwise.
	 */
	public boolean isComplete() {
		for (boolean partBuilt : partsBuilt) {
			if (!partBuilt) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Determines whether if this weapon is energetic.
	 *
	 * @return true if it is energetic, false otherwise.
	 */
	public boolean isEnergetic() {
		return !(crystal == null);
	}

	/**
	 * Describes whether if this weapon hasn't max upgrades.
	 *
	 * @return true if column has loose properties, false otherwise.
	 */
	public boolean isIncomplete() {
		for (boolean partBuilt : partsBuilt) {
			if (!partBuilt) {
				return true;
			}
		}
		return false;
	}

	@Override
	public synchronized void removeListener(InvalidationListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Unbuilds the energy crystal attached to this weapon.
	 *
	 * @return the unbuilt energy crystal.
	 */
	public EnergyCrystal unbuildCrystal() {
		EnergyCrystal crystal = this.crystal;
		this.crystal = null;
		return crystal;
	}

	public boolean unbuildPart(WeaponPartSpec part) {
		int partIndex = Arrays.binarySearch(parts, part);
		if (partIndex < 0 || !partsBuilt[partIndex]) { // Not found in parts list, or this is not already built on.
			return false;
		} else {
			partsBuilt[partIndex] = false;
			listeners.forEach(list -> list.invalidated(this));
			return true;
		}
	}
}
