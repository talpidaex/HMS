
package HMS.nurse;

import HMS.adminPanel.AdminPanelController;
import HMS.database.DatabaseHandler;
import HMS.doctor.DoctorController;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class NurseController implements Initializable {

    @FXML
    private TextField nurseId;
    @FXML
    private TextField nurseName;
    @FXML
    private TextField nurseSurname;
    @FXML
    private TextField nurseExp;
    
    DatabaseHandler databaseHandler;
    @FXML
    private AnchorPane rootPanel;
   
    Boolean editModeNurse = false;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
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
                int num1 = Integer.parseInt(nurseId.getText());
                nurseId.setStyle("-fx-control-inner-background: #ffffff;");       
               
        } catch (NumberFormatException e) {
                System.out.println("ERROR! ENTER INTEGER!");
                nurseId.setStyle("-fx-control-inner-background: #ff0000;");       
        }   
    }
    @FXML
    private void addNurse(ActionEvent event) {
        
        int id = Integer.valueOf(nurseId.getText());
        String name = nurseName.getText();
        String surname = nurseSurname.getText();
        int exp = Integer.valueOf(nurseExp.getText());
        
        if(editModeNurse){
            updateNurseOperation();
        }else{
           
          String query = "INSERT INTO NURSE VALUES("
                + "" +id+ ","
                + "'" +name+"',"
                + "'" +surname+"',"
                + "" +exp+ ""
                + ")";
        
        System.out.println(query);
        
        if(databaseHandler.databaseAction(query)){
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();
            
        }else{ //ERROR! {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Adding Transaction Failed!");
            alert.showAndWait();
        }
        cancel(event);
         }
    }
    
    private void checkDatabaseData() {      //Database'da var olan doctoru tekrar eklemememiz için!!!
        
             String query = "Select nurse_id from NURSE";
            ResultSet resultSet = databaseHandler.executeQuery(query);
        try {
            while(resultSet.next()){
                String nurse_id = resultSet.getString("nurse_id");
                    System.out.println(nurse_id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DoctorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public void inflateUI(AdminPanelController.Nurse nurse){
        
        nurseId.setText(nurse.getNurseId());
        nurseName.setText(nurse.getNurseName());
        nurseSurname.setText(nurse.getNurseSurname());
        nurseExp.setText(nurse.getNurseExp());
        editModeNurse = Boolean.TRUE;
    }
     
    public void updateNurseOperation(){
        AdminPanelController.Nurse nurse = new AdminPanelController.Nurse(nurseId.getText(),
                nurseName.getText(),
                nurseSurname.getText(), 
                nurseExp.getText());  
        
        if(databaseHandler.updateNurse(nurse)){
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
    @FXML
    private void cancel(ActionEvent event) {
          Stage stage = (Stage) rootPanel.getScene().getWindow();
          stage.close();
    }

    @FXML
    private void controlExpData(KeyEvent event) {
        
          try {
                int num1 = Integer.parseInt(nurseExp.getText());
                nurseExp.setStyle("-fx-control-inner-background: #ffffff;");       
               
        } catch (NumberFormatException e) {
                System.out.println("ERROR! ENTER INTEGER!");
                nurseExp.setStyle("-fx-control-inner-background: #ff0000;");       
        }
        
    }
}
