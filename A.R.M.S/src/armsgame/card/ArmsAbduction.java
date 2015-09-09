/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

<<<<<<< Upstream, based on origin/blueprint2
import armsgame.impl.DamageReport;
import armsgame.impl.Player;
import armsgame.impl.CardActionType.Likeness;

/**
 *
 * @author Henry
 */
public class ArmsAbduction extends Action {
	/**
	 *
	 */
	private static final long serialVersionUID = 4190593954097839138L;

	@Override
	public boolean actionPlayed(Player self) {
		Player target = self.selectPlayer("Please select another player to take a property set from.", Player::hasIncompleteSet);

		if (target == null) {
			return false;
		}

		WeaponSet takeSet = self.selectPropertyColumn("Please select a property set to take.", WeaponSet::isFullSet, target);

		if (takeSet == null) {
			return false;
		}

		DamageReport dealBreaker = new DamageReport(self, target);
=======
import armsgame.impl.Payment;
import armsgame.impl.Player;
import armsgame.impl.CardActionType.Likeness;

/**
 *
 * @author Henry
 */
public class SMultiTargetRaid extends Action {
	/**
	 *
	 */
	private static final long serialVersionUID = 4190593954097839138L;

	@Override
	public boolean actionPlayed(Player self) {
		Player target = self.selectPlayer("Please select another player to take a property set from.", Player::hasIncompleteSet);

		if (target == null) {
			return false;
		}

		WeaponSet takeSet = self.selectPropertyColumn("Please select a property set to take.", WeaponSet::isFullSet, target);

		if (takeSet == null) {
			return false;
		}

		Payment dealBreaker = new Payment(self, target);
>>>>>>> 798c29a Rename Dealbreaker stuff
		dealBreaker.requestPropertySet(takeSet.getPropertyColor());
		dealBreaker.finishRequest();
		return true;
	}

	@Override
	public String getInternalType() {
		return "action.breaker";
	}

	@Override
	public boolean isEnabled(Player self, Likeness action) {
		if (action == Likeness.Action) {
			return self.getGame()
					.playerStream()
					.parallel()
					.anyMatch(Player::hasCompleteSet);
		}
		return true;
	}
}
