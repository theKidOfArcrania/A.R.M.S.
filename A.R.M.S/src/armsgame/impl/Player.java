package armsgame.impl;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import armsgame.card.Card;
import armsgame.card.CardDefaults;
import armsgame.card.Response;
import armsgame.card.WeaponPart;
import armsgame.card.WeaponSet;
import armsgame.card.WeaponSpec;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerPropertyBase;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * This class describes the cards that a player has at any point.
 * <p>
 *
 * @author HW, Alex
 */
public abstract class Player {

	private static boolean hasPropertyInColumn(WeaponPart card, WeaponSet column) {
		return column.stream()
			.parallel()
			.anyMatch((prop) -> prop == card);
	}

	private final CardDefaults defs;
	private final Account playerAccount;
	private final IntegerProperty shieldLevel = new SimpleIntegerProperty(0);
	private final IntegerProperty energyLevel = new SimpleIntegerProperty(0);
	private final int cashCache = -1;
	private int setCache = -1;
	private Board game = null;
	// this is all the cards a player has in his/her hand.
	private final ObservableList<Card> hand;
	private int moves = 0;
	private final ObservableList<WeaponSet> weaponSets;
	private final ObservableList<CardAction> playerHistory;
	private final ReadOnlyIntegerProperty propertySets;

	public Player(CardDefaults defs, Account playerAccount) {
		this.playerAccount = playerAccount;
		this.defs = defs;
		weaponSets = FXCollections.observableArrayList();
		hand = FXCollections.observableArrayList();
		playerHistory = FXCollections.observableArrayList();

		propertySets = new ReadOnlyIntegerPropertyBase() {
			{
				ListChangeListener<Card> columnList = event -> {
					setCache = -1;
					this.fireValueChangedEvent();
				};
				weaponSets.addListener((ListChangeListener<WeaponSet>) event -> {
					if (event.wasAdded()) {
						event.getAddedSubList()
							.parallelStream()
							.forEach(column -> column.addListener(columnList));
					}

					setCache = -1;
					this.fireValueChangedEvent();
				});
			}

			@Override
			public int get() {
				if (setCache == -1) { // invalidated
					return setCache = (int) weaponSets.parallelStream()
						.filter(WeaponSet::isFullSet)
						.count();
				}
				return setCache;
			}

			@Override
			public Object getBean() {
				return this;
			}

			@Override
			public String getName() {
				return "propertySets";
			}

		};
	}

	/**
	 * This alerts the player of a message.
	 * <p>
	 *
	 * @param prompt
	 *            the selectRequest to tell the player.
	 */
	public abstract void alert(String prompt);

	public boolean checkWin() {
		int fullSets = weaponSets.stream()
			.parallel()
			.reduce(0, (count, column) -> count + (column.isFullSet() ? 1 : 0), (count1, count2) -> count1 + count2);
		return fullSets >= 3;
	}

	public Stream<WeaponSet> columnStream() {
		return weaponSets.stream();
	}

	public void damageEnergy(int damage) {
		energyLevel.set(Math.max(0, energyLevel.get() - damage));
	}

	public void damageShield(int damage) {
		int shieldLeft = shieldLevel.get();
		if (shieldLeft >= damage) {
			shieldLevel.set(shieldLeft - damage);
		} else {
			shieldLevel.set(0);
			energyLevel.set(Math.max(0, energyLevel.get() - (damage - shieldLeft)));
		}
	}

	// starts a player's turn with drawing cards.
	@SuppressWarnings("FinalMethod")
	public final void drawCards() {
		Card[] cards = game.getCenterPlay()
			.drawCards(2);
		hand.addAll(Arrays.asList(cards));
	}

	public CardDefaults getDefaults() {
		return defs;
	}

	public int getEnergyLevel() {
		return energyLevel.get();
	}

	public ObservableList<Card> getFullHand() {
		return FXCollections.unmodifiableObservableList(hand);
	}

	public Board getGame() {
		return game;
	}

	public Card getHand(int index) {
		return hand.get(index);
	}

	public int getHandCount() {
		return hand.size();
	}

	public int getMove() {
		return moves;
	}

	public String getName() {
		return playerAccount.getName();
	}

	public Account getPlayerAccount() {
		return playerAccount;
	}

	public ObservableList<CardAction> getPlayerHistory() {
		return FXCollections.unmodifiableObservableList(playerHistory);
	}

	public ObservableList<WeaponSet> getPropColumns() {
		return FXCollections.unmodifiableObservableList(weaponSets);
	}

	public WeaponSet getPropertyColumn(int index) {
		return weaponSets.get(index);
	}

	public WeaponSet getPropertyColumn(WeaponPart card) {
		return weaponSets.parallelStream()
			.filter(column -> hasPropertyInColumn(card, column))
			.findAny()
			.orElse(null);
	}

	public WeaponSet getPropertyColumn(WeaponSpec color) {
		return weaponSets.parallelStream()
			.filter(column -> column.getPropertyColor() == color)
			.findAny()
			.orElseGet(() -> {
				WeaponSet newColumn = new WeaponSet(defs, color);
				weaponSets.add(newColumn);
				return newColumn;
			});
	}

	public int getPropertyColumnCount() {
		return weaponSets.size();
	}

	public int getPropertySets() {
		return propertySets.get();
	}

	public int getShieldLevel() {
		return shieldLevel.get();
	}

	public Stream<Card> handStream() {
		return hand.stream();
	}

	public boolean hasCompleteSet() {
		return weaponSets.parallelStream()
			.anyMatch(WeaponSet::isFullSet);
	}

	public boolean hasIncompleteSet() {
		return weaponSets.parallelStream()
			.anyMatch(WeaponSet::hasIncompleteSet);
	}

	public void increaseBurst(int value) {
		shieldLevel.set(Math.min(shieldLevel.get() + value, 200));
	}

	public final boolean isLastTurn() {
		return moves == 2;
	}

	public final boolean isTurnDone() {
		return moves >= 3;
	}

	// player makes one move.
	@SuppressWarnings("FinalMethod")
	public final boolean playAction(CardAction move) {
		Card played = move.getPlayed();

		if (isTurnDone()) {
			return false;
		}

		hand.remove(played);

		// TO DO: checkReference(current, played);
		if (move.getActionType()
			.getAction()
			.apply(played, this)) {
			pushTurn(move);
		}
		return true;
	}

	public ReadOnlyIntegerProperty propertySetsProperty() {
		return propertySets;
	}

	public void pushTurn(CardAction move) {
		moves++;
		playerHistory.add(move);
	}

	public void registerGame(Board game) {
		if (this.game != null && this.game.isStarted()) {
			throw new IllegalStateException("This player is already registered to a started game.");
		}
		this.game = game;
	}

	public void resetTurn() {
		moves = 0;
	}

	/**
	 * This prompts the player to select a card to play (convenience method)
	 * <p>
	 *
	 * @return the card the player selected.
	 */
	public CardAction selectHand() {
		return selectHand("Please select a card to play.");
	}

	/**
	 * This prompts the player to select a card to play (convenience method)
	 * <p>
	 *
	 * @param prompt
	 *            the selectRequest that the player will see.
	 * @return the card the player selected.
	 */
	public CardAction selectHand(String prompt) {
		return selectHand(prompt, card -> true);
	}

	/**
	 * This prompts the player to select a card to play
	 * <p>
	 *
	 * @param prompt
	 *            the selectRequest that the player will see.
	 * @param filter
	 *            filter for cards to play.
	 * @return the card the player selected.
	 */
	public CardAction selectHand(String prompt, Predicate<Card> filter) {
		return selectHand(prompt, filter, cardAction -> true);
	}

	/**
	 * This prompts the player to select a card to play
	 * <p>
	 *
	 * @param prompt
	 *            the selectRequest that the player will see.
	 * @param filter
	 *            filter for cards to play.
	 * @param actionFilter
	 *            filter for the actions for the card.
	 * @return the card the player selected.
	 */
	public abstract CardAction selectHand(String prompt, Predicate<Card> filter, Function<CardActionType, Boolean> actionFilter);

	/**
	 * This prompts the player of a payment that must be made.
	 * <p>
	 *
	 * @param amount
	 *            the debt amount.
	 */
	public abstract void selectPayment(DamageReport amount);

	/**
	 * This prompts the player to select another player
	 * <p>
	 *
	 * @return another player that this player selected
	 */
	public Player selectPlayer() {
		return selectPlayer("Please select your target player.");
	}

	/**
	 * This prompts the player to select another player
	 * <p>
	 *
	 * @param prompt
	 *            the selectRequest that the player will see.
	 * @return another player that this player selected
	 */
	public Player selectPlayer(String prompt) {
		return selectPlayer(prompt, player -> true);
	}

	/**
	 * This prompts the player to select another player
	 * <p>
	 *
	 * @param prompt
	 *            the selectRequest that the player will see.
	 * @param filter
	 *            the filter for the other players
	 * @return another player that this player selected
	 */
	public abstract Player selectPlayer(String prompt, Predicate<Player> filter);

	/**
	 * This prompts the player to select a property column to use (convenience method)
	 * <p>
	 *
	 * @return the property the player selected.
	 */
	public WeaponPart selectProperty() {
		return selectProperty("Please select a property column to use.");
	}

	/**
	 * This prompts the player to select a property to use (convenience method)
	 * <p>
	 *
	 * @param prompt
	 *            the selectRequest that the player will see.
	 * @return the property the player selected.
	 */
	public WeaponPart selectProperty(String prompt) {
		return selectProperty(prompt, card -> true);
	}

	/**
	 * This prompts the player to select a property to use (convenience method)
	 * <p>
	 *
	 * @param prompt
	 *            the selectRequest that the player will see.
	 * @param filter
	 *            filter for properties
	 * @return the property the player selected.
	 */
	public WeaponPart selectProperty(String prompt, Predicate<WeaponPart> filter) {
		return selectProperty(prompt, filter, this);
	}

	/**
	 * This prompts the player to select a property to use from a particular player
	 * <p>
	 *
	 * @param prompt
	 *            the selectRequest that the player will see.
	 * @param filter
	 *            filter for properties
	 * @param context
	 *            the property columns that the player will select from.
	 * @return the property the player selected.
	 */
	public abstract WeaponPart selectProperty(String prompt, Predicate<WeaponPart> filter, Player context);

	/**
	 * This prompts the player to select a property column to use (convenience method)
	 * <p>
	 *
	 * @return the property column the player selected.
	 */
	public WeaponSet selectPropertyColumn() {
		return selectPropertyColumn("Please select a property column to use.");
	}

	/**
	 * This prompts the player to select a property column to use (convenience method)
	 * <p>
	 *
	 * @param prompt
	 *            the selectRequest that the player will see.
	 * @return the property column the player selected.
	 */
	public WeaponSet selectPropertyColumn(String prompt) {
		return selectPropertyColumn(prompt, card -> true);
	}

	/**
	 * This prompts the player to select a property column to use (convenience method)
	 * <p>
	 *
	 * @param prompt
	 *            the selectRequest that the player will see.
	 * @param filter
	 *            filter for property columns
	 * @return the property column the player selected.
	 */
	public WeaponSet selectPropertyColumn(String prompt, Predicate<WeaponSet> filter) {
		return selectPropertyColumn(prompt, filter, this);
	}

	/**
	 * This prompts the player to select a property column to use from a particular player
	 * <p>
	 *
	 * @param prompt
	 *            the selectRequest that the player will see.
	 * @param filter
	 *            filter for property columns
	 * @param context
	 *            the property columns that the player will select from.
	 * @return the property column the player selected.
	 */
	public abstract WeaponSet selectPropertyColumn(String prompt, Predicate<WeaponSet> filter, Player context);

	/**
	 * This prompts the player a yes or no question.
	 * <p>
	 *
	 * @param prompt
	 *            the select request to ask the player
	 * @return true if player responds with a "yes" or false otherwise.
	 */
	public abstract boolean selectRequest(String prompt);

	/**
	 * This prompts the player whether to agree
	 * <p>
	 *
	 * @param prompt
	 *            the prompt that the player will see.
	 * @return null if player accepts, the response card (currently only a just say no) if player rejects.
	 */
	public abstract Response selectResponse(String prompt);

	/**
	 * This method allows for a pause before player starts a turn... ie if player needs to be prompted to start turn and draw cards.
	 */
	public abstract void selectTurn();

	public IntegerProperty shieldLevelProperty() {
		return shieldLevel;
	}
}
