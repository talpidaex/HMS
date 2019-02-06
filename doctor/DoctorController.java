
package HMS.doctor;

import HMS.adminPanel.AdminPanelController;
import HMS.database.DatabaseHandler;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DoctorController implements Initializable {
    
    @FXML
    private AnchorPane rootPanel;
    @FXML
    private TextField docId;
    @FXML
    private TextField docName;
    @FXML
    private TextField docSurname;
    @FXML
    private ComboBox<String> comboList;
    
    DatabaseHandler databaseHandler;
    Boolean editMode = false;
    ObservableList<String> doctorDept = FXCollections.observableArrayList("ROMOLOGY","NOROLOGY","UROLOGY","PSYCHOLOGY","ORTHOPEDICS");
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        comboList.setItems(doctorDept);  
        try {
            databaseHandler = new DatabaseHandler();
        } catch (SQLException ex) {
            Logger.getLogger(DoctorController.class.getName()).log(Level.SEVERE, null, ex);
        }
        checkDatabaseData();
    }       
    @FXML
    private void controlData(KeyEvent event) {
   
        try {
                int num1 = Integer.parseInt(docId.getText());
                docId.setStyle("-fx-control-inner-background: #ffffff;");
                
        } catch (NumberFormatException e) {
                System.out.println("ERROR! ENTER INTEGER!");
                docId.setStyle("-fx-control-inner-background: #ff0000;");       
        }  
    }
    @FXML
    private void addDoctor(ActionEvent event) {
  
        int id = Integer.valueOf(docId.getText());
        String name = docName.getText();
        String surname = docSurname.getText();
        String dept = comboList.getSelectionModel().getSelectedItem();
        
        if(editMode){
            updateDoctorOperation();
            
        }else{
            String query = "INSERT INTO DOCTOR VALUES("
                + "" +id+ ","
                + "'" +name+"',"
                + "'" +surname+"',"
                + "'" +dept+ "' "
                + ")";
        
            System.out.println(query);
        if(databaseHandler.databaseAction(query)){
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();
            cancel(event);
        }else{ //ERROR! {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Adding Transaction Failed!");
            alert.showAndWait();
            }    
        }
    } 
    @FXML
    private void cancel(ActionEvent event) {
        
        Stage stage = (Stage) rootPanel.getScene().getWindow();
        stage.close();
    }
    
    private void checkDatabaseData() {      //Database'da var olan doctoru tekrar eklemememiz için!!!
             
             String query = "Select doc_id from Doctor";
            ResultSet resultSet = databaseHandler.executeQuery(query);
        try {
            while(resultSet.next()){
                String doctorID = resultSet.getString("doc_id");
                    System.out.println(doctorID);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DoctorController.class.getName()).log(Level.SEVERE, null, ex+"OGUZHAN");
        }
    }
    
    public void inflateUI(AdminPanelController.Doctor doctor){
        
        docId.setText(doctor.getDocId());
        docName.setText(doctor.getDocName());
        docSurname.setText(doctor.getDocSurname());
        editMode = Boolean.TRUE;
    }
    
    public void updateDoctorOperation(){
    
     AdminPanelController.Doctor doctor = new AdminPanelController.Doctor(docId.getText(), docName.getText(), docSurname.getText(), 
                                                                          comboList.getSelectionModel().getSelectedItem());  
     
     if(databaseHandler.updateDoctor(doctor)){
         Alert alert = new Alert(Alert.AlertType.INFORMATION);
         alert.setHeaderText(null);
         alert.setContentText("Success");
         alert.showAndWait();
         Stage stage = (Stage) rootPanel.getScene().getWindow();
         stage.close();
     }
     else{
         Alert alert = new Alert(Alert.AlertType.ERROR);
         alert.setHeaderText(null);
         alert.setContentText("Adding Transaction Failed!");
         alert.showAndWait();
     }      
  }
}
