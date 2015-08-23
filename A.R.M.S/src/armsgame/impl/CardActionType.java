package armsgame.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.function.BiFunction;

import armsgame.ResourceDefaults;
import armsgame.card.Card;
import armsgame.card.Action;
import armsgame.card.BurstCharge;
import common.util.Lambdas;

public class CardActionType {

	public enum Likeness {
		Action(ACTION_ACTION), SingleAction(Card::actionPlayed), Cashin(ACTION_CASHIN), Discard(ACTION_DISCARD);

		private BiFunction<? super Card, ? super Player, ? extends Boolean> action;

		Likeness(BiFunction<? super Card, ? super Player, ? extends Boolean> action) {
			this.action = action;
		}

		public BiFunction<? super Card, ? super Player, ? extends Boolean> getAction() {
			return action;
		}
	}

	// TO DO: make more standard.
	public static BiFunction<? super Card, ? super Player, ? extends Boolean> ACTION_DISCARD = (card, player) -> {
		player.getGame()
				.getCenterPlay()
				.discard(card);
		return true;
	};
	public static BiFunction<? super Card, ? super Player, ? extends Boolean> ACTION_CASHIN = Lambdas.convertType(Action.class)
			.andThen(Action::convertToCash)
			.filterReturn(BurstCharge::actionPlayed);
	public static BiFunction<? super Card, ? super Player, ? extends Boolean> ACTION_ACTION = (card, player) -> {
		boolean action = card.actionPlayed(player);
		if (action) {
			ACTION_DISCARD.apply(card, player);
			return true;
		} else {
			return false;
		}
	};

	private final String name;
	private final BiFunction<? super Card, ? super Player, ? extends Boolean> action;
	private final String internalType;
	private final ResourceDefaults defs;
	private final Likeness likeness;

	public CardActionType(String name, String internalType) {
		this(name, internalType, null);
	}

	public CardActionType(String name, String internalType, BiFunction<? super Card, ? super Player, ? extends Boolean> actionOverride) {
		this(name, internalType, actionOverride, ResourceDefaults.getDefaults());
	}

	public CardActionType(String name, String internalType, BiFunction<? super Card, ? super Player, ? extends Boolean> actionOverride, ResourceDefaults defs) {
		this.name = name;
		this.internalType = internalType;
		this.defs = defs;
		this.likeness = defs.getEnumProperty(internalType + ".like", Likeness.class, null);

		if (likeness == null) {
			throw new IllegalArgumentException("Invalid action type for internal type");
		}
		if (actionOverride == null) {
			this.action = this.likeness.getAction();
		} else {
			this.action = actionOverride;
		}
	}

	public BiFunction<? super Card, ? super Player, ? extends Boolean> getAction() {
		return action;
	}

	public BufferedImage getIcon() throws IOException {
		return defs.getImageProperty(internalType + ".icon");
	}

	public String getInternalType() {
		return internalType;
	}

	public Likeness getLikeness() {
		return likeness;
	}

	public String getName() {
		return name;
	}
}