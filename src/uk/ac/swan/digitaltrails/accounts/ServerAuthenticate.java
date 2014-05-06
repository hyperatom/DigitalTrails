package uk.ac.swan.digitaltrails.accounts;

import uk.ac.swan.digitaltrails.components.Account;

/**
 * @author Lewis Hancock
 *
 */
public interface ServerAuthenticate {
    /**
     * Sign up a user to the WhiteRock system.
     * @param firstName User's first name
     * @param lastName User's last name
     * @param email User's email address
     * @param pass User's password
     * @param authType Authentication type for the user
     * @return The account signed up
     * @throws Exception The exception thrown in case of error
     */
    public Account userSignUp(final String firstName, final String lastName, final String email, final String pass, String authType) throws Exception;
    /**
     * @param user User's account name
     * @param pass User's password
     * @param authType Authentication type for the user
     * @return The token for the user.
     * @throws Exception The exception thrown in case of error
     */
    public String userSignIn(final String user, final String pass, String authType) throws Exception;
}