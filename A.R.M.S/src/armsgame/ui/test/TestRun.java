package armsgame.ui.test;

import java.util.function.Supplier;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TestRun extends Application {
	private static Supplier<Pane> testView = Pane::new;

	public static Supplier<Pane> getTestView() {
		return testView;
	}

	public static void main(String args[]) {
		launch(args);
	}

	public static void setTestView(Supplier<Pane> testView) {
		if (testView == null) {
			throw new NullPointerException();
		}
		TestRun.testView = testView;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Pane root = testView.get();
		Scene scene = new Scene(root, root.getWidth(), root.getHeight());

		scene.setFill(null);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setScene(scene);

		primaryStage.show();
	}

}
