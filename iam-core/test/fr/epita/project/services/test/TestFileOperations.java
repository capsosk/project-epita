package fr.epita.project.services.test;

import java.io.IOException;
import java.util.List;

import fr.epita.project.dataModel.Identity;
import fr.epita.project.service.UnusedFileIdentityDAO;

public class TestFileOperations {

	public static void main(String[] args) throws IOException {
		// given: initial context
		testCreateUpdateAndSearch();
		
		
	}

	private static void testCreateUpdateAndSearch() throws IOException {
		final UnusedFileIdentityDAO dao = new UnusedFileIdentityDAO("test/tmp/identities.txt");
		Identity id1 = new Identity(null, null, null);
		
		id1.setDisplayName("Jakub");
		id1.setEmail("mail");
		id1.setUid("1");
		//we execute the test
		dao.create(id1);
		//test update
		//id1.setDisplayName("Jakub Novak");
		//dao.update(id1);
		
		//we execute the test
		final Identity criteria = new Identity(null, null, null);
		criteria.setDisplayName("Tom");
		final List<Identity> identities = dao.search(criteria);
		
		if (identities.get(0).equals(id1)) { /* FIX NEEDED */
			System.out.println("success");
		} else {
			System.out.println("fail");
		}
	}

}
