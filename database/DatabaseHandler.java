
package HMS.database;

import HMS.adminPanel.AdminPanelController;
import HMS.adminPanel.AdminPanelController.Doctor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class DatabaseHandler {
    
        private static Statement statement = null;

    public DatabaseHandler() throws SQLException {
   
            getConnection();
    }

        public static Connection getConnection() throws SQLException{
            
            String db_url = "jdbc:derby://localhost:1527/HMS";
            String user = "root";
            String pass = "root";
            Connection connection = DriverManager.getConnection(db_url,user,pass);
            System.out.println("DB Connection Success!");
            return connection; 
        }  
        
        public ResultSet executeQuery(String query){
            ResultSet result ;
            try {
              result = getConnection().createStatement().executeQuery(query);
             
            } catch (SQLException e) {
                System.out.println("Exception at executeQuery :dataHandler "+e.getLocalizedMessage());
                return null;
                
            }
            return result;
        }

        public boolean databaseAction(String query){
            try {
                    statement = getConnection().createStatement();
                    statement.execute(query);                    
                    
                    return true;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,"Error" + e.getMessage(),"Error Occured : ",JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        public boolean updateDoctor(Doctor doctor){
            
            try {
                String update = "Update doctor set DOC_NAME=?,DOC_SURNAME=?,DOC_DEPT=? WHERE DOC_ID=?";
                PreparedStatement ps = getConnection().prepareStatement(update);
                ps.setString(1, doctor.getDocName());
                ps.setString(2, doctor.getDocSurname());
                ps.setString(3, doctor.getDocDept());
                ps.setString(4, doctor.getDocId());
                int flag = ps.executeUpdate();
                return (flag>0);
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
                return false;
        }
        
        public boolean updateNurse(AdminPanelController.Nurse nurse){
            
            try {
                String update = "Update NURSE set NURSE_NAME=?,NURSE_SURNAME=?,NURSE_EXP=? WHERE NURSE_ID=?";
                PreparedStatement ps = getConnection().prepareStatement(update);
                ps.setString(1, nurse.getNurseName());
                ps.setString(2, nurse.getNurseSurname());
                ps.setString(3, nurse.getNurseExp());
                ps.setString(4, nurse.getNurseId());
                int flag = ps.executeUpdate();
                return (flag>0);
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
                return false;
        }
        
        
               
    }
    
 