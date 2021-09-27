package studiplayer.audio;



public class AudioFileFactory {

	public static AudioFile getInstance(String pathname) throws NotPlayableException {
		String extension = pathname.substring(pathname.lastIndexOf("."), pathname.length());
		try {
			if (extension.equalsIgnoreCase(".wav")) {
				WavFile wf = new WavFile(pathname);
				return wf;
			} else if (extension.equalsIgnoreCase(".mp3") || extension.equalsIgnoreCase(".ogg")) {
				TaggedFile tf = new TaggedFile(pathname);
				return tf;
			} 		
			throw new NotPlayableException(pathname, "Unknown suffix for AudioFile: ");
		} catch (NotPlayableException e) {
			e.printStackTrace();
			throw new NotPlayableException(pathname, e.getMessage(), e);
		}
	}
}
