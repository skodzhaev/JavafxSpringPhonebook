package fxClasses;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        Parent root = SpringFXMLLoader.load("/main.fxml");
        Scene scene = new Scene(root, 620, 500);
        primaryStage.setTitle("Phonebook");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
