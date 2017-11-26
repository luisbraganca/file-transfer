/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.LastVisitedDirectory;

import java.io.File;

/**
 * @author lbsilva
 */
public abstract class NetPane extends VBox {

    public final static int GENERAL_SPACING = 30;
    public final static int GENERAL_PADDING = 30;

    private Button selectButton;

    protected TextField pathTextField;
    protected Button doButton;

    protected Stage primaryStage;

    public NetPane(Stage primaryStage) {
        super();
        this.primaryStage = primaryStage;
        createPathTextField();
        createSelectButton();
        createDoButton();
        createWindow();
    }

    protected abstract void createDoButton();

    private void createPathTextField() {
        pathTextField = new TextField();
        pathTextField.setPrefWidth(250);
        pathTextField.setPromptText("File path...");
    }

    protected void createSelectButton() {
        selectButton = new Button("Select...");
        selectButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select file");
            if (LastVisitedDirectory.getInstance().wasVisited()) {
                fileChooser.setInitialDirectory(new File(LastVisitedDirectory.getInstance().getPath()));
            }
            try {
                String newPath = getFilePath(fileChooser, primaryStage);
                LastVisitedDirectory.getInstance().setLastVisitedDirectory(newPath);
                pathTextField.setText(newPath);
            } catch (NullPointerException e) {
                System.out.println("Canceled");
            }
        });
    }

    protected abstract String getFilePath(FileChooser fc, Stage stage);

    private void createWindow() {
        HBox path = new HBox();
        path.getChildren().addAll(pathTextField, selectButton);
        path.setSpacing(GENERAL_SPACING);
        getChildren().addAll(path, doButton);
        setSpacing(GENERAL_SPACING);
        setPadding(new Insets(GENERAL_PADDING));
    }
}
