package uk.ac.swan.digitaltrails.accounts;

import uk.ac.swan.digitaltrails.components.Account;

public interface ServerAuthenticate {
    public Account userSignUp(final String firstName, final String lastName, final String email, final String pass, String authType) throws Exception;
    public String userSignIn(final String user, final String pass, String authType) throws Exception;
}