package fxClasses;

import dbClasses.DatabaseDAO;
import dbClasses.Record;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component("buttonHandler")
public class ButtonHandler implements Initializable {
    @Autowired
    DatabaseDAO databaseDAO;

    @FXML
    private Button acceptButton;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private FlowPane recordsPane;

    @FXML
    private TextField searchField;

    public List<Record> showAll(String condition){
        recordsPane.getChildren().clear();
        List<Record> list;
        if(condition=="") {
            list = databaseDAO.readAllRecords();
        } else {
            list = databaseDAO.readRecordsWithCondition(condition);
        }
        List<CustomRecordElement> creList = new ArrayList<>();
        if (list.size()!=0) {
            for (Record r : list) {
                CustomRecordElement customRecordElement = SpringFXMLLoader.getCustomRecordElement();
                customRecordElement.setName(r.getName());
                customRecordElement.setPhone(r.getPhone());
                customRecordElement.setIdRecord(r.getId());
                creList.add(customRecordElement);
            }
            recordsPane.setAlignment(Pos.TOP_LEFT);
            recordsPane.getChildren().addAll(creList);
        } else {
            recordsPane.setAlignment(Pos.CENTER);
            Label label = new Label("Ваш список пуст!");
            HBox hBox = new HBox();
            hBox.getChildren().add(label);
            recordsPane.getChildren().add(hBox);
        }
        return list;
    }

    public void initialize(URL location, ResourceBundle resources){
        acceptButton.setOnAction(event -> {
            Record record = new Record(nameField.getText(), phoneField.getText());
            try {
                databaseDAO.createRecord(record);
                nameField.clear();
                phoneField.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
            showAll("");
        });

        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("^\\+?(\\d+-?)*")) {
            } else {
                phoneField.setText(oldValue);
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            showAll(newValue);
        });

        showAll("");
    }

    public void clearSearchField(){
        searchField.setText("");
        showAll("");
    }
}
