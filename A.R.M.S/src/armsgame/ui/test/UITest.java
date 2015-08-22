package armsgame.ui.test;

import java.awt.Dimension;
import java.awt.DisplayMode;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

public class UITest extends Application {

	private static final double ANCHOR = 10.0;
	private static final double SHIFT_RATE = .25;

	public static void main(String[] args) {
		launch(args);
	}

	public boolean setting = false;
	private final Scale big = new Scale();
	private final Translate pivot = new Translate(0, 0, 0);
	private final PerspectiveCamera cameraView = new PerspectiveCamera();
	private final DisplayMode defaultMode = getLocalGraphicsEnvironment().getDefaultScreenDevice()
			.getDisplayMode();
	private final Dimension displayRes = new Dimension(defaultMode.getWidth(), defaultMode.getHeight());
	private final Rotate cameraRX = new Rotate(-20, displayRes.getWidth() / 2, displayRes.getHeight() / 2, 0, Rotate.X_AXIS);
	private final Rotate cameraRY = new Rotate(0, displayRes.getWidth() / 2, displayRes.getHeight() / 2, 0, Rotate.Y_AXIS);
	private final Timeline enlarge = new Timeline();

	public UITest() {

	}

	public void popUp(Parent button) {
		if (enlarge.getStatus() == Status.RUNNING) {
			return;
		}
		if (!setting) {

			enlarge.playFromStart();
		} else {
			enlarge.setRate(-1);
			enlarge.jumpTo("end");
			enlarge.play();
		}

		setting = !setting;

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		init(primaryStage);
	}

	private void init(Stage primaryStage) {
		primaryStage.setFullScreen(true);
		primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		AnchorPane root = new AnchorPane();
		Rectangle rect = new Rectangle(300, 200);

		rect.setFill(Color.ALICEBLUE.darker());
		rect.setStyle(null);
		Label t = new Label("Monopoly!");
		t.setId("label1");
		InnerShadow is = new InnerShadow();
		is.setOffsetX(4.0);
		is.setOffsetY(4.0);

		GaussianBlur br = new GaussianBlur();
		br.setRadius(5);

		is.setInput(br);
		t.setEffect(is);

		GaussianBlur br3 = new GaussianBlur();
		br3.setRadius(5.0);
		rect.setEffect(br3);

		StackPane button = new StackPane();
		button.getChildren()
				.addAll(rect, t);

		AnchorPane.setBottomAnchor(button, ANCHOR);
		AnchorPane.setRightAnchor(button, ANCHOR);
		root.getChildren()
				.add(button);

		button.setOnMouseClicked(e -> popUp(button));
		Scene scene = new Scene(root, displayRes.getWidth(), displayRes.getHeight(), true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.WHITE);

		scene.getStylesheets()
				.add("/monopolycards/ui/test/ui.css");

		primaryStage.setScene(scene);

		scene.setCamera(cameraView);
		primaryStage.show();

		button.getTransforms()
				.addAll(pivot, big);

		double finalWidth = displayRes.getWidth() - ANCHOR * 2;
		double finalHeight = displayRes.getHeight() - ANCHOR * 2;

		// Timeline

		// position one
		enlarge.getKeyFrames()
				.add(new KeyFrame(Duration.ZERO, new KeyValue(big.yProperty(), 1.0), new KeyValue(t.visibleProperty(), true),
						new KeyValue(pivot.yProperty(), 0), new KeyValue(br3.radiusProperty(), 5.0)));
		enlarge.getKeyFrames()
				.add(new KeyFrame(Duration.ZERO, new KeyValue(big.xProperty(), 1.0), new KeyValue(pivot.xProperty(), 0)));

		// Text set position
		enlarge.getKeyFrames()
				.add(new KeyFrame(Duration.seconds(0.001), new KeyValue(t.visibleProperty(), false)));

		// slight delay at end of transform (modifies speed)
		enlarge.getKeyFrames()
				.add(new KeyFrame(Duration.seconds(.2), new KeyValue(big.yProperty(), finalHeight / button.getHeight() - SHIFT_RATE),
						new KeyValue(pivot.yProperty(), -finalHeight + button.getHeight() + button.getHeight() * SHIFT_RATE)));
		enlarge.getKeyFrames()
				.add(new KeyFrame(Duration.seconds(.2), new KeyValue(big.xProperty(), finalWidth / button.getWidth() - SHIFT_RATE),
						new KeyValue(pivot.xProperty(), -finalWidth + button.getWidth() + button.getWidth() * SHIFT_RATE)));

		// position two
		enlarge.getKeyFrames()
				.add(new KeyFrame(Duration.seconds(.3), new KeyValue(big.xProperty(), finalWidth / button.getWidth()),
						new KeyValue(pivot.xProperty(), -finalWidth + button.getWidth())));
		enlarge.getKeyFrames()
				.add(new KeyFrame(Duration.seconds(.3), new KeyValue(big.yProperty(), finalHeight / button.getHeight()),
						new KeyValue(pivot.yProperty(), -finalHeight + button.getHeight()),
						new KeyValue(br3.radiusProperty(), 5.0 * button.getHeight() / finalHeight)));

	}
}
