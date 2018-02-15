package artgraine.controller;

import artgraine.database.DatabaseConnection;
import artgraine.model.Sculpture;
import artgraine.model.SculptureDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;

public class SculptureFormController extends AbstractController{

    @FXML public TextField categoryTextField;
    @FXML public TextField titleTextField;
    @FXML public TextField descriptionTextField;
    @FXML public TextField sizeTextField;
    @FXML public TextField insuranceValueTextField;
    @FXML public Label formTitleLabel;

    private Sculpture sculpture;

    public SculptureFormController() {
        sculpture = new Sculpture();
    }

    public void validateForm(ActionEvent actionEvent) {
        sculpture.setTitle(titleTextField.getText())
                .setDescription(descriptionTextField.getText())
                .setCategory(categoryTextField.getText())
                .setSizeInCm(Integer.valueOf(sizeTextField.getText()))
                .setInsurancePrice(Integer.valueOf(insuranceValueTextField.getText()));

        try {
            Connection cn = DatabaseConnection.getInstance();
            SculptureDAO dao = new SculptureDAO(cn);
            dao.save(sculpture);
            closeWindow();

        } catch (SQLException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void setFormData(Sculpture sculpture){
        this.sculpture = sculpture;

        formTitleLabel.setText("Modification d'une sculpture");

        titleTextField.setText(sculpture.getTitle());
        descriptionTextField.setText(sculpture.getDescription());
        categoryTextField.setText(sculpture.getCategory());
        sizeTextField.setText(sculpture.getSizeInCm().toString());
        insuranceValueTextField.setText(sculpture.getInsurancePrice().toString());
    }
}
