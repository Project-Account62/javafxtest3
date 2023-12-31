package com.example.javafxtest3.controller;

import com.example.javafxtest3.model.Lift;
import com.example.javafxtest3.model.Operation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.concurrent.TimeUnit;

public class ActualController {
    @javafx.fxml.FXML
    private Label floorInputLabel;
    @javafx.fxml.FXML
    private TextField floorTextField1;
    @javafx.fxml.FXML
    private ChoiceBox upDownChoiceBox;
    @javafx.fxml.FXML
    private Button submitButton1;
    @javafx.fxml.FXML
    private TextField floorTextField2;
    @javafx.fxml.FXML
    private TextField liftTextField;
    @javafx.fxml.FXML
    private Button submitButton2;
    @javafx.fxml.FXML
    private TextArea liftStatus;

    public static int numFloors;
    public static int numLifts;
    public static int waitingTime;
    public static int movingTime;

    private Operation operation;
    @FXML
    private Button advanceTimeButton;
    @FXML
    private TextField tickBox;
    @FXML
    private TableView liftTable;

    public void initialize() {
        floorTextField1.setPromptText("Floor (1 to " + numFloors + ")");
        floorTextField2.setPromptText("Floor (1 to " + numFloors + ")");
        liftTextField.setPromptText("Lift (1 to " + numLifts + ")");
        upDownChoiceBox.setItems(FXCollections.observableArrayList("Up", "Down"));
        upDownChoiceBox.setValue("Up");
        submitButton1.setDisable(true);
        submitButton2.setDisable(true);
        operation = new Operation(numLifts, numFloors, movingTime, waitingTime);
        updateList();
    }

    @FXML
    public void submitFloor(ActionEvent actionEvent) {
        int floor;
        try {
            floor = Integer.parseInt(floorTextField1.getText());
            if (floor <= 0 || floor > numFloors) throw new NumberFormatException();
            String s = upDownChoiceBox.getValue().toString();
            if (s.equals("Up")) {
                if (floor == numFloors) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, String.format("A lift at the top floor cannot go up.", numFloors));
                    alert.showAndWait();
                }
                operation.triggerFloorInput(floor - 1, 1);
            } else {
                if (floor == 1) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, String.format("A lift at the bottom floor cannot go down.", numFloors));
                    alert.showAndWait();
                }
                operation.triggerFloorInput(floor - 1, -1);
            }
        } catch (NumberFormatException n) {
            Alert alert = new Alert(Alert.AlertType.WARNING, String.format("Please input an integer from 1 to %d for the floor", numFloors));
            alert.showAndWait();
            return;
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, String.format("Please input a direction for the lift", numFloors));
            alert.showAndWait();
            return;
        }
        submitButton1.setDisable(true);
    }

    @FXML
    public void submitLift(ActionEvent actionEvent) {
        int floor;
        int lift;

        try {
            floor = Integer.parseInt(floorTextField2.getText());
            if (floor <= 0 || floor > numFloors) throw new NumberFormatException();
        } catch (NumberFormatException n) {
            Alert alert = new Alert(Alert.AlertType.WARNING, String.format("Please input an integer from 1 to %d for the floor", numFloors));
            alert.showAndWait();
            return;
        }

        try {
            lift = Integer.parseInt(liftTextField.getText());
            if (lift <= 0 || lift > numLifts) throw new NumberFormatException();
        } catch (NumberFormatException n) {
            Alert alert = new Alert(Alert.AlertType.WARNING, String.format("Please input an integer from 1 to %d for the lift number", numLifts));
            alert.showAndWait();
            return;
        }

        operation.triggerLiftInput(lift - 1, floor - 1);
        submitButton2.setDisable(true);
    }

    @FXML
    public void advanceFrame(ActionEvent actionEvent) {
        int numTicks;
        try {
            if (tickBox.getText().isEmpty()) {
                numTicks = 0;
            } else {
                numTicks = Integer.parseInt(tickBox.getText());
                if (numTicks <= 0) throw new NumberFormatException();
            }
        } catch (NumberFormatException n) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please input a positive integer.");
            alert.showAndWait();
            return;
        }
        for (int i = 0; i < numTicks; i++) {
            operation.operate();
//            updateList();
        }
        updateList();
    }

    private void updateList() {
        //liftMenu.setItems(FXCollections.observableArrayList(operation.getLifts()));
        TableColumn<Lift, String> numberColumn = new TableColumn<>("Number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn<Lift, String> floorColumn = new TableColumn<>("Floor");
        floorColumn.setCellValueFactory(new PropertyValueFactory<>("floor"));

        TableColumn<Lift, String> directionColumn = new TableColumn<>("Direction");
        directionColumn.setCellValueFactory(new PropertyValueFactory<>("direction"));

        ObservableList<Lift> liftObservableList = FXCollections.observableArrayList(operation.getLifts());
        liftTable.setItems(liftObservableList);

        liftTable.getColumns().remove(0);
        liftTable.getColumns().remove(0);
        liftTable.getColumns().remove(0);
        liftTable.getColumns().addAll(numberColumn, floorColumn, directionColumn);

        //TableColumn<Lift, Integer> ageColumn = new TableColumn<>("Age");
        //ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
    }

    @FXML
    public void editFloor(Event event) {
        try {
            int floor = Integer.parseInt(floorTextField1.getText());
            int direction = (upDownChoiceBox.getValue().toString().equals("Up")) ? 1:0;
            if ((floor == 1 && direction == 0) || (floor == numFloors && direction == 1) || (floor < 1) || (floor > numFloors)){
                submitButton1.setDisable(true);
            } else {
                submitButton1.setDisable(operation.getFloorInputs()[floor-1][direction].isTriggered());
            }
        } catch(NumberFormatException | NullPointerException e){
            submitButton1.setDisable(true);
        }
    }

    @FXML
    public void editLift(Event event) {
        try {
            int lift = Integer.parseInt(liftTextField.getText());
            int floor = Integer.parseInt(floorTextField2.getText());
            if ((floor >= 1 && floor <= numFloors) && (lift >= 1 && lift <= numLifts)) {
                submitButton2.setDisable(operation.getLiftInputs()[lift-1][floor-1].isTriggered());
            }
        } catch (NumberFormatException | NullPointerException e) {
            submitButton1.setDisable(true);
        }
    }

    @FXML
    public void editTime(Event event){
        try{
            int time = Integer.parseInt(tickBox.getText());
            advanceTimeButton.setDisable(time <= 0);
        } catch (NumberFormatException e){
            advanceTimeButton.setDisable(true);
        } catch (NullPointerException e){
            advanceTimeButton.setDisable(false);
        }
    }

}