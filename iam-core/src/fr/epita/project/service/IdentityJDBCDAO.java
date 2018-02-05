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

import fr.epita.project.service.IdentityDAO;
import fr.epita.project.exceptions.DaoCreationException;
import fr.epita.project.service.Configuration;
import fr.epita.project.dataModel.Identity;
import fr.epita.project.logger.Logger;

public class IdentityJDBCDAO implements IdentityDAO{
	private static final Logger LOGGER = new Logger(IdentityJDBCDAO.class);
	
	public void delete(Identity identity) throws FileNotFoundException, IOException {
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM IDENTITIES WHERE UID=? AND DISPLAY_NAME=? AND EMAIL = ?");
			preparedStatement.setString(1, identity.getUid());
			preparedStatement.setString(2, identity.getDisplayName());
			preparedStatement.setString(3, identity.getEmail());
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
	
	public void create(Identity identity) throws DaoCreationException, FileNotFoundException, IOException {
		Connection connection = null;
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

	public List<Identity> search(Identity criteria) throws FileNotFoundException, IOException {
		final List<Identity> identities = new ArrayList<>();
		// TODO reduce the number of lines to avoid repetition
		// the pattern is always the same, improve with your own ideas.
		Connection connection = null;
		try {
			connection = getConnection();
			final PreparedStatement preparedStatement = connection
					.prepareStatement("select UID, DISPLAY_NAME, EMAIL FROM IDENTITIES WHERE DISPLAY_NAME = ? OR EMAIL = ? OR UID = ? ");
			preparedStatement.setString(3, criteria.getUid());
			preparedStatement.setString(1, criteria.getDisplayName());
			preparedStatement.setString(2, criteria.getEmail());

			final ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				final Identity identity = new Identity(null, null, null);
				identity.setDisplayName(resultSet.getString(2));
				identity.setEmail(resultSet.getString(3));
				identity.setUid(resultSet.getString(1));
				identities.add(identity);
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

		return identities;
	}
	
	public void printDB() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
		final Connection connection = getConnection();

		final String sqlQuery = "select * from IDENTITIES";

		final PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
		final ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			System.out.println(resultSet.getString(1));
			System.out.println(resultSet.getString(2));
			System.out.println(resultSet.getString(3));
			System.out.println(resultSet.getInt(4));
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
	
	public void update(Identity identity, Identity updated) throws FileNotFoundException, IOException {
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE IDENTITIES SET UID=? AND DISPLAY_NAME=? AND EMAIL = ? WHERE UID=? AND DISPLAY_NAME=? AND EMAIL = ?");
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

	

}
