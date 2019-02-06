
package HMS.adminPanel;

import HMS.database.DatabaseHandler;
import HMS.doctor.DoctorController;
import HMS.main.MainController;
import HMS.nurse.NurseController;
import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;


public class AdminPanelController implements Initializable {
        
        ObservableList<Doctor> doctorList = FXCollections.observableArrayList();
        ObservableList<Nurse> nurseList = FXCollections.observableArrayList();
        
    @FXML
    private TableColumn<Doctor,Integer> docIdCol;
    @FXML
    private TableColumn<Doctor,String> docNameCol;
    @FXML
    private TableColumn<Doctor,String> docSurCol;
    @FXML
    private TableColumn<Doctor,String> docDeptCol;
    @FXML
    private TableView<Doctor> docTable;
    @FXML
    private TableView<Nurse> nurseTable;
    @FXML
    private TableColumn<Nurse,String> nurseIdCol;
    @FXML
    private TableColumn<Nurse,String> nurseNameCol;
    @FXML
    private TableColumn<Nurse,String> nurseSurnameCol;
    @FXML
    private TableColumn<Nurse,String> nurseExpCol;
    @FXML
    private BorderPane adminMainPanel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       loadDataNurse();
       initColDoctor();
       initColNurse();
       loadDataDoctor();  
    }
      private void loadDataDoctor() {
       
       try {
            DatabaseHandler handler = new DatabaseHandler();
            String query = "Select * from doctor";
            ResultSet rs = handler.executeQuery(query);
            try {
                while(rs.next()){
                    String doctor_id = rs.getString("doc_id");
                    String doctor_name =rs.getString("doc_name");
                    String doctor_surname =rs.getString("doc_surname");
                    String doctor_dept =rs.getString("doc_dept");
                    doctorList.add(new Doctor(doctor_id, doctor_name, doctor_surname, doctor_dept));
                }
            } catch (SQLException e) {
                Logger.getLogger(AdminPanelController.class.getName()).log(Level.SEVERE, null, e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminPanelController.class.getName()).log(Level.SEVERE, null, ex);
        }
        docTable.getItems().setAll(doctorList);
    }
      private void initColDoctor() {
       
        docIdCol.setCellValueFactory(new PropertyValueFactory<>("docId"));
        docNameCol.setCellValueFactory(new PropertyValueFactory<>("docName"));
        docSurCol.setCellValueFactory(new PropertyValueFactory<>("docSurname"));
        docDeptCol.setCellValueFactory(new PropertyValueFactory<>("docDept"));

    }
      private void initColNurse() {
         nurseIdCol.setCellValueFactory(new PropertyValueFactory<>("nurseId"));
        nurseNameCol.setCellValueFactory(new PropertyValueFactory<>("nurseName"));
        nurseSurnameCol.setCellValueFactory(new PropertyValueFactory<>("nurseSurname"));
        nurseExpCol.setCellValueFactory(new PropertyValueFactory<>("nurseExp"));
    }
      private void loadDataNurse() {
        
        try {
            DatabaseHandler handler = new DatabaseHandler();
            String query = "Select * from NURSE";
            ResultSet rs = handler.executeQuery(query);
            try {
                while(rs.next()){
                    String n_id = rs.getString("NURSE_ID");
                    String n_name =rs.getString("NURSE_NAME");
                    String n_surname =rs.getString("NURSE_SURNAME");
                    String n_exp =rs.getString("NURSE_EXP");
                    nurseList.add(new Nurse(n_id, n_name, n_surname, n_exp));
                }
            } catch (SQLException e) {
                Logger.getLogger(AdminPanelController.class.getName()).log(Level.SEVERE, null, e);
            }
      
        } catch (SQLException ex) {
            Logger.getLogger(AdminPanelController.class.getName()).log(Level.SEVERE, null, ex);
        }
         nurseTable.getItems().setAll(nurseList);
    }

    @FXML
    private void deleteDoctor(ActionEvent event) throws SQLException {
        
        DatabaseHandler db = new DatabaseHandler();
        Doctor selectionForDelete = docTable.getSelectionModel().getSelectedItem();
            
        if(selectionForDelete == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No doctor Selected");
            alert.setContentText("Please select a doctor for deletion");
            alert.showAndWait();
            return;
        }else{
            String query = "Delete from ROOT.DOCTOR where DOC_ID="+selectionForDelete.getDocId();
            db.databaseAction(query);
            System.out.println(query);
            JOptionPane.showMessageDialog(null, "Deletion Transaction Success!");
            Stage stage = (Stage) adminMainPanel.getScene().getWindow();
            stage.close();
        } 
    }

    @FXML
    private void UpdateDoctor(ActionEvent event) throws SQLException {
        
        DatabaseHandler db = new DatabaseHandler();
        Doctor selectionForUpdate = docTable.getSelectionModel().getSelectedItem();
        if(selectionForUpdate == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No doctor Selected");
            alert.setContentText("Please select a doctor for deletion");
            alert.showAndWait();
            return;
        }else{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/HMS/doctor/DoctorTheme.fxml"));
                Parent parent = loader.load();
                
                DoctorController controller = (DoctorController) loader.getController();
                controller.inflateUI(selectionForUpdate);
                
                Stage stage = new Stage(StageStyle.DECORATED);
                stage.setTitle("Edit Table");
                stage.setScene(new Scene(parent));
                stage.show();   
            } catch (IOException e) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE,null,e);
            } 
        }         
    }

    @FXML
    private void deleteNurse(ActionEvent event) throws SQLException {
        
        DatabaseHandler db = new DatabaseHandler();
        Nurse selectionForDelete = nurseTable.getSelectionModel().getSelectedItem();
        
         if(selectionForDelete == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No doctor Selected");
            alert.setContentText("Please select a doctor for deletion");
            alert.showAndWait();
            return;
        }else{
            String query = "Delete from ROOT.NURSE where NURSE_ID="+selectionForDelete.getNurseId();
            db.databaseAction(query);
            System.out.println(query);
            JOptionPane.showMessageDialog(null, "Deletion Transaction Success!");
            Stage stage = (Stage) adminMainPanel.getScene().getWindow();
            stage.close();
        } 
    }
    @FXML
    private void UpdateNurse(ActionEvent event) throws SQLException {
        
        DatabaseHandler db = new DatabaseHandler();
        Nurse selectionForUpdate = nurseTable.getSelectionModel().getSelectedItem();
        
         if(selectionForUpdate == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No doctor Selected");
            alert.setContentText("Please select a doctor for deletion");
            alert.showAndWait();
            return;
        }else{
             try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/HMS/nurse/NurseTheme.fxml"));
                Parent parent = loader.load();
                
                 NurseController nurseController = loader.getController();
                 nurseController.inflateUI(selectionForUpdate);
                Stage stage = new Stage(StageStyle.DECORATED);
                stage.setTitle("Edit Table");
                stage.setScene(new Scene(parent));
                stage.show();   
            } catch (IOException e) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE,null,e);
            }
         }
    }
    
    @FXML
    private void refleshTableNurse(ActionEvent event) {
        
            nurseList.removeAll(nurseList);
            loadDataNurse();     
    }

    public  static class Doctor  {
        
    private  final SimpleStringProperty docId;
    private  final SimpleStringProperty docName;
    private  final SimpleStringProperty docSurname;
    private  final SimpleStringProperty docDept;
    
   public Doctor(String docId,String docName,String docSurname,String docDept){
            
            this.docId = new SimpleStringProperty(docId);
            this.docName = new SimpleStringProperty(docName);
            this.docSurname = new SimpleStringProperty(docSurname);
            this.docDept = new SimpleStringProperty(docDept);
        }

     public String getDocId() {
        return docId.get();
    }

    public String getDocName() {
        return docName.get();
    }

    public String getDocSurname() {
        return docSurname.get();
    }

    public String getDocDept() {
        return docDept.get();
    }
  }
    public  static class Nurse {
        
        private final    SimpleStringProperty nurseId;
        private final    SimpleStringProperty nurseName;
        private final    SimpleStringProperty nurseSurname;
        private final    SimpleStringProperty nurseExp;

   public Nurse(String nID,String nName,String nSurname,String nExp){
            
            this.nurseId = new SimpleStringProperty(nID);
            this.nurseName = new SimpleStringProperty(nName);
            this.nurseSurname = new SimpleStringProperty(nSurname);
            this.nurseExp = new SimpleStringProperty(nExp);
   
        }

        public String getNurseId() {
            return nurseId.get();
        }

        public String getNurseName() {
            return nurseName.get();
        }

        public String getNurseSurname() {
            return nurseSurname.get();
        }

        public String getNurseExp() {
            return nurseExp.get();
        }
   }   
      
    @FXML
    private void LoadAddDoctorWindow(ActionEvent event) {
        
        LoadWindows("/HMS/doctor/DoctorTheme.fxml", "Doctor Window");
    }
    @FXML
    private void LoadAddNurseWindow(ActionEvent event) {
        
        LoadWindows("/HMS/nurse/NurseTheme.fxml", "Nurse Window");
       
    }
    
    void LoadWindows(String location,String title){
        
            try {
                
                Parent parent = FXMLLoader.load(getClass().getResource(location));
                Stage stage = new Stage(StageStyle.DECORATED);
                stage.setTitle(title);
                stage.setScene(new Scene(parent));
                stage.show();
            
        } catch (IOException e) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE,null,e);
        }
    }
    
    @FXML
    public void refleshTableDoctor(){
        
            doctorList.removeAll(doctorList);
            loadDataDoctor();    
    }
    
}
