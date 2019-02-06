package HMS.main;

import HMS.database.DatabaseHandler;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainController implements Initializable {
    
   
    @FXML
    private Button main_admin;
    @FXML
    private Button main_doc;
    @FXML
    private Button main_res;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
            try {
                DatabaseHandler.getConnection();
                
        } catch (SQLException e) {
            
             Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
             
        }
    }    
    
    @FXML
    private void adminPaneliAc(ActionEvent event) {
        
        LoadWindows("/HMS/adminPanel/adminPanel.fxml", "Admin Window");
        
    }

    @FXML
    private void doctorPaneliAc(ActionEvent event) {
        
        LoadWindows("/HMS/doctorPanel/DocPanelTheme.fxml", "Doctor Window");
    }

    @FXML
    private void reservationPanelAc(ActionEvent event) {
        
        LoadWindows("/HMS/resPanel/reservationTheme.fxml", "Reservation Window");
        
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
    
}
