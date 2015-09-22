package armsgame.card.impl;

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
public class Weapon implements Observable
{
	private final WeaponSpec spec;
	private boolean vampiric = false;
	private final WeaponPartSpec[] parts;
	private final boolean[] partsBuilt;
	private final ArrayList<InvalidationListener> listeners = new ArrayList<>();

	/**
	 * Constructs a Weapon class
	 *
	 * @param spec
	 *            the specifications of this weapon.
	 */
	public Weapon(WeaponSpec spec)
	{
		// Initialize the parts needed for this weapon.
		this.spec = spec;
		parts = spec.getPartSpecs();
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
	public boolean buildPart(WeaponPartSpec part)
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

	public void damage(Player attacker, Player victim)
	{
		double guarantee = Math.min(Arrays.stream(parts).mapToDouble(WeaponPartSpec::getAccuracy).sum(), 100);
		double efficiency = attacker.selectAccuracy(guarantee);
		for (int i = 0; i < parts.length; i++)
		{
			if (partsBuilt[i])
			{
				parts[i].damage(attacker, victim, this.isVampiric(), efficiency);
			}
		}
	}

	public Color getColorClass()
	{
		int colorRGB = spec.getRGBColor();
		return Color.rgb((colorRGB >> 16) & 0xFF, (colorRGB >> 8) & 0xFF, colorRGB & 0xFF);
	}

	/**
	 * This obtains the internal typing of this weapon spec
	 *
	 * @return the internal type.
	 */
	public String getInternalType()
	{
		return spec.getInternalType();
	}

	public WeaponSpec getSpec()
	{
		return spec;
	}

	/**
	 * Describes whether if this weapon hasn't max upgrades.
	 *
	 * @return true if column has loose properties, false otherwise.
	 */
	public boolean isIncomplete()
	{
		for (boolean partBuilt : partsBuilt)
		{
			if (!partBuilt)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Specifies whether if this part is needed on this weapon to attain max upgrades.
	 *
	 * @param part
	 *            the weapon part to test with
	 * @return true if it can be built on here, false otherwise.
	 */
	public boolean isBuildable(WeaponPartSpec part)
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

	public boolean isVampiric()
	{
		return vampiric;
	}

	@Override
	public synchronized void removeListener(InvalidationListener listener)
	{
		listeners.remove(listener);
	}

	void setVampiric(boolean vampiric)
	{
		this.vampiric = vampiric;
	}
}
