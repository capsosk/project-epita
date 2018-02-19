package fr.epita.project.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import fr.epita.project.exceptions.DaoCreationException;


public interface IdentityDAO {

	public void create(Scanner scanner) throws DaoCreationException, FileNotFoundException, IOException, ClassNotFoundException, SQLException;

	public void update(Scanner scanner) throws FileNotFoundException, IOException, ClassNotFoundException, SQLException;

	public void delete(Scanner scanner) throws FileNotFoundException, IOException, ClassNotFoundException, SQLException;

	public void search(Scanner scanner) throws FileNotFoundException, IOException;
	
	public void printDB() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException;
}

