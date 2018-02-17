package artgraine.controller;

import artgraine.database.DatabaseConnection;
import artgraine.model.Exhibition;
import artgraine.model.ExhibitionDAO;
import artgraine.model.SculptureDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class ExhibitionFormController extends AbstractController{


    @FXML public Label formTitleLabel;
    @FXML public TextField titleTextField;
    @FXML public DatePicker startDatePicker;
    @FXML public DatePicker endDatePicker;
    @FXML public DatePicker departureDatePicker;
    @FXML public DatePicker returnDatePicker;

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
}
