package fr.epita.project.service;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.epita.project.dataModel.Identity;
import fr.epita.project.service.IdentityDAO;
import fr.epita.project.service.IdentityXMLDAO;

public class TestXml {

	public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

		// given
		final IdentityDAO dao = new IdentityXMLDAO();

		// when
		final List<Identity> identities = dao.search(new Identity("Thomas", null, null));

		// then
		if (identities.isEmpty()) {
			System.out.println("failure");
		} else {
			System.out.println("success");
		}

		System.out.println(identities);

	}

}
