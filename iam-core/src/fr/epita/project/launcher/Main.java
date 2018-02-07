package fr.epita.project.launcher;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import fr.epita.project.dataModel.*;
import fr.epita.project.exceptions.DaoCreationException;
import fr.epita.project.service.*;


public class Main {

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, NoSuchAlgorithmException, DaoCreationException {
		
		Identity id1 = new Identity(null, null, null);
		
		id1.setDisplayName("Santaaaa");
		id1.setEmail("heii@gmaill");
		id1.setUid("123");
		//id1.printIdentity();
		//dao.create(id1);
		
		IdentityJDBCDAO dataB = new IdentityJDBCDAO();
		//dataB.delete(id1);
		dataB.printDB();
		// md5 pwd solution
		User u1 = new User();
		u1.setUserName("capso");
		u1.setPwdHash(u1.password("ahoj"));
		
		UserJDBCAO user1 = new UserJDBCAO();
		//user1.createUser(u1);
		user1.printDB();
		//System.out.println(u1.getPwdHash());
	}

}
