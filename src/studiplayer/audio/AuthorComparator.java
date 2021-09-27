package studiplayer.audio;

import java.lang.NullPointerException;
import java.util.Comparator;

public class AuthorComparator implements Comparator<AudioFile> {
	
	@Override
	public int compare(AudioFile o1, AudioFile o2) {
		if(o1 == null || o2 == null ) {
			throw new NullPointerException("At least one of the given objects is not an instance of AudioFile");
		}
		return o1.getAuthor().compareTo(o2.getAuthor());
	}
}


