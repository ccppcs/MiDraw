import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

class Tone implements Comparable<Tone>			// Have Tone Information
{
	// Member Variable
	private int start;
	private int beat;
	private int note;
	// Method
	public Tone(int _start,int _beat,int _note)
	{
		start = _start;
		beat = _beat;
		note = _note;	// 0 은 쉽표
	}
	public int getStart()
	{
		return start;
	}
	public int getBeat()
	{
		return beat;
	}
	public int getNote()
	{
		return note;
	}
	public int getStop()
	{
		return start+beat;
	}
	public int compareTo(Tone t)
	{
		return this.getStart()-t.getStart();
	}
	public String toString()	//@디버깅용
	{
		if(note==0)		// rest
		{
			return new String("rest "+getStart()+"~"+getStop());
		}
		else
		{
			return new String("start "+getStart()+" / beat "+getBeat()+" / note "+getNote());
		}
	}
}

class TrackMaker 	// Track Maker
{
	//Member Variable
	private ArrayList<Tone> track;
	// Constructor
	public TrackMaker()
	{
		track = new ArrayList<Tone>();
	}
	// Method
	public void addTone(Tone t)
	{
		track.add(t);
	}
	public ArrayList<Tone> makeTrack()
	{
		// 정렬
		Collections.sort(track);
		Iterator<Tone> it = track.iterator();
		while(it.hasNext()) // @디버깅용
		{
			Tone t = it.next();
			System.out.println(t);
		}
		System.out.println("end makeTrack()");
		return track;
	}
}

class NoteMessage implements Comparable<NoteMessage>		// Have ShortMessage info and Rank
{
	private int beat;
	private ShortMessage smg;
	NoteMessage(int _beat,ShortMessage _smg)
	{
		beat = _beat;
		smg = _smg;
	}
	public int getBeat()
	{
		return beat;
	}
	public ShortMessage getMessage()
	{
		return smg;
	}
	public int compareTo(NoteMessage t)
	{
		return this.getBeat()-t.getBeat();
	}
}

class MusicPlayer implements Runnable
{
	// Member Variable
	private Synthesizer synth;
	private Receiver receiver;
	private int instrument=0;
	private int channel = 0;
	static public final int BPM = 40;		// #차후 수정
	
	private ArrayList<Tone> track;			// 받아올 트랙
	//-실제 재생 정보
	private ArrayList<NoteMessage> music;	// 음의 재생 정보 저장
	// Method
 	public MusicPlayer(ArrayList<Tone> _track,int _instrument)
	{
 		track = _track;
		try
		{	
			music = new ArrayList<NoteMessage>();
			synth = MidiSystem.getSynthesizer();
			synth.open();
			receiver = synth.getReceiver();	
			setInstrument(_instrument);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("------ERROR------");
			System.out.println("  MusicPlayer()");
		}
	}
	public void setInstrument(int _instrument)
	{
		instrument = _instrument;
		synth.getChannels()[0].programChange(instrument);
	}
	public void setChannel(int _channel)
	{
		channel = _channel;
	}
	public void setTrack(ArrayList<Tone> _track)
	{
		track = _track;
	}
	//-make ShortMessage
	private ShortMessage makeShortMessage(int onOrOff, int note)
	{
		ShortMessage msg = null;
		try{
			msg = new ShortMessage(onOrOff,channel,note,100);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("------ERROR------");
			System.out.println("makeShortMessage()");
		}
		return msg;
	}
	//-make Note
	private NoteMessage makeNoteMessage(int beat, ShortMessage msg)
	{
		return new NoteMessage(beat,msg);
	}
	public void MakeMusic()
	{
		Iterator<Tone> it = track.iterator();
		ArrayList<NoteMessage> temp = new ArrayList<NoteMessage>();
		while(it.hasNext())
		{
			Tone t = it.next();
			temp.add(makeNoteMessage(t.getStart(), 
					makeShortMessage(ShortMessage.NOTE_ON, t.getNote())
					));
			temp.add(makeNoteMessage(t.getStop(), 
					makeShortMessage(ShortMessage.NOTE_OFF, t.getNote())
					));
		}
		music.addAll(temp);
		Collections.sort(music);
	}
	public void Play()
	{
		Iterator<NoteMessage> it = music.iterator();
		int beatStream = 0;
		System.out.println("------------Play()--------------");
		System.out.println("__________");
		
		while(it.hasNext())
		{
			NoteMessage nm = it.next();
			
			System.out.println(" command "+nm.getMessage().getCommand()+" beat "+nm.getBeat()+" note "+nm.getMessage().getData1());
			System.out.println("__________");
			
			int beat = nm.getBeat();
			try
			{
				System.out.println((beat-beatStream));
				Thread.sleep((beat-beatStream)*BPM);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			receiver.send(nm.getMessage(), -1);		
			beatStream = beat;
			

		}
		
		System.out.println("\n-------------End---------------");
	}
	public void run()
	{
		Play();
	}
}

class MidrawInstrument {
	private MusicPlayer player;
	private int instrument;
	static final int MAJOR = 90; 	// min MAJOR - NOTE_HEIGHT  / max MAJOR
	static final int SUBNOTEFORBASS = 30;
	public void Intro()
	{
		TrackMaker track = new TrackMaker();
		
		int majorC=36;
		int beat = 12;
		int between = 0;
		int stream = 10;
		//RunThreads runner;
		track.addTone(new Tone(stream,beat-between,majorC+4));
		stream+=beat;
		track.addTone(new Tone(stream,beat-between,majorC+19));
		stream+=beat;
		
		track.addTone(new Tone(stream,beat-between,majorC+23));
		track.addTone(new Tone(stream,beat-between,majorC+31));
		stream+=beat;
		
		track.addTone(new Tone(stream,beat-between,majorC+19));
		stream+=beat;
		
		track.addTone(new Tone(stream,beat/2,majorC+23));
		stream+=beat/2;
		track.addTone(new Tone(stream,beat/2-between,majorC+21));
		stream+=beat/2;
		
		track.addTone(new Tone(stream,beat-between,majorC+19));
		stream+=beat;
		track.addTone(new Tone(stream,beat-between,majorC+21));
		stream+=beat;
		track.addTone(new Tone(stream,beat-between,majorC+19));
		stream+=beat;
		
		track.addTone(new Tone(stream,beat*2-between,majorC+31));
		track.addTone(new Tone(stream,beat*2-between,majorC+23));
		track.addTone(new Tone(stream,beat*2-between,majorC+19));
		track.addTone(new Tone(stream,beat*2-between,majorC+14));
		track.addTone(new Tone(stream,beat*2-between,majorC+11));
		track.addTone(new Tone(stream,beat*2-between,majorC+7));
		stream+=beat;
		
		MusicPlayer player = new MusicPlayer(track.makeTrack(), 0);
		player.MakeMusic();
		Thread runner = new Thread(player);
		
		MusicPlayer player2 = new MusicPlayer(track.makeTrack(), 96);
		player2.MakeMusic();
		Thread runner2 = new Thread(player2);
		
		runner.start();
		runner2.start();
	
	}
	public MidrawInstrument(int _instrument) {
		setInstrument(_instrument);
	}
	public void setChannel(int channel)
	{
		player.setChannel(channel);
	}
	public void setInstrument(int _instrument)
	{
		instrument = _instrument;
	}
	public void Make(ArrayList<PageInfo> ins)
	{
		TrackMaker track = new TrackMaker();
		Iterator<PageInfo> it = ins.iterator();
		int subNoteForBass = 0;
		while(it.hasNext())
		{
			PageInfo in = it.next();
			if(instrument == MidrawFrame.BASS)
			{
				subNoteForBass = SUBNOTEFORBASS;
			}
			track.addTone(
					new Tone(in.getPoint().x / MidrawFrame.DIVIDE_WIDTH , 
							in.getBeat() / MidrawFrame.DIVIDE_WIDTH, 
							MAJOR - subNoteForBass - (in.getPoint().y / (MidrawFrame.PAGE_HEIGHT/ MidrawFrame.NOTE_HEIGHT))
							)
					);
		}
		player = new MusicPlayer(track.makeTrack(), instrument);	
		player.MakeMusic();
	}
	
	public void MakeDrum(ArrayList<PageInfo> ins)
	{
		TrackMaker track = new TrackMaker();
		Iterator<PageInfo> it = ins.iterator();
		int[] drumNote = {31,36,38,40,
							45,47,48,
							42,46,49,51,53,57,59};	// count 14
		int note=0;
		while(it.hasNext())
		{
			PageInfo in = it.next();
			note = in.getPoint().y / (MidrawFrame.PAGE_HEIGHT / MidrawFrame.NOTE_HEIGHT_DRUM);
			track.addTone(
					new Tone(in.getPoint().x / MidrawFrame.DIVIDE_WIDTH , 
							in.getBeat(), 
							drumNote[note]
							)
					);
		}
		player = new MusicPlayer(track.makeTrack(), instrument);
		
		if(instrument == MidrawFrame.DRUM)	
		{
			setChannel(9);	// drum channel
		}
		player.MakeMusic();
	}
	public void Play()
	{
		Thread runner = new Thread(player);
		runner.start();
	}
}
