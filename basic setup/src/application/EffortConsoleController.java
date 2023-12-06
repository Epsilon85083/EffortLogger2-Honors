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
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class EffortConsoleController {
	public Button startButton;
	public Button stopButton;
	public Button backButton;
	public Button readLogsButton;
	public Label clockLabel;
	public TextField stringField;
	boolean isClockRunning;
	private Stage stage;
	public TextArea logTextArea;
	private String key = "reggol456troffe6";
	
	//ComboBoxes
	public ComboBox<String> projectComboBox;
	public ComboBox<String> lifeCycleStepComboBox;
	public ComboBox<String> effortCategoryComboBox;
	public ComboBox<String> categoryDetailComboBox;
	
	//time tracking variables
	private LocalDateTime startTime;
    private LocalDateTime stopTime;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	private Scene mainScene;
    
	//constructor that stores reference to mainline scene
    public EffortConsoleController(Stage stage, Scene mainScene ) {
        this.stage = stage;
        this.mainScene = mainScene;
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        // Switch back to the main scene
        stage.setScene(mainScene);
    }
	
    //initialize ComboBoxes with starting values
	@FXML
	public void initialize() {
		isClockRunning = false;
		projectComboBox.getItems().removeAll(projectComboBox.getItems());
		projectComboBox.getItems().addAll("Business Project", "Development Project");
		projectComboBox.getSelectionModel().select("Business Project");
	    
		lifeCycleStepComboBox.getItems().removeAll(lifeCycleStepComboBox.getItems());
		lifeCycleStepComboBox.getItems().addAll("Planning", "Information Gathering", "Information Understanding");
		lifeCycleStepComboBox.getSelectionModel().select("Planning");
	    
		effortCategoryComboBox.getItems().removeAll(effortCategoryComboBox.getItems());
		effortCategoryComboBox.getItems().addAll("Plans", "Deliverables", "Interruptions");
		effortCategoryComboBox.getSelectionModel().select("Plans");
	    
		categoryDetailComboBox.getItems().removeAll(categoryDetailComboBox.getItems());
		categoryDetailComboBox.getItems().addAll("Project Plan", "Risk Management Plan", "Comceptual Design Plan");
		categoryDetailComboBox.getSelectionModel().select("Project Plan");
	}
	
	//Handles start activity button that starts timer
	public void handleStartButtonClick() {
		if(!isClockRunning) {
			clockLabel.setText("Clock Started");
			clockLabel.setTextFill(Color.rgb(0, 255, 0));
			startTime = LocalDateTime.now();
			isClockRunning = true;
		}
	}
	
	//handles stop time button that called log handler
	public void handleStopButtonClick() {
		if(isClockRunning) {
			clockLabel.setText("Clock Stopped");
			clockLabel.setTextFill(Color.rgb(255, 0, 0));
			stopTime = LocalDateTime.now();
			handleEffortLog();
			isClockRunning = false;
		}
	}
	
	//creates log and stores it in logs table
	private void handleEffortLog() {
		String timeLog = "Logged from " + formatter.format(startTime) + " to " + formatter.format(stopTime);
		//retrieves values from ComboBoxes
		String project = projectComboBox.getValue();
		String lifeCycleStep = lifeCycleStepComboBox.getValue();
		String effortCategory = effortCategoryComboBox.getValue();
		String categoryDetail = categoryDetailComboBox.getValue();
		
		//After adding SQL functionality
				///////////////////////////////////////////
			        String sqlStatement = "INSERT INTO logs(Project_ID, Title, Date, Time_Start, Time_End, Delta_Time, Effort_Category, LifeCycle_Step) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
							
						//establishes connection with database
						Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/practice?useSSL=false&allowPublicKeyRetrieval=true","root","qh93gxV%k!8nzheQaAK");
							
						PreparedStatement stmt = con.prepareStatement(sqlStatement);
						
						//place values into the prepared statement
						stmt.setInt(1, 1);
						stmt.setString(2, "title");
						stmt.setString(3, "1999-12-05");
						stmt.setString(4, startTime.toString());
						stmt.setString(5, stopTime.toString());
						stmt.setString(6, "15:25:00");
						stmt.setString(7, effortCategory);
						stmt.setString(8,  categoryDetail);
						
						int rowsAffected = stmt.executeUpdate();
							
						//close the connection
						con.close();
					} catch(Exception e) {
							System.out.println(e);
					}
				///////////////////////////////////////////
					
					
		//WRITTEN BEFORE HONORS PROJECT
		///////////////////////////////////
//		//creates EffortLog from values
//		EffortLog effortLog = new EffortLog(project, timeLog, lifeCycleStep, effortCategory, categoryDetail);
//
//		//creates string representation of log
//		String log = effortLog.createLog();
//
//		//saves in file
//		try {
//			//encrypts log before storing
//			DataUtil.saveData(EncryptionUtil.encrypt(key, log), "effortLoggerData.txt");
//			DataUtil.createBackup("effortLoggerData.txt");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		///////////////////////////////////////
	}
	
	//reads encrypted logs and decrypts them so that they are viewable
	public void handleReadLogs() {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
				
			//establishes connection with database
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/practice?useSSL=false&allowPublicKeyRetrieval=true","root","qh93gxV%k!8nzheQaAK");
	
			Statement stmt = con.createStatement();
				
			//Execute the query
			ResultSet rs = stmt.executeQuery("SELECT * FROM logs");
			String output = "";
			while(rs.next()) {
				output += ("Title: " + rs.getString(3) + ", Date: " + rs.getString(4) + ", Logged from" + rs.getString(5) + " to " + rs.getString(6) + ", Category: " + rs.getString(7) + ", LifeCycle: " + rs.getString(8) + "\n");
			}
			logTextArea.setText(output);
				
			//close the connection
			con.close();
		} catch(Exception e) {
				System.out.println(e);
		}
		
		//WRITTEN BEFORE HONORS PROJECT
		///////////////////////////////////
//		String[] effortLogs = DataUtil.loadData("effortLoggerData.txt");
//		
//		//decrypts each effort log from file
//		try {
//			for(int i = 0; i < effortLogs.length; i++) {
//				effortLogs[i] = EncryptionUtil.decrypt(key, effortLogs[i]);
//			}
//		} catch(NullPointerException e) {
//			System.err.println("Error: There are not any stored effort logs");
//			e.printStackTrace();
//		}
//		
//		for(int i = 0; i < effortLogs.length; i++) {
//			 System.out.println(effortLogs[i]);
//		}
//		
//		//loads them onto TextArea so they are viewable
//		StringBuilder sb = new StringBuilder();
//        for (String entry : effortLogs) {
//            sb.append(entry).append("\n");
//        }
//        logTextArea.setText(sb.toString());
	}
	
	public void changeToDefectLogger() throws IOException {
		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DefectLoggerConsole.fxml"));
            // Pass the stage or main scene to the controller
            DefectConsoleController controller = new DefectConsoleController(stage, mainScene);
            loader.setController(controller);
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
    
   
}

    