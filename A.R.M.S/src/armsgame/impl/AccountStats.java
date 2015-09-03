package armsgame.impl;

public class AccountStats
{
	private int rank;
	private int score;
	private int gamesPlayed;
	private final long userID;

	AccountStats(long userID)
	{
		this.userID = userID;
		// TO DO: login to server to obtain account info.
	}

	public int getGamesPlayed()
	{
		return gamesPlayed;
	}

	public int getRank()
	{
		return rank;
	}

	public int getScore()
	{
		return score;
	}

	public long getUserID()
	{
		return userID;
	}

	public void update()
	{
		// TO DO: connect to server.
	}
}
