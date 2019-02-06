
package HMS.patient;

import HMS.database.DatabaseHandler;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class PatientController implements Initializable {
    @FXML
    private TableView<Patient> patTableView;
    @FXML
    private TableColumn<Patient,String> pat_idCol;
    @FXML
    private TableColumn<Patient,String> pat_nameCol;
    @FXML
    private TableColumn<Patient,String> pat_surCol;
    @FXML
    private TableColumn<Patient,String> pat_ageCol;
    @FXML
    private TableColumn<Patient,String> pat_sexCol;
    @FXML
    private TableColumn<Patient,String> pat_dateCol;
    
    ObservableList<Patient>  patientList = FXCollections.observableArrayList();
    @FXML
    private AnchorPane patientListPanel;
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
            initColPat();
        try {
            loadDataPatient();
        } catch (SQLException ex) {
            Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    private void initColPat() {
        
        pat_idCol.setCellValueFactory(new PropertyValueFactory<>("patient_id"));
        pat_nameCol.setCellValueFactory(new PropertyValueFactory<>("patient_name"));
        pat_surCol.setCellValueFactory(new PropertyValueFactory<>("patient_surname"));
        pat_ageCol.setCellValueFactory(new PropertyValueFactory<>("patient_age"));
        pat_sexCol.setCellValueFactory(new PropertyValueFactory<>("patient_gender"));
        pat_dateCol.setCellValueFactory(new PropertyValueFactory<>("patient_date"));
   
    }
    
    @FXML
    private void deletePatient(ActionEvent event) throws SQLException {
        
        DatabaseHandler db = new DatabaseHandler();
        Patient selectionForDelete = patTableView.getSelectionModel().getSelectedItem();
        if(selectionForDelete == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No doctor Selected");
            alert.setContentText("Please select a doctor for deletion");
            alert.showAndWait();
            return;
        }else{
            String query = "Delete from ROOT.PATIENT where PAT_ID="+selectionForDelete.getPatient_id();
            db.databaseAction(query);
            System.out.println(query);
            JOptionPane.showMessageDialog(null, "Deletion Transaction Success!");
            Stage stage = (Stage) patientListPanel.getScene().getWindow();
            stage.close();
        }
        
        
        
        
    }
    public class  Patient {
        
            private final SimpleStringProperty patient_id;
            private final SimpleStringProperty patient_name;
            private final SimpleStringProperty patient_surname;
            private final SimpleStringProperty patient_age;
            private final SimpleStringProperty patient_gender;
            private final SimpleStringProperty patient_date;

        public Patient(String patient_id, String patient_name, String patient_surname, String patient_age, String patient_gender, String patient_date) {
            this.patient_id = new SimpleStringProperty(patient_id);
            this.patient_name = new SimpleStringProperty(patient_name);
            this.patient_surname =new SimpleStringProperty(patient_surname);
            this.patient_age = new SimpleStringProperty(patient_age);
            this.patient_gender = new SimpleStringProperty(patient_gender);
            this.patient_date = new SimpleStringProperty(patient_date);
        }

        public String getPatient_id() {
            return patient_id.get();
        }

        public String getPatient_name() {
            return patient_name.get();
        }

        public String getPatient_surname() {
            return patient_surname.get();
        }

        public String getPatient_age() {
            return patient_age.get();
        }

        public String getPatient_gender() {
            return patient_gender.get();
        }

        public String getPatient_date() {
            return patient_date.get();
        }
    }
    

    private void loadDataPatient() throws SQLException {
         
            DatabaseHandler handler = new DatabaseHandler();
            String query = "Select * from PATIENT";
            ResultSet rs = handler.executeQuery(query);
            
            try {
                while(rs.next()){
                    String pat_id = rs.getString("pat_id");
                    String pat_name = rs.getString("pat_name");
                    String pat_surname = rs.getString("pat_surname");
                    String pat_age = rs.getString("pat_age");
                    String pat_sex = rs.getString("pat_sex");
                    String pat_date = rs.getString("pat_date");
                    patientList.add(new Patient(pat_id, pat_name, pat_surname, pat_age, pat_sex, pat_date));
            }
                patTableView.getItems().setAll(patientList);
        } catch (SQLException e) {
             Logger.getLogger(PatientController.class.getName()).log(Level.SEVERE, null, e);
        }    
    }  
}
