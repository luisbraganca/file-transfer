/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

/**
 *
 * @author lbsilva
 */
public class AlertWindow extends Stage {

    private final static int GENERAL_SPACING = 10;
    private final static int GENERAL_PADDING = 10;

    public AlertWindow(String title, String content) {
        VBox root = new VBox();
        Button okButton = new Button("Ok");
        okButton.setOnAction(event -> close());
        setTitle("FileSender - " + title);
        setResizable(false);
        setIconified(false);
        setAlwaysOnTop(true);
        initModality(Modality.APPLICATION_MODAL);
        root.getChildren().addAll(new Label(content), okButton);
        root.setSpacing(GENERAL_SPACING);
        root.setPadding(new Insets(GENERAL_PADDING));
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(".." + File.separator + "styles" + File.separator + "Style.css").toExternalForm());
        getIcons().add(new Image(getClass().getResourceAsStream(".." + File.separator + "images" + File.separator + "logo.png")));
        setScene(scene);
    }
}
