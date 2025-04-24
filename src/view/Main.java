package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create a label
        Label label = new Label("Hello, JavaFX!");

        // Create a button
        Button button = new Button("Click Me");
        button.setOnAction(e -> label.setText("Button Clicked!"));

        // Create a layout and add the label and button
        VBox layout = new VBox(10); // 10px spacing
        layout.getChildren().addAll(label, button);

        // Create a scene with the layout
        Scene scene = new Scene(layout, 300, 200);

        // Set up the stage
        primaryStage.setTitle("JavaFX Test Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}