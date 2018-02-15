package artgraine.controller;

import artgraine.database.DatabaseConnection;
import artgraine.model.Sculpture;
import artgraine.model.SculptureDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;

public class SculptureFormController extends AbstractController{

    @FXML public TextField categoryTextField;
    @FXML public TextField titleTextField;
    @FXML public TextField descriptionTextField;
    @FXML public TextField sizeTextField;
    @FXML public TextField insuranceValueTextField;

    public void validateForm(ActionEvent actionEvent) {
        Sculpture sculpture = new Sculpture();
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
}
