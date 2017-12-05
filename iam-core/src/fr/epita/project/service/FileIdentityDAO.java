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
		return results;
	}
	
	public void update(Identity identity) {
		
	}
	
	public void delete(Identity identity) {
		
	}
	
}
