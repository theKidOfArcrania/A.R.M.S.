package armsgame.card.standard;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import armsgame.card.CardDefaults;

public class StandardCardDefaults extends CardDefaults {
	private static final long serialVersionUID = 5753795010090212920L;
	private static final CardDefaults defs;

	static {
		try {
			defs = new StandardCardDefaults();
		} catch (IOException ex) {
			Logger.getLogger(CardDefaults.class.getName())
					.log(Level.SEVERE, "Unable to load standard card defaults. Crashing now.", ex);
			System.exit(1);
			throw new InternalError(); // this should not be called.
		}
	}

	public static CardDefaults getCardDefaults() {
		return defs;
	}

	private StandardCardDefaults() throws IOException {
		super("monopolycards\\cardstandard\\carddefs.properties");
	}
}
