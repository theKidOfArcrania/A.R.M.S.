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
import armsgame.card.impl.Weapon;
import armsgame.card.impl.WeaponSpec;

import static java.util.Objects.requireNonNull;

/**
 * This is a collection of cash cards and property cards used as a payment.
 * <p>
 *
 * @author HW
 */
public class DamageReport
{

	private static int getValue(Card c)
	{
		if (!(c instanceof BurstCharge) && !(c instanceof PartCard))
		{
			throw new IllegalArgumentException("Must be a money card or a property card.");
		}

		if (c instanceof Valuable)
		{
			return ((Valuable) c).getValue();
		}
		return 0;
	}

	private static String joinList(ArrayList<String> list)
	{
		if (list.size() == 0)
		{
			return "";
		}
		if (list.size() == 1)
		{
			return list.get(0);
		}
		if (list.size() == 2)
		{
			return list.get(0) + " and " + list.get(1);
		}

		String last = list.remove(list.size() - 1);
		return list.stream().collect(Collectors.joining(", ")) + ", and " + last;
	}

	private static void transferProp(Player giver, Player reciever, PartCard prop)
	{
		Weapon columnGiver = giver.getWeapon(prop);
		Weapon columnReciever = reciever.getWeapon(columnGiver.getPropertyColor());

		if (!columnGiver.isMoveable())
		{
			throw new AssertionError();
		}

		columnGiver.remove(prop);
		columnReciever.addAndSort(prop);
	}

	private static void transferProp(Player giver, Player reciever, WeaponSpec propSet)
	{
		Weapon columnGiver = giver.getWeapon(propSet);
		Weapon columnReciever = reciever.getWeapon(propSet);

		if (!columnGiver.isFullSet())
		{
			throw new AssertionError();
		}

		columnReciever.addAllAndSort(columnGiver.removeFullSet());
	}

	private final ArrayList<Card> bills = new ArrayList<>(10);
	private final Player damager;

	private int hp;
	private final Player victim;
	private int paidAmount = 0;
	private final ArrayList<PartCard> partsRequested = new ArrayList<>(3);
	private final ArrayList<WeaponSpec> weaponsRequested = new ArrayList<>(3);
	private final ArrayList<PartCard> partsGiven = new ArrayList<>(3);
	private final ArrayList<WeaponSpec> weaponsGiven = new ArrayList<>(3);
	private boolean energyZapMode = false;
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
	public DamageReport(Player damager, Player victim)
	{
		requireNonNull(damager);
		requireNonNull(victim);
		this.damager = damager;
		this.victim = victim;
		this.hp = 0;
	}

	/**
	 * Constructs a payment object with a pre-initialized debt.
	 * <p>
	 *
	 * @param damager
	 *            the player who dealed the damage.
	 * @param victim
	 *            the player who is recieving this damage.
	 * @param hp
	 *            the amount of debt to pay
	 */
	public DamageReport(Player damager, Player victim, int hp, boolean energyZapMode)
	{
		requireNonNull(damager);
		requireNonNull(victim);
		if (hp <= 0)
		{
			throw new IllegalArgumentException("Debt must be positive");
		}
		this.damager = damager;
		this.victim = victim;
		this.hp = hp;
		this.energyZapMode = energyZapMode;
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
	public DamageReport(Player creditor, Player debtor, PartCard propRequested)
	{
		requireNonNull(creditor);
		requireNonNull(debtor);
		requireNonNull(propRequested);
		this.damager = creditor;
		this.victim = debtor;
		this.hp = 0;
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
	public DamageReport(Player creditor, Player debtor, WeaponSpec propSetRequested)
	{
		requireNonNull(creditor);
		requireNonNull(debtor);
		requireNonNull(partsRequested);
		this.damager = creditor;
		this.victim = debtor;
		this.hp = 0;
		this.weaponsRequested.add(propSetRequested);
	}

	public void finishPay()
	{
		// if (!metPayment()) {
		// throw new IllegalStateException("Debt has not been fully met");
		// }

		partsRequested.forEach(this::takePart0);
		weaponsRequested.forEach(this::takePart0);
		partsGiven.forEach(this::givePart0);
		weaponsGiven.forEach(this::givePart0);

		bills.forEach(card -> {
			// TO DO: check ref. and also shift ref.
			if (card instanceof BurstCharge)
			{
				BurstCharge nrg = (BurstCharge) card;

				victim.damageShield(nrg.getEnergyValue());
				if (energyZapMode)
				{
					damager.healShield(nrg.getEnergyValue());
				}
			} else
			{
				takePart0((PartCard) card);
			}
		});
	}

	/**
	 * This method finishes the payment request and hands it over to the victim/ ower.
	 */
	public void finishRequest()
	{

		if (victim == damager)
		{
			return;
		}

		// currently, we only support one type of response: Just say no.
		handleResponse(this.toString(), damager, victim);

		if (!canceled)
		{
			victim.selectResponse(this);
			this.finishPay();
		}
	}

	/**
	 * Adds a property to the give list
	 * <p>
	 *
	 * @param partCard
	 *            the property to add.
	 * @return whether if this property was added to request list.
	 */
	public boolean giveProperty(PartCard partCard)
	{
		// TO DO: check prop ref.
		if (partsGiven.contains(partCard))
		{
			return false;
		}
		partsGiven.add(partCard);
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
	public boolean givePropertySet(WeaponSpec set)
	{
		if (weaponsGiven.contains(set))
		{
			return false;
		}
		weaponsGiven.add(set);
		return true;
	}

	public boolean isEnergyZapMode()
	{
		return energyZapMode;
	}

	public boolean metPayment()
	{
		return hp <= paidAmount;
	}

	public boolean payBill(Card bill)
	{
		if (getValue(bill) <= 0 || hp <= paidAmount)
		{
			return false;
		}
		bills.add(bill);
		paidAmount += getValue(bill);
		return true;
	}

	public void payBills(Collection<? extends Card> c)
	{
		bills.ensureCapacity(bills.size() + c.size());
		c.forEach(card -> {
			// omit any repeated cards or cards that have no value or if the
			// debt is already met.
			if (hp > paidAmount && getValue(card) > 0 && !bills.contains(card) && !c.contains(card))
			{
				paidAmount += getValue(card);
				bills.add(card);
			}
		});
	}

	public boolean removePaidBill(Card bill)
	{
		bills.remove(bill);
		return true;
	}

	/**
	 * Adds a property to the request list
	 * <p>
	 *
	 * @param partCard
	 *            the property to add.
	 * @return whether if this property was added to request list.
	 */
	public boolean requestProperty(PartCard partCard)
	{
		// TO DO: check prop ref.
		if (partsRequested.contains(partCard))
		{
			return false;
		}
		partsRequested.add(partCard);
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
	public boolean requestPropertySet(WeaponSpec set)
	{
		if (weaponsRequested.contains(set))
		{
			return false;
		}
		weaponsRequested.add(set);
		return true;
	}

	public void setEnergyZapMode(boolean energyZapMode)
	{
		this.energyZapMode = energyZapMode;
	}

	public void setHpDamage(int debt)
	{
		this.hp = debt;
	}

	@Override
	public String toString()
	{
		ArrayList<String> requests = new ArrayList<>(10);
		ArrayList<String> gives = new ArrayList<>(10);

		if (!partsRequested.isEmpty())
		{
			partsRequested.stream().map(PartCard::getPropertyName).forEach(requests::add);
		}

		if (!weaponsRequested.isEmpty())
		{
			weaponsRequested.stream().map(WeaponSpec::toString).map(color -> color + " set").forEach(requests::add);
		}

		if (hp > 0)
		{
			requests.add(BurstCharge.moneyString(hp, true));
		}

		if (requests.isEmpty())
		{
			requests.add("nothing");
		}

		if (!partsGiven.isEmpty())
		{
			partsGiven.stream().map(PartCard::getPropertyName).forEach(gives::add);
		}

		if (!weaponsGiven.isEmpty())
		{
			weaponsGiven.stream().map(WeaponSpec::toString).map(color -> color + " set").forEach(gives::add);
		}

		String requestsString = joinList(requests);
		String givesString = joinList(gives);
		StringBuilder description = new StringBuilder(
				25 + requestsString.length() + givesString.length()).append("Player ").append(damager.getName()).append(" wants ");
		description.append(requestsString);

		if (!givesString.isEmpty())
		{
			description.append(" for ").append(givesString);
		}
		return description.append(".").toString();
	}

	private void givePart0(PartCard prop)
	{
		transferProp(damager, victim, prop);
	}

	private void givePart0(WeaponSpec propset)
	{
		transferProp(damager, victim, propset);
	}

	private void handleResponse(String responseString, Player requester, Player responder)
	{
		Response response = responder.selectResponse(responseString);
		if (response != null && response.getResponseType() != null)
		{
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

	private void takePart0(PartCard prop)
	{
		transferProp(victim, damager, prop);
	}

	private void takePart0(WeaponSpec propset)
	{
		transferProp(victim, damager, propset);
	}

}
