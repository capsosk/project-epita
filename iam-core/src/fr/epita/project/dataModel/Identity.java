package fr.epita.project.dataModel;

public class Identity {
	
	private String displayName;
	
	private String email;
	private String uid;
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	
	
	@Override
	public String toString() {
		return "Identity [displayName=" + displayName + ", email=" + email + ", uid=" + uid + "]";
	}
}
