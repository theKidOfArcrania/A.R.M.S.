/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package armsgame.card.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import armsgame.ResourceDefaults;

//FIXME: change "rent" to "fire"
//FIXME: change "props"
//FIXME: change "money" or "cash" into energy
//FIXME: reimplement damage dealing... and also change rent to damage

//action.[actionName].* --> this set of properties describes an action of that name.
//action.rent.[color] --> this set of properties describes an rent action of that color type.
//cash.[value].* --> this set of properties describe a cash card of a specific value.
//deck.length --> determines the number of card entries.
//deck.[index].class --> determines the class of the card at that index.
//deck.[index].subType --> determines the internal sub type of the card at that index.
//deck.[index].count --> determines how many of these cards are in the deck. (optional)
//parts.[color].fullset --> this is the number of weapon parts needed to build a full weapon of that spec.
//parts.[color].[num].* --> this set of properties describes a weapon part card of the specific color of the specific number.
//parts.wild.[colorCombo].* --> this set of properties describes a property card wild of the specific colors
//scale --> this property sets the multiplier on all number properties starting with a $.
//
//Card Prop-set:
//[card].image --> this is the image filepath of the card.
//
//Action Card Prop-set (extends from Card):
//[card].sellValue --> this defines the sell value.
//[card].description --> this specifies a detailed description of the card.
//[card].name --> name of card
//[card].actionName --> action tooltip text (optional)
//
//PartCard Card Prop-set (extends from Card):
//[card].name --> this defines the property names.
//[card].value --> this defines the value of the property.
//[card].damage --> this defines the damage the weapon can deal with that particular part.

/**
 * This class determines some basic property values such as rents, number of properties in full set, etc.
 * <p>
 *
 * @author HW
 */
public class CardDefaults extends ResourceDefaults {

	private static final long serialVersionUID = -7651695787655927167L;

	private static final CardDefaults defs;

	static {
		try {
			defs = new CardDefaults("carddefs.PROPERTIES");
		} catch (IOException ex) {
			Logger.getLogger(CardDefaults.class.getName()).log(Level.SEVERE, "Unable to load standard card defaults. Crashing now.", ex);
			System.exit(1);
			throw new InternalError(); // this should not be called.
		}
	}

	public static CardDefaults getCardDefaults() {
		return defs;
	}

	protected CardDefaults(String propFile) throws IOException {
		load(CardDefaults.class.getResourceAsStream(propFile));
	}

	public Deck generateDeck() throws DeckInitializationFailureException {
		int length = getIntProperty("deck.length", 0);
		Deck generated = new Deck();
		try {
			for (int i = 0; i < length; i++) {
				String className = getProperty("deck." + i + ".class");
				String subType = getProperty("deck." + i + ".subType");

				if (className == null) {
					throw new DeckInitializationFailureException("Class name is omitted.");
				}

				if (subType == null) {
					Class.forName(className).newInstance();
				} else {
					Class.forName(className).getConstructor(String.class).newInstance(subType);
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw new DeckInitializationFailureException(e);
		}
		generated.finalizeDeck();
		return generated;
	}

}
