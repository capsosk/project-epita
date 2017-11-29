package fr.epita.project.launcher;

import fr.epita.project.dataModel.Identity;

public class Main {

	public static void main(String[] args) {		

		Identity id1 = new Identity();
		
		id1.setDisplayName("Jakub");
		id1.setEmail("mail");
		id1.setUid("123");
		
		System.out.println(id1);
	}

}
