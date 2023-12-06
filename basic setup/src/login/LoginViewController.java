//Author: David Ellis
package login;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import login.SignUpController;

public class LoginViewController {
	private Scene scene;
	private Stage stage;
	private Scene mainScene;
	
	@FXML
	private PasswordField passwordField;
	@FXML
	private TextField usernameField;
	@FXML 
	private Label passwordErrorLabel;
	@FXML 
	private Label usernameErrorLabel;
	@FXML
	private Label invalidLoginLabel;
	
	public LoginViewController(Stage stage, Scene mainScene ) {
        this.stage = stage;
        this.mainScene = mainScene;
    }
	
	//Someone that already has a valid log in is able to sign in to their account. Ensures that there is security when logging in.
	public void checkLogin(ActionEvent event) 
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
	{
		Security secure = new Security();
		
		//Re-prompt user if username field doesn't adhere to requirements
		if(!secure.checkRequirements(usernameField)) {
			resetFields();
			usernameErrorLabel.setText("Username outside of acceptable input range");
			return;
		}
		//Re-prompt user if password field doesn't adhere to requirements
		if(!secure.checkRequirements(passwordField)) {
			resetFields();
			passwordErrorLabel.setText("Password outside of acceptable input range");
			return;
		}
		
		String userCheck = "";
		String passwordCheck = "";
		boolean validLogin = false;
		
		//Written after Honors Contract
		//////////////////////////////////
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
				
			//establishes connection with database
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/practice?useSSL=false&allowPublicKeyRetrieval=true","root","qh93gxV%k!8nzheQaAK");
			//here *name* is database name, *user* is username and *password* is password 
				
			Statement stmt = con.createStatement();
				
			//Execute the query
			ResultSet rs = stmt.executeQuery("select * from log_in");
			
			//check to see if any of the saved login information in the log_in table matches the user input
			while(rs.next()) {
				userCheck = rs.getString(2);
				passwordCheck = "1000:" + rs.getString(3) + ":" + rs.getString(4);
				System.out.println(passwordCheck);
				if(userCheck.equals(usernameField.getText()) && secure.validatePassword(passwordField.getText(), passwordCheck)) {
					validLogin = true;
					break;
				}
			}
				
			//close the connection
			con.close();
		} catch(Exception e) {
				System.out.println(e);
		}
		
		if(validLogin) {
			stage.setScene(mainScene);
		} else {
			resetFields();
			invalidLoginLabel.setText("Please input valid login credentials");
		}
		///////////////////////////////////

		/*WRITTEN BEFORE HONORS CONTRACT
		/////////////////////////////////////////
		Path filePath = Path.of("src/main/java/login/unimportant.txt");
		String filePathString = "src/main/java/login/unimportant.txt"; 
		FileReader fileReader = new FileReader(filePathString);
		
		List<String> allSavedLogins = Files.readAllLines(filePath);
		
		String read = "";
		
		//check to see if any of the saved login info in unimportant.txt matches the user input
		for(Iterator<String> iter = allSavedLogins.iterator(); iter.hasNext(); ) {
			read = iter.next();
			userCheck = read.substring(0, read.indexOf("|"));
			passwordCheck = read.substring(read.indexOf("|") + 1, read.length());
			if(userCheck.equals(usernameField.getText()) && secure.validatePassword(passwordField.getText(), passwordCheck)) {
				validLogin = true;
				break;
			}
		}
		////////////////////////////////////////////
		*/
	}
	
	public void resetFields() {
		usernameField.setText("");
		usernameErrorLabel.setText("");
		passwordField.setText("");
		passwordErrorLabel.setText("");
		invalidLoginLabel.setText("");
	}
	
	public void signUpPage(ActionEvent event) throws IOException
	{		
		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../login/SignUpPage.fxml"));
            SignUpController controller = new SignUpController(stage, mainScene);
            loader.setController(controller);
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}