package armsgame.ui.test;

import javafx.application.Application;
import javafx.scene.layout.AnchorPane;

public class BluePrintTest extends AnchorPane {
	public static void main(String[] args) {
		// This opens the window in the middle.
		TestRun.setTestView(BluePrintTest::new);
		Application.launch(TestRun.class);
	}

	// In the future, if we need to supply arguments, ie Player objects then change this constructor.
	public BluePrintTest() {
		init();
	}

	public void init() {
		// set the window dimensions by using this.setWidth(int) and this.setHeight(int).
		this.setWidth(100);
		this.setHeight(100);

		// initialization procedure right here!!!
	}
}
