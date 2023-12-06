//Author: David Ellis
package login;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.sql.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class SignUpController {
	private Scene scene;
	private Scene mainScene;
	private Stage stage;
	private Login login = new Login();
	
	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField1;
	@FXML
	private PasswordField passwordField2;
	@FXML
	private Label incorrectPassword;
	Security secure = new Security();

	public SignUpController(Stage stage, Scene mainScene) {
		this.mainScene = mainScene;
		this.stage = stage;
	}
	
	public void submitLogin(ActionEvent event) 
		throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
	{
		//if the passwords are the same, username meets requirements, and password meets requirements EffortLogger will store their credentials
		if(checkPasswordSimilarity(passwordField1, passwordField2) && secure.checkRequirements(usernameField) && secure.checkRequirements(passwordField1)) {
			String username = usernameField.getText(); 
			String password = passwordField2.getText();
			
			login.setName(username);
			login.setPassword(password);
		
		//writeToFile(login); WRITTEN BEFORE HONORS CONTRACT
			
		//After adding SQL functionality
		///////////////////////////////////////////
			String encryptedPassword = secure.generateStrongPasswordHash(login.getPassword());
			String[] parts = encryptedPassword.split(":");
	        
	        String sqlStatement = "INSERT INTO log_in(Username, salt, encryptedPassword) VALUES (?, ?, ?)";
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
					
				//establishes connection with database
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/practice?useSSL=false&allowPublicKeyRetrieval=true","root","qh93gxV%k!8nzheQaAK");
					
				PreparedStatement stmt = con.prepareStatement(sqlStatement);
				
				//place values into the prepared statement
				stmt.setString(1, username);
				stmt.setString(2, parts[1]);
				stmt.setString(3, parts[2]);
				
				int rowsAffected = stmt.executeUpdate();
					
				//Execute the query
				ResultSet rs = stmt.executeQuery("select * from log_in");
				while(rs.next()) {
					System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + ":" + rs.getString(4));
				}
					
				//close the connection
				con.close();
			} catch(Exception e) {
					System.out.println(e);
			}
		///////////////////////////////////////////
			stage.setScene(mainScene);
			stage.show();
		}
		else {
			usernameField.setText("");
			passwordField1.setText("");
			passwordField2.setText("");
			incorrectPassword.setText("Login credentials did not meet the requirements. Please resubmit.");
		}
	}
	
	public boolean checkPasswordSimilarity(PasswordField pass1, PasswordField pass2) { //Makes certain the user inputs the correct password
		if(pass1.getText().equals(pass2.getText())) 
			return true;
		else
			return false;
	}
	
	/*WRITTEN BEFORE HONORS CONTRACT
	/////////////////////////////////////
	//Writes the username and encrypted password from the textfields into a separate text file
	public void writeToFile(Login login) 
		throws NoSuchAlgorithmException, InvalidKeySpecException 
	{
		//FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PlanningPoker.fxml"));
		String content = login.getName() + "|" + secure.generateStrongPasswordHash(login.getPassword()) + "\n"; 
		Path filePath = Paths.get("src\\main\\java\\login\\unimportant.txt");
		System.out.println(filePath.toString());
			
		try { // Write to "unimportant.txt" otherwise throw an error
		      Files.writeString(filePath, content, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		}
	//////////////////////////////////////
	*/
}
