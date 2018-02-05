package fr.epita.project.dataModel;


import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class User {
	private String userName;
	private String pwdHash;
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPwdHash() {
		return pwdHash;
	}
	
	public void setPwdHash(String pwdHash) {
		this.pwdHash = pwdHash;
	}
	public static String password(String string) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(StandardCharsets.UTF_8.encode(string));
		return String.format("%032x", new BigInteger(1, md5.digest()));
	}
	
	
}