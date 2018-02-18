package fr.epita.project.launcher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Scanner;

import fr.epita.project.dataModel.*;
import fr.epita.project.exceptions.DaoCreationException;
import fr.epita.project.service.*;


public class Main {

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, NoSuchAlgorithmException, DaoCreationException {
		
		Identity id1 = new Identity(null, null, null);
		
		id1.setDisplayName("Santaaaa");
		id1.setEmail("heii@gmaill");
		id1.setUid("123");
		Scanner scanner = new Scanner(System.in);
		//id1.printIdentity();
		UserJDBCAO userDB = new UserJDBCAO();
		IdentityJDBCDAO idDB = new IdentityJDBCDAO();
		while (userDB.Login(scanner) != true) {
			userDB.Login(scanner);
		}
		int result = select(scanner);
		
		userORid(scanner, userDB, idDB, result);
		
		/*
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
		//System.out.println(u1.getPwdHash());*/
		scanner.close();
	}
	private static int select(Scanner scanner) {
		boolean bool = true;
		int result = 0;
		while (bool == true) {
			System.out.println("What do you want to do? Edit Users (1) or Identities(2)? ");
			System.out.println("Write down your number");
			result = scanner.nextInt();
			if (result == 1) {
				System.out.println("Users");
				bool = false;
			}else if(result == 2) {System.out.println("Identities"); bool = false;}
			else { System.out.println("only accepting 1 or 2");}
		}
		return result;
	}
	
	private static void userORid(Scanner scanner, UserJDBCAO userDB, IdentityJDBCDAO idDB,int result)
			throws FileNotFoundException, IOException, DaoCreationException, ClassNotFoundException, SQLException {
		int again = 0;
		boolean bool = true;
		if (result == 1) {
			while (bool == true) {
				System.out.println("What do you want to do with Users? Update(1), Delete(2) or Create(3)?");
				System.out.println("you can also print all users with (4");
				result = scanner.nextInt();
				
				switch (result) {
	            case 1:  bool = false;userDB.update();
	                     break;
	            case 2:  bool = false;userDB.delete();
	                     break;
	            case 3:  bool = false;userDB.createUser();
	                     break;
	            case 4:  bool = false;userDB.printDB();
                		 break;
	            default: System.out.println("only accepting 1,2,3,4");
	                     break;
	            }
				
			}
			
		}
			
		else if (result == 2) {
			while (bool == true) {
				System.out.println("What do you want to do with Identities? Update(1), Delete(2), Create(3) or Search?");
				System.out.println("you can also print all identities with (5)");
				result = scanner.nextInt();
				
				switch (result) {
	            case 1:  bool = false;idDB.update();
	                     break;
	            case 2:  bool = false;idDB.delete();
	                     break;
	            case 3:  bool = false;idDB.create();
	                     break;
	            case 5:  bool = false;idDB.printDB();
                		 break;
	            case 4:  bool = false;idDB.search();
       		 			 break;
	            default: System.out.println("only accepting 1,2,3,4,");
	                     break;
	            }
			}
		}else {return;}
		
		System.out.println("What do you want to edit now? Users(1), Identities(2) or write any other number to exit");
		again = scanner.nextInt();
		userORid(scanner, userDB, idDB, again);
		
		
			
			
		
	}

}
