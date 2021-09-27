package studiplayer.audio;

import java.util.Comparator;

public class DurationComparator implements Comparator<AudioFile> {
	
	@Override
	public int compare(AudioFile o1, AudioFile o2) {
		return o1.getFormattedDuration().compareTo(o2.getFormattedDuration());
	}
}
