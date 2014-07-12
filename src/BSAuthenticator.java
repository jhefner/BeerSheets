import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

public class BSAuthenticator {
	//constants
	

	/** Directory to store user credentials. */
	private static final java.io.File DATA_STORE_DIR =
			new java.io.File(System.getProperty("user.home"), ".store/beersheets");

	/**
	 * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
	 * globally shared instance across your application.
	 */
	private static FileDataStoreFactory dataStoreFactory;

	/** Global instance of the HTTP transport. */
	private static HttpTransport httpTransport;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private static final String kBSAuthenticatorRedirectURI = "urn:ietf:wg:oauth:2.0:oob";
	private static final String kBSAuthenticationScope = "https://spreadsheets.google.com/feeds";

	public BSAuthenticator() {
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			System.exit(1);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		try {
			dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public Credential authorize() throws IOException {
		// load client secrets
		InputStream inputStream = new FileInputStream(new File("src/resources/client_secrets.json"));
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, inputStreamReader);

	    // set up authorization code flow
	    ArrayList<String> scopes = new ArrayList<String>();
	    scopes.add(kBSAuthenticationScope);
	    GoogleAuthorizationCodeFlow flow = 
	    		new GoogleAuthorizationCodeFlow.Builder(
	    				httpTransport,  
	    				JSON_FACTORY, 
	    				clientSecrets, 
	    				scopes).setDataStoreFactory(dataStoreFactory).build();
	    		
	    // authorize
	    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}
}
