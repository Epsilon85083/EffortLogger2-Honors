package login;

import java.sql.*;

public class mySQLConnectionPoint {
	public static void main(String args[]) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
				
			//establishes connection with database
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/practice?useSSL=false&allowPublicKeyRetrieval=true","root","qh93gxV%k!8nzheQaAK");
			//here *name* is database name, *user* is username and *password* is password 
				
			Statement stmt = con.createStatement();
				
			int what = stmt.executeUpdate("DELETE FROM logs WHERE Log_ID > 1");
			System.out.println("this is what" + what);
				
			//Execute the query
			ResultSet rs = stmt.executeQuery("select * from logs");
			while(rs.next()) {
				System.out.println(rs.getInt(1) + " " + rs.getInt(2) + " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5) + " " + rs.getString(6) + " " + rs.getString(7) + " " +  rs.getString(8) + " " +  rs.getString(9));
			}
				
			//close the connection
			con.close();
		} catch(Exception e) {
				System.out.println(e);
		}
	}
		
	public static void printUser(int user_ID) {
		System.out.print(user_ID);
	}
}
