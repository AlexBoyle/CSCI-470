import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class AlbumHandler  extends DefaultHandler {
	// Class Vars
	public ArrayList<Album> output = new ArrayList<Album>();
	private boolean bAlbum = false;
	private boolean bArtist = false;
	private String album;
	private String artist;
	private String catigory;
	private boolean didCat = false;
	// Starting of an XML element
	public void startElement(String uri, String localName,String qName, Attributes attributes) {
		if (qName == "im:name") {bAlbum = true;album = "";didCat=false;}
		if (qName == "im:artist") {bArtist = true;artist ="";}
		if (!didCat && qName == "category"){catigory = attributes.getValue("term");}
	}

	// Parsing the text in an element
	public void characters(char ch[], int start, int length) {
		if(bAlbum) 
			album = album + new String(ch, start, length);
		if(bArtist) 
			artist = artist + new String(ch, start, length);
	}
	
	// End of XML element
	public void endElement(String uri, String localName, String qName) {
		if (qName == "im:name") {bAlbum = false; didCat = false;}
		if (qName == "im:artist") {bArtist = false;}
		if (!didCat && qName == "category"){output.add(new Album(album, artist, catigory));didCat = true;}
	}
	// allow the output to be gotten
	public ArrayList<Album> getOutput(){
		return output;
	}

}
