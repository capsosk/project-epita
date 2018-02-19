package fr.epita.project.launcher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Scanner;

import fr.epita.project.exceptions.DaoCreationException;
import fr.epita.project.service.*;


public class Main {

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, NoSuchAlgorithmException, DaoCreationException {
		
		
		Scanner scanner = new Scanner(System.in);
		UserJDBCDAO userDB = new UserJDBCDAO();
		IdentityJDBCDAO idDB = new IdentityJDBCDAO();
		
		while ( userDB.Login(scanner) != true);
		
		int result = select(scanner);
		
		userORid(scanner, userDB, idDB, result);
		scanner.close();
	}
	
	private static int select(Scanner scanner) {
		boolean bool = true;
		int result = 0;
		
		while (bool == true) {
			System.out.println("What do you want to do? Edit Users (1) or Identities(2)? ");
			System.out.println("Write down your Number");
			while (!scanner.hasNextInt()) {System.out.println("please write a number (1) or (2)");scanner.next();}
			result = scanner.nextInt();
			scanner.nextLine();
			if (result == 1) {
				System.out.println("Users");
				bool = false;
			}else if(result == 2) {System.out.println("Identities"); bool = false;}
			else { System.out.println("only accepting 1 or 2");}
		}
		return result;
	}
	
	private static void userORid(Scanner scanner, UserJDBCDAO userDB, IdentityJDBCDAO idDB,int result)
			throws FileNotFoundException, IOException, DaoCreationException, ClassNotFoundException, SQLException, NoSuchAlgorithmException {
		int again = 0;
		boolean bool = true;
		if (result == 1) {
			while (bool == true) {
				System.out.println("What do you want to do with Users? Change password(1), Delete(2), Create(3) or Search for users(4)?");
				System.out.println("you can also print all users with (5), exit with (6) or go back to selection with (7)");
				while (!scanner.hasNextInt()) scanner.next();
				result = scanner.nextInt();
				scanner.nextLine();
				switch (result) {
	            case 1:  userDB.update(scanner);
	                     break;
	            case 2:  userDB.delete(scanner);
	                     break;
	            case 3:  userDB.createUser(scanner);
	                     break;
	            case 4:  userDB.Search(scanner);
	            		 break;
	            case 5:  userDB.printDB();
                		 break;
	            case 6:  return;
	            
	            case 7:  bool = false; break;
	            default: System.out.println("only accepting NUMBERS 1,2,3,4,5,6,7");
	                     break;
	            }
				
			}
			
		}
			
		else if (result == 2) {
			while (bool == true) {
				System.out.println("What do you want to do with Identities? Update(1), Delete(2), Create(3) or Search(4)?");
				System.out.println("you can also print all identities with (5), exit with (6) or go back to selection with (7)");
				while (!scanner.hasNextInt()) {System.out.println("please write a number");scanner.next();}
				result = scanner.nextInt();
				scanner.nextLine();
				switch (result) {
	            case 1:  idDB.update(scanner);
	                     break;
	            case 2:  idDB.delete(scanner);
	                     break;
	            case 3:  idDB.create(scanner);
	                     break;
	            case 4:  idDB.search(scanner);
                		 break;
	            case 5:  idDB.printDB();
       		 			 break;
	            case 6:  return;
	            case 7:  bool = false; break;
	            default: System.out.println("only accepting NUMBERS 1,2,3,4,5,6,7");
	                     break;
	            }
			}
		}else {return;}
		
		System.out.println("What do you want to edit now? Users(1), Identities(2) or write any other number to exit");
		while (!scanner.hasNextInt()) {System.out.println("please write a number (1) or (2)");scanner.next();}
		again = scanner.nextInt();
		scanner.nextLine();
		userORid(scanner, userDB, idDB, again);
		
		
			
			
		
	}

}
