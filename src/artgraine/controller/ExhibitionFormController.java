package artgraine.controller;

import artgraine.database.DatabaseConnection;
import artgraine.model.Exhibition;
import artgraine.model.ExhibitionDAO;
import artgraine.model.SculptureDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class ExhibitionFormController extends AbstractController implements Initializable{


    @FXML public Label formTitleLabel;
    @FXML public TextField titleTextField;
    @FXML public DatePicker startDatePicker;
    @FXML public DatePicker endDatePicker;
    @FXML public DatePicker departureDatePicker;
    @FXML public DatePicker returnDatePicker;
    @FXML public Label durationLabel;

    private Exhibition exhibition;
    private ExhibitionDAO exhibitionDAO;

    public ExhibitionFormController() {
        this.exhibition = new Exhibition();
    }

    public void setFormData(Exhibition exhibition){

    }

    public void validateForm(ActionEvent actionEvent) {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = startDatePicker.getValue();
        LocalDate departureDate = startDatePicker.getValue();
        LocalDate returnDate = startDatePicker.getValue();

        this.exhibition .setTitle(titleTextField.getText())
                        .setStartDate(Date.valueOf(startDate))
                        .setEndDate(Date.valueOf(endDate))
                        .setDepartureDate(Date.valueOf(departureDate))
                        .setReturnDate(Date.valueOf(returnDate));

        try {
            Connection cn = DatabaseConnection.getInstance();
            exhibitionDAO = new ExhibitionDAO(cn);
            exhibitionDAO.save(exhibition);
            closeWindow();

        } catch (SQLException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }


    }

    private void setDuration(LocalDate fromDate, LocalDate toDate){
        Long duration = 0L;
        if(departureDatePicker.getValue() != null){
            duration = ChronoUnit.DAYS.between(fromDate, toDate);
        }
        String dayLabel = (duration>1)?" jours":" jour";
        durationLabel.setText(duration + dayLabel);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(departureDatePicker.getValue() == null || departureDatePicker.getValue().isAfter(newValue)){
                departureDatePicker.setValue(newValue);
            }
            if(endDatePicker.getValue() == null || endDatePicker.getValue().isBefore(newValue)){
                endDatePicker.setValue(newValue.plusDays(7));
            }
        });

        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(returnDatePicker.getValue() == null || returnDatePicker.getValue().isBefore(newValue)){
                returnDatePicker.setValue(newValue);
            }
        });

        returnDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            setDuration(departureDatePicker.getValue(), newValue);
        });

    }
}
