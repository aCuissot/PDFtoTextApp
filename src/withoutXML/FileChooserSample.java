package withoutXML;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.imageio.ImageIO;


public final class FileChooserSample extends Application {

    private Desktop desktop = Desktop.getDesktop();
    private TextField directory;
    private Label resultLabel;
    @Override
    public void start(final Stage stage) {
        stage.setTitle("File Chooser Sample");

        final FileChooser fileChooser = new FileChooser();

        final Button openButton = new Button("Open a PDF...");
        final Button openMultipleButton = new Button("Open PDFs...");

        directory = new TextField();
        resultLabel = new Label();
        directory.setPromptText("Repertoire de destination");
        openButton.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                openFile(file);
            }
        });

        openMultipleButton.setOnAction(e -> {
            List<File> list = fileChooser.showOpenMultipleDialog(stage);
            if (list != null) {
                for (File file : list) {
                    openFile(file);
                }
            }
        });



        final GridPane inputGridPane = new GridPane();

        GridPane.setConstraints(openButton, 0, 1);
        GridPane.setConstraints(openMultipleButton, 1, 1);
        GridPane.setConstraints(directory, 0, 0);
        GridPane.setConstraints(resultLabel, 0, 2);

        inputGridPane.setHgap(6);
        inputGridPane.setVgap(6);
        inputGridPane.getChildren().addAll(openButton, openMultipleButton, directory, resultLabel);

        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(inputGridPane);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));

        stage.setScene(new Scene(rootGroup));
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    private void openFile(File file) {
        try {
            PDDocument document = PDDocument.load(file);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);
            String fileName = "";
//            if (directory.getText().trim().equals("")){
//                fileName = "Documents/PDFDefault/";
//            } else {
//                fileName = directory.getText();
//            }

            if (directory.getText().trim().equals("")) {
                int i = 0;
                File theDir = new File(System.getProperty("user.home") + "/" + "Documents/PDFDefault/" + i);

                while (theDir.exists()) {
                    ++i;
                    theDir = new File(System.getProperty("user.home") + "/" + "Documents/PDFDefault/" + i);
                }
                System.out.println("creating directory: " + theDir.getName());
                boolean result = false;

                try {
                    theDir.mkdir();
                    result = true;
                    fileName = theDir.getAbsolutePath();
                } catch (SecurityException se) {
                    resultLabel.setTextFill(Color.rgb(200, 30, 30));
                    resultLabel.setText("Error creating dir");
                    System.out.println("Error creating dir");
                }
                if (result) {
                    resultLabel.setTextFill(Color.rgb(30, 220, 30));
                    resultLabel.setText("dir created");
                    System.out.println("dir created");
                }

            } else {
                boolean result = false;
                try {
                    File theDir = new File(System.getProperty("user.home") + "/" + directory.getText());
                    theDir.mkdir();
                    result = true;
                    fileName = theDir.getAbsolutePath();
                } catch (SecurityException se) {
                    resultLabel.setTextFill(Color.rgb(200, 30, 30));
                    resultLabel.setText("Error creating dir");
                    System.out.println("Error creating dir");

                }
                if (result) {
                    resultLabel.setTextFill(Color.rgb(30, 220, 30));
                    resultLabel.setText("dir created");
                    System.out.println("dir created");

                }
            }

            FileWriter fileWriter = new FileWriter(fileName + "/text.txt");
            fileWriter.write(text);
            fileWriter.close();
            int image = 0;
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage bufferedImage = renderer.renderImage(0);
            ImageIO.write(bufferedImage, "PNG", new File(fileName + "/img" + image + ".png"));
            image++;
        document.close();
        } catch (IOException ex) {
//            Logger.getLogger(
//                    FileChooserSample.class.getName()).log(
//                    Level.SEVERE, null, ex
//            );
            resultLabel.setTextFill(Color.rgb(200, 30, 30));
            resultLabel.setText(ex.toString());
        }
        resultLabel.setTextFill(Color.rgb(20, 20, 20));
        resultLabel.setText("Done");
    }
}