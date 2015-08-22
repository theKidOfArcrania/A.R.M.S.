package armsgame.ui.test;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Group;
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
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

public class MoveCounter extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	private final PerspectiveCamera cameraView = new PerspectiveCamera();
	private final DisplayMode defaultMode = getLocalGraphicsEnvironment().getDefaultScreenDevice()
			.getDisplayMode();
	private final Dimension displayRes = new Dimension(defaultMode.getWidth(), defaultMode.getHeight());
	
	public final double dispWidth = displayRes.getWidth();
	public final double dispHeight = displayRes.getHeight();
	
	private static final double ANCHOR = 10.0;
	private static int count;
	private SimpleDoubleProperty alph;

	private final Timeline solid = new Timeline();
	
	public MoveCounter() {
		alph = new SimpleDoubleProperty(1.0);
		count = 3;
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		init(primaryStage);
	}

	private void init(Stage primaryStage) {
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		
		AnchorPane root = new AnchorPane();
		
		//shades
		InnerShadow smallShade = new InnerShadow();
		smallShade.setRadius(2.0);
		
		InnerShadow mediumShade = new InnerShadow();
		mediumShade.setRadius(3.0);
		
		InnerShadow largeShade = new InnerShadow();
		mediumShade.setRadius(5.0);
		
		DropShadow out = new DropShadow();
		out.setRadius(2.0);

		
		//lightholder
		Rectangle panel = new Rectangle(dispWidth/6-50, dispHeight/4-85);
		panel.setArcHeight(60.0);
		panel.setArcWidth(60.0);
		AnchorPane.setBottomAnchor(panel, 25.0);
		AnchorPane.setLeftAnchor(panel, 22.0);
		panel.setFill(Color.DARKGRAY.darker().darker());
		panel.setEffect(largeShade);
		
		//label
		Text name = new Text("Moves Left");
		name.setFill(Color.GRAY);
		name.setFont(Font.loadFont(PlayerInfoTest.class.getResource("Xolonium-Bold.otf").toExternalForm(), 30));
		name.setWrappingWidth(dispWidth/6);
		name.setTextAlignment(TextAlignment.CENTER);
		AnchorPane.setTopAnchor(name, 15.0);
		name.setEffect(smallShade);
		
		Scene scene = new Scene(root, dispWidth/6, dispHeight/4);
		//first light
		
		ArrayList<ImageView> lightList = new ArrayList<ImageView>();
		
		ImageView light1 = createLight(mediumShade);
        AnchorPane.setBottomAnchor(light1, panel.getHeight()/2);
        AnchorPane.setLeftAnchor(light1, panel.getWidth()/6);
        lightList.add(light1);
        
		ImageView light2 = createLight(mediumShade);
        AnchorPane.setBottomAnchor(light2, panel.getHeight()/2);
        AnchorPane.setLeftAnchor(light2, scene.getWidth()/2-light2.getFitHeight()/2-3);
        lightList.add(light2);
        
		ImageView light3 = createLight(mediumShade);
        AnchorPane.setBottomAnchor(light3, panel.getHeight()/2);
        AnchorPane.setRightAnchor(light3, panel.getWidth()/6);
        lightList.add(light3);
        
		ImageView light1off = createLightOff(largeShade);
        AnchorPane.setBottomAnchor(light1off, panel.getHeight()/2);
        AnchorPane.setLeftAnchor(light1off, panel.getWidth()/6);
        
        ImageView light2off = createLightOff(largeShade);
        AnchorPane.setBottomAnchor(light2off, panel.getHeight()/2);
        AnchorPane.setLeftAnchor(light2off, scene.getWidth()/2-light2.getFitHeight()/2-3);
        
        ImageView light3off = createLightOff(largeShade);
        AnchorPane.setBottomAnchor(light3off, panel.getHeight()/2);
        AnchorPane.setRightAnchor(light3off, panel.getWidth()/6);
        
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
		root.getChildren().addAll(name, panel, light1off, light2off, light3off, light1, 
				light2, light3,nut1,nut2,nut3,nut4);
		root.setOnMouseClicked(e->lightOff(lightList));
   
		
		root.setId("ROOTNODE");
		scene.getStylesheets().add("/monopolycards/ui/test/border.css");
		primaryStage.setX(0);
		primaryStage.setY(dispHeight - scene.getHeight());

		primaryStage.setScene(scene);
		scene.setCamera(cameraView);
		primaryStage.show();

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
	public void lightOff(ArrayList<ImageView> lightList)
	{
		count--;
		if(count<0)
		{
			resetMoves(lightList);
		}
		else
		{
			genAnimation(lightList.get(count));
		}
	}
	public void genAnimation(ImageView light)
	{
		
		Timeline anim = new Timeline();
		anim.getKeyFrames()
		.add(new KeyFrame(Duration.ZERO, new KeyValue(light.opacityProperty(), 1.0)));
		
		anim.getKeyFrames()
		.add(new KeyFrame(Duration.seconds(.05), new KeyValue(light.opacityProperty(), .75)));
		
		anim.getKeyFrames()
		.add(new KeyFrame(Duration.seconds(.1), new KeyValue(light.opacityProperty(), .5)));
		
		anim.getKeyFrames()
		.add(new KeyFrame(Duration.seconds(.15), new KeyValue(light.opacityProperty(), .25)));
		
		anim.getKeyFrames()
		.add(new KeyFrame(Duration.seconds(.2), new KeyValue(light.opacityProperty(), 0)));
		
		anim.playFromStart();
		
	}
	
	public void resetMoves(ArrayList<ImageView> lightList)
	{
		count = 3;
		for(ImageView light: lightList)
		{
			light.setOpacity(1.0);
		}
	}
	
	public ImageView createLight(Effect g)
	{
		Image turn1 = new Image((PlayerInfoTest.class.getResource("Greenlight.png").toExternalForm()));
		ImageView light1 = new ImageView();
		light1.setImage(turn1);
		light1.setFitHeight(dispHeight/16);
		light1.setFitWidth(dispWidth/16);
		light1.setPreserveRatio(true);
        light1.setSmooth(true);
        light1.setCache(true);
        light1.setEffect(g);

        
        return light1;

	}
	public ImageView createLightOff(Effect g)
	{
		Image turnoff = new Image((PlayerInfoTest.class.getResource("lightoff.png").toExternalForm()));
		ImageView lightoff = new ImageView();
		lightoff.setImage(turnoff);
		lightoff.setFitHeight(dispHeight/16);
		lightoff.setFitWidth(dispWidth/16);
		lightoff.setPreserveRatio(true);
        lightoff.setSmooth(true);
        lightoff.setCache(true);
        lightoff.setEffect(g);
        
        return lightoff;
	}
}