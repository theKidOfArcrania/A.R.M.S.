package armsgame.ui.test;

import java.awt.Dimension;
import java.awt.DisplayMode;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

public class PlayerInfoTest extends Application {

	private static final double ANCHOR = 10.0;
	//test

	public static void main(String[] args) {
		launch(args);
	}

	private final PerspectiveCamera cameraView = new PerspectiveCamera();
	private final DisplayMode defaultMode = getLocalGraphicsEnvironment().getDefaultScreenDevice()
			.getDisplayMode();
	private final Dimension displayRes = new Dimension(defaultMode.getWidth(), defaultMode.getHeight());
	public final double dispWidth = displayRes.getWidth();
	public final double dispHeight = displayRes.getHeight();
	public double wRatio = dispWidth/1600;
	public double hRatio = dispHeight/900;
	//user variables
	private SimpleIntegerProperty moneyValue;
	private SimpleStringProperty playerName;
	private final SimpleDoubleProperty alph;
	private SimpleIntegerProperty setNumber;


	private final Timeline solid = new Timeline();

	public PlayerInfoTest() {
		alph = new SimpleDoubleProperty(.4);
	}

	public ImageView createScrew(Effect g)
	{
		Image turnoff = new Image((PlayerInfoTest.class.getResource("screw.png").toExternalForm()));
		ImageView lightoff = new ImageView();
		lightoff.setImage(turnoff);
		lightoff.setFitHeight(15);
		lightoff.setFitWidth(15);
		lightoff.setPreserveRatio(true);
		lightoff.setSmooth(true);
		lightoff.setCache(true);
		lightoff.setEffect(g);

		return lightoff;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		init(primaryStage);
	}
	public void trans(boolean status){
		if (solid.getStatus() == Status.RUNNING) {
			return;
		}
		if (status) {

			solid.playFromStart();
		} else {
			solid.setRate(-1);
			solid.jumpTo("end");
			solid.play();
		}

	}
	private void init(Stage primaryStage) {

		primaryStage.initStyle(StageStyle.TRANSPARENT);

		AnchorPane root = new AnchorPane();

		InnerShadow smallShade = new InnerShadow();
		smallShade.setRadius(2.0);

		InnerShadow mediumShade = new InnerShadow();
		mediumShade.setRadius(3.0);

		InnerShadow largeShade = new InnerShadow();
		mediumShade.setRadius(5.0);

		InnerShadow greenShade = new InnerShadow();
		greenShade.setRadius(2);
		greenShade.setColor(Color.DARKGREEN.darker());

		DropShadow out = new DropShadow();
		out.setRadius(2.0);


		Text name = new Text("MyUsername");
		//name.textProperty().bind(playerName);
		name.setFill(Color.GRAY);
		name.setFont(Font.loadFont(PlayerInfoTest.class.getResource("Xolonium-Bold.otf").toExternalForm(), 24));
		AnchorPane.setTopAnchor(name, 15.0);
		AnchorPane.setLeftAnchor(name,80.0);
		name.setEffect(smallShade);

		Text money = new Text("Money:");
		money.setFill(Color.GREEN.brighter());
		money.setFont(Font.loadFont(PlayerInfoTest.class.getResource("Xolonium-Regular.otf").toExternalForm(), 16));
		AnchorPane.setTopAnchor(money, 85.0);
		AnchorPane.setLeftAnchor(money, 15.0);
		money.setEffect(greenShade);

		Text moneyDisplay = new Text("$1000M");
		//moneyDisplay.textProperty().bind(new SimpleStringProperty(moneyValue.getValue()+""));
		moneyDisplay.setFill(Color.GRAY);
		moneyDisplay.setFont(Font.loadFont(PlayerInfoTest.class.getResource("Xolonium-Regular.otf").toExternalForm(), 16));
		AnchorPane.setTopAnchor(moneyDisplay, 85.0);
		AnchorPane.setLeftAnchor(moneyDisplay, 85.0);
		moneyDisplay.setEffect(smallShade);

		Text properties = new Text("Full Set Count:");
		properties.setFill(Color.GREEN.brighter());
		properties.setFont(Font.loadFont(PlayerInfoTest.class.getResource("Xolonium-Regular.otf").toExternalForm(), 16));
		AnchorPane.setTopAnchor(properties, 112.0);
		AnchorPane.setLeftAnchor(properties, 15.0);
		properties.setEffect(greenShade);

		Text setDisplay = new Text("0 Sets");
		//setDisplay.textProperty().bind(new SimpleStringProperty(setNumber.getValue()+""));
		setDisplay.setFill(Color.GRAY);
		setDisplay.setFont(Font.loadFont(PlayerInfoTest.class.getResource("Xolonium-Regular.otf").toExternalForm(), 16));
		AnchorPane.setTopAnchor(setDisplay, 112.0);
		AnchorPane.setLeftAnchor(setDisplay, 150.0);
		setDisplay.setEffect(smallShade);

		Image profileImage = new Image((PlayerInfoTest.class.getResource("blank-profile.jpg").toExternalForm()));
		ImageView profileView = new ImageView();
		profileView.setImage(profileImage);
		profileView.setFitHeight(60);
		profileView.setFitWidth(60);
		profileView.setSmooth(true);
		profileView.setCache(true);
		profileView.setEffect(smallShade);
		AnchorPane.setTopAnchor(profileView, 15.0);
		AnchorPane.setLeftAnchor(profileView, 10.0);




		//moneypic
		/*Image moneyPic = new Image((PlayerInfoTest.class.getResource("Dollar Sign.png").toExternalForm()));
		ImageView moneyView = new ImageView();
		moneyView.setImage(moneyPic);
		moneyView.setFitHeight(20);
		moneyView.setFitWidth(20);
		moneyView.setPreserveRatio(true);
        moneyView.setSmooth(true);
        moneyView.setCache(true);
        AnchorPane.setTopAnchor(moneyView, 65.0);
        AnchorPane.setLeftAnchor(moneyView, 90.0);
        moneyView.setEffect(shader);

        //fullsets pic
        Image cardPic = new Image((PlayerInfoTest.class.getResource("fullset.png").toExternalForm()));
        ImageView cardView = new ImageView();
		cardView.setImage(cardPic);
		cardView.setFitHeight(50);
		cardView.setFitWidth(50);
		cardView.setPreserveRatio(true);
        cardView.setSmooth(true);
        cardView.setCache(true);

        cardView.setEffect(shader);
        AnchorPane.setTopAnchor(cardView, 85.0);
        AnchorPane.setLeftAnchor(cardView, 15.0);*/

		Scene scene = new Scene(root, dispWidth/5.5, dispHeight/6);

		scene.setFill(Color.color(.85, .85, .85,.4));

		ImageView nut1 = createScrew(out);
		AnchorPane.setTopAnchor(nut1, 0.0);
		AnchorPane.setLeftAnchor(nut1, 0.0);

		ImageView nut2 = createScrew(out);
		AnchorPane.setTopAnchor(nut2, 0.0);
		AnchorPane.setLeftAnchor(nut2, scene.getWidth()-21);

		ImageView nut3 = createScrew(out);
		AnchorPane.setTopAnchor(nut3, scene.getHeight()-20);
		AnchorPane.setLeftAnchor(nut3, 0.0);

		ImageView nut4 = createScrew(out);
		AnchorPane.setTopAnchor(nut4, scene.getHeight()-20);
		AnchorPane.setLeftAnchor(nut4, scene.getWidth()-21);

		Rectangle panel = new Rectangle(scene.getWidth()-100, 25);
		panel.setArcHeight(5.0);
		panel.setArcWidth(5.0);
		AnchorPane.setTopAnchor(panel, 50.0);
		AnchorPane.setLeftAnchor(panel,80.0);
		panel.setFill(Color.DARKGRAY.darker().darker());
		panel.setEffect(largeShade);


		Text playerRank = new Text("Rank:");
		playerRank.setFill(Color.WHITE);
		playerRank.setFont(Font.loadFont(PlayerInfoTest.class.getResource("Xolonium-Regular.otf").toExternalForm(), 10));
		AnchorPane.setTopAnchor(playerRank, 57.0);
		AnchorPane.setLeftAnchor(playerRank,85.0);
		playerRank.setEffect(out);

		Text playerGames = new Text("Games:");
		playerGames.setFill(Color.WHITE);
		playerGames.setFont(Font.loadFont(PlayerInfoTest.class.getResource("Xolonium-Regular.otf").toExternalForm(), 10));
		AnchorPane.setTopAnchor(playerGames, 57.0);
		AnchorPane.setRightAnchor(playerGames,50.0);
		playerGames.setEffect(out);

		root.getChildren().addAll(name, properties, money, moneyDisplay, setDisplay,  profileView, nut1
				,nut2, nut3,nut4, panel, playerRank, playerGames);
		root.opacityProperty().bind(alph);

		scene.setOnMouseEntered(e->trans(true));
		scene.setOnMouseExited(e->trans(false));
		//white background
		AnchorPane secLayout = new AnchorPane();
		Stage secondStage = new Stage();
		secondStage.initStyle(StageStyle.TRANSPARENT);
		secondStage.setScene(new Scene(secLayout, dispWidth,dispHeight));
		//secondStage.show();
		root.setId("ROOTNODE");

		scene.getStylesheets().add("/monopolycards/ui/test/border.css");
		primaryStage.setX(dispWidth/2-scene.getWidth()/2);
		primaryStage.setY(0);

		primaryStage.setScene(scene);
		scene.setCamera(cameraView);
		primaryStage.show();



		// Timeline

		//start
		solid.getKeyFrames()
		.add(new KeyFrame(Duration.ZERO, new KeyValue(alph, .4),
				new KeyValue(scene.fillProperty(), Color.color(.85, .85, .85,.4))));

		solid.getKeyFrames()
		.add(new KeyFrame(Duration.seconds(.05), new KeyValue(alph, .6),
				new KeyValue(scene.fillProperty(), Color.color(.85, .85, .85,.6))));

		solid.getKeyFrames()
		.add(new KeyFrame(Duration.seconds(.1), new KeyValue(alph, .8),
				new KeyValue(scene.fillProperty(), Color.color(.85, .85, .85,.8))));
		solid.getKeyFrames()
		.add(new KeyFrame(Duration.seconds(.15), new KeyValue(alph, 1.0),
				new KeyValue(scene.fillProperty(), Color.color(.85, .85, .85,1.0))));
	}
}
