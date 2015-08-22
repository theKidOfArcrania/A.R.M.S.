package armsgame.impl;

import javafx.scene.image.Image;

public class Account {
	public static void verifyID(Account player) {
		// TO DO: verify the player name and userID.
	}

	private final String name;
	private final Image profileImage;

	private final long userID;

	public Account(String name, Image profileImage, long userID) {
		super();
		this.name = name;
		this.profileImage = profileImage;
		this.userID = userID;
	}

	public String getName() {
		return name;
	}

	public Image getProfileImage() {
		return profileImage;
	}

	public long getUserID() {
		return userID;
	}
}
