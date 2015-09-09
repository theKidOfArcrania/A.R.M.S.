package armsgame.card;

import java.util.ArrayList;
import java.util.Arrays;

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
public abstract class Weapon implements Observable
{

	private final WeaponPart[] parts;
	private final boolean[] partsBuilt;
	private final ArrayList<InvalidationListener> listeners = new ArrayList<>();

	/**
	 * Constructs a Weapon class
	 */
	public Weapon()
	{
		// Initialize the parts needed for this weapon.
		String[] partsType = CardDefaults.getCardDefaults().getProperty(getInternalType() + ".parts").split(" *, *");
		parts = Arrays.stream(partsType).map(WeaponPart::new).toArray(WeaponPart[]::new);
		partsBuilt = new boolean[parts.length];
		Arrays.sort(parts);
	}

	@Override
	public synchronized void addListener(InvalidationListener listener)
	{
		listeners.add(listener);
	}

	/**
	 * This builds the the current part onto the weapon.
	 *
	 * @param part
	 *            the part to implement on this weapon.
	 * @return whether if this is actually built or not.
	 */
	public boolean buildPart(WeaponPart part)
	{
		int partIndex = Arrays.binarySearch(parts, part);
		if (partIndex < 0 || partsBuilt[partIndex])
		{ // Not found in parts list, or this is already built on.
			return false;
		} else
		{
			listeners.forEach(list -> list.invalidated(this));
			return partsBuilt[partIndex] = true;
		}
	}

	public abstract int damage(Player attacker, Player defender);

	public Color getColorClass()
	{
		int colorRGB = CardDefaults.getCardDefaults().getIntProperty(getInternalType() + ".color", 0xFFFFFF);
		return Color.rgb((colorRGB >> 16) & 0xFF, (colorRGB >> 8) & 0xFF, colorRGB & 0xFF);
	}

	/**
	 * This obtains the internal typing of this weapon spec
	 *
	 * @return the internal type.
	 */
	public abstract String getInternalType();

	public String getName()
	{
		return CardDefaults.getCardDefaults().getProperty(getInternalType() + ".name");
	}

	/**
	 * Specifies whether if this part is needed on this weapon to attain max upgrades.
	 *
	 * @param part
	 *            the weapon part to test with
	 * @return true if it can be built on here, false otherwise.
	 */
	public boolean isBuildable(WeaponPart part)
	{
		int partIndex = Arrays.binarySearch(parts, part);
		return partIndex >= 0 && !partsBuilt[partIndex];
	}

	/**
	 * Specifies if the weapon has all max upgrades (other than the energy upgrade);
	 * 
	 * @return true if it is completed, false otherwise.
	 */
	public boolean isComplete()
	{
		for (boolean partBuilt : partsBuilt)
		{
			if (!partBuilt)
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public synchronized void removeListener(InvalidationListener listener)
	{
		listeners.remove(listener);
	}
}
