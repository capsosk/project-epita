package fr.epita.project.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import fr.epita.project.dataModel.Identity;

public class FileIdentityDAO {
	private PrintWriter printWriter;
	private Scanner scanner;
	
	public FileIdentityDAO(String path) throws IOException {
		
		File file = new File(path);
		
		if(!file.exists()) {
			file.getParentFile().mkdirs(); //make parent dirs to our filepath
			file.createNewFile();
			
		}
		
		this.printWriter = new PrintWriter(file);
		this.scanner = new Scanner(file);
		
		
	}
	
	public void create(Identity identity) {
		this.printWriter.println("---------");
		this.printWriter.println(identity.getDisplayName());
		this.printWriter.println(identity.getEmail());
		this.printWriter.println(identity.getUid());
		this.printWriter.println("---------");
		this.printWriter.flush();
	}
	
	public List<Identity> search(Identity criteria){
		List<Identity> results = new ArrayList<Identity>();
		while(scanner.hasNext()) {
			Identity currentIdentity = new Identity();
			scanner.nextLine();
			currentIdentity.setDisplayName(scanner.nextLine());
			currentIdentity.setEmail(scanner.nextLine());
			currentIdentity.setUid(scanner.nextLine());
			scanner.nextLine();
			
			if (checkMatch(criteria, currentIdentity)) {
				results.add(currentIdentity);
			}
			
			
		}
		
		return results;
	}

	private boolean checkMatch(Identity criteria, Identity currentIdentity) {
		boolean result = false;
		if (criteria.getDisplayName() != null) {
			result = currentIdentity.getDisplayName().startsWith(criteria.getDisplayName());
		}
		if (criteria.getEmail() != null) {
			result = currentIdentity.getEmail().startsWith(criteria.getEmail());
		}
		if (criteria.getUid() != null) {
			result = currentIdentity.getUid().equals(criteria.getUid());
		}
		return result;
		
		
	}
	
	public void update(Identity identity) {
		
	}
	
	public void delete(Identity identity) {
		
	}
	
}
