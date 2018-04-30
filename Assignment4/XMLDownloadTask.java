
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.SwingWorker;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;

public class XMLDownloadTask extends SwingWorker<String, Object> {
	private String urlString = "";
	
	/**
	 * 
	 * Return string
	 */
	@Override
    public String doInBackground() {
		HttpURLConnection connection = null;
		//Try to get and parse an xml doc
		try {
			// Create url obj
			URL url = new URL(urlString);
			// Open a connection
			connection = (HttpURLConnection) url.openConnection();
			// Send a get request
			connection.setRequestMethod("GET");
			// If the server replied
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// Create a string builder
				StringBuilder xmlResponse = new StringBuilder();
				// Read the reply through a buffer reader
				BufferedReader input = new BufferedReader(
				new InputStreamReader(connection.getInputStream()));
				String strLine;
				while ((strLine = input.readLine()) != null) {
					xmlResponse.append(strLine);
				}
				String xmlString = xmlResponse.toString();
				input.close();
				// create SAX obj and use it to parse the XML
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();
				AlbumHandler hand = new AlbumHandler();
				saxParser.parse(new InputSource(new ByteArrayInputStream(xmlString.getBytes("utf-8"))), hand);
				process(hand.getOutput());

			}
		}
		//catch and discard exceptions
		catch (MalformedURLException e) {}
		catch (IOException e) {}
		catch (Exception e) {}
		finally {
				//close connection
				if (connection != null) {
					connection.disconnect();
				}
		}
		return "";
    }
	
	
	/**
	 * Format outputs from the do in background function
	 * @param arrayList parsed XML
	 */
	protected void process(ArrayList<Album> arrayList) {
		// Loop through results
		for(int i = 0; i < arrayList.size(); i ++) {
			// format and add to the displayed text area
			XMLDownloadPanel.textarea.append(arrayList.get(i).getName() + " | " + arrayList.get(i).getArtist() + " | " + arrayList.get(i).getGenre() + "\n");
        }
		
	}
	
	/**
	 * Called when the file is done being parsed
	 */
    @Override
    protected void done() {}
    
    /**
     * Create the Url being called by this worker
     * @param type type of music
     * @param limit the number of songs being requested
     * @param ex explicit?
     */
    public void setUrl(String type, String limit, String ex) {
    	urlString = "https://rss.itunes.apple.com/api/v1/us/itunes-music/" + type.replace(' ', '-') + "/all/" + limit + "/" + ex + ".atom";
    }
}
