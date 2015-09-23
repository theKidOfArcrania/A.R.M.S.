package armsgame.impl;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import armsgame.card.Card;
import armsgame.card.Response;
import armsgame.card.util.CardAction;
import armsgame.card.util.CardActionType;
import armsgame.card.util.CardDefaults;
import armsgame.weapon.Weapon;
import armsgame.weapon.WeaponPartSpec;
import armsgame.weapon.WeaponSet;
import armsgame.weapon.WeaponSpec;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerPropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * This class describes the cards that a player has at any point.
 * <p>
 *
 * @author HW
 */
public abstract class Player
{

	private static boolean containsWeaponPart(WeaponPartSpec spec, Weapon column)
	{
		// TO DO: implment
		return false;
	}

	private final CardDefaults defs;
	private final Account playerAccount;
	private final DoubleProperty shieldLevel = new SimpleDoubleProperty(0);
	private final DoubleProperty energyLevel = new SimpleDoubleProperty(0);
	private final int cashCache = -1;
	private int setCache = -1;
	private Board game = null;
	// this is all the cards a player has in his/her hand.
	private final ObservableList<Card> hand;
	private int moves = 0;
	private final ObservableList<Weapon> weaponSets;
	private final ObservableList<CardAction> playerHistory;
	private final ReadOnlyIntegerProperty propertySets;

	public Player(CardDefaults defs, Account playerAccount)
	{
		this.playerAccount = playerAccount;
		this.defs = defs;
		weaponSets = FXCollections.observableArrayList();
		hand = FXCollections.observableArrayList();
		playerHistory = FXCollections.observableArrayList();

		propertySets = new ReadOnlyIntegerPropertyBase()
		{
			{
				InvalidationListener buildList = event -> {
					setCache = -1;
					this.fireValueChangedEvent();
				};
				weaponSets.addListener((ListChangeListener<Weapon>) event -> {
					if (event.wasAdded())
					{
						event.getAddedSubList().parallelStream().forEach(weapon -> weapon.addListener(buildList));
					}

					setCache = -1;
					this.fireValueChangedEvent();
				});
			}

			@Override
			public int get()
			{
				if (setCache == -1)
				{ // invalidated
					return setCache = (int) weaponSets.parallelStream().filter(Weapon::isComplete).count();
				}
				return setCache;
			}

			@Override
			public Object getBean()
			{
				return this;
			}

			@Override
			public String getName()
			{
				return "propertySets";
			}

		};
	}

	/**
	 * This alerts the player of a message.
	 * <p>
	 *
	 * @param prompt the selectRequest to tell the player.
	 */
	public abstract void alert(String prompt);

	public boolean checkWin()
	{
		int fullSets = weaponSets.stream().parallel().reduce(0, (count, column) -> count + (column.isComplete() ? 1 : 0), (count1, count2) -> count1 + count2);
		return fullSets >= 3;
	}

	public Stream<Weapon> columnStream()
	{
		return weaponSets.stream();
	}

	public void damageEnergy(double damage)
	{
		energyLevel.set(Math.max(0, energyLevel.get() - damage));
	}

	public void damageShield(double damage)
	{
		double shieldLeft = shieldLevel.get();
		if (shieldLeft >= damage)
		{
			shieldLevel.set(shieldLeft - damage);
		} else
		{
			shieldLevel.set(0);
			energyLevel.set(Math.max(0, energyLevel.get() - (damage - shieldLeft)));
		}
	}

	// starts a player's turn with drawing cards.
	@SuppressWarnings("FinalMethod")
	public final void drawCards()
	{
		Card[] cards = game.getCenterPlay().drawCards(2);
		hand.addAll(Arrays.asList(cards));
	}

	public DoubleExpression energyLevelProperty()
	{
		return energyLevel;
	}

	public CardDefaults getDefaults()
	{
		return defs;
	}

	public double getEnergyLevel()
	{
		return energyLevel.get();
	}

	public ObservableList<Card> getFullHand()
	{
		return FXCollections.unmodifiableObservableList(hand);
	}

	public Board getGame()
	{
		return game;
	}

	public Card getHand(int index)
	{
		return hand.get(index);
	}

	public int getHandCount()
	{
		return hand.size();
	}

	/**
	 * Retrieves the number of weapons that have max upgrades Note: the energy crystal upgrade doesn't determine whether if the weapon is max upgrades
	 *
	 * @return the number of weapon sets
	 */
	public int getMaxedWeapons()
	{
		return propertySets.get();
	}

	public int getMove()
	{
		return moves;
	}

	public String getName()
	{
		return playerAccount.getName();
	}

	public Account getPlayerAccount()
	{
		return playerAccount;
	}

	public ObservableList<CardAction> getPlayerHistory()
	{
		return FXCollections.unmodifiableObservableList(playerHistory);
	}

	/**
	 * Retrieves the current shield level
	 *
	 * @return a value of the shielding level.
	 */
	public double getShieldLevel()
	{
		return shieldLevel.get();
	}

	public Weapon getWeapon(int index)
	{
		return weaponSets.get(index);
	}

	public Weapon getWeapon(WeaponSpec spec)
	{
		return weaponSets.parallelStream().filter(weapon -> weapon.getSpec() == spec).findAny().orElseGet(() -> {
			Weapon newColumn = new Weapon(spec);
			weaponSets.add(newColumn);
			return newColumn;
		});
	}

	public ObservableList<Weapon> getWeaponSets()
	{
		return FXCollections.unmodifiableObservableList(weaponSets);
	}

	/**
	 * Gets the stream of the cards of a player's hand
	 *
	 * @return the stream of cards.
	 */
	public Stream<Card> handStream()
	{
		return hand.stream();
	}

	/**
	 * Identifies if this player has at least one weapon set that isn't at max upgrades
	 *
	 * @return true if at least one set is incomplete, but has upgrades, false otherwise.
	 */
	public boolean hasFreeWeaponParts()
	{
		return weaponSets.parallelStream().anyMatch(Weapon::isIncomplete);
	}

	/**
	 * Identifies if this player has max upgrades for any weapon.
	 * <p>
	 * Note: the energy crystal upgrade doesn't determine whether if the weapon is max upgrades
	 *
	 * @return true if at least one set is complete, false otherwise.
	 */
	public boolean hasMaxWeapon()
	{
		return getMaxedWeapons() > 0;
	}

	public void heal(double value)
	{
		double max = 200; // TO DO: get a max shielding.
		double energyHealLeft = max - energyLevel.get();
		if (energyHealLeft >= value)
		{
			shieldLevel.set(energyLevel.get() + value);
		} else
		{
			shieldLevel.set(max);
			healShield(value - energyHealLeft);
		}
	}

	public void healShield(double value)
	{
		shieldLevel.set(Math.min(shieldLevel.get() + value, 200));
	}

	public final boolean isLastTurn()
	{
		return moves == 2;
	}

	public final boolean isTurnDone()
	{
		return moves >= 3;
	}

	// player makes one move.
	@SuppressWarnings("FinalMethod")
	public final boolean playAction(CardAction move)
	{
		Card played = move.getPlayed();

		if (isTurnDone())
		{
			return false;
		}

		hand.remove(played);

		if (move.getActionType().getAction().apply(played, this))
		{
			pushTurn(move);
		}
		return true;
	}

	public ReadOnlyIntegerProperty propertySetsProperty()
	{
		return propertySets;
	}

	public void pushTurn(CardAction move)
	{
		moves++;
		playerHistory.add(move);
	}

	public void registerGame(Board game)
	{
		if (this.game != null && this.game.isStarted())
		{
			throw new IllegalStateException("This player is already registered to a started game.");
		}
		this.game = game;
	}

	public void resetTurn()
	{
		moves = 0;
	}

	/**
	 * This method allows the user to select a particular accuracy for a weapon fire. Note that this can only be used when a player is playing a card.
	 *
	 * @param guarantee a value from 0 to 1 that is guaranteed to be 100% efficiency
	 * @return a value from 0 to 1 determining weapon efficiency.
	 * @throws IllegalStateException when this is called outside of playing a card
	 */
	public abstract double selectAccuracy(double guarantee);

	/**
	 * This prompts the player to select a card to play (convenience method)
	 * <p>
	 *
	 * @return the card the player selected.
	 */
	public CardAction selectHand()
	{
		return selectHand("Please select a card to play.");
	}

	/**
	 * This prompts the player to select a card to play (convenience method)
	 * <p>
	 *
	 * @param prompt the selectRequest that the player will see.
	 * @return the card the player selected.
	 */
	public CardAction selectHand(String prompt)
	{
		return selectHand(prompt, card -> true);
	}

	/**
	 * This prompts the player to select a card to play
	 * <p>
	 *
	 * @param prompt the selectRequest that the player will see.
	 * @param filter filter for cards to play.
	 * @return the card the player selected.
	 */
	public CardAction selectHand(String prompt, Predicate<Card> filter)
	{
		return selectHand(prompt, filter, cardAction -> true);
	}

	/**
	 * This prompts the player to select a card to play
	 * <p>
	 *
	 * @param prompt the selectRequest that the player will see.
	 * @param filter filter for cards to play.
	 * @param actionFilter filter for the actions for the card.
	 * @return the card the player selected.
	 */
	public abstract CardAction selectHand(String prompt, Predicate<Card> filter, Function<CardActionType, Boolean> actionFilter);

	/**
	 * This prompts the player to select a weapon upgrade to use (convenience method)
	 * <p>
	 *
	 * @return the weapon part upgrade the player selected.
	 */
	public WeaponPartSpec selectPartUpgrade()
	{
		return selectPartUpgrade("Please select a weapon upgrade to use.");
	}

	/**
	 * This prompts the player to select a property to use (convenience method)
	 * <p>
	 *
	 * @param prompt the selectRequest that the player will see.
	 * @return the weapon part upgrade the player selected.
	 */
	public WeaponPartSpec selectPartUpgrade(String prompt)
	{
		return selectPartUpgrade(prompt, card -> true);
	}

	/**
	 * This prompts the player to select a property to use (convenience method)
	 * <p>
	 *
	 * @param prompt the selectRequest that the player will see.
	 * @param filter filter for properties
	 * @return the property the player selected.
	 */
	public WeaponPartSpec selectPartUpgrade(String prompt, Predicate<WeaponPartSpec> filter)
	{
		return selectPartUpgrade(prompt, filter, this);
	}

	/**
	 * This prompts the player to select a property to use from a particular player
	 * <p>
	 *
	 * @param prompt the selectRequest that the player will see.
	 * @param filter filter for properties
	 * @param context the property columns that the player will select from.
	 * @return the property the player selected.
	 */
	public abstract WeaponPartSpec selectPartUpgrade(String prompt, Predicate<WeaponPartSpec> filter, Player context);

	/**
	 * This prompts the player to select another player
	 * <p>
	 *
	 * @return another player that this player selected
	 */
	public Player selectPlayer()
	{
		return selectPlayer("Please select your target player.");
	}

	/**
	 * This prompts the player to select another player
	 * <p>
	 *
	 * @param prompt the selectRequest that the player will see.
	 * @return another player that this player selected
	 */
	public Player selectPlayer(String prompt)
	{
		return selectPlayer(prompt, player -> true);
	}

	/**
	 * This prompts the player to select another player
	 * <p>
	 *
	 * @param prompt the selectRequest that the player will see.
	 * @param filter the filter for the other players
	 * @return another player that this player selected
	 */
	public abstract Player selectPlayer(String prompt, Predicate<Player> filter);

	/**
	 * This prompts the player to select a property column to use (convenience method)
	 * <p>
	 *
	 * @return the property column the player selected.
	 */
	public WeaponSet selectPropertyColumn()
	{
		return selectPropertyColumn("Please select a property column to use.");
	}

	/**
	 * This prompts the player to select a property column to use (convenience method)
	 * <p>
	 *
	 * @param prompt the selectRequest that the player will see.
	 * @return the property column the player selected.
	 */
	public WeaponSet selectPropertyColumn(String prompt)
	{
		return selectPropertyColumn(prompt, card -> true);
	}

	/**
	 * This prompts the player to select a property column to use (convenience method)
	 * <p>
	 *
	 * @param prompt the selectRequest that the player will see.
	 * @param filter filter for property columns
	 * @return the property column the player selected.
	 */
	public WeaponSet selectPropertyColumn(String prompt, Predicate<WeaponSet> filter)
	{
		return selectPropertyColumn(prompt, filter, this);
	}

	/**
	 * This prompts the player to select a property column to use from a particular player
	 * <p>
	 *
	 * @param prompt the selectRequest that the player will see.
	 * @param filter filter for property columns
	 * @param context the property columns that the player will select from.
	 * @return the property column the player selected.
	 */
	public abstract WeaponSet selectPropertyColumn(String prompt, Predicate<WeaponSet> filter, Player context);

	/**
	 * This prompts the player a yes or no question.
	 * <p>
	 *
	 * @param prompt the select request to ask the player
	 * @return true if player responds with a "yes" or false otherwise.
	 */
	public abstract boolean selectRequest(String prompt);

	/**
	 * This prompts the player whether to agree
	 * <p>
	 *
	 * @param prompt the prompt that the player will see.
	 * @return null if player accepts, the response card (currently only a just say no) if player rejects.
	 */
	public abstract Response selectResponse(String prompt);

	/**
	 * This prompts the player of a payment that must be made.
	 * <p>
	 *
	 * @param amount the debt amount.
	 */
	public abstract void selectResponse(WeaponTransfer amount);

	/**
	 * This method allows for a pause before player starts a turn... ie if player needs to be prompted to start turn and draw cards.
	 */
	public abstract void selectTurn();

	public DoubleProperty shieldLevelProperty()
	{
		return shieldLevel;
	}
}
