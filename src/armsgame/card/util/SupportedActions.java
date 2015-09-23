package armsgame.card.util;

import java.util.HashMap;
import java.util.Iterator;

import armsgame.card.util.CardActionType.Likeness;

public class SupportedActions implements Iterable<CardActionType>
{
	private final HashMap<Likeness, CardActionType> supported = new HashMap<>();

	public void addAction(CardActionType type)
	{
		supported.put(type.getLikeness(), type);
	}

	public boolean containsLikeness(Likeness likeness)
	{
		return supported.containsKey(likeness);
	}

	public CardActionType getActionType(Likeness likeness)
	{
		return supported.get(likeness);
	}

	@Override
	public Iterator<CardActionType> iterator()
	{
		return supported.values().iterator();
	}

	public void removeAction(CardActionType type)
	{
		supported.remove(type.getLikeness(), type);
	}

	public void removeAction(Likeness likeness)
	{
		supported.remove(likeness);
	}

	public CardActionType replaceAction(CardActionType type)
	{
		if (!supported.containsKey(type.getLikeness()))
		{
			throw new IllegalArgumentException("Likeness does not already exist");
		}

		return supported.put(type.getLikeness(), type);
	}
}
