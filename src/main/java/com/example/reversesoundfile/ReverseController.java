package com.example.reversesoundfile;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Stack;

public class ReverseController implements Initializable {
    @FXML
    private Label sampleLabel;
    @FXML
    private Label warningLabel;
    @FXML
    private Label doneLabel;
    private String firstLine;
    private String secondLine;
    private Stack<Double> timeStackReverse;
    private Stack<Double> soundStack;
    private Scanner scnr;
    private boolean fileUploaded = false;

    FileChooser fileChooser = new FileChooser();

    public void uploadFile(ActionEvent e) {
        File file = fileChooser.showOpenDialog(new Stage());
        try {
            scnr = new Scanner(file);
            createSoundFile(scnr);

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    public void reverseAndSave(ActionEvent e) throws FileNotFoundException {
        if (fileUploaded) {
            File outputFile = fileChooser.showSaveDialog(new Stage());
            PrintStream output = new PrintStream(outputFile);
            output.println(firstLine);
            output.println(secondLine);

            double timeData;
            double soundData;
            Stack<Double> timeStack = new Stack<>();

            while (!timeStackReverse.isEmpty()) {
                timeData = timeStackReverse.peek();
                timeStack.push(timeData);
                timeStackReverse.pop();
            }

            while (!(timeStack.isEmpty() && soundStack.isEmpty())) {
                timeData = timeStack.peek();
                soundData = soundStack.peek();
                output.printf("  %14.8g\t%10.6g\n", timeData, soundData);
                timeStack.pop();
                soundStack.pop();
            }

            doneLabel.setText("Done!");

        } else {
            warningLabel.setText("You must upload a file first!");
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File("/"));
    }

    private void createSoundFile(Scanner infile) {

        firstLine = infile.nextLine();   // read in the first line
        secondLine = infile.nextLine();  // read in the second line

        timeStackReverse = new Stack<>();
        soundStack = new Stack<>();
        double timeDataReverse;
        double soundData;
        int sampleCount = 0;

        while (infile.hasNextLine()) {
            timeDataReverse = infile.nextDouble();
            timeStackReverse.push(timeDataReverse);
            soundData = infile.nextDouble();
            soundStack.push(soundData);
            ++sampleCount;
            infile.nextLine();
        }

        warningLabel.setText("");
        sampleLabel.setText("There were " + sampleCount + " samples in the file.");
        fileUploaded = true;
    }
}