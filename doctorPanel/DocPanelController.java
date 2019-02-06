/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HMS.doctorPanel;

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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class DocPanelController implements Initializable {

    @FXML
    private TextField doctor_id;
    @FXML
    private TableView<Patient> patTableView;
    @FXML
    private TableColumn<Patient,String> p_nameCol;
    @FXML
    private TableColumn<Patient,String> p_surCol;
    @FXML
    private TableColumn<Patient,String> p_ageCol;
    @FXML
    private TableColumn<Patient,String> p_sexCol;
    @FXML
    private TableColumn<Patient,String> p_date;
    
     ObservableList<Patient>  patientList = FXCollections.observableArrayList();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        initColPat();
        
    }    
    @FXML
    private void bringPatInfo(MouseEvent event) throws SQLException {
            patientList.removeAll(patientList);
            DatabaseHandler handler = new DatabaseHandler();
            int d_id = Integer.valueOf(doctor_id.getText());
            String query = "SELECT * FROM reservation r, patient p where r.pat_id = p.pat_id and doc_id="+d_id;
            System.out.println(query);
            ResultSet rs = handler.executeQuery(query);
            try {
                while(rs.next()){
                    String pat_name = rs.getString("pat_name");
                    String pat_surname = rs.getString("pat_surname");
                    String pat_age = rs.getString("pat_age");
                    String pat_sex = rs.getString("pat_sex");
                    String pat_date = rs.getString("pat_date");
                    patientList.add(new Patient(pat_name, pat_surname, pat_age, pat_sex, pat_date));    
            }
                patTableView.getItems().setAll(patientList);
                
        } catch (SQLException e) {
            Logger.getLogger(DocPanelController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
     private void initColPat() {
        
        p_nameCol.setCellValueFactory(new PropertyValueFactory<>("patient_name"));
        p_surCol.setCellValueFactory(new PropertyValueFactory<>("patient_surname"));
        p_ageCol.setCellValueFactory(new PropertyValueFactory<>("patient_age"));
        p_sexCol.setCellValueFactory(new PropertyValueFactory<>("patient_gender"));
        p_date.setCellValueFactory(new PropertyValueFactory<>("patient_date"));
   
    }

    @FXML
    private void controlData(KeyEvent event) {
        
         try {
                int num1 = Integer.parseInt(doctor_id.getText());
                doctor_id.setStyle("-fx-control-inner-background: #ffffff;");
                
        } catch (NumberFormatException e) {
                System.out.println("ERROR! ENTER INTEGER!");
                doctor_id.setStyle("-fx-control-inner-background: #ff0000;");       
        }
    }
    public class  Patient {

            private final SimpleStringProperty patient_name;
            private final SimpleStringProperty patient_surname;
            private final SimpleStringProperty patient_age;
            private final SimpleStringProperty patient_gender;
            private final SimpleStringProperty patient_date;

        public Patient(String patient_name, String patient_surname, String patient_age, String patient_gender, String patient_date) {
           
            this.patient_name = new SimpleStringProperty(patient_name);
            this.patient_surname =new SimpleStringProperty(patient_surname);
            this.patient_age = new SimpleStringProperty(patient_age);
            this.patient_gender = new SimpleStringProperty(patient_gender);
            this.patient_date = new SimpleStringProperty(patient_date);
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
    
}
