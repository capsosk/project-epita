package fr.epita.project.launcher;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import fr.epita.project.dataModel.*;
import fr.epita.project.service.*;


public class Main {

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, NoSuchAlgorithmException {
		
		FileIdentityDAO dao = new FileIdentityDAO("test/tmp/identities.txt");
		Identity id1 = new Identity(null, null, null);
		
		id1.setDisplayName("Santaaaa");
		id1.setEmail("heii@gmaill");
		id1.setUid("123");
		id1.printIdentity();
		//dao.create(id1);
		
		IdentityJDBCDAO dataB = new IdentityJDBCDAO();
		//dataB.delete(id1);
		dataB.printDB();
		// md5 pwd solution
		System.out.println(User.password("abcd1234"));
	}

}
