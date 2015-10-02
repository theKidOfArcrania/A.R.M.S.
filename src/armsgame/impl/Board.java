/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.impl;

import armsgame.card.util.CardAction;
import armsgame.card.util.CardDefaults;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//TO DO: RMI settings.

/**
 * This is the heart of the entire game. It holds the players and the board stats and runs the game itself.
 * <p>
 *
 * @author HW
 */
public class Board {

	private final CenterPlay centerPlay;
	private final SimpleObjectProperty<Player> current = new SimpleObjectProperty<>(null);
	private final ObservableList<Player> players = FXCollections.observableArrayList();
	private final ObservableList<Player> playersRead = FXCollections.unmodifiableObservableList(players);
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
		return players.parallelStream().filter(Player::checkWin).toArray(Player[]::new);
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

	/**
	 * Obtains an UNMODIFIABLE copy of the players list.
	 * 
	 * @return an unmodifiable copy of the players list.
	 */
	public ObservableList<Player> getPlayers() {
		return playersRead;
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

	public boolean isStarted() {
		return started;
	}

	public void removePlayer(int index) {
		checkStarted();
		players.remove(index).registerGame(null);
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
}
