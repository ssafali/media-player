package studiplayer.audio;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
public class Main {
	
	
	public static void main(String[] args) throws NotPlayableException {
		
		final String m3uName = "pl.m3u";
        File m3u = new File(m3uName);

        // Create a play list file with non-existent names for audio files
        // and an existing file at the end of the list
        try {
            FileWriter fw = new FileWriter(m3u);
            fw.write("gibt es nicht.wav\n");
            fw.write("und das auch nicht.ogg\n");
            fw.write("und das - sowieso nicht.mp3\n");
            fw.write("audiofiles/Rock 812.mp3");
            fw.close();
        } catch (IOException e) {
           System.out.println(e.getMessage());
          
        }
        
        PlayList pl = null;
        pl = new PlayList(m3uName);
        System.out.println(pl.toString());
        m3u.delete();
		
//		Method meth;
//		@SuppressWarnings("rawtypes")
//		Constructor ctor;
		
//		try {
//			meth = AudioFile.class.getDeclaredMethod("play", new Class[] {});
//			System.out.println(meth.getName());
//			
//			ctor = AudioFile.class
//                    .getDeclaredConstructor(new Class[] { java.lang.String.class });
//			System.out.println(ctor.getName());
//			
//		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}


