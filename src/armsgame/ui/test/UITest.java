package armsgame.ui.test;

import java.awt.Dimension;
import java.awt.DisplayMode;

import armsgame.card.CardDefaults;
import armsgame.impl.Player;
import armsgame.res.Tools;
import armsgame.ui.PlayerInfo;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

public class UITest extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	private final DisplayMode defaultMode = getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
	private final Dimension displayRes = new Dimension(defaultMode.getWidth(), defaultMode.getHeight());
	private final double dispWidth = displayRes.getWidth();
	private final double dispHeight = displayRes.getHeight();
	private final double wRatio = dispWidth/1600.0;
	private final double hRatio = dispHeight/900.0;

	public UITest() {

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		init(primaryStage);
	}

	private void init(Stage primaryStage) {
		primaryStage.setFullScreen(true);
		AnchorPane root = new AnchorPane();
		PlayerInfo info = new PlayerInfo(new Player());

		root.getChildren().add(info);

		Scene scene = new Scene(root, displayRes.getWidth(), displayRes.getHeight(), true, SceneAntialiasing.BALANCED);

		primaryStage.setScene(scene);
		primaryStage.show();

	}
}
