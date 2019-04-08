package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {
    @FXML
    Button fileBtn;

    @FXML
    Label errorLabel;

    private Desktop desktop = Desktop.getDesktop();
    private final FileChooser fileChooser = new FileChooser();

    void init(){
        fileBtn.setOnAction(
                e -> {
                    System.out.println("aaa");
                    File file = fileChooser.showOpenDialog(fileBtn.getScene().getWindow());
                    if (file != null) {
                        openFile(file);
                    }
                });
    }

    private void openFile(File file) {

        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                    Controller.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }
}
