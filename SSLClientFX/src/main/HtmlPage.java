/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.File;

/**
 *
 * @author lbsilva
 */
public class HtmlPage extends Stage
{

    public HtmlPage(String link) {
        VBox root = new VBox();
        Scene scene = new Scene(root);
        setTitle("FileSender - Page");
        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();
        webEngine.load(link);
        root.getChildren().add(browser);
        VBox.setVgrow(browser, Priority.ALWAYS);
        getIcons().add(new Image(getClass().getResourceAsStream(".." + File.separator + "images" + File.separator + "logo.png")));
        setScene(scene);
        setMaximized(true);
    }

}
