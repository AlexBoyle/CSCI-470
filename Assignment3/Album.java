/**
 * 
 * This class stores all the needed information for an album
 *
 */
public class Album {
	
	// Storing vars
	private String name;
	private String artist;
	private String genre;
	
	// Create an album
	public Album(String name, String artist, String genre) {
		this.setName(name);
		this.setArtist(artist);
		this.setGenre(genre);
	}
	// Create an empty album
	public Album() {
		this(null, null, null);
	}
	
	// setters
	public void setName(String str) {this.name = str;}
	public void setArtist(String str) {this.artist = str;}
	public void setGenre(String str) {this.genre = str;}
	
	// Getters
	public String getName() {return this.name;}
	public String getArtist() {return this.artist;}
	public String getGenre() {return this.genre;}
}
