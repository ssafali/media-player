package studiplayer.audio;

import java.util.Comparator;


public class TitleComparator implements Comparator<AudioFile> {
	@Override
	public int compare(AudioFile o1, AudioFile o2)  {
		if(o1 == null || o2 == null) {
			throw new NullPointerException("At least one of the given objects is not an instance of AudioFile");
		}
		return o1.getTitle().compareTo(o2.getTitle());
	}
}
