package uk.ac.swan.digitaltrails.accounts;

public class AccountGeneral {
	
	public static final String ACCOUNT_TYPE = "whiterock.digitaltrails";
	
	public static final String ACCOUNT_NAME = "DigiTrails";
	
	public static final String AUTHTOKEN_TYPE_READ_ONLY = "Read Only";
	public static final String AUTHTOKEN_TYPE_READ_ONLY_LABEL = "Read only access to digital trails account";
	public static final String AUTHTOKEN_TYPE_FULL_ACCESS = "Full Access";
	public static final String AUTHTOKEN_TYPE_FULL_ACCESS_LABEL = "Full access to a digital trails account";
	public static final ServerAuthenticate sServerAuthenticate = new DigitalTrailsComServerAuthenticate();

}
