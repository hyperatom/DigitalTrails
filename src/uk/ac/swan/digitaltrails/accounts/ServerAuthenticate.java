package uk.ac.swan.digitaltrails.accounts;

public interface ServerAuthenticate {
    public String userSignUp(final String firstName, final String lastName, final String email, final String pass, String authType) throws Exception;
    public String userSignIn(final String user, final String pass, String authType) throws Exception;
}