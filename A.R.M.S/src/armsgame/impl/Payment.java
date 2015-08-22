/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import armsgame.card.Card;
import armsgame.card.Energy;
import armsgame.card.Response;
import armsgame.card.Valuable;
import armsgame.card.WeaponPart;
import armsgame.card.WeaponSet;
import armsgame.card.WeaponSpec;

import static java.util.Objects.requireNonNull;

/**
 * This is a collection of cash cards and property cards used as a payment.
 * <p>
 *
 * @author HW
 */
public class Payment {

	private static int getValue(Card c) {
		if (!(c instanceof Energy) && !(c instanceof WeaponPart)) {
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
		return list.stream()
				.collect(Collectors.joining(", ")) + ", and " + last;
	}

	private static void transferProp(Player giver, Player reciever, WeaponPart prop) {
		WeaponSet columnGiver = giver.getPropertyColumn(prop);
		WeaponSet columnReciever = reciever.getPropertyColumn(columnGiver.getPropertyColor());

		if (!columnGiver.isMoveable()) {
			throw new AssertionError();
		}

		columnGiver.remove(prop);
		columnReciever.addAndSort(prop);
	}

	private static void transferProp(Player giver, Player reciever, WeaponSpec propSet) {
		WeaponSet columnGiver = giver.getPropertyColumn(propSet);
		WeaponSet columnReciever = reciever.getPropertyColumn(propSet);

		if (!columnGiver.isFullSet()) {
			throw new AssertionError();
		}

		columnReciever.addAllAndSort(columnGiver.removeFullSet());
	}

	private final ArrayList<Card> bills = new ArrayList<>(10);
	private final Player damager;

	private int debt;
	private final Player victim;
	private int paidAmount = 0;
	private final ArrayList<WeaponPart> partsRequested = new ArrayList<>(3);

	private final ArrayList<WeaponSpec> weaponsRequested = new ArrayList<>(3);

	private final ArrayList<WeaponPart> partsGiven = new ArrayList<>(3);

	private final ArrayList<WeaponSpec> weaponsGiven = new ArrayList<>(3);

	private boolean canceled = false;

	/**
	 * Constructs a payment object without any payment parameters.
	 * <p>
	 *
	 * @param damager
	 *            the player who dealed the damage.
	 * @param victim
	 *            the player who is recieving this damage.
	 */
	public Payment(Player damager, Player victim) {
		requireNonNull(damager);
		requireNonNull(victim);
		this.damager = damager;
		this.victim = victim;
		this.debt = 0;
	}

	/**
	 * Constructs a payment object with a pre-initialized debt.
	 * <p>
	 *
	 * @param damager
	 *            the player who dealed the damage.
	 * @param victim
	 *            the player who is recieving this damage.
	 * @param debt
	 *            the amount of debt to pay
	 */
	public Payment(Player creditor, Player debtor, int debt) {
		requireNonNull(creditor);
		requireNonNull(debtor);
		if (debt <= 0) {
			throw new IllegalArgumentException("Debt must be positive");
		}
		this.damager = creditor;
		this.victim = debtor;
		this.debt = debt;
	}

	/**
	 * Constructs a payment object with a pre-initialized property to request.
	 * <p>
	 *
	 * @param damager
	 *            the reciever of this payment.
	 * @param victim
	 *            the victim of this payment.
	 * @param partsRequested
	 *            the requested property
	 */
	public Payment(Player creditor, Player debtor, WeaponPart propRequested) {
		requireNonNull(creditor);
		requireNonNull(debtor);
		requireNonNull(propRequested);
		this.damager = creditor;
		this.victim = debtor;
		this.debt = 0;
		this.partsRequested.add(propRequested);
	}

	/**
	 * Constructs a payment object with a pre-initialized property set to request.
	 * <p>
	 *
	 * @param damager
	 *            the reciever of this payment.
	 * @param victim
	 *            the victim of this payment.
	 * @param propSetRequested
	 *            the requested property set
	 */
	public Payment(Player creditor, Player debtor, WeaponSpec propSetRequested) {
		requireNonNull(creditor);
		requireNonNull(debtor);
		requireNonNull(partsRequested);
		this.damager = creditor;
		this.victim = debtor;
		this.debt = 0;
		this.weaponsRequested.add(propSetRequested);
	}

	public void finishPay() {
		// if (!metPayment()) {
		// throw new IllegalStateException("Debt has not been fully met");
		// }

		partsRequested.forEach(this::takePart0);
		weaponsRequested.forEach(this::takePart0);
		partsGiven.forEach(this::givePart0);
		weaponsGiven.forEach(this::givePart0);

		bills.forEach(card -> {
			// TO DO: check ref. and also shift ref.
			if (card instanceof Energy) {
				Energy nrg = (Energy) card;
				victim.removeBill(nrg);
				damager.addBill(nrg);
			} else {
				takePart0((WeaponPart) card);
			}
		});
	}

	/**
	 * This method finishes the payment request and hands it over to the victim/ ower.
	 */
	public void finishRequest() {

		if (victim == damager) {
			return;
		}

		// currently, we only support one type of response: Just say no.
		handleResponse(this.toString(), damager, victim);

		if (!canceled) {
			victim.selectPayment(this);
			this.finishPay();
		}
	}

	/**
	 * Adds a property to the give list
	 * <p>
	 *
	 * @param weaponPart
	 *            the property to add.
	 * @return whether if this property was added to request list.
	 */
	public boolean giveProperty(WeaponPart weaponPart) {
		// TO DO: check prop ref.
		if (partsGiven.contains(weaponPart)) {
			return false;
		}
		partsGiven.add(weaponPart);
		return true;
	}

	/**
	 * Adds a property set to the give list
	 * <p>
	 *
	 * @param set
	 *            the property set to add.
	 * @return whether if this property was added to request list.
	 */
	public boolean givePropertySet(WeaponSpec set) {
		if (weaponsGiven.contains(set)) {
			return false;
		}
		weaponsGiven.add(set);
		return true;
	}

	public boolean metPayment() {
		return debt <= paidAmount;
	}

	public boolean payBill(Card bill) {
		if (getValue(bill) <= 0 || debt <= paidAmount) {
			return false;
		}
		bills.add(bill);
		paidAmount += getValue(bill);
		return true;
	}

	public void payBills(Collection<? extends Card> c) {
		bills.ensureCapacity(bills.size() + c.size());
		c.forEach(card -> {
			// omit any repeated cards or cards that have no value or if the
			// debt is already met.
			if (debt > paidAmount && getValue(card) > 0 && !bills.contains(card) && !c.contains(card)) {
				paidAmount += getValue(card);
				bills.add(card);
			}
		});
	}

	public boolean removePaidBill(Card bill) {
		bills.remove(bill);
		return true;
	}

	/**
	 * Adds a property to the request list
	 * <p>
	 *
	 * @param weaponPart
	 *            the property to add.
	 * @return whether if this property was added to request list.
	 */
	public boolean requestProperty(WeaponPart weaponPart) {
		// TO DO: check prop ref.
		if (partsRequested.contains(weaponPart)) {
			return false;
		}
		partsRequested.add(weaponPart);
		return true;
	}

	/**
	 * Adds a property set to the request list
	 * <p>
	 *
	 * @param set
	 *            the property set to add.
	 * @return whether if this property was added to request list.
	 */
	public boolean requestPropertySet(WeaponSpec set) {
		if (weaponsRequested.contains(set)) {
			return false;
		}
		weaponsRequested.add(set);
		return true;
	}

	public void setDebt(int debt) {
		this.debt = debt;
	}

	@Override
	public String toString() {
		ArrayList<String> requests = new ArrayList<>(10);
		ArrayList<String> gives = new ArrayList<>(10);

		if (!partsRequested.isEmpty()) {
			partsRequested.stream()
					.map(WeaponPart::getPropertyName)
					.forEach(requests::add);
		}

		if (!weaponsRequested.isEmpty()) {
			weaponsRequested.stream()
					.map(WeaponSpec::toString)
					.map(color -> color + " set")
					.forEach(requests::add);
		}

		if (debt > 0) {
			requests.add(Energy.moneyString(debt, true));
		}

		if (requests.isEmpty()) {
			requests.add("nothing");
		}

		if (!partsGiven.isEmpty()) {
			partsGiven.stream()
					.map(WeaponPart::getPropertyName)
					.forEach(gives::add);
		}

		if (!weaponsGiven.isEmpty()) {
			weaponsGiven.stream()
					.map(WeaponSpec::toString)
					.map(color -> color + " set")
					.forEach(gives::add);
		}

		String requestsString = joinList(requests);
		String givesString = joinList(gives);
		StringBuilder description = new StringBuilder(25 + requestsString.length() + givesString.length()).append("Player ")
				.append(damager.getName())
				.append(" wants ");
		description.append(requestsString);

		if (!givesString.isEmpty()) {
			description.append(" for ")
					.append(givesString);
		}
		return description.append(".")
				.toString();
	}

	private void givePart0(WeaponPart prop) {
		transferProp(damager, victim, prop);
	}

	private void givePart0(WeaponSpec propset) {
		transferProp(damager, victim, propset);
	}

	private void handleResponse(String responseString, Player requester, Player responder) {
		Response response = responder.selectResponse(responseString);
		if (response != null && response.getResponseType() != null) {
			switch (response.getResponseType()) {
			case SHIELD:
				canceled = !canceled;
				handleResponse("Player " + responder.getName() + " has Just-Say-No-ed your request.", responder, requester);
				break;
			default:
				// not supported.
				throw new InternalError();
			}
		}
	}

	private void takePart0(WeaponPart prop) {
		transferProp(victim, damager, prop);
	}

	private void takePart0(WeaponSpec propset) {
		transferProp(victim, damager, propset);
	}

}
