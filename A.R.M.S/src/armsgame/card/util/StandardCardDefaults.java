package armsgame.card.util;

import java.io.IOException;

public class StandardCardDefaults extends CardDefaults {
	private static final long serialVersionUID = 5753795010090212920L;

	private StandardCardDefaults() throws IOException {
		super("monopolycards\\cardstandard\\carddefs.properties");
	}
}
