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
import armsgame.card.WeaponPart;
import armsgame.card.WeaponSpec;
import armsgame.card.WeaponSet;
import armsgame.card.Response;
import armsgame.card.Valuable;

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
	private final Player creditor;

	private int debt;
	private final Player debtor;
	private int paidAmount = 0;
	private final ArrayList<WeaponPart> propRequested = new ArrayList<>(3);

	private final ArrayList<WeaponSpec> propSetsRequested = new ArrayList<>(3);

	private final ArrayList<WeaponPart> propGiven = new ArrayList<>(3);

	private final ArrayList<WeaponSpec> propSetsGiven = new ArrayList<>(3);

	private boolean canceled = false;

	/**
	 * Constructs a payment object without any payment parameters.
	 * <p>
	 *
	 * @param creditor
	 *            the reciever of this payment.
	 * @param debtor
	 *            the debtor of this payment.
	 */
	public Payment(Player creditor, Player debtor) {
		requireNonNull(creditor);
		requireNonNull(debtor);
		this.creditor = creditor;
		this.debtor = debtor;
		this.debt = 0;
	}

	/**
	 * Constructs a payment object with a pre-initialized debt.
	 * <p>
	 *
	 * @param creditor
	 *            the reciever of this payment.
	 * @param debtor
	 *            the debtor of this payment.
	 * @param debt
	 *            the amount of debt to pay
	 */
	public Payment(Player creditor, Player debtor, int debt) {
		requireNonNull(creditor);
		requireNonNull(debtor);
		if (debt <= 0) {
			throw new IllegalArgumentException("Debt must be positive");
		}
		this.creditor = creditor;
		this.debtor = debtor;
		this.debt = debt;
	}

	/**
	 * Constructs a payment object with a pre-initialized property to request.
	 * <p>
	 *
	 * @param creditor
	 *            the reciever of this payment.
	 * @param debtor
	 *            the debtor of this payment.
	 * @param propRequested
	 *            the requested property
	 */
	public Payment(Player creditor, Player debtor, WeaponPart propRequested) {
		requireNonNull(creditor);
		requireNonNull(debtor);
		requireNonNull(propRequested);
		this.creditor = creditor;
		this.debtor = debtor;
		this.debt = 0;
		this.propRequested.add(propRequested);
	}

	/**
	 * Constructs a payment object with a pre-initialized property set to request.
	 * <p>
	 *
	 * @param creditor
	 *            the reciever of this payment.
	 * @param debtor
	 *            the debtor of this payment.
	 * @param propSetRequested
	 *            the requested property set
	 */
	public Payment(Player creditor, Player debtor, WeaponSpec propSetRequested) {
		requireNonNull(creditor);
		requireNonNull(debtor);
		requireNonNull(propRequested);
		this.creditor = creditor;
		this.debtor = debtor;
		this.debt = 0;
		this.propSetsRequested.add(propSetRequested);
	}

	public void finishPay() {
		// if (!metPayment()) {
		// throw new IllegalStateException("Debt has not been fully met");
		// }

		propRequested.forEach(this::takeProp0);
		propSetsRequested.forEach(this::takeProp0);
		propGiven.forEach(this::giveProp0);
		propSetsGiven.forEach(this::giveProp0);

		bills.forEach(card -> {
			// TO DO: check ref. and also shift ref.
			if (card instanceof Energy) {
				Energy bill = (Energy) card;
				debtor.removeBill(bill);
				creditor.addBill(bill);
			} else {
				takeProp0((WeaponPart) card);
			}
		});
	}

	/**
	 * This method finishes the payment request and hands it over to the debtor/ ower.
	 */
	public void finishRequest() {

		if (debtor == creditor) {
			return;
		}

		// currently, we only support one type of response: Just say no.
		handleResponse(this.toString(), creditor, debtor);

		if (!canceled) {
			debtor.selectPayment(this);
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
		if (propGiven.contains(weaponPart)) {
			return false;
		}
		propGiven.add(weaponPart);
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
		if (propSetsGiven.contains(set)) {
			return false;
		}
		propSetsGiven.add(set);
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
		if (propRequested.contains(weaponPart)) {
			return false;
		}
		propRequested.add(weaponPart);
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
		if (propSetsRequested.contains(set)) {
			return false;
		}
		propSetsRequested.add(set);
		return true;
	}

	public void setDebt(int debt) {
		this.debt = debt;
	}

	@Override
	public String toString() {
		ArrayList<String> requests = new ArrayList<>(10);
		ArrayList<String> gives = new ArrayList<>(10);

		if (!propRequested.isEmpty()) {
			propRequested.stream()
					.map(WeaponPart::getPropertyName)
					.forEach(requests::add);
		}

		if (!propSetsRequested.isEmpty()) {
			propSetsRequested.stream()
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

		if (!propGiven.isEmpty()) {
			propGiven.stream()
					.map(WeaponPart::getPropertyName)
					.forEach(gives::add);
		}

		if (!propSetsGiven.isEmpty()) {
			propSetsGiven.stream()
					.map(WeaponSpec::toString)
					.map(color -> color + " set")
					.forEach(gives::add);
		}

		String requestsString = joinList(requests);
		String givesString = joinList(gives);
		StringBuilder description = new StringBuilder(25 + requestsString.length() + givesString.length()).append("Player ")
				.append(creditor.getName())
				.append(" wants ");
		description.append(requestsString);

		if (!givesString.isEmpty()) {
			description.append(" for ")
					.append(givesString);
		}
		return description.append(".")
				.toString();
	}

	private void giveProp0(WeaponPart prop) {
		transferProp(creditor, debtor, prop);
	}

	private void giveProp0(WeaponSpec propset) {
		transferProp(creditor, debtor, propset);
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

	private void takeProp0(WeaponPart prop) {
		transferProp(debtor, creditor, prop);
	}

	private void takeProp0(WeaponSpec propset) {
		transferProp(debtor, creditor, propset);
	}

}
