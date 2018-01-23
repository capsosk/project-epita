package fr.epita.project.services.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestDataConnection {

	public static void main(String[] args) throws SQLException, ClassNotFoundException{
		
		Connection connection = getConnection();
		
		testConnectSelect(connection);
		
		//insertIntoDB(connection);
		connection.close();
	}

	private static void insertIntoDB(Connection connection) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO IDENTITIES(DISPLAY_NAME, EMAIL, UID) VALUES (?,?,?)");          
		preparedStatement.setString(1, "Novak");
		preparedStatement.setString(2, "kkk@gmail");
		preparedStatement.setString(3, "235");
		preparedStatement.execute();
	}
	
	

	private static Connection getConnection() throws ClassNotFoundException, SQLException {
		String connectionString = "jdbc:derby://localhost:1527/iam;create=true";
		String userName = "root";
		String pwd = "root";
		Class.forName("org.apache.derby.jdbc.ClientDriver");
		Connection connection = DriverManager.getConnection(connectionString, userName, pwd);
		return connection;
	}
	
	private static void testDelete(Connection connection, String toDelete){
		
		String sqlQuery = "DELETE ";
		
	}
	
	
	private static void testConnectSelect(Connection connection) throws SQLException {
		String sqlQuery = "select * from IDENTITIES";
		
		PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			System.out.println(resultSet.getInt(1));
			System.out.println(resultSet.getString(2));
			System.out.println(resultSet.getString(3));
			System.out.println(resultSet.getInt(4));
		}
		
		
	}

}
