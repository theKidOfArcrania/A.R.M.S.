/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.impl;

import java.util.ArrayList;
import java.util.stream.Collectors;

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

	public class Transfer {
		private final boolean positiveDirection;
		private final Weapon weaponTake;
		private final WeaponPartSpec partTake;

		public Transfer(boolean positiveDirection, Weapon weaponTake, WeaponPartSpec partTake) {
			this.positiveDirection = positiveDirection;
			this.weaponTake = weaponTake;
			this.partTake = partTake;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof Transfer)) {
				return false;
			}
			Transfer other = (Transfer) obj;
			if (!getOuterType().equals(other.getOuterType())) {
				return false;
			}
			if (partTake == null) {
				if (other.partTake != null) {
					return false;
				}
			} else if (!partTake.equals(other.partTake)) {
				return false;
			}
			if (positiveDirection != other.positiveDirection) {
				return false;
			}
			if (weaponTake == null) {
				if (other.weaponTake != null) {
					return false;
				}
			} else if (!weaponTake.equals(other.weaponTake)) {
				return false;
			}
			return true;
		}

		/**
		 * Executes the transfer w/o prompting the player for responses.
		 */
		public void execute() {
			if (partTake == null) {
				WeaponPartSpec[] parts = weaponTake.getSpec().getPartSpecs();
				for (WeaponPartSpec part : parts) {
					// TO DO: Acquire part.
				}
			} else {
				// TO DO: Acquire Part.
			}
		}

		/**
		 * Retrieves the part to take or null if the entire set is to be taken.
		 *
		 * @return the part to take.
		 */
		public WeaponPartSpec getPartTake() {
			return partTake;
		}

		/**
		 * Retrieves the weapon that the part(s) is (are) to be taken
		 *
		 * @return the weapon to take parts from.
		 */
		public Weapon getWeaponTake() {
			return weaponTake;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((partTake == null) ? 0 : partTake.hashCode());
			result = prime * result + (positiveDirection ? 1231 : 1237);
			result = prime * result + ((weaponTake == null) ? 0 : weaponTake.hashCode());
			return result;
		}

		/**
		 * Determines whether if this transfer is in the positive direction (i.e. weapon transfer is from principal giver to the principal reciever.
		 *
		 * @return whether if this transfer is positive or negative.
		 */
		public boolean isPositiveDirection() {
			return positiveDirection;
		}

		private WeaponTransfer getOuterType() {
			return WeaponTransfer.this;
		}

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

	private final Player reciever;
	private final Player giver;
	private final ArrayList<Transfer> transfers = new ArrayList<>();

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
		this.reciever = damager;
		this.giver = victim;
	}

	/**
	 * Constructs a payment object with a pre-initialized property to request.
	 * <p>
	 *
	 * @param receiver the receiver of this weapon transfer.
	 * @param giver the person transferring weapon set
	 * @param specRequested the requested weapon set
	 */
	public WeaponTransfer(Player receiver, Player giver, WeaponSpec specRequested) {
		requireNonNull(receiver);
		requireNonNull(giver);
		requireNonNull(specRequested);
		this.reciever = receiver;
		this.giver = giver;
		request(specRequested);
	}

	/**
	 * Constructs a payment object with a pre-initialized property to request.
	 * <p>
	 *
	 * @param receiver the receiver of this weapon transfer.
	 * @param giver the person transferring weapon part
	 * @param specRequested the spec where this weapon part comes from
	 * @param partRequested the requested weapon part
	 */
	public WeaponTransfer(Player receiver, Player giver, WeaponSpec specRequested, WeaponPartSpec partRequested) {
		requireNonNull(receiver);
		requireNonNull(giver);
		requireNonNull(partRequested);
		this.reciever = receiver;
		this.giver = giver;
		request(specRequested, partRequested);
	}

	/**
	 * This method finishes the payment request and hands it over to the victim/ ower.
	 */
	public void finishRequest() {

		if (giver == reciever) {
			return;
		}

		// TO DO: handle responses from weapon transfer
		giver.selectResponse(this);
		transfers.forEach(Transfer::execute);
	}

	/**
	 * Adds a max-upgrade weapon set to the transfer list (giving).
	 * <p>
	 *
	 * @param spec the weapon set to give
	 * @return whether if this set was added onto the transfer list
	 */
	public boolean give(WeaponSpec spec) {
		Transfer transfer = new Transfer(false, reciever.getWeapon(spec), null);
		if (!transfers.contains(transfer)) {
			transfers.add(transfer);
			return true;
		}
		return false;
	}

	/**
	 * Adds a weapon part to the transfer list (giving).
	 * <p>
	 *
	 * @param spec the spec where this part comes from.
	 * @param part part to give.
	 * @return whether if this part was added onto the transfer list.
	 */
	public boolean give(WeaponSpec spec, WeaponPartSpec part) {
		Transfer transfer = new Transfer(false, reciever.getWeapon(spec), part);
		if (!transfers.contains(transfer)) {
			transfers.add(transfer);
			return true;
		}
		return false;
	}

	/**
	 * Adds a max-upgrade weapon set to the transfer list (taking).
	 * <p>
	 *
	 * @param spec the weapon set to take
	 * @return whether if this set was added onto the transfer list
	 */
	public boolean request(WeaponSpec spec) {
		Transfer transfer = new Transfer(true, giver.getWeapon(spec), null);
		if (!transfers.contains(transfer)) {
			transfers.add(transfer);
			return true;
		}
		return false;
	}

	/**
	 * Adds a weapon part to the transfer list (taking).
	 * <p>
	 *
	 * @param spec the spec where this part comes from.
	 * @param part part to take.
	 * @return whether if this part was added onto the transfer list.
	 */
	public boolean request(WeaponSpec spec, WeaponPartSpec part) {
		Transfer transfer = new Transfer(true, giver.getWeapon(spec), part);
		if (!transfers.contains(transfer)) {
			transfers.add(transfer);
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		// TO DO: request string.
		return super.toString();
	}

}
