package fr.epita.project.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import fr.epita.project.dataModel.Identity;
import fr.epita.project.exceptions.DaoCreationException;


public interface IdentityDAO {

	public void create(Identity identity) throws DaoCreationException, FileNotFoundException, IOException;

	public void update(Identity identity, Identity updated) throws FileNotFoundException, IOException;

	public void delete(Identity identity) throws FileNotFoundException, IOException;

	public List<Identity> search(Identity criteria) throws FileNotFoundException, IOException;
	
	public void printDB() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException;
}

