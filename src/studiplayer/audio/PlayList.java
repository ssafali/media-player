package studiplayer.audio;

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class PlayList extends LinkedList<AudioFile> {

	private int current;
	private boolean randomOrder;

	public PlayList() {
		this.current = 0;
		this.randomOrder = false;
	}
	
	public PlayList(String pathname) {
		this();
		
		try {
			this.loadFromM3U(pathname);
		} catch (NotPlayableException e) {
			
		}
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public int getCurrent() {
		return this.current;
	}

	public AudioFile getCurrentAudioFile() {
		int size = this.size(); 	// checking if the index exists
		if (getCurrent() >= size) {
			return null;
		}
		return this.get(this.current);
	}

	public void changeCurrent() {
		if(this.current == this.indexOf(this.getLast())) {				 
			if(randomOrder == true) {							//If the index already points to the last song in the list,
				Collections.shuffle(this);						//the index should be set to the beginning back to the position 
			}													//of the first song (position 0)
																			
			this.current = 0;

		} else {
			this.current++;
		}
	}
	
	public void setRandomOrder(boolean randomOrder) {
		this.randomOrder = randomOrder;
		if(this.randomOrder == true) {
			Collections.shuffle(this);
		}
	}
	public void saveAsM3U(String pathname) {
		String sep = System.getProperty("line.separator");
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(pathname);
			for(int i = 0; i < this.size(); i++) {
				fw.write(this.get(i).getPathname() + sep);
			}
		} catch (IOException e) {
			throw new RuntimeException("Unable to to write to file " + pathname + ":" + e.getMessage());
		} finally {
			try {
				fw.close();
			} catch (Exception e) {
				
			}
		}
	}
	public void loadFromM3U(String pathname) throws NotPlayableException {
		String line;
		Scanner scanner = null;
		File readable;
		
		try {
			scanner = new Scanner(new File(pathname));
			while(scanner.hasNextLine()) {
				
				line = scanner.nextLine();
				readable = new File(line);
				
				if ((!(line.startsWith("#") || line.isBlank())) && readable.canRead()){
					this.add(AudioFileFactory.getInstance(line));
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				scanner.close();			
			} catch(Exception e) {
				//throw new NotPlayableException(pathname, e.getMessage());
			}
		}
	}
	
//	public String toString() {
//		return this.toString();
//	}
	
	public void sort(SortCriterion order) {
		
		switch (order) {
		case AUTHOR:
			Collections.sort(this, new AuthorComparator());
			break;	
		case TITLE:
			Collections.sort(this, new TitleComparator());
			break;
		case ALBUM:
			Collections.sort(this, new AlbumComparator());
			break;
		case DURATION:
			Collections.sort(this, new DurationComparator());
			break;	
		default:
			throw new IllegalArgumentException("Unexpected value as string: " + order);
	}
		
	}
}
