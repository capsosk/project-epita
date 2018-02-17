package fr.epita.project.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.epita.project.logger.Logger;
import fr.epita.project.dataModel.Identity;
import fr.epita.project.dataModel.User;
import fr.epita.project.exceptions.DaoCreationException;

public class UserJDBCAO {
	
	private static final Logger LOGGER = new Logger(IdentityJDBCDAO.class);
	
	private static Connection getConnection() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
		final String connectionString = Configuration.getInstance().getProperty("db.host");
		final String userName = Configuration.getInstance().getProperty("db.userName");
		final String password = Configuration.getInstance().getProperty("db.password");

		Class.forName("org.apache.derby.jdbc.ClientDriver");

		final Connection connection = DriverManager.getConnection(connectionString, userName, password);
		return connection;
	}
	
	public void createUser(User user) throws FileNotFoundException, IOException, DaoCreationException {
		Connection connection = null;
		try {
			connection = getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO USERS(USERNAME, PWD) values (?,?) ");
			preparedStatement.setString(1, user.getUserName());
			preparedStatement.setString(2, user.getPwdHash());
			preparedStatement.execute();

		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.error("error in create method :" + e.getMessage());
			final DaoCreationException businessException = new DaoCreationException(user, e);

			throw businessException;
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (final SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public boolean Search(User user) throws FileNotFoundException, IOException {
		Connection connection = null;
		boolean result = true;
		try {
			connection = getConnection();
			final PreparedStatement preparedStatement = connection
					.prepareStatement("select USERNAME, PWD FROM USERS WHERE USERNAME = ? AND EMAIL = ? ");
			preparedStatement.setString(1, user.getUserName());
			preparedStatement.setString(2, user.getPwdHash());

			final ResultSet resultSet = preparedStatement.executeQuery();
			if (!resultSet.isBeforeFirst() ) {    
			    result = false; 
			} 
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.error("error while searching", e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
	public void printDB() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
		final Connection connection = getConnection();

		final String sqlQuery = "select * from USERS";

		final PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
		final ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			System.out.println(resultSet.getString(1));
			System.out.println(resultSet.getString(2));
			System.out.println(resultSet.getString(3));
		}
		connection.close();
		
	}
	
	String pwdHash;
	
	public boolean Login(User user) throws ClassNotFoundException, FileNotFoundException, SQLException, IOException {
		boolean result = true;
		Connection connection = getConnection();
		try {
			connection = getConnection();
			final PreparedStatement preparedStatement = connection
					.prepareStatement("select PWD FROM USERS WHERE USERNAME = ?");
			preparedStatement.setString(1, user.getUserName());

			final ResultSet resultSet = preparedStatement.executeQuery();
			if (!resultSet.isBeforeFirst() ) {    
			    result = false; 
			} 
			while(resultSet.next()) {
				if (user.getPwdHash() != resultSet.getString("PWD")) {
					result = false;
				}else {result = true; break;}
			}
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.error("error while searching", e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}
	return result;
	}
}
