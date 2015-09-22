/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.weapon;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

import armsgame.card.Card;
import armsgame.card.EnergyCrystal;
import armsgame.card.PartCard;
import armsgame.card.util.CardDefaults;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;

/**
 *
 * @author HW
 */
public class WeaponSet implements Iterable<PartCard>, Serializable, Observable {

	private static final long serialVersionUID = 5264125689357215996L;

	private final ObservableList<PartCard> parts = FXCollections.observableArrayList();

	private EnergyCrystal crystalBoost = new EnergyCrystal();
	private final CardDefaults defs;
	private final WeaponSpec weaponSpec;

	public WeaponSet(CardDefaults defs, WeaponSpec weaponSpec) {
		this.defs = defs;
		this.weaponSpec = weaponSpec;
	}

	public boolean add(EnergyCrystal crystal) {
		if (crystalBoost != null) {
			return false; // There already is a crystal attached to this.
		}
		this.crystalBoost = crystal;
		return true;
	}

	public boolean add(PartCard prop) {
		requireNonNull(prop);
		checkCard(prop);
		// TO DO: check ref.
		return parts.add(prop);
	}

	public void addAll(Collection<PartCard> prop) {
		prop.stream()
			.forEach(this::add);
	}

	public void addAll(WeaponSet prop) {
		prop.stream()
			.forEach(this::add);
	}

	public void addAllAndSort(Collection<PartCard> prop) {
		prop.stream()
			.forEach(this::add);
		sort();
	}

	public void addAllAndSort(WeaponSet prop) {
		prop.stream()
			.forEach(this::add);
		sort();
	}

	public boolean addAndSort(EnergyCrystal crystal) {
		if (crystalBoost != null) {
			return false; // There already is a crystal attached to this.
		}
		this.crystalBoost = crystal;
		sort();
		return true;
	}

	public void addAndSort(PartCard prop) {
		requireNonNull(prop);
		checkCard(prop);
		parts.add(prop);
		sort();
	}

	@Override
	public void addListener(InvalidationListener listener) {
		parts.addListener(listener);
	}

	public void addListener(ListChangeListener<? super PartCard> listener) {
		parts.addListener(listener);

	}

	public void clear() {
		parts.clear();
	}

	public boolean contains(PartCard o) {
		return parts.contains(o);
	}

	public Card get(int index) {
		return parts.get(index);
	}

	public int getAttackPoints() {
		int damage = CardDefaults.getCardDefaults();
		for (PartCard part : parts) {
			damage = part.modifyDamage(damage);
		}
		return damage;
	}

	public int getFullSet() {
		return defs.getPropertyFullSet(weaponSpec);
	}

	public WeaponSpec getPropertyColor() {
		return weaponSpec;
	}

	/**
	 * Calculates the sum of the property cards.
	 * <p>
	 *
	 * @return the number of property cards in this column.
	 */
	public int getPropertyCount() {
		return extractFullSet().size();
	}

	/**
	 * Describes whether if this column has loose properties.
	 *
	 * @return true if column has loose properties, false otherwise.
	 */
	public boolean hasIncompleteSet() {
		return getPropertyCount() != 0 && getPropertyCount() != defs.getPropertyFullSet(weaponSpec);
	}

	public int indexOf(Object o) {
		return parts.indexOf(o);
	}

	public boolean isEmpty() {
		return getPropertyCount() == 0;
	}

	public boolean isEnergetic() {
		return crystalBoost != null;
	}

	/**
	 * Describes whether if this column contains a full set.
	 * <p>
	 *
	 * @return true for full sets, false otherwise.
	 */
	public boolean isFullSet() {
		return getPropertyCount() >= defs.getPropertyFullSet(weaponSpec);
	}

	/**
	 * Describes whether if this column can downgrade from a full set.
	 * <p>
	 *
	 * @return true for full sets, false otherwise.
	 */
	public boolean isMoveable() {
		return getPropertyCount() >= defs.getPropertyFullSet(weaponSpec);
	}

	@Override
	public Iterator<PartCard> iterator() {
		return parts.iterator();
	}

	public Stream<PartCard> parallelStream() {
		return parts.parallelStream();
	}

	public boolean remove(EnergyCrystal crystal) {
		if (crystalBoost != crystal) {
			return false;
		}
		this.crystalBoost = null;
		return true;
	}

	public PartCard remove(int index) {
		return parts.remove(index);
	}

	public boolean remove(PartCard o) {
		return parts.remove(o);
	}

	public WeaponSet removeFullSet() {
		if (!isFullSet()) {
			throw new IllegalStateException("Not a full set.");
		}

		WeaponSet set = extractFullSet();
		parts.removeAll(set.parts);

		if (crystalBoost != null) {
			set.add(crystalBoost);
			crystalBoost = null;
		}

		set.sort();

		return set;
	}

	@Override
	public void removeListener(InvalidationListener listener) {
		parts.removeListener(listener);
	}

	public void removeListener(ListChangeListener<? super Card> listener) {
		parts.removeListener(listener);
	}

	public int size() {
		return parts.size();
	}

	public void sort() {
		parts.sort(comparing(Card::getCardName)); // sort by weapon name.
	}

	public Stream<PartCard> stream() {
		return parts.stream();
	}

	@Override
	public String toString() {
		return weaponSpec.getCodeName() + " set";
	}

	private void checkCard(PartCard card) {
		if (parts.contains(card)) {
			throw new IllegalArgumentException("PartCard already exists: " + card);
		}
		if (card.getSpec() != weaponSpec) {
			throw new IllegalArgumentException("Must be a property card that has the color " + weaponSpec);
		}

	}

	private WeaponSet extractFullSet() {
		WeaponSet set = new WeaponSet(defs, weaponSpec);
		for (int i = 0; i < this.size(); i++) {
			PartCard part = parts.get(i);
			if (!set.stream()
				.map(PartCard::getInternalType)
				.equals(part.getInternalType())) {
				set.add(part);
			}
		}
		return set;
	}

}
