/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.impl;

import java.util.ArrayList;
import java.util.stream.Stream;

import armsgame.card.CardDefaults;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

//TO DO: RMI settings.

/**
 * This is the heart of the entire game. It holds the players and the board stats and runs the game itself.
 * <p>
 *
 * @author HW
 */
public class Board {

	private final CenterPlay centerPlay;
	private final SimpleObjectProperty<Player> current = new SimpleObjectProperty<Player>(null);
	private final ArrayList<Player> players = new ArrayList<>(10);
	private volatile String progress;
	// private final HashMap<Card, Player> references = new HashMap<>(10);
	private boolean started = false;
	private final CardDefaults def;

	public Board(CardDefaults def, CenterPlay centerPlay) {
		this.def = def;
		this.centerPlay = centerPlay;
	}

	public void addPlayer(Player player) {
		checkStarted();
		player.registerGame(this);
		if (player.getDefaults() != def) {
			throw new IllegalArgumentException("Player must have same card defaults as game.");
		}
		player.drawCards();
		players.add(player);

	}

	public void checkStarted() {
		if (started) {
			throw new IllegalStateException("Game has already started.");
		}
	}

	public Player[] checkWin() {
		return players.parallelStream()
				.filter(Player::checkWin)
				.toArray(Player[]::new);
	}

	public ObjectProperty<Player> currentProperty() {
		return current;
	}

	public CenterPlay getCenterPlay() {
		return centerPlay;
	}

	public Player getCurrent() {
		return current.get();
	}

	public Player getPlayer(int index) {
		return players.get(index);
	}

	public int getPlayerCount() {
		return players.size();
	}

	/**
	 * This is used to describe what the current player is doing.
	 * <p>
	 *
	 * @return what the player is currently doing.
	 */
	public String getProgress() {
		return progress;
	}

	public int indexOfPlayer(Player player) {
		return players.indexOf(player);
	}

	public boolean isStarted() {
		return started;
	}

	public Stream<Player> playerStream() {
		return players.stream();
	}

	public void removePlayer(int index) {
		checkStarted();
		players.remove(index)
				.registerGame(null);
	}

	public void removePlayer(Player player) {
		checkStarted();
		player.registerGame(null);
		players.remove(player);
	}

	/**
	 * This is the central function that runs the entire game.
	 */
	public void start() {
		@SuppressWarnings("unused")
		Player[] winner;
		checkStarted();
		started = true;

		// start game!
		int playerIndex = 0;
		while ((winner = checkWin()).length == 0) {
			Player current = players.get(playerIndex);
			this.current.set(current);
			current.drawCards();
			current.selectTurn();

			// three turns.
			for (; !current.isTurnDone();) {
				CardAction action = current.selectHand();
				if (!current.playAction(action)) {
					throw new AssertionError();
				}
				// check winner.
				if ((winner = checkWin()).length > 0) {
					break;
				}
			}

			playerIndex++;
			if (playerIndex >= players.size()) {
				playerIndex = 0;
			}
		}

		// Congratulations!!! We have a winner.
		// TO DO: win screen.
	}

	/**
	 * This internal method checks if this card has already been referenced to in the game.
	 * <p>
	 *
	 * @throws IllegalStateException
	 *             if this reference is not logged with this player or if it is not null.
	 */
	/*
	 * void checkReference(Player player, Card c) { if (!centerPlay.getDeck().contains(c)) { throw new IllegalArgumentException( "Card is not in deck"); } if (references.get(c) != null && references.get(c) != player) { throw new IllegalStateException( "This card is already in use by someone else.");
	 * } }
	 */
	/**
	 * This internal method first checks for existing references/ usages. Then it adds a reference of this card to the player game.
	 */
	/*
	 * void referenceCard(Player player, Card c) { if (!centerPlay.getDeck().contains(c)) { throw new IllegalArgumentException( "Card is not in deck"); } checkReference(player, c); references.put(c, player); }
	 */
}
