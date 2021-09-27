package studiplayer.audio;

import java.util.Map;

public class TaggedFile extends SampledFile {
	protected String album;
	
	public TaggedFile() {
		super();
	}

	public TaggedFile(String pathName) throws NotPlayableException {
		super(pathName);
		
		if (pathName.contains(".cut.")) {
			throw new NotPlayableException(pathName, "File is not playable!");
		}
		
		try {
			this.readAndStoreTags(super.getPathname());
			
		} catch (NotPlayableException e) {
			throw new NotPlayableException(pathName, e.getMessage());
		}
	}

	public void readAndStoreTags(String pathName) throws NotPlayableException {			
		String author = null;
		String album = null;
		String title = null;
		
		Map<String, Object> tags = null;
		try {
			tags = studiplayer.basic.TagReader.readTags(pathName);
		} catch (Exception e) {
			e.printStackTrace();
			//throw new NotPlayableException(pathName, e.getMessage(), e);
		}		
		author = (String) tags.get("author");
		album = (String) tags.get("album");
		title = (String) tags.get("title");
		super.duration = (long) tags.get("duration");
			
		// Following three if conditions are where we check whether the music file already has an implemented attribute or not.
		// If it has, then they're our priorities and therefore author, album and title names are assigned to those in the music file.
			
		if((author != null)) {
			author = author.trim();
			super.author = author;
		}		
		if((album != null)) {
			album = album.trim();
			super.album = album;
		}
		if((title != null)) {
			title = title.trim();
			super.title = title;
		}
		
	}
	public String getAlbum() {
		return super.album;
	}

	public String toString() {
		if((super.album == null) || super.album.isEmpty()) {				// if we have don't have an album name.
			return super.toString() + " - "  + getFormattedDuration();
	}
		return super.toString() + " - "  + getAlbum() + " - " + getFormattedDuration();
	}

	@Override
	public String[] fields() {
		String stringDuration = getFormattedDuration() + "";
		String[] fields = {super.author, super.title, super.album, stringDuration};
		return fields;
	}
}