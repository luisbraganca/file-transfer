/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.FileReception;
import net.HandshakeException;
import sun.nio.ch.Net;

import java.io.File;
import java.io.IOException;

/**
 * @author lbsilva
 */
public class ReceiveNetPane extends NetPane {

    private FileReception fileReception;

    public ReceiveNetPane(Stage primaryStage) {
        super(primaryStage);
    }

    @Override
    protected void createDoButton() {
        doButton = new Button("Receive...");
        doButton.setOnAction(event -> {
            if (pathTextField.getText().trim().isEmpty()) {
                new AlertWindow("Error", "File path can't be empty").show();
            } else {
                new ReceiveWindow().show();
            }
        });
    }

    @Override
    protected String getFilePath(FileChooser fc, Stage stage) {
        return fc.showSaveDialog(stage).getAbsolutePath();
    }

    private class ReceiveWindow extends Stage {

        private Label id;
        private Button receiveButton;

        public ReceiveWindow() {
            super();
            createIDTextField();
            ready();
            createCancelButton();
            createWindow();
        }

        private void ready() {
            new Thread(() -> {
                try {
                    try {
                        fileReception = new FileReception(pathTextField.getText());
                    } catch (IOException | HandshakeException e) {
                        Platform.runLater(() -> {
                            new AlertWindow("Error", "Some unknown error ocurred.").show();
                            e.printStackTrace();
                            cancel();
                        });
                    }
                    Platform.runLater(() -> id.setText("Your ID is: " + fileReception.getId()));
                    fileReception.start();
                    Platform.runLater(() -> {
                        new AlertWindow("Finished", fileReception.getStatus()).show();
                        close();
                    });
                } catch (Exception ex) {
                    Platform.runLater(() -> {
                        new AlertWindow("Error", ex.getMessage()).show();
                        ex.printStackTrace();
                        cancel();
                    });
                }
            }).start();
        }

        private void createIDTextField() {
            id = new Label();
            id.setText("Retreiving ID from server...");
        }

        private void cancel() {
            try {
                fileReception.closeConnection();
            } catch (Exception ignored) {
            }
            fileReception = null;
            close();
        }

        private void createCancelButton() {
            receiveButton = new Button("Cancel");
            receiveButton.setOnAction(event -> {
                cancel();
            });
        }

        private void createWindow() {
            VBox root = new VBox();
            root.getChildren().addAll(id, receiveButton);
            root.setPadding(new Insets(NetPane.GENERAL_PADDING));
            root.setSpacing(NetPane.GENERAL_SPACING);
            root.setAlignment(Pos.CENTER);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource(".." + File.separator + "styles" + File.separator + "Style.css").toExternalForm());
            setScene(scene);
            setResizable(false);
            initModality(Modality.APPLICATION_MODAL);
            getIcons().add(new Image(getClass().getResourceAsStream(".." + File.separator + "images" + File.separator + "logo.png")));
            setTitle("FileSender - Receive");
        }
    }
}
