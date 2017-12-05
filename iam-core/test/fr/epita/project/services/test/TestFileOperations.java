package fr.epita.project.services.test;

import java.io.IOException;

import fr.epita.project.dataModel.Identity;
import fr.epita.project.service.FileIdentityDAO;

public class TestFileOperations {

	public static void main(String[] args) throws IOException {
		
		FileIdentityDAO dao = new FileIdentityDAO("test/tmp/identities.txt");
		Identity id1 = new Identity();
		
		id1.setDisplayName("Jakub");
		id1.setEmail("mail");
		id1.setUid("1");
		
		dao.create(id1);
		
	}

}
