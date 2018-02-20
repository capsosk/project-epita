package fr.epita.project.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import fr.epita.project.dataModel.User;
import fr.epita.project.exceptions.DaoCreationException;
import fr.epita.project.logger.Logger;

public class UserJDBCDAO {
	
	private static final Logger LOGGER = new Logger(IdentityJDBCDAO.class);
	
	private static Connection getConnection() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
		final String connectionString = Configuration.getInstance().getProperty("db.host");
		final String userName = Configuration.getInstance().getProperty("db.userName");
		final String password = Configuration.getInstance().getProperty("db.password");

		Class.forName("org.apache.derby.jdbc.ClientDriver");

		final Connection connection = DriverManager.getConnection(connectionString, userName, password);
		return connection;
	}
	
	public void createUser(Scanner scanner) throws FileNotFoundException, IOException, DaoCreationException, NoSuchAlgorithmException {
		Connection connection = null;
		User user = new User();
		System.out.println("What will be the user name?");
		String username = scanner.next();
		user.setUserName(username);
		
		while (Search(user)) {
			System.out.println("User already exists!");
			System.out.println("Please select different username");
			username = scanner.next();
			user.setUserName(username);
		}
		
		System.out.println("Password?");
		String password = scanner.next();
		user.setPwdHash(user.password(password));
		
		System.out.println("Adding user now!");
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
		boolean result = false;
		try {
			connection = getConnection();
			final PreparedStatement preparedStatement = connection
					.prepareStatement("select USERNAME FROM USERS WHERE USERNAME = ?");
			
			preparedStatement.setString(1, user.getUserName());

			final ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				result = true;
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
	public void Search(Scanner scanner) throws FileNotFoundException, IOException {
		Connection connection = null;
		boolean result = false;
		System.out.println("What is the name of the user you are searching for?");
		String username = scanner.next();
		try {
			connection = getConnection();
			final PreparedStatement preparedStatement = connection
					.prepareStatement("select USERNAME FROM USERS WHERE USERNAME = ?");
			
			preparedStatement.setString(1, username);

			final ResultSet resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				result = true;
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

		if (result) {
			System.out.println("User " + username + " is in the database.");
		} else {System.out.println("User " + username + " is not in the database, try again.");}
		
	}
	public void printDB() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
		final Connection connection = getConnection();

		final String sqlQuery = "select * from USERS";

		final PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
		final ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) { do {
			System.out.println("ID: " + resultSet.getString(1));
			System.out.println("Username: " + resultSet.getString(2));
			System.out.println("Password (hash): " + resultSet.getString(3));
		}
		while (resultSet.next());
		} else {System.out.println("No users in database!");}
			
		connection.close();
		
	}
	
	
	public boolean Login(Scanner scanner) throws ClassNotFoundException, FileNotFoundException, SQLException, IOException, NoSuchAlgorithmException {
		boolean result = false;
		Connection connection = getConnection();
		User user = new User();
		
		
		
		System.out.println("Enter your name: ");
		String userName = scanner.next();
		user.setUserName(userName);
		while (!Search(user)) {
			System.out.println("Wrong username!");
			System.out.println("Please try again");
			userName = scanner.next();
			user.setUserName(userName);
		}
		System.out.println("Enter your password: ");
		String pwd = scanner.next();
		user.setPwdHash(user.password(pwd));
		
		
		try {
			connection = getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement("select PWD FROM USERS WHERE USERNAME = ?");
			preparedStatement.setString(1, user.getUserName());

			final ResultSet rs = preparedStatement.executeQuery();
		
			
			
			while(rs.next()) {
				String equals = rs.getString("PWD");
				if (!user.getPwdHash().equals(equals)) {
					result = false;
					
				} else {result = true; break;}
				
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
		
	if (!result) System.out.println("Wrong username / password! please try again");
	
	return result;
	}
	public boolean Login(User user) throws ClassNotFoundException, FileNotFoundException, SQLException, IOException, NoSuchAlgorithmException {
		boolean result = true;
		Connection connection = getConnection();
		try {
			connection = getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement("select PWD FROM USERS WHERE USERNAME = ?");
			preparedStatement.setString(1, user.getUserName());

			final ResultSet rs = preparedStatement.executeQuery();
			
			while(rs.next()) {
				String equals = rs.getString("PWD");
				if (!user.getPwdHash().equals(equals)) {
					result = false;
					
				} else {result = true; break;}
				
			}  
			
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.error("error while searching(login with user)", e);
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
	public void update(Scanner scanner) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
		User user = new User();
		System.out.println("What is the name of the user you want to change?");
		String username = scanner.next();
		user.setUserName(username);
		while (!Search(user) || user.getUserName().equals("root")) {
			System.out.println("Wrong username / cannot change the password of user root");
			System.out.println("Please try again");
			username = scanner.next();
			user.setUserName(username);
		}
		System.out.println("Please enter your new password");
		String password = scanner.next();
		user.setPwdHash(user.password(password));
		System.out.println("Changing password now!");
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE USERS SET PWD=? WHERE USERNAME=?");
			
			preparedStatement.setString(1, user.getPwdHash());
			
			preparedStatement.setString(2, user.getUserName());
			
			
			preparedStatement.execute();
					
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.error("error while changing password", e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (final SQLException e) {
				LOGGER.error("error while closing connection (change password)", e);
			}
		}
	}
	
	public void delete(Scanner scanner) throws FileNotFoundException, IOException, NoSuchAlgorithmException, ClassNotFoundException, SQLException {
		Connection connection = null;
		User user = new User();
		
		System.out.println("What is the name of the user you want to delete?");
		String username = scanner.next();
		user.setUserName(username);
		while (!Search(user) || user.getUserName().equals("root")) {
			System.out.println("Wrong username or cannot delete user 'root'");
			System.out.println("Please try again");
			username = scanner.next();
			user.setUserName(username);
		}
		System.out.println("Please enter password of this user");
		String password = scanner.next();
		user.setPwdHash(user.password(password));
		if (Login(user) != true) {
			System.out.println("Wrong username/password! returning to selection");
			return;
		}
		try {
			connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM USERS WHERE USERNAME = ? AND PWD = ?");
			preparedStatement.setString(1, user.getUserName());
			preparedStatement.setString(2, user.getPwdHash());
			
			preparedStatement.execute();
			System.out.println("Deleting user " + user.getUserName());
					
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.error("error while deleting", e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (final SQLException e) {
				LOGGER.error("error while closing connection (delete)", e);
			}
		}

		
	}
}
