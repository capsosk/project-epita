package fr.epita.project.exceptions;

import fr.epita.project.dataModel.*;

@SuppressWarnings("serial")
public class DaoCreationException extends Exception {

	/**
	 * 
	 */
	Identity faultyIdentity = null;
	User faultyUser= null;
	/**
	 *
	 */
	public DaoCreationException(Identity faultyIdentities, Exception originalCause) {
		faultyIdentity = faultyIdentities;
		initCause(originalCause);

	}


	public DaoCreationException(User user, Exception originalCause) {
		faultyUser = user;
		initCause(originalCause);
	}


	@Override
	
	public String getMessage() {
		if (faultyIdentity != null) {
			return "problem occured while creating that identity in the system " + faultyIdentity.toString();}
		if(faultyUser != null) {
			return "problem occured while creating that identity in the system " + faultyIdentity.toString();}
		return null;
	}

}
