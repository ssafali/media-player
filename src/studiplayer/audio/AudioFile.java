package studiplayer.audio;

// @author Safak Safali
import java.io.File;
import java.util.regex.Matcher;

public abstract class AudioFile {
	
	// Attributes (they're protected, so that we can change them in TaggedFile.java if necessary).
	protected String author;
	protected String title;
	protected String album;
	protected long duration;
	
	private String pathName;
	private String fileName;
	
	// First constructor without parameter
	public AudioFile(){
		this.pathName = "";
		this.fileName = "";
		this.author = "";
		this.title = "";
	}
	
	// Second constructor with parameter
	public AudioFile(String audioMusicPath) throws NotPlayableException {
		if(audioMusicPath.isEmpty()) {
			this.pathName = audioMusicPath;
			this.fileName = pathName;
			this.author = "";
			this.title = "";
		} else {
			this.parsePathname(audioMusicPath);
			this.parseFilename(this.pathName);
		}
		
		
		File readable = new File(getPathname());
		if (!readable.canRead()){
			throw new NotPlayableException(this.pathName,  "File cannot be read! No file is found in the given path.");
		}
		 
	}
	
	// Abstract methods.
	public abstract void play() throws NotPlayableException;
	public abstract void togglePause();
	public abstract void stop();
	public abstract String getFormattedDuration();
	public abstract String getFormattedPosition();
	public abstract String[] fields();
	
	
	public boolean isWindows() {
		String osName = System.getProperty("os.name");
		return osName.toLowerCase().contains("windows");
	}
	
	
	// parsePathname replaces the slashes with operating system's accepted format by using Matcher and Properties libraries.
	protected void  parsePathname(String musicPath) {
		String osName = System.getProperty("os.name");	
		musicPath = musicPath.replaceAll("[/\\\\]+",
				Matcher.quoteReplacement(System.getProperty("file.separator")));		
		if(!osName.toLowerCase().contains("windows") && musicPath.contains(":")) {		// pathname formatting for the operating system
			musicPath = "/" + musicPath.replaceFirst(":", "");
		}
		this.pathName = musicPath;	
		
		String sepchar = System.getProperty("file.separator");		//checking if the pathname ends with a slash.
		if(this.pathName.stripTrailing().endsWith(sepchar)) {
			this.fileName = "";
		} else {
			File songName = new File(this.pathName);		//if there's no slash at the end, 
															//then using getName() function to remove the extension.
			this.fileName = songName.getName();
		}
	}
	public String getPathname() {		
		return this.pathName;
	}	
	public void parseFilename(String fileName) {	
		
		// Below parsing the author name.		
		fileName = this.fileName;
				
		if(fileName == null) {
			this.author = "";		
		}
		else if (fileName.contains(".")) {
			fileName = fileName.substring(0, fileName.lastIndexOf("."));		//removing the extension.
		}
		if ((fileName.length() == 0) || fileName.trim().equals("-")) {			//if the filename is only "-" after trimming.
			this.author = "";
		} else if(!fileName.contains(" - ")) {									//when filename doesn't have " - ", author name is empty.
			this.author = "";
		} else {
			String[] parts = fileName.split(" - ");
			this.author = parts[0].trim();
		}						
	
		// Below parsing the title name.
		String title = fileName;		
		if (this.nameIsOnlyDash() || (title.length() == 0) || (title.equals(""))) {		
			this.title = "";
		} else if (!this.nameIsOnlyDash()) {
			String[] parts = title.split(" - ");		// these next 3 lines in case of an example like
														// "C:\music\     -    a   .mp3".
			title = title.trim();						
			title = parts[parts.length-1];
			this.title = title.trim();			
		} else if (this.nameIsOnlyDash()) {				//When filename is "-".
			this.title = "-";
		} else if (!title.contains(" - ")) {			// if we have a file with no author but with title and extension.
			this.title = title.trim();
		} else {			
			if (title.endsWith(".")) { 					// removing the dot if there's one right before the extension.
				title = title.replaceAll(".$", ""); 	// the dollar sign means the last occurrence of ".".			
			}
			this.title = title.trim();
		}		
	}
	public String getFilename() {		
		return this.fileName;
	}
	public String getAuthor() {
		return this.author;
	}
	public boolean nameIsOnlyDash() {	// if the filename is " - " or contains " - " and no other 
										// character when it's trimmed, then we'll return an empty string.
		String singleDash = this.fileName.strip();
		if (this.fileName.contains(" - ") && (singleDash.equals("-"))) {
			return true;
		}
		return false;
	}
	public String getTitle() {
		return this.title;
	}
	public String toString() {
		if (this.author.equals("")) {
			return this.title;
		}
		return this.author + " - " + this.title;
	}
}