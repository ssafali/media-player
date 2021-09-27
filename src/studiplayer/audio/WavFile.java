package studiplayer.audio;


import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile {

	public WavFile() {
		super();
	}

	public WavFile(String pathName) throws NotPlayableException {
		super(pathName);
		try {
			if (pathName.contains(".cut.")) {
				throw new NotPlayableException(pathName, "File is not playable!");
			}		
		} catch (NotPlayableException e) {
			throw new NotPlayableException(pathName, e.getMessage());
		}
		
	}

	public static String readAndSetDurationFromFile(String pathname) throws NotPlayableException  {
		try {
			WavParamReader.readParams(pathname);
		} catch (Exception e) {
			e.printStackTrace();
			throw new NotPlayableException(pathname, e.getMessage(), e);
			
		}
		
		WavFile.frameRate = WavParamReader.getFrameRate();
		WavFile.frameNumber = WavParamReader.getNumberOfFrames();
		long result = computeDuration(WavFile.frameNumber, WavFile.frameRate);
		return SampledFile.timeFormatter(result);
	}

	public String toString() {
		if ((super.album == null) || super.album.isEmpty()) { // if we don't have an album name.
			return super.toString() + " - " + getFormattedDuration();
		}
		return super.toString() + " - " + this.album + " - " + getFormattedDuration();
	}

	public static long computeDuration(long numberOfFrames, float frameRate) {
		long result = (long) ((numberOfFrames / frameRate) * 1000000); // formula to calculate the duration.
		return result;
	}

	@Override
	public String[] fields() {
		if (this.album == null) {
			this.album = "";
		}
		String stringDuration = getFormattedDuration() + "";
		String[] fields = { super.author, super.title, super.album, stringDuration };
		return fields;
	}
}
