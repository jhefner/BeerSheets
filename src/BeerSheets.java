import java.io.IOException;
import java.net.MalformedURLException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class BeerSheets {
	/**
	 * Be sure to specify the name of your application. If the application name is {@code null} or
	 * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
	 */
	private static final String APPLICATION_NAME = "BeerSheets";
	
	public static void main(String[] args) throws AuthenticationException, MalformedURLException, IOException, ServiceException {
		BSAuthenticator bsAuthenticator = new BSAuthenticator();
		Credential credential = bsAuthenticator.authorize();
		System.out.println("Finished");
	}
}
