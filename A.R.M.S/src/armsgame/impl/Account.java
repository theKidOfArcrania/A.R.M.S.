package armsgame.impl;

import armsgame.res.Tools;
import javafx.scene.image.Image;

public class Account
{
	/**
	 * Utility method for obtaining stats for an account id.
	 *
	 * @return the stats of the user.
	 */
	public static AccountStats obtainStats(long userID)
	{
		return new AccountStats(userID);
	}

	public static void verifyID(Account player)
	{
		// TO DO: verify the player name and userID.
	}

	private final String name;
	private final Image profileImage;
	private final AccountStats stats;
	private final long userID;

	public Account(String name, Image profileImage, long userID)
	{
		super();
		verifyID(this);

		this.stats = obtainStats(userID);
		this.name = name;
		this.profileImage = profileImage;
		this.userID = userID;
	}

	/**
	 * Obtains the name of this player
	 *
	 * @return the name of this player
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Obtains the profile image of this player
	 *
	 * @return
	 */
	public Image getProfileImage()
	{
		if (profileImage == null)
		{
			return Tools.createImage("armsgame/res/blank-profile.jpg");
		}
		return profileImage;
	}

	/**
	 * Obtains the statistics of this player.
	 *
	 * @return the stats object
	 */
	public AccountStats getStats()
	{
		return stats;
	}

	public long getUserID()
	{
		return userID;
	}
}
