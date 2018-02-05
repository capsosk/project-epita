package fr.epita.project.exceptions;

import fr.epita.project.dataModel.Identity;

@SuppressWarnings("serial")
public class DaoCreationException extends Exception {

	/**
	 * 
	 */
	Identity faultyIdentity;

	/**
	 *
	 */
	public DaoCreationException(Identity identity, Exception originalCause) {
		faultyIdentity = identity;
		initCause(originalCause);

	}

	@Override
	public String getMessage() {
		return "problem occured while creating that identity in the system " + faultyIdentity.toString();
	}

}