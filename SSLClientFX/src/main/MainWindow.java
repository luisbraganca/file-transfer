/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author lbsilva
 */
public class MainWindow extends BorderPane {

    private Stage primaryStage;

    public MainWindow(Stage primaryStage) {
        this.primaryStage = primaryStage;
        TabPane tabPane = new TabPane();
        Tab aboutTab = new Tab("About", new AboutPane(primaryStage));
        aboutTab.setClosable(false);
        Tab receiveTab = new Tab("Receive", new ReceiveNetPane(primaryStage));
        receiveTab.setClosable(false);
        Tab sendTab = new Tab("Send", new SendNetPane(primaryStage));
        sendTab.setClosable(false);
        tabPane.getTabs().addAll(aboutTab, sendTab, receiveTab);
        setCenter(tabPane);
    }
}
