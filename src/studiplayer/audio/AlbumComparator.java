package studiplayer.audio;

import java.util.Comparator;

public class AlbumComparator implements Comparator<AudioFile> {
	
	@Override
	public int compare(AudioFile o1, AudioFile o2) {

		if (o1 instanceof TaggedFile && !(o2 instanceof TaggedFile)) {
			return 1;
		} else if (!(o1 instanceof TaggedFile) && o2 instanceof TaggedFile) {
			return -1;
		} else if (o1 instanceof TaggedFile && o2 instanceof TaggedFile) {
			TaggedFile tf1 = (TaggedFile) o1;
			TaggedFile tf2 = (TaggedFile) o2;
			
			if(tf1.getAlbum() != null && tf2.getAlbum() != null) {
				return tf1.getAlbum().compareTo(tf2.getAlbum());
			} else if (tf1.getAlbum() != null && tf2.getAlbum() == null) {
				return 1;
			} else if (tf1.getAlbum() == null && tf2.getAlbum() != null) {
				return -1;
			} else {
				return 0;
			}
			
			
		} else {
			return 0;
		}
	}
}
