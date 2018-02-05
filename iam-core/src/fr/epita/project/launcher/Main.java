package fr.epita.project.launcher;

import java.io.IOException;
import java.sql.SQLException;

import fr.epita.project.dataModel.Identity;
import fr.epita.project.service.FileIdentityDAO;
import fr.epita.project.service.IdentityJDBCDAO;

public class Main {

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		
		FileIdentityDAO dao = new FileIdentityDAO("test/tmp/identities.txt");
		Identity id1 = new Identity(null, null, null);
		
		id1.setDisplayName("Santaaaa");
		id1.setEmail("heii@gmaill");
		id1.setUid("123");
		
		dao.create(id1);
		
		IdentityJDBCDAO dataB = new IdentityJDBCDAO();
		//dataB.delete(id1);
		dataB.printDB();
		
	}

}
