package CPSC.Final.Copy;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class StarWarsApp extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Star Wars");
		
		List<String> imageNames = new ArrayList<String>();
		imageNames.add("starwars.png");
		imageNames.add("vader.png");
		imageNames.add("yoda.png");
		imageNames.add("skywalker.png");
		imageNames.add("chewbacca.png");
		imageNames.add("hansolo.png");
		imageNames.add("leia.png");
		imageNames.add("c-3po.png");
		imageNames.add("r2-d2.png");
		imageNames.add("obi-wan.png");
		imageNames.add("padme.png");
		imageNames.add("finn.png");
		imageNames.add("rey.png");
		
		List<ImageView> images = new ArrayList<ImageView>();
		
		for(String s : imageNames)
			images.add(new ImageView(getClass().getResource(s).toExternalForm()));
		
		List<StackPane> stacks = new ArrayList<StackPane>();
		
		for(ImageView i : images) {
			i.setFitWidth(120);
			i.setPreserveRatio(true);
			stacks.add(new StackPane(i));
		}
		
		Label fileLabel = new Label("Filename:");
		Label fileName = new Label(imageNames.get(0)); 
		stacks.get(0).setBackground(new Background(new BackgroundFill( Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY )));
		
		for(StackPane i : stacks)
			i.setPadding(new Insets(5));
		
		HBox centerBox = new HBox();
		HBox bottomBox = new HBox();
		
		centerBox.getChildren().addAll(stacks);
		centerBox.setBackground( new Background(new BackgroundFill( Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY )));
		
		bottomBox.getChildren().add(fileLabel);
		bottomBox.getChildren().add(fileName);
		
		ScrollPane scroll = new ScrollPane();
		scroll.setContent(centerBox);
		
		scroll.hvalueProperty().addListener( (o,old,now) -> {
			int index = (int)Math.floor( now.doubleValue() * (imageNames.size() - 1));
			fileName.setText("" + imageNames.get(index));
			stacks.get(index).setBackground(new Background(new BackgroundFill( Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY )));
			for(int i = 0; i < imageNames.size(); i++) {
				if(i != index)
					stacks.get(i).setBackground(new Background(new BackgroundFill( Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY )));
			}
		});
		
		BorderPane root = new BorderPane(); 
		
		root.setCenter(scroll);
		root.setBottom(bottomBox);
		
		Scene scene = new Scene(root, 400, 280);
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String [] args) {
		launch(args);
	}
}
