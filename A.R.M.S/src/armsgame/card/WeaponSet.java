/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
public class WeaponSet implements Iterable<WeaponPart>, Serializable, Observable {

	private static final long serialVersionUID = 5264125689357215996L;

	private static int getValue(Card card) {
		if (card instanceof WeaponPart) {
			return ((WeaponPart) card).getValue();
		}
		return card.getEnergyValue();
	}

	private final ObservableList<Card> properties = FXCollections.observableArrayList();

	private final CardDefaults defs;
	private final WeaponSpec weaponSpec;

	public WeaponSet(CardDefaults defs, WeaponSpec weaponSpec) {
		this.defs = defs;
		this.weaponSpec = weaponSpec;
	}

	public boolean add(Card prop) {
		requireNonNull(prop);
		checkCard(prop);
		// TO DO: check ref.
		return properties.add(prop);
	}

	public void addAll(Collection<Card> prop) {
		prop.stream()
				.forEach(this::add);
	}

	public void addAll(WeaponSet prop) {
		prop.stream()
				.forEach(this::add);
	}

	public void addAllAndSort(Collection<Card> prop) {
		prop.stream()
				.forEach(this::add);
		sort();
	}

	public void addAllAndSort(WeaponSet prop) {
		prop.stream()
				.forEach(this::add);
		sort();
	}

	public void addAndSort(Card prop) {
		requireNonNull(prop);
		checkCard(prop);
		properties.add(prop);
		sort();
	}

	@Override
	public void addListener(InvalidationListener listener) {
		properties.addListener(listener);
	}

	public void addListener(ListChangeListener<? super Card> listener) {
		properties.addListener(listener);
	}

	public void clear() {
		properties.clear();
	}

	public boolean contains(Card o) {
		return properties.contains(o);
	}

	/**
	 * This removes any houses and hotels, cashing them into money.
	 *
	 * @return an array of cash that was the former houses/ hotels.
	 */
	public BurstCharge[] downgrade() {
		if (isMoveable()) {
			return new BurstCharge[0];
		}
		Action[] houses = properties.parallelStream()
				.filter(card -> card instanceof Action)
				.toArray(Action[]::new);
		properties.removeAll(Arrays.asList(houses));
		return Arrays.stream(houses)
				.parallel()
				.map(Action::convertToCash)
				.toArray(BurstCharge[]::new);
	}

	public Card get(int index) {
		return properties.get(index);
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
		return properties.parallelStream()
				.mapToInt((card) -> (card instanceof WeaponPart) ? 1 : 0)
				.sum();
	}

	public int getRent() {
		if (isRentable()) {
			int base = defs.getRent(weaponSpec, getPropertyCount());
			int additional = properties.parallelStream()
					.filter(card -> card instanceof Upgrade)
					.mapToInt(card -> ((Upgrade) card).getRaiseValue())
					.sum();
			return base + additional;
		}
		return 0;
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
		return properties.indexOf(o);
	}

	public boolean isEmpty() {
		return getPropertyCount() == 0;
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

	/**
	 * This describes whether if it contains any stand-alone property cards.
	 *
	 * @return true if it is rentable, false otherwise.
	 */
	public boolean isRentable() {
		return properties.parallelStream()
				.filter((card) -> (card instanceof WeaponPart))
				.anyMatch(card -> ((WeaponPart) card).canStandAlone());
	}

	@Override
	public Iterator<WeaponPart> iterator() {
		return properties.iterator();
	}

	public Stream<Card> parallelStream() {
		return properties.parallelStream();
	}

	public boolean remove(Card o) {
		return properties.remove(o);
	}

	public Card remove(int index) {
		return properties.remove(index);
	}

	public WeaponSet removeFullSet() {
		if (!isFullSet()) {
			throw new IllegalStateException("Not a full set.");
		}

		sort(); // make sure props come first.
		WeaponSet set = new WeaponSet(defs, weaponSpec);
		int fullSet = defs.getPropertyFullSet(weaponSpec);
		for (int i = 0; i < fullSet; i++) {
			Card removed = properties.remove(0);
			if (!(removed instanceof WeaponPart)) {
				throw new AssertionError();
			}
			set.add(removed);
		}

		ArrayList<Card> misc = properties.parallelStream()
				.filter(card -> !(card instanceof WeaponPart))
				.collect(Collectors.toCollection(ArrayList::new));
		set.addAll(misc);
		properties.removeAll(misc);
		set.sort();

		return set;
	}

	@Override
	public void removeListener(InvalidationListener listener) {
		properties.removeListener(listener);
	}

	public void removeListener(ListChangeListener<? super Card> listener) {
		properties.removeListener(listener);
	}

	public int size() {
		return properties.size();
	}

	public void sort() {
		Comparator<Card> comp = comparing((card) -> !(card instanceof WeaponPart));// property cards first.
		comp = comp.thenComparing(WeaponSet::getValue) // order by lowest value to highest
				.thenComparing(Card::getCardName) // order by alphabetical order (based on card name).
				.thenComparing(Card::getCardId); // order finally, by card id.
		properties.sort(comp);
	}

	public Stream<Card> stream() {
		return properties.stream();
	}

	@Override
	public String toString() {
		return weaponSpec.getClassName() + " set";
	}

	private void checkCard(Card card) {
		if (properties.contains(card)) {
			throw new IllegalArgumentException("WeaponPart already exists: " + card);
		}
		if (card instanceof Action) {
			if (!(card instanceof Upgrade)) {
				throw new IllegalArgumentException("Must be a property card or a house/hotel card");
			}
			return;
		}

		if (!((WeaponPart) card).getDualColors()
				.compatibleWith(weaponSpec)) {
			throw new IllegalArgumentException("Must be a property card that has the color " + weaponSpec);
		}

	}

}
