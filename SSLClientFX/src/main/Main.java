package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

/**
 * @author lbsilva
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        System.setProperty("javax.net.ssl.trustStore", "trustStore" + File.separatorChar + "truststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "storePassword");
        Scene scene = new Scene(new MainWindow(primaryStage));
        scene.getStylesheets().add(getClass().getResource(".." + File.separator + "styles" + File.separator + "Style.css").toExternalForm());
        primaryStage.setTitle("FileSender");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(".." + File.separator + "images" + File.separator + "logo.png")));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
