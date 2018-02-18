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
		System.out.println("What is the identity name?");
		String name = scanner.next();
		System.out.println("Email?");
		String email = scanner.next();
		System.out.println("UID?(has to be a number)");
		while (!scanner.hasNextInt()) { scanner.next();}
		String UID = scanner.next();
		identity.setDisplayName(name);
		identity.setEmail(email);
		identity.setUid(UID);
		if (!searchFor(identity)) {
			return;
		}
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
	
	public void create(Scanner scanner) throws DaoCreationException, FileNotFoundException, IOException {
		Connection connection = null;
		Identity identity = new Identity(null, null, null);
		System.out.println("What is the identity name?");
		String name = scanner.next();
		System.out.println("Email?");
		String email = scanner.next();
		System.out.println("ID? (has to be a number)");
		while (!scanner.hasNextInt()) { scanner.next();}
		String ID = scanner.next();
		identity.setDisplayName(name);
		identity.setEmail(email);
		identity.setUid(ID);
		try {
			connection = getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO IDENTITIES(UID, DISPLAY_NAME, EMAIL) values (?,?,?) ");
			preparedStatement.setString(1, identity.getUid());
			preparedStatement.setString(2, identity.getDisplayName());
			preparedStatement.setString(3, identity.getEmail());
			preparedStatement.execute();

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

	public void search(Scanner scanner) throws FileNotFoundException, IOException {
		final List<Identity> identities = new ArrayList<>();
		Identity identity = new Identity(null, null, null);
		System.out.println("Name you are looking for?");
		scanner.nextLine();
		String name = scanner.nextLine();
		System.out.println("Email?");
		String email = scanner.nextLine();
		System.out.println("ID?(has to be a number in order to work)");	
		String ID = scanner.nextLine();
		

		
		
		identity.setDisplayName(name);
		identity.setEmail(email);
		identity.setUid(ID);
		Connection connection = null;
		try {
			connection = getConnection();
			final PreparedStatement preparedStatement = connection
					.prepareStatement("select UID, DISPLAY_NAME, EMAIL FROM IDENTITIES WHERE DISPLAY_NAME = ? OR EMAIL = ? OR UID = ? ");
			preparedStatement.setString(3, identity.getUid());
			preparedStatement.setString(1, identity.getDisplayName());
			preparedStatement.setString(2, identity.getEmail());

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

		for (Identity id : identities) {
			id.printIdentity();
		}
	}
	
	public void printDB() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
		final Connection connection = getConnection();

		final String sqlQuery = "select * from IDENTITIES";

		final PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
		final ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			System.out.println("Username: " + resultSet.getString(2));
			System.out.println("Email: " + resultSet.getString(3));
			System.out.println("UID: " + resultSet.getString(4));
			System.out.println("ID (in database): " + resultSet.getInt(1));
		}
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
		/*while (!scanner.hasNextInt()) { scanner.next();}*/
		String ID = scanner.next();
		identity.setDisplayName(name);
		identity.setEmail(email);
		identity.setUid(ID);
		if (!searchFor(identity)) {
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
		System.out.println("ID?(has to be a number)");
		String IDU = null;
		while(!scanner.hasNextInt()) {scanner.next();}
		IDU = scanner.next();
		updated.setEmail(emailU);
		updated.setDisplayName(nameU);
		updated.setUid(IDU);
		
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
		boolean result = true;
		Connection connection = getConnection();
		try {
			connection = getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement("select * FROM IDENTITIES WHERE UID = ? and DISPLAY_NAME = ? and EMAIL = ?");
			preparedStatement.setString(1, identity.getUid());
			preparedStatement.setString(2, identity.getDisplayName());
			preparedStatement.setString(3, identity.getEmail());

			final ResultSet rs = preparedStatement.executeQuery();
			
			if (!rs.next() ) {
				System.out.println("No such identity!");
				System.out.println("Returning to menu, try printing the DB");
			    result = false;
			}  
			else {result = true;}
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
