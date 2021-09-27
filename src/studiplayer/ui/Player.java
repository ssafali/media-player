package studiplayer.ui;

import java.net.URL;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import studiplayer.audio.*;

public class Player extends Application {

	private Button playButton, pauseButton, stopButton, nextButton, editorButton;
	private PlayList playList;
	private Stage primaryStage;
	private String playListPathname;

	private final String titleLine = "Current song: ";
	public static final String default_playlist = "playlists/DefaultPlayList.m3u";
	public static final String DEFAULT_PLAYLIST = default_playlist;
	
	private Label songDescription;
	private Label playTime = new Label();
	private boolean stopped;
	private boolean paused = false;
	private String playPosition;
	
	private PlayListEditor playListEditor;
	private boolean editorVisible;

	public Player() {
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public void setPlayList(String pathname) {
		if(pathname != null && !pathname.isEmpty()) {
			this.playList = new PlayList(pathname);
			refreshUI();
		} else {
			this.playList = new PlayList(default_playlist);
			refreshUI();
		}
	}
	
	private Button createButton(String iconfile) {
		Button button = null;
		try {
			URL url = getClass().getResource(iconfile);
			Image icon = new Image(url.toString());

			ImageView imageView = new ImageView(icon);
			imageView.setFitHeight(48);
			imageView.setFitWidth(48);

			button = new Button("", imageView);
			button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		} catch (Exception e) {
			System.out.println("Image " + iconfile + " not found!");
			System.exit(-1);
		}
		return button;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// 1.Initializing the attributes.
		this.primaryStage = primaryStage;
		this.playButton = createButton("/icons/play.png");
		this.pauseButton = createButton("/icons/pause.png");
		this.stopButton = createButton("/icons/stop.png");
		this.nextButton = createButton("/icons/next.png");
		this.editorButton = createButton("/icons/pl_editor.png");
		this.songDescription = new Label();
		this.stopped = true;

		editorVisible = false;

		// 2. The initial layout labels when there's no playlist.

		// 3. Getting parameters from command line
		Parameters params = this.getParameters();
		List<String> rawParams = params.getRaw();

		if (rawParams.isEmpty() || rawParams == null) {
			this.playList = new PlayList(default_playlist);
			playListPathname = default_playlist;
			
			if(playList.isEmpty()) {
				songDescription.setText("no current song");
				playPosition = "--:--";
			} else {
				//songDescription.setText(playList.getCurrentAudioFile().toString());
				songDescription.setText(playList.get(0).toString());
				
				//playTime.setText(playList.getCurrentAudioFile().getFormattedPosition());
				playPosition = "00:00";
			}
		} else {
			playList = new PlayList(rawParams.get(0));
			playListPathname = rawParams.get(0);
			
			if(playList.isEmpty()) {
				songDescription.setText("no current song");
				playPosition = "--:--";
			} else {
				songDescription.setText(playList.get(0).toString());
				//playTime.setText(playList.getCurrentAudioFile().getFormattedPosition());
				playPosition = "00:00";
			}
		}

		// 4. Creating the main layout.
		BorderPane layout = new BorderPane();

		// 4.1. Changing the font and the size of song information.
		
		playTime.setFont(new Font("Arial", 20));
		songDescription.setFont(new Font("Arial", 14));
		playTime.setText(playPosition);

		// 4.2 Using HBox for the buttons created.
		HBox buttons = new HBox();
		buttons.getChildren().addAll(playTime, playButton, pauseButton, stopButton, nextButton, editorButton);

		// 4.3 Placement of songDescription and the buttons.
		layout.setTop(songDescription);
		layout.setCenter(buttons);

		// 5. Setting actions
		playButton.setOnAction((event) -> {
			playCurrentSong();
		});

		pauseButton.setOnAction((event) -> {
			pauseCurrentSong();
		});

		stopButton.setOnAction((event) -> {
			stopCurrentSong();
			playTime.setText("00:00");
			
		});

		nextButton.setOnAction((event) -> {	
			nextSong();
			int i = playList.getCurrent();
			playList.setCurrent(i);
			
			playCurrentSong();
		});

		editorButton.setOnAction((event) -> {
			if (editorVisible) {
				editorVisible = false;
				playListEditor.hide();
			} else {
				editorVisible = true;
				playListEditor.show();
			}
		});

		Scene scene = new Scene(layout, 700, 90);
		playListEditor = new PlayListEditor(this, this.playList);
		
		setButtonStates(false, true, false, true, false);
		
		// 6. Setting the title name as the currently playing song info.
		primaryStage.setTitle(titleLine + songDescription.getText());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// 7. Method to set visibility on or off for the Editor button.
	public void setEditorVisible(boolean editorVisible) {
		this.editorVisible = editorVisible;
	}

	// 8. Following 5 methods to initiate media player buttons
	public void playCurrentSong() {
		setButtonStates(true, false, false, false, false);
		stopped = false;
		this.updateSongInfo(playList.getCurrentAudioFile());
		
		if (playList.getCurrentAudioFile() != null) { // Start threads
			(new TimerThread()).start();
			(new PlayerThread()).start();
		}
	}

	public void pauseCurrentSong() {
		setButtonStates(true, false, false, false, false);
		 if(playList != null && paused == true) {
			playCurrentSong();
			paused = false;
			
				
		} else if (playList != null && paused == false) {
			playList.getCurrentAudioFile().togglePause();
			paused = true;
		}
	}

	public void stopCurrentSong() {
		setButtonStates(false, true, false, true, false);
		stopped = true;
		playPosition = "00:00";
		playList.getCurrentAudioFile().stop();
		
		
		
		songDescription.setText(playList.getFirst().toString());
		playList.setCurrent(0);
		primaryStage.setTitle(playList.getFirst().toString());
	}

	public void nextSong() {
		setButtonStates(true, false, false, false, false);
		playList.changeCurrent();

		updateSongInfo(playList.getCurrentAudioFile());
		songDescription.setText(playList.getCurrentAudioFile().toString());
		primaryStage.setTitle(playList.getCurrentAudioFile().toString());
		

	}

	public void pl_Editor() {
		//setButtonStates(true, true, false, true, false);
	}

	// 9. Method to update the song info when for example clicked on next.
	private void updateSongInfo(AudioFile af) {
		if(stopped) {
			playTime.setText("00:00");
		} else if (af == null) {
			songDescription.setText("no current song");
			playTime.setText("--:--");
		} else if (af != null) {
			playTime.setText(af.getFormattedDuration());
			songDescription.setText(af.toString());
		}
//			if(stopped) {
//				playTime.setText("00:00");
//			}
//			else if (!stopped) {
//				playTime.setText(af.getFormattedDuration());
//				songDescription.setText(af.toString());
//			}

	}

	// 10. Getting the pathname of the currently playing song.
	public String getPlayListPathname() {
		this.playListPathname = playList.getCurrentAudioFile().getPathname();
		return playListPathname;
	}

	// 11. This method refreshes the interface and handles the visibility of the
	// buttons.
	private void refreshUI() {
		Platform.runLater(() -> {
			if (playList != null && playList.size() > 0) {
				updateSongInfo(playList.getCurrentAudioFile());
				setButtonStates(false, true, false, true, false);
			} else {
				updateSongInfo(null);
				setButtonStates(true, true, true, true, false);
			}
		});
	}

	// 12. Setting the button states on or off based on an order.
	private void setButtonStates(Boolean playButtonState, Boolean stopButtonState, Boolean nextButtonState,
			Boolean pauseButtonState, Boolean editorButtonState) {

		playButton.setDisable(playButtonState);
		stopButton.setDisable(stopButtonState);
		nextButton.setDisable(nextButtonState);
		pauseButton.setDisable(pauseButtonState);
		editorButton.setDisable(editorButtonState);
	}

	// 13. Timer subclass thread
	private class TimerThread extends Thread {

		@Override
		public void run() {
			while (!stopped) {
				Platform.runLater(() -> { 
					playPosition = playList.getCurrentAudioFile().getFormattedPosition();
					playTime.setText(playPosition);
				});
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 14. Play subclass thread
	private class PlayerThread extends Thread {
		public void run() {
			while (!playList.isEmpty() && !stopped) {
				
				try {
					playList.getCurrentAudioFile().play();
					refreshUI();
				} catch (NotPlayableException e) {
					e.printStackTrace();
				}
			}
		}

	}
}