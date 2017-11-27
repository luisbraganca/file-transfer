/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.FileSend;
import net.HandshakeException;
import net.UnreachableHostException;

import java.io.File;

/**
 * @author lbsilva
 */
public class SendNetPane extends NetPane {

    public SendNetPane(Stage primaryStage) {
        super(primaryStage);
    }

    @Override
    protected void createDoButton() {
        doButton = new Button("Send...");
        doButton.setOnAction(event -> {
            if (pathTextField.getText().trim().isEmpty()) {
                new AlertWindow("Error", "File path can't be empty").show();
            } else {
                new SendWindow().show();
            }
        });
    }

    @Override
    protected String getFilePath(FileChooser fc, Stage stage) {
        return fc.showOpenDialog(stage).getAbsolutePath();
    }

    private class SendWindow extends Stage {

        private TextField id;
        private Button sendButton;
        private FileSend fileSend;

        public SendWindow() {
            super();
            createIPTextField();
            createSendButton();
            createWindow();
        }

        private void createIPTextField() {
            id = new TextField();
            addTextFieldCharLimit(id, 4);
            id.setPrefColumnCount(4);
        }

        private void addTextFieldCharLimit(TextField textField, int maxChars) {
            textField.textProperty().addListener((ObservableValue<? extends String> ov, String oldValue, String newValue) -> {
                if (textField.getText().length() > maxChars) {
                    String filter = textField.getText().substring(0, maxChars);
                    textField.setText(filter);
                }
            });
        }

        private void send() {
            try {
                fileSend = new FileSend(pathTextField.getText(), id.getText());
                fileSend.start();
                new AlertWindow("Finished", fileSend.getStatus()).show();
                close();
            } catch (UnreachableHostException | HandshakeException ex) {
                new AlertWindow("Error", "Server down?").show();
                cancel();
            } catch (Exception ex) {
                new AlertWindow("Error", ex.getMessage()).show();
                cancel();
            }
        }

        private void createSendButton() {
            sendButton = new Button("Send");
            sendButton.setOnAction(event -> {
                send();
            });
        }

        private void cancel() {
            if (fileSend != null) fileSend.closeConnection();
            fileSend = null;
            close();
        }

        private void createWindow() {
            VBox root = new VBox();
            Label label = new Label("Enter the destination ID.");
            root.getChildren().addAll(label, id, sendButton);
            root.setPadding(new Insets(NetPane.GENERAL_PADDING));
            root.setSpacing(NetPane.GENERAL_SPACING);
            root.setAlignment(Pos.CENTER);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource(".." + File.separator + "styles" + File.separator + "Style.css").toExternalForm());
            setScene(scene);
            setResizable(false);
            initModality(Modality.APPLICATION_MODAL);
            getIcons().add(new Image(getClass().getResourceAsStream(".." + File.separator + "images" + File.separator + "logo.png")));
            setTitle("FileSend - Send");
        }
    }
}
