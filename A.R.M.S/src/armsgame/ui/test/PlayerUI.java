package armsgame.ui.test;

import java.awt.Dimension;
import java.awt.DisplayMode;

import armsgame.impl.Player;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

public class PlayerUI extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	private final PerspectiveCamera cameraView = new PerspectiveCamera();
	private final DisplayMode defaultMode = getLocalGraphicsEnvironment().getDefaultScreenDevice()
			.getDisplayMode();
	private final Dimension displayRes = new Dimension(defaultMode.getWidth(), defaultMode.getHeight());
	private final Rotate cameraRX = new Rotate(-20, displayRes.getWidth() / 2, displayRes.getHeight() / 2, 0, Rotate.X_AXIS);
	private final Rotate cameraRY = new Rotate(0, displayRes.getWidth() / 2, displayRes.getHeight() / 2, 0, Rotate.Y_AXIS);

	private Player viewer;

	public PlayerUI() {
		if (viewer == null) {
			// throw new NullPointerException();
		}
	}

	public Player getViewer() {
		return viewer;
	}

	public void setViewer(Player viewer) {
		if (viewer == null) {
			throw new NullPointerException();
		}
		this.viewer = viewer;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		init(primaryStage);
	}

	private void init(Stage primaryStage) {
		Group root = new Group();
		Scene scene = new Scene(root, displayRes.getWidth(), displayRes.getHeight(), true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.GRAY);
		primaryStage.setScene(scene);
		cameraView.getTransforms()
				.addAll(cameraRX, cameraRY);
		scene.setCamera(cameraView);
		primaryStage.show();

		Box board = new Box(displayRes.getWidth(), displayRes.getHeight(), 100.0);

	}
}
