/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card;

/**
 * Interface that specifies if a card has some value.
 * <p>
 *
 * @author HW
 */
public interface Valuable {

	/**
	 * Retrives the energy value that this card is worth.
	 * <p>
	 *
	 * @return the value of this card.
	 */
	public int getValue();
}
