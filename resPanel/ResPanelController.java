
package HMS.resPanel;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class ResPanelController implements Initializable {
    @FXML
    private AnchorPane resPanel;
    @FXML
    private TextField pat_name;
    @FXML
    private TextField pat_surname;
    @FXML
    private TextField pat_id;
    @FXML
    private TextField pat_age;
    @FXML
    private RadioButton pat_genderMale;
    @FXML
    private RadioButton pat_genderFemale;
    @FXML
    private ComboBox<String> DepartmentList;
    @FXML
    private ComboBox<String> DoctorList;
    @FXML
    private DatePicker date;

   
    //1.tab Pane
    @FXML
    private TableView<Reservation> resTableView;
    @FXML
    private TableColumn<Reservation,String> resIdCol;
    @FXML
    private TableColumn<Reservation,String> resDocIdCol;
    @FXML
    private TableColumn<Reservation,String> resPatIdCol;
    @FXML
    private TableColumn<Reservation,String> resNurIdCol;
    @FXML
    private TableColumn<Reservation,String> resPatDateCol;
    @FXML
    private TableColumn<Reservation,String> resOperationCol;
  
    ObservableList<String> doctorDept = FXCollections.observableArrayList("ROMOLOGY","NOROLOGY","UROLOGY","PSYCHOLOGY","ORTHOPEDICS");
    ObservableList<String> DoctorListforDept = FXCollections.observableArrayList();
    ObservableList<String> nurseListArray= FXCollections.observableArrayList();
    ObservableList<Reservation> resTableArray = FXCollections.observableArrayList();
    String patient_gender;
    int doc_id,nurse_id;
    DatabaseHandler handler;
    @FXML
    private TextField res_id;
    @FXML
    private ComboBox<String> nurseList;
    @FXML
    private TextField Ope_theater;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

            DepartmentList.setItems(doctorDept);
        try {
            loadNurseList();
        } catch (SQLException ex) {
            Logger.getLogger(ResPanelController.class.getName()).log(Level.SEVERE, null, ex);
        } 
            initColRes();
        try {
            loadTableData();
        } catch (SQLException ex) {
            Logger.getLogger(ResPanelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    @FXML
    private void patSaveBtn(ActionEvent event) throws SQLException {
        handler = new DatabaseHandler();
        int operation_theater = Integer.valueOf(Ope_theater.getText());
        int reservation_id = Integer.valueOf(res_id.getText());
        int patient_id = Integer.valueOf(pat_id.getText());
        String patient_name = pat_name.getText();
        String patient_surname = pat_surname.getText();
        if(pat_genderFemale.isSelected()){
            //System.out.println("Female Choosed!");
        patient_gender = pat_genderFemale.getText();
        }else{
            //System.out.println("Female seçildi!");
        patient_gender = pat_genderMale.getText();
        }
        int patient_age = Integer.valueOf(pat_age.getText());
        String patient_dept = DoctorList.getSelectionModel().getSelectedItem();
        date.getValue();
        
        System.out.println(""+patient_id+""+patient_name+""+patient_surname+""+patient_gender+""+""+patient_age+""+patient_dept+""+date.getValue());
        
        String query = "INSERT INTO PATIENT VALUES("+patient_id+",'"
                                                    +patient_name+"','"
                                                    +patient_surname+"',"  
                                                    +patient_age+",'"
                                                    +patient_gender+"','"
                                                    +date.getValue()+"')";
       

        String queryForRes ="INSERT INTO RESERVATION VALUES("+reservation_id+","
                                                             +doc_id+","                  //buraya_doctor_id_gelecek
                                                             +patient_id+","
                                                             +nurse_id+",'"                        //buraya_nurse_id_gelecek
                                                             +date.getValue()+"',"
                                                             +operation_theater+")";    //buraya_operation_theater gelecek
        
            System.out.println(query);
            System.out.println(queryForRes);
        if(handler.databaseAction(query)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();
            cancel(event);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Adding Transaction Failed!");
            alert.showAndWait();
        }
        if(handler.databaseAction(queryForRes)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();
            cancel(event);
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Adding Transaction Failed!");
            alert.showAndWait();
        }
        
    }     
    void ListDoctor() throws SQLException {
        DatabaseHandler handler = new DatabaseHandler();
        String query = "Select * from  doctor where doc_dept = '"+DepartmentList.getSelectionModel().getSelectedItem()+"'";
        System.out.println(query);
        ResultSet rs = handler.executeQuery(query);
        while (rs.next()) {            
                String doc_name = rs.getString("DOC_NAME");
                DoctorListforDept.add(doc_name);
        }     
    }
    @FXML
    private void isSelected(ActionEvent event) throws SQLException {
                    DoctorList.getItems().removeAll(DoctorListforDept);
                    ListDoctor();
                    DoctorList.setItems(DoctorListforDept);    
    }
    @FXML
    private void takeDoctorID(ActionEvent event) throws SQLException {
        
        DatabaseHandler handler = new DatabaseHandler();
        String query = "Select doc_id from  doctor where doc_name ='"+DoctorList.getSelectionModel().getSelectedItem()+"'";
        System.out.println(query);
        ResultSet rs = handler.executeQuery(query);
        while (rs.next()) {            
                doc_id = rs.getInt("doc_id");
                System.out.println(doc_id);
        }   
    } 
    private void loadNurseList() throws SQLException {
        
        DatabaseHandler handler = new DatabaseHandler();
        String query = "Select * from NURSE";
        System.out.println(query);
        ResultSet rs = handler.executeQuery(query);
        while(rs.next()){
            String nurseName = rs.getString("nurse_name");   
            nurseListArray.add(nurseName);
            System.out.println(nurseName);   
        }
        nurseList.setItems(nurseListArray);
    }   
    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) resPanel.getScene().getWindow();
        stage.close();  
    }
    @FXML
    private void takeNurseID(ActionEvent event) throws SQLException {
        DatabaseHandler handler = new DatabaseHandler();
        String query = "Select nurse_id from nurse where nurse_name='"+nurseList.getSelectionModel().getSelectedItem()+"'";
        System.out.println(query);
        ResultSet rs = handler.executeQuery(query);
        while (rs.next()) {            
                nurse_id = rs.getInt("nurse_id");
                System.out.println(nurse_id);
        }
    }
    private void initColRes() {
        
        resIdCol.setCellValueFactory(new PropertyValueFactory<>("res_id"));
        resDocIdCol.setCellValueFactory(new PropertyValueFactory<>("doctor_id"));
        resPatIdCol.setCellValueFactory(new PropertyValueFactory<>("patient_id"));
        resNurIdCol.setCellValueFactory(new PropertyValueFactory<>("nurse_id"));
        resPatDateCol.setCellValueFactory(new PropertyValueFactory<>("patient_date"));
        resOperationCol.setCellValueFactory(new PropertyValueFactory<>("operation_the"));
    }
    private void loadTableData() throws SQLException {
       
        DatabaseHandler handler = new DatabaseHandler();
        String query = "Select * from reservation";
        ResultSet rs = handler.executeQuery(query);
        while(rs.next()){
            String r_id = rs.getString("res_id");
            String d_id = rs.getString("doc_id");
            String p_id = rs.getString("pat_id");
            String n_id = rs.getString("nurse_id");
            String p_date=rs.getString("pat_date");
            String o_ph = rs.getString("ope_theatre");
            resTableArray.add(new Reservation(r_id, d_id, p_id, n_id, p_date, o_ph));
        }
        
        resTableView.getItems().setAll(resTableArray);
    }
    @FXML
    private void patientInfo(ActionEvent event) {
        
        resLoader rs = new resLoader();
        rs.LoadWindows("/HMS/patient/PatientList.fxml", "Patient Table");
   
    }

    @FXML
    private void deleteRes(ActionEvent event) throws SQLException {
        
         DatabaseHandler db = new DatabaseHandler();
        Reservation selectionForDelete = resTableView.getSelectionModel().getSelectedItem();
            
        if(selectionForDelete == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No doctor Selected");
            alert.setContentText("Please select a doctor for deletion");
            alert.showAndWait();
            return;
        }else{
            String query = "Delete from ROOT.RESERVATION where RES_ID="+selectionForDelete.getRes_id();
            db.databaseAction(query);
            System.out.println(query);
            JOptionPane.showMessageDialog(null, "Deletion Transaction Success!");
            Stage stage = (Stage) resPanel.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void dataResControl(KeyEvent event) {
        try {
                int num1 = Integer.parseInt(res_id.getText());
                res_id.setStyle("-fx-control-inner-background: #ffffff;");
                
        } catch (NumberFormatException e) {
                System.out.println("ERROR! ENTER INTEGER!");
                res_id.setStyle("-fx-control-inner-background: #ff0000;");       
        }
    }

    @FXML
    private void dataAgeControl(KeyEvent event) {
        
        try {
        int num1 = Integer.parseInt(pat_age.getText());
        pat_age.setStyle("-fx-control-inner-background: #ffffff;");
                
        } catch (NumberFormatException e) {
        System.out.println("ERROR! ENTER INTEGER!");
        pat_age.setStyle("-fx-control-inner-background: #ff0000;");       
        }
    }

    @FXML
    private void dataPatControl(KeyEvent event) {
            try {
        int num1 = Integer.parseInt(pat_id.getText());
        pat_id.setStyle("-fx-control-inner-background: #ffffff;");
                
        } catch (NumberFormatException e) {
        System.out.println("ERROR! ENTER INTEGER!");
        pat_id.setStyle("-fx-control-inner-background: #ff0000;");       
        }
    }
    
      public class Reservation {

           private  final SimpleStringProperty res_id;
           private  final SimpleStringProperty doctor_id;
           private  final SimpleStringProperty patient_id;
           private  final SimpleStringProperty nurse_id;
           private  final SimpleStringProperty patient_date;
           private  final SimpleStringProperty operation_the;

        public Reservation(String res_id, String doctor_id, String patient_id, String nurse_id, String patient_date, String operation_the) {
            this.res_id = new SimpleStringProperty(res_id);
            this.doctor_id = new SimpleStringProperty(doctor_id);
            this.patient_id = new SimpleStringProperty(patient_id);
            this.nurse_id = new SimpleStringProperty(nurse_id);
            this.patient_date = new SimpleStringProperty(patient_date);
            this.operation_the = new SimpleStringProperty(operation_the);
        }

        public String getRes_id() {
            return res_id.get();
        }

        public String getDoctor_id() {
            return doctor_id.get();
        }

        public String getPatient_id() {
            return patient_id.get();
        }

        public String getNurse_id() {
            return nurse_id.get();
        }

        public String getPatient_date() {
            return patient_date.get();
        }

        public String getOperation_the() {
            return operation_the.get();
        }
      
      }
    
}

  
  
