/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

import java.util.ArrayList;

import armsgame.impl.CardAction;
import armsgame.impl.CenterPlay;
import armsgame.impl.Player;
import armsgame.impl.CardActionType.Likeness;

/**
 *
 * @author Henry
 */
public class Boost extends Action {
	/**
	 *
	 */
	private static final long serialVersionUID = -5163764459341688966L;

	@Override
	public boolean actionPlayed(Player self) {
		ArrayList<Boost> extraDoubles = new ArrayList<>(10);
		CardAction played;
		CenterPlay centerPlay = self.getGame()
				.getCenterPlay();
		int multiplier = 2;

		do {
			played = self
					.selectHand("Select a rent card to rent others with.",
							card -> ((!self.isLastTurn() && card instanceof Boost)
									|| (card instanceof Ammunition && card.isEnabled(self, Likeness.Action))),
							cardAction -> cardAction.getInternalType()
									.equals("move.action"));

			if (played == null) {
				return false;
			}
			if (played.getPlayed() instanceof Boost) {
				extraDoubles.add((Boost) played.getPlayed());
				multiplier *= 2;
			}
		} while (played.getPlayed() instanceof Boost);
		Ammunition ammunition = (Ammunition) played.getPlayed();

		int rent = self.columnStream()
				.parallel()
				.filter(ammunition::isValidRent)
				.mapToInt(WeaponSet::getRent)
				.max()
				.orElse(0);
		if (rent == 0) {
			return false;
		}

		if (payRequest(self, ammunition.isGlobal(), rent * multiplier, "rent")) {
			extraDoubles.forEach((doubling) -> {
				self.pushTurn(new CardAction(doubling, getSupportedTypes().getActionType(Likeness.Action)));
				centerPlay.discard(doubling);
			});
			centerPlay.discard(ammunition);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String getInternalType() {
		return "action.double";
	}

	@Override
	public boolean isEnabled(Player self, Likeness action) {
		if (action == Likeness.Action) {
			if (self.isLastTurn()) {
				return false;
			}

			return self.handStream()
					.parallel()
					.filter(card -> card instanceof Ammunition)
					.anyMatch(rent -> rent.isEnabled(self, Likeness.Action));
		}
		return true;
	}
}
