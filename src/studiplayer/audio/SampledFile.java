package studiplayer.audio;

import studiplayer.basic.BasicPlayer;

public abstract class SampledFile extends AudioFile {
    
	// Following two attributes are made for WavFile.java
	protected static float frameRate;
    protected static long frameNumber;
    
	public SampledFile() {
		super();
	}
	
	public SampledFile(String pathName) throws NotPlayableException{
		super(pathName);
	}
	
	public void play() throws NotPlayableException {
		try {
			BasicPlayer.play(super.getPathname());
		} catch (Exception e) {
			e.printStackTrace();
			throw new NotPlayableException(super.getPathname(), e.getMessage(), e);
		}
	}
	
	public void togglePause() {
		BasicPlayer.togglePause();
	}
	
	public void stop() {
		BasicPlayer.stop();
	}
	
	public String getFormattedPosition() {
		long position = studiplayer.basic.BasicPlayer.getPosition();
		String formattedPosition = SampledFile.timeFormatter(position);
		return formattedPosition;
	}
	
	public String getFormattedDuration() {
		if(this.getPathname().stripTrailing().endsWith("wav")) {		//if our file's extension is wav, then we call the method from WavFile.java
			try {
				return WavFile.readAndSetDurationFromFile(getPathname());
			} catch (NotPlayableException e) {
				e.printStackTrace();
			}
		}
		String formattedDuration;
		formattedDuration = SampledFile.timeFormatter(super.duration);
		return formattedDuration;
	}
	
	public static String timeFormatter(long microtime) {
		long max = 6000000000L;		//this equals 100 minutes.
		
		if (microtime < 0) {
			throw new RuntimeException("Negative time value provided");
		} else if (microtime >= max) {
			throw new RuntimeException("Time value exceeds allowed format");
		}
		
		long minutes = (microtime / 1000000) / 60;
		long seconds = (microtime / 1000000) % 60;	
		
		String formattedMinutes = minutes + "";
		String formattedSeconds = seconds + "";
		
		if(minutes < 10) {
			formattedMinutes = "0" + minutes;
		}
		if(seconds < 10) {
			formattedSeconds = "0" + seconds;
		}
		return formattedMinutes + ":" + formattedSeconds;
	}
	
	public String toString() {
		return super.toString();
	}
}