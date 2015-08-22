package armsgame.card.standard;

import java.awt.Image;
import java.io.IOException;

import armsgame.card.AbstractCard;
import armsgame.card.CardDefaults;

import static armsgame.card.standard.StandardCardDefaults.getCardDefaults;

/*
 * To change this license header, choose License Headers in Project Properties. To change
 * this template file, choose Tools | Templates and open the template in the editor.
 */
/**
 * This is the top abstract class of cards for all monopoly cards. (Classic and Millionare)
 * <p>
 *
 * @author HW
 */
public abstract class StandardCard extends AbstractCard {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5207918406669141417L;

	protected StandardCard() {
	}

	@SuppressWarnings("static-method")
	public CardDefaults getDefaults() {
		return getCardDefaults();
	}

	@Override
	public String getDescription() {
		return getInternalProperty("description");
	}

	@Override
	public Image getImage() throws IOException {
		return getCardDefaults().getImageProperty(getInternalType() + ".image");
	}

	@Override
	protected int getInternalIntProperty(String subKey) {
		return getCardDefaults().getIntProperty(getInternalType() + "." + subKey, 0);
	}

	@Override
	protected int getInternalIntProperty(String subKey, int defValue) {
		return getCardDefaults().getIntProperty(getInternalType() + "." + subKey, defValue);
	}

	@Override
	protected String getInternalProperty(String subKey) {
		return getCardDefaults().getProperty(getInternalType() + "." + subKey);
	}

	@Override
	protected String getInternalProperty(String subKey, String defValue) {
		return getCardDefaults().getProperty(getInternalType() + "." + subKey, defValue);
	}

}
