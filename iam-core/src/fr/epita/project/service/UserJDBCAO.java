package fr.epita.project.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.epita.project.logger.Logger;
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
	private void createUser(User user) throws FileNotFoundException, IOException, DaoCreationException {
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
}