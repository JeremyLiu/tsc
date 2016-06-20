package com.jec.utils.tool;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

public class Player {
	
	private AudioStream as;
	private ContinuousAudioDataStream cas;
	private boolean playing = false;
	private boolean looping = false;
	
	public static Player getPlayer(InputStream in){
		//Create an AudioStream object from the input stream
		AudioStream as;
		try{
			as = new AudioStream(in);
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}

		Player p = new Player();
		p.as = as;
		return p;
	}


	public static Player getPlayer(String filename){
		//open an input stream to the audio file
		InputStream in;
		try{
			in = new FileInputStream(filename);
		}catch(FileNotFoundException e){
			e.printStackTrace();
			return null;
		}

		return getPlayer(in);
	}
	
	private Player() {
		
	}
	
	private void buildCAS() {
		
		if(cas == null) {
			
			try {
				AudioData data = as.getData();
				cas = new ContinuousAudioDataStream(data);
			} catch(IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public boolean isPlaying() {
		return playing;
	}

	public void setLooping(boolean loop) {
		looping = loop;
	}


	public boolean isLooping() {
		return looping;
	}
	
	public void start() {
		
		if(!playing) {
			
			playing = true;
			
			if(looping) {
				
				buildCAS();
				
				if(cas != null) {
					AudioPlayer.player.start();
				}
				
			} else {
				
				if(as != null) {
					AudioPlayer.player.start(as);
				}
				
			}
			
		}
		
	}
	
	
	public void stop() {
		
		if(playing) {
			
			playing = false;
			
			if(looping) {
				
				if(cas != null) {
					AudioPlayer.player.stop(cas);
				}
				
			} else {
				
				if(as != null) {
					
					AudioPlayer.player.stop(as);
					
				}
				
			}
			
			
		}
		
		
	}


	@Override
	protected void finalize() throws Throwable {

		if(cas != null) {
			cas.close();
			cas = null;
		}
		
		if(as != null) {
			as.close();
			as = null;
		}
		
		super.finalize();
		
		
	}
	
	
	
}
