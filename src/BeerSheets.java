import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class BeerSheets {
	/**
	 * Be sure to specify the name of your application. If the application name is {@code null} or
	 * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
	 */
	private static final String APPLICATION_NAME = "BeerSheets";
	private static final String kBeerSheetsBeerSheetURL = "https://spreadsheets.google.com/feeds/worksheets/1iTl4MG5wWLW58fO9k2PTMx7uycwStK3GowSbijuuuDY/private/full";

	public static void main(String[] args) throws AuthenticationException, MalformedURLException, IOException, ServiceException {
		BeerSheets beerSheets = new BeerSheets();
		BSAuthenticator bsAuthenticator = new BSAuthenticator();
		Credential credential = bsAuthenticator.authorize();

		beerSheets.updateTop250BeerList(credential);

		System.out.println("Finished");
	}

	public void updateTop250BeerList(Credential credential) {
		SpreadsheetService service =
				new SpreadsheetService("MySpreadsheetIntegration-v1");
		String str = credential.getAccessToken();
		service.setAuthSubToken(credential.getAccessToken());

		// TODO: Authorize the service object for a specific user (see other sections)

		// Define the URL to request.  This should never change.
		URL spreadsheedFeedURL = null;
		try {
			spreadsheedFeedURL = new URL(kBeerSheetsBeerSheetURL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Make a request to the API and get all spreadsheets.
		SpreadsheetFeed feed = null;
		try {
			feed = service.getFeed(spreadsheedFeedURL,
					SpreadsheetFeed.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<SpreadsheetEntry> spreadsheets = feed.getEntries();

		if (spreadsheets.size() == 0) {
			// TODO: There were no spreadsheets, act accordingly.
		}

		// TODO: Choose a spreadsheet more intelligently based on your
		// app's needs.
		SpreadsheetEntry spreadsheet = spreadsheets.get(0);


		WorksheetFeed worksheetFeed = null;
		try {
			worksheetFeed = service.getFeed(
					spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		WorksheetEntry worksheet = worksheets.get(0);

		// Iterate through each worksheet in the spreadsheet.
		// Get the worksheet's title, row count, and column count.
		// Fetch the cell feed of the worksheet.
		URL cellFeedUrl = worksheet.getCellFeedUrl();
		CellFeed cellFeed = null;
		try {
			cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (CellEntry cellEntry : cellFeed.getEntries()) {
			System.out.println(cellEntry.getCell().getValue());
		}

		// Print the fetched information to the screen for this worksheet.

	}
}
