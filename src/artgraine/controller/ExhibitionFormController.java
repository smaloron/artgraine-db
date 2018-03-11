package artgraine.controller;

import artgraine.FxUtils.DatePickerUtils;
import artgraine.database.DatabaseConnection;
import artgraine.model.Exhibition;
import artgraine.model.ExhibitionDAO;
import artgraine.model.SculptureDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class ExhibitionFormController extends AbstractController implements Initializable {


    @FXML
    public Label formTitleLabel;
    @FXML
    public TextField titleTextField;
    @FXML
    public DatePicker startDatePicker;
    @FXML
    public DatePicker endDatePicker;
    @FXML
    public DatePicker departureDatePicker;
    @FXML
    public DatePicker returnDatePicker;
    @FXML
    public Label durationLabel;

    private Exhibition exhibition;
    private ExhibitionDAO exhibitionDAO;

    public ExhibitionFormController() {
        this.exhibition = new Exhibition();
        Connection cn = null;
        try {
            cn = DatabaseConnection.getInstance();
            exhibitionDAO = new ExhibitionDAO(cn);
        } catch (SQLException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }

    }


    public ExhibitionFormController setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
        return this;
    }

    public void setFormData(Exhibition exhibition) {

        this.exhibition = exhibition;
        formTitleLabel.setText("Modification d'une expo");

        titleTextField.setText(exhibition.getTitle());
        startDatePicker.setValue(LocalDate.parse(exhibition.getStartDate().toString()));
        endDatePicker.setValue(LocalDate.parse(exhibition.getEndDate().toString()));
        departureDatePicker.setValue(LocalDate.parse(exhibition.getDepartureDate().toString()));
        returnDatePicker.setValue(LocalDate.parse(exhibition.getReturnDate().toString()));

        setDuration(departureDatePicker.getValue(), returnDatePicker.getValue());

    }

    public void validateForm(ActionEvent actionEvent) {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        LocalDate departureDate = departureDatePicker.getValue();
        LocalDate returnDate = returnDatePicker.getValue();

        this.exhibition.setTitle(titleTextField.getText())
                .setStartDate(Date.valueOf(startDate))
                .setEndDate(Date.valueOf(endDate))
                .setDepartureDate(Date.valueOf(departureDate))
                .setReturnDate(Date.valueOf(returnDate));

        try {
            exhibitionDAO.save(exhibition);
            closeWindow();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private void setDuration(LocalDate fromDate, LocalDate toDate) {
        Long duration = 0L;
        if (fromDate != null && toDate != null) {
            duration = ChronoUnit.DAYS.between(fromDate, toDate);
        }
        String dayLabel = duration > 1 ? " jours" : " jour";
        durationLabel.setText(duration + dayLabel);
    }

    private void setDatePickerEvents(){
        //Pré-définition des dates de départ et de fin en fonction de la date de début
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (departureDatePicker.getValue() == null || departureDatePicker.getValue().isAfter(newValue)) {
                departureDatePicker.setValue(newValue);
            }
            if (endDatePicker.getValue() == null || endDatePicker.getValue().isBefore(newValue)) {
                endDatePicker.setValue(newValue.plusDays(7));
            }
        });

        //Pré-définition de la date de retour en fonction de la date de fin
        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (returnDatePicker.getValue() == null || returnDatePicker.getValue().isBefore(newValue)) {
                returnDatePicker.setValue(newValue);
            }
        });

        //Calcul de la durée lors du changement de la date de départ
        departureDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            setDuration(newValue, returnDatePicker.getValue());
        });

        //Calcul de la durée lors du changement de la date de retour
        returnDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            setDuration(departureDatePicker.getValue(), newValue);
        });

        /* Limitation de la sélection des dates
         *  évite de définir une expo qui se termine avant sa date de début par exemple
         */
        startDatePicker.setDayCellFactory(DatePickerUtils.getDateCellFactory(LocalDate.now(), false));
        endDatePicker.setDayCellFactory(DatePickerUtils.getDateCellFactory(startDatePicker, false));
        departureDatePicker.setDayCellFactory(DatePickerUtils.getDateCellFactory(startDatePicker, true));
        returnDatePicker.setDayCellFactory(DatePickerUtils.getDateCellFactory(endDatePicker, false));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setDatePickerEvents();

        //Calcul de la durée totale d'une expo de sa date de départ jusqu'à sa date d'arrivée
        //setDuration(departureDatePicker.getValue(), returnDatePicker.getValue());

    }
}
