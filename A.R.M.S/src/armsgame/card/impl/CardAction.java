/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card.impl;

import armsgame.card.Card;

/**
 *
 * @author Henry
 */
public class CardAction {
	private final CardActionType actionType;
	private final Card played;

	public CardAction(Card played, CardActionType actionType) {
		this.played = played;
		this.actionType = actionType;
	}

	public CardActionType getActionType() {
		return actionType;
	}

	public Card getPlayed() {
		return played;
	}

}
