/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import armsgame.card.BurstCharge;
import armsgame.card.Card;
import armsgame.card.PartCard;
import armsgame.card.Response;
import armsgame.card.Valuable;
import armsgame.weapon.Weapon;
import armsgame.weapon.WeaponPartSpec;
import armsgame.weapon.WeaponSpec;

import static java.util.Objects.requireNonNull;

/**
 * This is a collection of cash cards and property cards used as a payment.
 * <p>
 *
 * @author HW
 */
public class WeaponTransfer {

	private static int getValue(Card c) {
		if (!(c instanceof BurstCharge) && !(c instanceof PartCard)) {
			throw new IllegalArgumentException("Must be a money card or a property card.");
		}

		if (c instanceof Valuable) {
			return ((Valuable) c).getValue();
		}
		return 0;
	}

	private static String joinList(ArrayList<String> list) {
		if (list.size() == 0) {
			return "";
		}
		if (list.size() == 1) {
			return list.get(0);
		}
		if (list.size() == 2) {
			return list.get(0) + " and " + list.get(1);
		}

		String last = list.remove(list.size() - 1);
		return list.stream().collect(Collectors.joining(", ")) + ", and " + last;
	}

	private static void transferPart(Player giver, Player reciever, WeaponPartSpec partSpec) {
		Weapon specTake = giver.getWeapon(partSpec);
		specTake.unbuildPart(partSpec);

		if (reciever != null) {
			Weapon specRecieve = reciever.getWeapon(specTake.getSpec());
			specRecieve.buildPart(partSpec);
		}
	}

	private static void transferPart(Player giver, Player reciever, WeaponSpec propSet)
	{
		Weapon specTake = giver.getWeapon(propSet);

		if (reciever == null) {

		}else {
			Weapon specRecieve = reciever.getWeapon(propSet);
			for (int i = 0; i < specTake.buildPart(part))
		}


	}

	private final ArrayList<Card> bills = new ArrayList<>(10);
	private final Player damager;

	private int hp;
	private final Player victim;
	private final ArrayList<WeaponPartSpec> partsRequested = new ArrayList<>(3);
	private final ArrayList<WeaponSpec> weaponsRequested = new ArrayList<>(3);
	private final ArrayList<WeaponPartSpec> partsGiven = new ArrayList<>(3);
	private final ArrayList<WeaponSpec> weaponsGiven = new ArrayList<>(3);
	private boolean energyZapMode = false;
	private final boolean canceled = false;

	/**
	 * Constructs a payment object without any payment parameters.
	 * <p>
	 *
	 * @param damager the player who dealed the damage.
	 * @param victim the player who is recieving this damage.
	 */
	public WeaponTransfer(Player damager, Player victim) {
		requireNonNull(damager);
		requireNonNull(victim);
		this.damager = damager;
		this.victim = victim;
		this.hp = 0;
	}

	/**
	 * Constructs a payment object with a pre-initialized property to request.
	 * <p>
	 *
	 * @param receiver the receiver of this weapon transfer.
	 * @param giver the person transferring weapon part
	 * @param partRequested the requested weapon part
	 */
	public WeaponTransfer(Player receiver, Player giver, WeaponPartSpec partRequested) {
		requireNonNull(receiver);
		requireNonNull(giver);
		requireNonNull(partRequested);
		this.damager = receiver;
		this.victim = giver;
		this.hp = 0;
		this.partsRequested.add(partRequested);
	}

	/**
	 * Constructs a payment object with a pre-initialized property to request.
	 * <p>
	 *
	 * @param receiver the receiver of this weapon transfer.
	 * @param giver the person transferring weapon part
	 * @param partRequested the requested weapon set
	 */
	public WeaponTransfer(Player receiver, Player giver, WeaponSpec partRequested) {
		requireNonNull(receiver);
		requireNonNull(giver);
		requireNonNull(partRequested);
		this.damager = receiver;
		this.victim = giver;
		this.hp = 0;
		this.weaponsRequested.add(partRequested);
	}

	public void finishPay() {
		// if (!metPayment()) {
		// throw new IllegalStateException("Debt has not been fully met");
		// }

		partsRequested.forEach(this::takePart0);
		weaponsRequested.forEach(this::takePart0);
		partsGiven.forEach(this::givePart0);
		weaponsGiven.forEach(this::givePart0);
	}

	/**
	 * This method finishes the payment request and hands it over to the victim/ ower.
	 */
	public void finishRequest() {

		if (victim == damager) {
			return;
		}

		// TO DO: handle responses from weapon transfer

		if (!canceled) {
			victim.selectResponse(this);
			this.finishPay();
		}
	}

	/**
	 * Adds a property to the give list
	 * <p>
	 *
	 * @param part the property to add.
	 * @return whether if this property was added to request list.
	 */
	public boolean giveProperty(WeaponPartSpec part) {
		// TO DO: check prop ref.
		if (partsGiven.contains(part)) {
			return false;
		}
		partsGiven.add(part);
		return true;
	}

	/**
	 * Adds a property set to the give list
	 * <p>
	 *
	 * @param set the property set to add.
	 * @return whether if this property was added to request list.
	 */
	public boolean givePropertySet(WeaponSpec set) {
		if (weaponsGiven.contains(set)) {
			return false;
		}
		weaponsGiven.add(set);
		return true;
	}

	/**
	 * Adds a property to the request list
	 * <p>
	 *
	 * @param part the property to add.
	 * @return whether if this property was added to request list.
	 */
	public boolean requestProperty(WeaponPartSpec part) {
		// TO DO: check prop ref.
		if (partsRequested.contains(part)) {
			return false;
		}
		partsRequested.add(part);
		return true;
	}

	/**
	 * Adds a property set to the request list
	 * <p>
	 *
	 * @param set the property set to add.
	 * @return whether if this property was added to request list.
	 */
	public boolean requestPropertySet(WeaponSpec set) {
		if (weaponsRequested.contains(set)) {
			return false;
		}
		weaponsRequested.add(set);
		return true;
	}

	public void setEnergyZapMode(boolean energyZapMode) {
		this.energyZapMode = energyZapMode;
	}

	public void setHpDamage(int debt) {
		this.hp = debt;
	}

	@Override
	public String toString() {
		ArrayList<String> requests = new ArrayList<>(10);
		ArrayList<String> gives = new ArrayList<>(10);

		if (!partsRequested.isEmpty()) {
			partsRequested.stream().map(WeaponPartSpec::getName).forEach(requests::add);
		}

		if (!weaponsRequested.isEmpty()) {
			weaponsRequested.stream().map(WeaponSpec::toString).map(color -> color + " set").forEach(requests::add);
		}

		if (hp > 0) {
			requests.add(BurstCharge.moneyString(hp, true));
		}

		if (requests.isEmpty()) {
			requests.add("nothing");
		}

		if (!partsGiven.isEmpty()) {
			partsGiven.stream().map(WeaponPartSpec::getName).forEach(gives::add);
		}

		if (!weaponsGiven.isEmpty()) {
			weaponsGiven.stream().map(WeaponSpec::toString).map(color -> color + " set").forEach(gives::add);
		}

		String requestsString = joinList(requests);
		String givesString = joinList(gives);
		StringBuilder description = new StringBuilder(
				25 + requestsString.length() + givesString.length()).append("Player ").append(damager.getName()).append(" wants ");
		description.append(requestsString);

		if (!givesString.isEmpty()) {
			description.append(" for ").append(givesString);
		}
		return description.append(".").toString();
	}

	private void givePart0(WeaponPartSpec prop) {
		transferPart(damager, victim, prop);
	}

	private void givePart0(WeaponSpec propset) {
		transferPart(damager, victim, propset);
	}

	private void takePart0(WeaponPartSpec prop) {
		transferPart(victim, damager, prop);
	}

	private void takePart0(WeaponSpec propset) {
		transferPart(victim, damager, propset);
	}

}
