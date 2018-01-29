package fr.epita.project.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.epita.project.dataModel.Identity;
import fr.epita.project.logger.Logger;

public class IdentityJDBCDAO {
	private static final Logger LOGGER = new Logger(IdentityJDBCDAO.class);
	public void delete(Identity identity) {
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM IDENTITIES WHERE UID=? OR DISPLAY_NAME=? OR EMAIL = ?");
			preparedStatement.setString(1, identity.getUid());
			preparedStatement.setString(2, identity.getDisplayName());
			preparedStatement.setString(3, identity.getEmail());
			preparedStatement.execute();
					
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
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
	public void create(Identity identity) {
		Connection connection = null;
		try {
			connection = getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO IDENTITIES(UID, DISPLAY_NAME, EMAIL) values (?,?,?) ");
			preparedStatement.setString(1, identity.getUid());
			preparedStatement.setString(2, identity.getDisplayName());
			preparedStatement.setString(3, identity.getEmail());
			preparedStatement.execute();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
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

	public List<Identity> search(Identity criteria) {
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
				final Identity identity = new Identity();
				identity.setDisplayName(resultSet.getString(2));
				identity.setEmail(resultSet.getString(3));
				identity.setUid(resultSet.getString(1));
				identities.add(identity);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
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
	
	public void printDB() throws ClassNotFoundException, SQLException {
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
	
	private static Connection getConnection() throws ClassNotFoundException, SQLException {
		final String connectionString = "jdbc:derby://localhost:1527/iam";
		final String userName = "root";
		final String password = "root";

		Class.forName("org.apache.derby.jdbc.ClientDriver");

		final Connection connection = DriverManager.getConnection(connectionString, userName, password);
		return connection;
	}

}
