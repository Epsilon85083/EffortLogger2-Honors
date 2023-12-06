//Author: Joseph Felix
package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import application.DataUtil;
import application.EffortLog;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class DefectConsoleController {
	public Button backButton;
	public Button readLogsButton;
	public Button createLogButton;
	public TextField defectNameTextField;
	private Stage stage;
	public TextArea defectDescriptionTextArea;
	public TextArea logTextArea;
	private String key = "reggol456troffe6";
	
	//ComboBoxes
	public ComboBox<String> projectComboBox;
	
	//time tracking variables
	private Scene mainScene;
    
	//constructor that stores reference to mainline scene
    public DefectConsoleController(Stage stage, Scene mainScene ) {
        this.stage = stage;
        this.mainScene = mainScene;
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) throws IOException {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EffortLoggerConsole.fxml"));
            // Pass the stage or main scene to the controller
            EffortConsoleController controller = new EffortConsoleController(stage, mainScene);
            loader.setController(controller);
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    //initialize ComboBoxes with starting values
	@FXML
	public void initialize() {
		projectComboBox.getItems().removeAll(projectComboBox.getItems());
		projectComboBox.getItems().addAll("Business Project", "Development Project");
		projectComboBox.getSelectionModel().select("Business Project");
	    

	}
	
	//creates log and stores it in a file
	public void handleDefectLog() {
		
		//retrieves values from ComboBoxes
		String project = projectComboBox.getValue();
		String defectName = defectNameTextField.getText();
		String defectDescription = defectDescriptionTextArea.getText();
		
		//After adding SQL functionality
		///////////////////////////////////////////
	        String sqlStatement = "INSERT INTO defects(Project_ID, Title, Symptoms, Is_Closed, Defect_Category) VALUES (?, ?, ?, ?, ?)";
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
					
				//establishes connection with database
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/practice?useSSL=false&allowPublicKeyRetrieval=true","root","qh93gxV%k!8nzheQaAK");
					
				PreparedStatement stmt = con.prepareStatement(sqlStatement);
				
				//place values into the prepared statement
				stmt.setInt(1, 1);
				stmt.setString(2, defectName);
				stmt.setString(3, defectDescription);
				stmt.setInt(4, 1);
				stmt.setString(5, "category");
				
				int rowsAffected = stmt.executeUpdate();
					
				//Execute the query
				ResultSet rs = stmt.executeQuery("select * from defects");
				while(rs.next()) {
					System.out.println(rs.getInt(1) + " " + rs.getInt(2) + " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getInt(5) + " " + rs.getString(6));
				}
					
				//close the connection
				con.close();
			} catch(Exception e) {
					System.out.println(e);
			}
		///////////////////////////////////////////
		
//		//creates EffortLog from values
//		DefectLog defectLog = new DefectLog(project, defectName, defectDescription);
//
//		//creates string representation of log
//		String log = defectLog.createLog();
//
//		//saves in file
//		try {
//			//encrypts log before storing
//			DataUtil.saveData(EncryptionUtil.encrypt(key, log), "defectLoggerData.txt");
//			System.out.println(log);
//			DataUtil.createBackup("defectLoggerData.txt");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
	}
	
	//reads encrypted logs and decrypts them so that they are viewable
	//Edited by David to add try/catch
	public void handleReadLogs() {
			String[] effortLogs = DataUtil.loadData("defectLoggerData.txt");

			
		//decrypts each effort log from file
		try {
			for(int i = 0; i < effortLogs.length; i++) {
				effortLogs[i] = EncryptionUtil.decrypt(key, effortLogs[i]);
			}
		} catch(NullPointerException e) {
			System.err.println("Error: There are not any stored defect logs");
			e.printStackTrace();
		}
		
//		for(int i = 0; i < effortLogs.length; i++) {
//			 System.out.println(effortLogs[i]);
//		}
		
		//loades them onto TextArea so they are viewable
		StringBuilder sb = new StringBuilder();
        for (String entry : effortLogs) {
            sb.append(entry).append("\n");
        }
        logTextArea.setText(sb.toString());
		
	}
	
    
   
}

    