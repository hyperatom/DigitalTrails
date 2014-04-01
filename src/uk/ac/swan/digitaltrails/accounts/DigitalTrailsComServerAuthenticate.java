package uk.ac.swan.digitaltrails.accounts;

/**
 * Communicates with DigitalTrails website
 * 
 */
public class DigitalTrailsComServerAuthenticate implements ServerAuthenticate {

	@Override
	public String userSignUp(String firstName, String lastName, String email,
			String pass, String authType) throws Exception {
		String url = "whiterockapi.tmilner.co.uk";
		return null;
	}

	@Override
	public String userSignIn(String user, String pass, String authType)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
