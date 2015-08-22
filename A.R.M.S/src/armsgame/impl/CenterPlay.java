/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.impl;

import java.util.ArrayList;
import java.util.Collections;

import armsgame.card.Card;
import armsgame.card.Deck;

/**
 * This class consists of a collection of all the face down cards in the center, often unknown, and the face-up played deck, which is known. The access methods for the face down cards will all be opaque and only allow you to draw cards.
 * <p>
 *
 * @author HW
 */
public class CenterPlay {

	private final Deck deck;
	private final ArrayList<Card> playedCards;
	private final ArrayList<Card> drawPile;

	public CenterPlay(Deck deck) {
		this.playedCards = new ArrayList<>(deck.size());
		this.drawPile = new ArrayList<>(deck.size());
		this.deck = deck;

	}

	public void discard(Card card) {
		// TO DO: remove ref.
		playedCards.add(card);
	}

	/**
	 * This draws the specified number of cards from the face down pile. If there are no more cards in the face-down pile, it shuffles the face-up cards and then places it in the face-down pile and continues play.
	 * <p>
	 *
	 * @param count
	 *            the number of cards to draw
	 * @return the drawed cards.
	 */
	public Card[] drawCards(int count) {
		Card[] drawn = new Card[count];
		for (int i = 0; i < count; i++) {
			drawn[i] = drawPile.remove(0);
			if (drawPile.isEmpty()) {
				// refill.
				if (playedCards.isEmpty()) {
					// uh-oh! someone crashed the game.
					Card[] drawnShrunk = new Card[i];
					System.arraycopy(drawn, 0, drawnShrunk, 0, i);
					return drawnShrunk;
				} else {
					drawPile.addAll(playedCards);
					playedCards.clear();
					shuffle();
				}
			}
		}
		return drawn;
	}

	/**
	 * This retrieves the count of face down cards.
	 *
	 * @return the number of face down cards.
	 */
	public int faceDownCount() {
		return drawPile.size();
	}

	/**
	 * Specifies if the card exists in the face-up pile.
	 * <p>
	 *
	 * @param card
	 *            the card to test
	 * @return true if it exists in the face-up pile, false otherwise
	 */
	public boolean faceUpContains(Card card) {
		return playedCards.contains(card);
	}

	/**
	 * Retrieves the total number of face up cards
	 * <p>
	 *
	 * @return the total number of face up cards.
	 */
	public int faceUpCount() {
		return playedCards.size();
	}

	/**
	 * Get the value of deck
	 * <p>
	 *
	 * @return the value of deck
	 */
	public Deck getDeck() {
		return deck;
	}

	/**
	 * Retrieves one card from the face-up card deck. The most top card would be index 0.
	 * <p>
	 *
	 * @param index
	 *            the index of the card
	 * @return the card at the specified index.
	 */
	public Card getFaceUpCard(int index) {
		return playedCards.get(index);
	}

	private void shuffle() {
		Collections.shuffle(drawPile);
	}

}
