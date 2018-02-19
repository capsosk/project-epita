package fr.epita.project.service;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fr.epita.project.exceptions.DaoCreationException;
import fr.epita.project.service.Configuration;
import fr.epita.project.dataModel.Identity;
import fr.epita.project.logger.Logger;

public class IdentityJDBCDAO implements IdentityDAO{
	private static final Logger LOGGER = new Logger(IdentityJDBCDAO.class);
	
	public void delete(Scanner scanner) throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {
		
		Identity identity = new Identity(null, null, null);
		System.out.println("If you wish to delete an identity, you have to write down each attribute");
		System.out.println("What is the identity name?");
		String name = scanner.next();
		System.out.println("Email?");
		String email = scanner.next();
		System.out.println("UID?(has to be a number)");
		while (!scanner.hasNextInt()) { System.out.println("Please write a number!");scanner.next();}
		String UID = scanner.next();
		identity.setDisplayName(name);
		identity.setEmail(email);
		identity.setUid(UID);
		if (!searchFor(identity)) {
			System.out.println("No such identity, returning to selection menu");
			return;
		}
		System.out.println("Deleting identity " + identity.getDisplayName());
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM IDENTITIES WHERE UID=? AND DISPLAY_NAME=? AND EMAIL = ?");
			preparedStatement.setString(1, identity.getUid());
			preparedStatement.setString(2, identity.getDisplayName());
			preparedStatement.setString(3, identity.getEmail());
			preparedStatement.execute();
					
		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.error("error while deleting!", e);
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
	
	public void create(Scanner scanner) throws DaoCreationException, FileNotFoundException, IOException, ClassNotFoundException, SQLException {
		Connection connection = null;
		Identity identity = new Identity(null, null, null);
		System.out.println("What is the identity name?");
		String name = scanner.next();
		System.out.println("Email?");
		String email = scanner.next();
		System.out.println("UID? (has to be a number)");
		while (!scanner.hasNextInt()) { System.out.println("Please write a number");scanner.next();}
		String ID = scanner.next();
		identity.setDisplayName(name);
		identity.setEmail(email);
		identity.setUid(ID);
		if (searchFor(identity)) {
			System.out.println("Identity already exists, please create a different one");
			System.out.println("Returning to selection");
			return;
		}
		try {
			connection = getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO IDENTITIES(UID, DISPLAY_NAME, EMAIL) values (?,?,?) ");
			preparedStatement.setString(1, identity.getUid());
			preparedStatement.setString(2, identity.getDisplayName());
			preparedStatement.setString(3, identity.getEmail());
			preparedStatement.execute();
			System.out.println("creating identity "+identity.getDisplayName());

		} catch (ClassNotFoundException | SQLException e) {
			LOGGER.error("error in create method :" + e.getMessage());
			final DaoCreationException businessException = new DaoCreationException(identity, e);

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
	public static boolean isInteger(String s, int radix) {
	    if(s.isEmpty()) return false;
	    if(s.equals("")) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}
	
	public void search(Scanner scanner) throws FileNotFoundException, IOException {
		final List<Identity> identities = new ArrayList<>();
		Identity identity = new Identity(null, null, null);
		
		System.out.println("Name of the identity you are looking for?");
		String name = scanner.nextLine();
		System.out.println("Email?");
		String email = scanner.nextLine();
		System.out.println("UID?");
		String UID = null;
		
		while (true) {
			
			UID = scanner.nextLine();
			if(UID.equals("null")) {
				break;
			}
			if (isInteger(UID, 10)) {
				break;
			}	
			System.out.println(UID+" is not an integer, please try again or write (null)");
		}
		
		identity.setDisplayName(name);
		identity.setEmail(email);
		identity.setUid(UID);
		Connection connection = null;
		try {
			connection = getConnection();
			final PreparedStatement preparedStatement = connection
					.prepareStatement("select DISPLAY_NAME, EMAIL, UID FROM IDENTITIES WHERE DISPLAY_NAME = ? OR EMAIL = ? OR UID = ? ");
			
			preparedStatement.setString(1, identity.getDisplayName());
			preparedStatement.setString(2, identity.getEmail());
			preparedStatement.setString(3, identity.getUid());

			final ResultSet resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				final Identity id = new Identity(null, null, null);
				id.setDisplayName(resultSet.getString(1));
				id.setEmail(resultSet.getString(2));
				id.setUid(resultSet.getString(3));
				identities.add(id);
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
		if (identities.isEmpty()) {
			System.out.println("No such identity");
		}
		for (Identity id : identities) {
			id.printIdentity();
		}
	}
	
	public void printDB() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
		final Connection connection = getConnection();

		final String sqlQuery = "select * from IDENTITIES";

		final PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
		final ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()){do {
			System.out.println("Username: " + resultSet.getString(2));
			System.out.println("Email: " + resultSet.getString(3));
			System.out.println("UID: " + resultSet.getString(4));
			System.out.println("ID (in database): " + resultSet.getInt(1));
		} while (resultSet.next());
		} else {System.out.println("Database is empty!");}
		
		connection.close();
		
	}
	
	private static Connection getConnection() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
		final String connectionString = Configuration.getInstance().getProperty("db.host");
		final String userName = Configuration.getInstance().getProperty("db.userName");
		final String password = Configuration.getInstance().getProperty("db.password");

		Class.forName("org.apache.derby.jdbc.ClientDriver");

		final Connection connection = DriverManager.getConnection(connectionString, userName, password);
		return connection;
	}
	
	public void update(Scanner scanner) throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {
		
		Identity identity = new Identity(null, null, null);
		System.out.println("What is the name of the identity you want to change?");
		String name = scanner.next();
		System.out.println("Whats its Email?");
		String email = scanner.next();
		System.out.println("and ID? (has to be a number)");
		while (!scanner.hasNextInt()) {System.out.println("Please write a number!");scanner.next();}
		String ID = scanner.next();
		identity.setDisplayName(name);
		identity.setEmail(email);
		identity.setUid(ID);
		if (!searchFor(identity)) {
			System.out.println("Identity doesnt exist");
			return;
		}
		
		Identity updated = new Identity(null, null, null);
		System.out.println("Leave the category blank if you dont wish to change it");
		System.out.println("How do you want it to be called?");
		scanner.nextLine();
		String nameU = scanner.nextLine();
		if (nameU.equals("")) {
			nameU = name;
		}
		System.out.println("Email?");
		String emailU = scanner.nextLine();
		if (emailU.equals("")) {
			emailU = email;
		}
		System.out.println("UID?(write 'null' if you dont want to change UID)");
		String UID = null;
		while (true) {
			
			UID = scanner.nextLine();
			if(UID.equals("null")) {
				UID = ID;
				break;
			}
			if (isInteger(UID, 10)) {
				break;
			}	
			System.out.println(UID+" is not an integer, please try again or write (null)");
		}
		updated.setEmail(emailU);
		updated.setDisplayName(nameU);
		updated.setUid(UID);
		if (searchFor(updated)) {
			System.out.println("Identity you wish to create already exists!");
			return;
		}
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE IDENTITIES SET UID=?, DISPLAY_NAME=?, EMAIL = ? WHERE UID=? AND DISPLAY_NAME=? AND EMAIL = ?");
			preparedStatement.setString(1, updated.getUid());
			preparedStatement.setString(2, updated.getDisplayName());
			preparedStatement.setString(3, updated.getEmail());
			preparedStatement.setString(4, identity.getUid());
			preparedStatement.setString(5, identity.getDisplayName());
			preparedStatement.setString(6, identity.getEmail());
			preparedStatement.execute();
			System.out.println("Updated!");
					
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
	public boolean searchFor(Identity identity) throws ClassNotFoundException, FileNotFoundException, SQLException, IOException {
		boolean result = false;
		Connection connection = getConnection();
		try {
			connection = getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement("select * FROM IDENTITIES WHERE UID = ? and DISPLAY_NAME = ? and EMAIL = ?");
			preparedStatement.setString(1, identity.getUid());
			preparedStatement.setString(2, identity.getDisplayName());
			preparedStatement.setString(3, identity.getEmail());

			final ResultSet rs = preparedStatement.executeQuery();
			
			if (rs.next() ) {
			    result = true;
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

	

}
