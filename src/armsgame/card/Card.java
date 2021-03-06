package armsgame.card;

import java.awt.Image;
import java.io.IOException;

import armsgame.card.util.CardActionType;
import armsgame.card.util.CardActionType.Likeness;
import armsgame.card.util.CardDefaults;
import armsgame.card.util.SupportedActions;
import armsgame.impl.Player;

import static armsgame.card.util.CardDefaults.getCardDefaults;

public abstract class Card {

	private static final long serialVersionUID = -8288322516558088995L;

	public Card() {
		super();
	}

	/**
	 * This method is called by the Board class whenever this card is played. Any special actions that would happen will be placed in here.
	 * <p>
	 *
	 * @param self the player playing this card.
	 * @return true if this action is done, false if this action was canceled.
	 */
	public abstract boolean actionPlayed(Player self);

	/**
	 * Getter for the name of this card.
	 * <p>
	 *
	 * @return the name of this card
	 */
	public abstract String getCardName();

	@SuppressWarnings("static-method")
	public CardDefaults getDefaults() {
		return getCardDefaults();
	}

	public String getDescription() {
		return getInternalProperty("description");
	}

	/**
	 * Retrieves the card energy conversion value. This value is used if a player decides to turn it into energy.
	 * <p>
	 *
	 * @return the energy conversion value of this card, or 0 if it cannot be converted.
	 */
	public int getEnergyValue() {
		return getInternalIntProperty("energyValue");
	}

	/**
	 * This retrieves the image of the card when face up. Default implementations will look to the carddefs.
	 * <p>
	 *
	 * @return the image of this card.
	 * @exception IOException when the image cannot be read
	 */
	public Image getImage() throws IOException {
		return getCardDefaults().getImageProperty(getInternalType() + ".image");
	}

	/**
	 * Gets the internal type of this card, often making a descriptive, programmer-friendly name.
	 * <p>
	 *
	 * @return the internal type of this card.
	 */
	public abstract String getInternalType();

	/**
	 * This retrieves the supported actions that this card can do.
	 * <p>
	 *
	 * @return The supported actions in an array.
	 */
	public SupportedActions getSupportedTypes() {
		SupportedActions actions = new SupportedActions();
		actions.addAction(new CardActionType("Play " + getCardName(), "move.action"));
		actions.addAction(new CardActionType("Discard", "move.discard"));
		return actions;
	}

	public abstract boolean isEnabled(Player self, Likeness action);

	@Override
	public String toString() {
		return getCardName();
	}

	protected int getInternalIntProperty(String subKey) {
		return getCardDefaults().getIntProperty(getInternalType() + "." + subKey, 0);
	}

	protected int getInternalIntProperty(String subKey, int defValue) {
		return getCardDefaults().getIntProperty(getInternalType() + "." + subKey, defValue);
	}

	protected String getInternalProperty(String subKey) {
		return getCardDefaults().getProperty(getInternalType() + "." + subKey);
	}

	protected String getInternalProperty(String subKey, String defValue) {
		return getCardDefaults().getProperty(getInternalType() + "." + subKey, defValue);
	}

}