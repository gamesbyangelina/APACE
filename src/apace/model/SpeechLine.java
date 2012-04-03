package apace.model;

public class SpeechLine{
	
	public String content;
	public String speakerId;
	public int lifespan;
	
	public boolean scripted = false;
	
	public boolean sayAt = false;
	public int sayX;
	public int sayY;
	
	public SpeechLine(String content, String speakerId, int lifespan){
		this.content = content;
		this.speakerId = speakerId;
		this.lifespan = lifespan;
		this.scripted = true;
		this.sayAt = false;
	}
	
	public SpeechLine(String content, String speakerId, int lifespan, int x, int y){
		this.content = content;
		this.speakerId = speakerId;
		this.lifespan = lifespan;
		this.scripted = true;
		this.sayAt = true;
		this.sayX = x;
		this.sayY = y;
	}
	
}
