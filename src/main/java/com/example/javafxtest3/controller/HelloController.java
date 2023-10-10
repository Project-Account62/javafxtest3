package com.example.javafxtest3.controller;

import com.example.javafxtest3.controller.ActualController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HelloController {

    @FXML
    private TextField numLift;
    @FXML
    private TextField numFloor;
    @FXML
    private Button button;
    @FXML
    private Label label;
    @FXML
    private TextField movingTime;
    @FXML
    private TextField waitingTime;

    @FXML
    protected void onGoButtonClick() {
        try {
            ActualController.numLifts = Integer.parseInt(numLift.getText());
            ActualController.numFloors = Integer.parseInt(numFloor.getText());
            if (ActualController.numLifts <= 0 || ActualController.numFloors <= 0) {
                label.setText("Input positive integers only!");
            }
            if (movingTime.getText().isEmpty()) ActualController.movingTime = 5;
            else ActualController.movingTime = Integer.parseInt(movingTime.getText());
            if (waitingTime.getText().isEmpty()) ActualController.waitingTime = 3;
            else ActualController.waitingTime = Integer.parseInt(waitingTime.getText());
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/javafxtest3/view/actual-view.fxml"));
            button.getScene().getWindow().hide();
            stage.setResizable(false);
            stage.setTitle("Lift App");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (NumberFormatException n) {
            label.setText("Input positive integers only!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void numChange(){
        try{
            int numLifts = Integer.parseInt(numLift.getText());
            int numFloors = Integer.parseInt(numFloor.getText());
            button.setDisable(numFloors <= 0 || numLifts <= 0);
        } catch (NullPointerException | NumberFormatException e){
            button.setDisable(true);
        }
    }
}