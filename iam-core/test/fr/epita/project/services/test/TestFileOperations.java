package fr.epita.project.services.test;

import java.io.IOException;
import java.util.List;

import fr.epita.project.dataModel.Identity;
import fr.epita.project.service.FileIdentityDAO;

public class TestFileOperations {

	public static void main(String[] args) throws IOException {
		// given: initial context
		final FileIdentityDAO dao = new FileIdentityDAO("test/tmp/identities.txt");
		Identity id1 = new Identity();
		
		id1.setDisplayName("Jakub");
		id1.setEmail("mail");
		id1.setUid("1");
		//we execute the test
		dao.create(id1);
		
		final List<Identity> identities = dao.search(id1);
		
		if (identities.get(0).equals(id1)) {
			System.out.println("success");
		} else {
			System.out.println("fail");
		}
		
		
	}

}
