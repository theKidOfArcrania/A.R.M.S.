package armsgame.ui;

import java.awt.Dimension;
import java.awt.DisplayMode;

import armsgame.impl.Board;
import armsgame.impl.Player;
import armsgame.res.Tools;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.binding.IntegerExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

public class PlayerInfo extends AnchorPane {

	// TO DO: remove stage part and have animations in blueprint.

	private final DisplayMode defaultMode = getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
	private final Dimension displayRes = new Dimension(defaultMode.getWidth(), defaultMode.getHeight());
	private final double dispWidth = displayRes.getWidth();
	private final double dispHeight = displayRes.getHeight();
	private final double wRatio = dispWidth / 1600.0;
	private final double hRatio = dispHeight / 900.0;

	public double sRatio;
	// user variables.
	private final String playerName;
	private final DoubleExpression energyLevel;
	private final DoubleExpression shieldLevel;
	private final IntegerExpression weaponSets;
	private final BooleanExpression playerTurn;

	private final SimpleBooleanProperty detailView = new SimpleBooleanProperty();

	private final Player player;
	private final Board game;

	public PlayerInfo(Player player) {
		this.game = player.getGame();
		this.player = player;

		playerName = player.getName();

		sRatio = getSmallRatio();
		shieldLevel = player.shieldLevelProperty().divide(200.0);
		energyLevel = player.energyLevelProperty().divide(100.0);

		playerTurn = game.currentProperty().isEqualTo(player);

		weaponSets = player.propertySetsProperty();

		init();
	}

	public boolean isDetailView() {
		return detailView.get();
	}

	public void setDetailView(boolean detailView) {
		this.detailView.set(detailView);
	}

	// public void createMoveCounter(Stage primaryStage)
	// {
	// Stage countWindow = new Stage();
	// countWindow.initStyle(StageStyle.TRANSPARENT);
	// countWindow.initOwner(primaryStage);
	//
	// AnchorPane root = new AnchorPane();
	// }

	public BooleanProperty viewPlayerDetailProperty() {
		return detailView;
	}

	/*
	 * private void createTransparentStage(Stage primaryStage) { Stage second = new Stage(); second.initStyle(StageStyle.TRANSPARENT); second.initOwner(primaryStage);
	 *
	 * AnchorPane root = new AnchorPane(); Blueprint scene = new Blueprint(root, 1300 * wRatio, 800 * hRatio);
	 *
	 * second.setScene(scene); second.show(); }
	 */

	private double getSmallRatio() {
		return (wRatio > hRatio) ? hRatio : wRatio;
	}

	private void init() {
		double width = 290 * wRatio;
		double height = 190 * hRatio;

		this.setMinSize(width, height);
		this.setPrefSize(width, height);

		InnerShadow smallShade = new InnerShadow(2.0, Color.BLACK);
		InnerShadow largeShade = new InnerShadow(5.0, Color.BLACK);
		DropShadow out = new DropShadow(2.0, Color.BLACK);

		ImageView profileView = Tools.createImageView(Tools.createImage("blank-profile.jpg"), 60, 60, 10, 10, sRatio, wRatio, hRatio, smallShade);
		Image screw = Tools.createImage("screw.png");
		ImageView screw1 = Tools.createImageView(screw, 15, 15, 3, 0.0, sRatio, wRatio, hRatio, out);
		ImageView screw2 = Tools.createImageView(screw, 15, 15, 275, 0.0, sRatio, wRatio, hRatio, out);
		ImageView screw3 = Tools.createImageView(screw, 15, 15, 3, 175, sRatio, wRatio, hRatio, out);
		ImageView screw4 = Tools.createImageView(screw, 15, 15, 275, 175, sRatio, wRatio, hRatio, out);

		Image blueButton = Tools.createImage("BlueButton.png");
		ImageView opener = Tools.createImageView(blueButton, 45, 45, 155, 145, sRatio, wRatio, hRatio, out);
		opener.setOnMouseEntered(e -> opener.setEffect(largeShade));
		opener.setOnMouseExited(e -> opener.setEffect(out));
		opener.setOnMouseClicked(e -> detailView.set(true));
		Text wDescription = Tools.createText(205, 150, wRatio, hRatio, "Weapons\nManager", Color.web("#a9e1f7"), smallShade, Tools.createRegularFont(12, sRatio));

		Text name = Tools.createText(80, 10, wRatio, hRatio, playerName, Color.GRAY, smallShade, Tools.createBoldFont(24, sRatio));
		Text weapons = Tools.createText(12, 145, wRatio, hRatio, "Weapons Status:", Color.GRAY, smallShade, Tools.createBoldFont(13.5, sRatio));
		Text weaponDisplay = Tools.createText(42, 163, wRatio, hRatio, "0 Complete", Color.GRAY, smallShade, Tools.createRegularFont(12, sRatio));
		weaponDisplay.textProperty().bind(weaponSets.asString().concat(" complete"));

		Text energy = Tools.createText(10, 80, wRatio, hRatio, "Energy:", Color.rgb(116, 229, 135), out, Tools.createRegularFont(15.5, sRatio));
		Text energyDisplay = Tools.createText(135, 85, wRatio, hRatio, "100 / 100V", Color.WHITE, out, Tools.createRegularFont(10, sRatio));
		energyDisplay.textProperty().bind(energyLevel.asString().concat(" / 100V"));

		Text shield = Tools.createText(10, 112.5, wRatio, hRatio, "Shields:", Color.LIGHTBLUE, out, Tools.createRegularFont(15.5, sRatio));
		Text shieldDisplay = Tools.createText(135, 117.5, wRatio, hRatio, "0 / 200V", Color.WHITE, out, Tools.createRegularFont(10, sRatio));
		shieldDisplay.textProperty().bind(shieldLevel.asString().concat(" / 200V"));

		Text playerRank = Tools.createText(85.0, 47.0, wRatio, hRatio, "Rank: "
				+ player.getPlayerAccount().getRank(), Color.WHITE, out, Tools.createRegularFont(10, sRatio));
		Text playerGames = Tools.createText(150.0, 47.0, wRatio, hRatio, "Games: "
				+ player.getPlayerAccount().getGamesPlayed(), Color.WHITE, out, Tools.createRegularFont(10, sRatio));

		Rectangle shieldEmpty = Tools.createRoundedRectangle(170, 22.5, 5, 5, 85, 111.5, sRatio, wRatio, hRatio, Color.DARKGRAY.darker(), largeShade);
		Stop[] list = { new Stop(0.0, Color.WHITE), new Stop(0.2, Color.web("#c4e0f4")), new Stop(.49, Color.web("#acd5f2")),
				new Stop(.5, Color.web("#74b2dd")), new Stop(1.0, Color.web("#a9e1f7")) };
		LinearGradient shieldGrad = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, list);
		Rectangle shieldBar = Tools.createRoundedRectangle((168) * shieldLevel.getValue(), 20, 5, 5, 86, 112.5, sRatio, wRatio, hRatio, Color.WHITE, null);
		shieldBar.widthProperty().bind(shieldLevel.multiply(168 * wRatio));
		shieldBar.setFill(shieldGrad);

		Rectangle energyEmpty = Tools.createRoundedRectangle(170, 22.5, 5, 5, 85, 79, sRatio, wRatio, hRatio, Color.DARKGRAY.darker(), largeShade);
		Stop[] elist = { new Stop(0.0, Color.WHITE), new Stop(0.3, Color.rgb(202, 255, 202)), new Stop(.49, Color.web("#acf0b7")),
				new Stop(.5, Color.rgb(116, 229, 135)), new Stop(1.0, Color.web("#c7f3ce")) };
		LinearGradient energyGrad = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, elist);
		Rectangle energyBar = Tools.createRoundedRectangle((168) * energyLevel.getValue(), 20, 5, 5, 86, 80, sRatio, wRatio, hRatio, Color.WHITE, null);
		energyBar.widthProperty().bind(energyLevel.multiply(168 * wRatio));
		energyBar.setFill(energyGrad);

		Rectangle panel = Tools.createRoundedRectangle(190, 25, 5, 5, 80, 40, sRatio, wRatio, hRatio, Color.DARKGRAY.darker().darker(), largeShade);

		this.getChildren().addAll(name, weapons, energy, shieldEmpty, shieldBar, energyEmpty, energyBar, weaponDisplay, energyDisplay, shieldDisplay, shield, profileView, screw1, screw2, screw3, screw4, panel, playerRank, playerGames, opener, wDescription);

		Image background = Tools.createImage("background.jpg");
		BackgroundImage back = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				new BackgroundSize(width, height, false, false, false, false));
		this.setBackground(new Background(back));
		this.setEffect(largeShade);
	}
}
