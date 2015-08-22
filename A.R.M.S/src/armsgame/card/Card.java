package armsgame.card;

import java.awt.Image;
import java.io.IOException;
import java.io.Serializable;

import armsgame.impl.CardActionType;
import armsgame.impl.CardActionType.Likeness;
import armsgame.impl.Player;
import armsgame.impl.SupportedActions;

public interface Card extends Serializable {

	/**
	 * This method is called by the Board class whenever this card is played. Any special actions that would happen will be placed in here.
	 * <p>
	 *
	 * @param self
	 *            the player playing this card.
	 * @return true if this action is done, false if this action was canceled.
	 */
	boolean actionPlayed(Player self);

	int getCardId();

	/**
	 * Getter for the name of this card.
	 * <p>
	 *
	 * @return the name of this card
	 */
	String getCardName();

	String getDescription();

	/**
	 * Retrieves the card energy conversion value. This value is used if a player decides to turn it into energy.
	 * <p>
	 *
	 * @return the energy conversion value of this card, or 0 if it cannot be converted.
	 */
	int getEnergyValue();

	/**
	 * This retrieves the image of the card when face up. Default implementations will look to the carddefs.
	 * <p>
	 *
	 * @return the image of this card.
	 * @exception IOException
	 *                when the image cannot be read
	 */
	Image getImage() throws IOException;

	/**
	 * Gets the internal type of this card. For example, Parker Place (first blue property card) would have an internal type of "props.blue.1"
	 * <p>
	 *
	 * @return the internal type of this card.
	 */
	String getInternalType();

	/**
	 * This retrieves the supported actions that this card can do.
	 * <p>
	 *
	 * @return The supported actions in an array.
	 */
	default SupportedActions getSupportedTypes() {
		SupportedActions actions = new SupportedActions();
		actions.addAction(new CardActionType("Play " + getCardName(), "move.action"));
		actions.addAction(new CardActionType("Discard", "move.discard"));
		return actions;
	}

	boolean isEnabled(Player self, Likeness action);
}