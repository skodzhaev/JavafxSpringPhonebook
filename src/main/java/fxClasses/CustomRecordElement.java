package fxClasses;

import dbClasses.DatabaseDAO;
import dbClasses.Record;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("CustomRecordElement")
@Scope("prototype")
public class CustomRecordElement extends AnchorPane{
    Integer idRecord;

    Record tempRecord = null;

    boolean editState = false;

    @Autowired
    ButtonHandler buttonHandler;

    @Autowired
    DatabaseDAO databaseUtilities;

    @FXML
    TextField name;

    @FXML
    TextField phone;

    @FXML
    ImageView enableUpdateIcon;

    @FXML
    ImageView updateIcon;

    public CustomRecordElement() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/CustomRecordElement.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        SpringFXMLLoader.setControllerFactoryToLoader(fxmlLoader);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        name.setDisable(true);
        phone.setDisable(true);
    }

    public Integer getIdRecord() {
        return idRecord;
    }

    public void setIdRecord(Integer idRecord) {
        this.idRecord = idRecord;
    }

    public TextField getName() {
        return name;
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public TextField getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.setText(phone);
    }

    public void deleteElem(){
        databaseUtilities.deleteRecord(idRecord);
        buttonHandler.showAll("");
    }

    public void enableFields(){
        updateIcon.setDisable(!editState);
        updateIcon.setVisible(editState);
        name.setDisable(!editState);
        phone.setDisable(!editState);
    }

    public void enableUpdate(){
        if (!editState){
            editState = true;
            tempRecord = new Record(this.idRecord,this.name.getText(),this.phone.getText());
            enableUpdateIcon.setImage(new Image("icon_circle.png"));
        } else {
            editState=false;
            this.name.setText(tempRecord.getName());
            this.phone.setText(tempRecord.getPhone());
            enableUpdateIcon.setImage(new Image("icon_editing.png"));
        }
        enableFields();
    }

    public void update(){
        Record editedRecord = new Record(idRecord,name.getText(),phone.getText());
        if(!tempRecord.equals(editedRecord)) {
            databaseUtilities.updateRecord(editedRecord);
            editState=false;
            enableFields();
            buttonHandler.showAll("");
        } else {
            editState=false;
            enableFields();
        }
    }
}
