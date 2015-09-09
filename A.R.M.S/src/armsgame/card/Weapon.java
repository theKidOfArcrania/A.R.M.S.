package armsgame.card;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

/**
 * This class is the base for implementing different weapon classes. It also contains a list of parts required to complete this weapons set.
 *
 * @author Henry
 *
 */
public abstract class Weapon implements Observable {
	private WeaponPart[] parts;
	private boolean[] built;
	private final ArrayList<InvalidationListener> listeners = new ArrayList<>();

	/**
	 * Constructs a Weapon specs class
	 */
	public Weapon() {
		// Initialize the parts needed for this weapon.

		Arrays.sort(parts);
	}

	@Override
	public synchronized void addListener(InvalidationListener listener) {
		listeners.add(listener);
	}

	/**
	 * This builds the the current part onto the weapon.
	 * 
	 * @param part
	 *            the part to implement on this weapon.
	 * @return
	 */
	public boolean buildPart(WeaponPart part) {
		int partIndex = Arrays.binarySearch(parts, part);
		if (partIndex < 0 || built[partIndex]) { // Not found in parts list, or this is already built on.
			return false;
		} else {
			return built[partIndex] = true;
		}
	}

	/**
	 * This obtains the internal typing of this weapon spec
	 *
	 * @return the internal type.
	 */
	public abstract String getInternalType();

	public String getName() {

	}

	/**
	 *
	 * @param part
	 * @return
	 */
	public boolean isBuildable(WeaponPart part) {
		int partIndex = Arrays.binarySearch(parts, part);
		return partIndex >= 0 && !built[partIndex];
	}

	@Override
	public synchronized void removeListener(InvalidationListener listener) {
		listeners.remove(listener);
	}
}
