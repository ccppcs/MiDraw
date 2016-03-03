import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

class MidrawFrame extends JFrame {
 
	/************************/
	/**      Constant      **/
	/************************/
	
	static final int PAGE_HEIGHT = 800;
	static final int PAGE_WIDTH = 1000;
	static final int PREVIEW_HEIGHT = 20;
	static final int PREVIEW_WIDTH = 10;
	
	static final int PIANO = 1;
	static final int GUITAR = 30;
	static final int BASS = 36;		// Slap bass
	static final int DRUM = 0;
	
	static final int DIVIDE_HEIGHT = 20;
	static final int DIVIDE_WIDTH = 10;
	
	static final int NOTE_HEIGHT = 40;
	static final int NOTE_HEIGHT_DRUM = 14;
	
	/************************/
	/**   Member Variable  **/
	/************************/
	 JFrame thisFrame = this;
	 JPanel MainPanel = new JPanel();
	 JFileChooser Chooser = new JFileChooser("");
	 
	 Font font = new Font("궁서",Font.BOLD,20);
	 
	 IB piano = new IB(".\\images\\piano.jpg");
	 IB bass = new IB(".\\images\\bass.jpg");
	 IB guitar = new IB(".\\images\\guitar.jpg");
	 IB drum = new IB(".\\images\\drum.jpg");
	 
	 IB eraser = new IB(".\\images\\eraser.jpg");
	 IB left = new IB(".\\images\\left2.jpg");
	 IB right = new IB(".\\images\\right.jpg");
	 IB play = new IB(".\\images\\play.jpg");
	 
	 
	 ImageIcon lengthj=new ImageIcon(".\\images\\length.jpg");
	 ImageIcon lengthg=new ImageIcon(".\\images\\length.gif");
	 JButton length = new JButton(lengthj);
	 
	 ImageIcon widthj=new ImageIcon(".\\images\\width.jpg");
	 ImageIcon widthg=new ImageIcon(".\\images\\width.gif");
	 JButton width = new JButton(widthj);
	 
	 IB save = new IB(".\\images\\save.jpg");
	 IB open = new IB(".\\images\\open.jpg");
	 IB update = new IB(".\\images\\update.jpg");
	 
	 boolean widthFlag = false;
	 boolean lengthFlag = false;
	 
	 // 현재 재생정보
	 boolean isPlaying = false;
	 int maxPage = 1;
	 int bar_x = 0;
	 
	 int select = PIANO;
	 String str;
	 MidrawPanel drawPanel = new MidrawPanel();  //악보 찍는 패널
	 MidrawPanel2 PreviewPanel = new MidrawPanel2(); // 미리보기 패널

	 Integer numOfPage = 1;
	 JLabel page = new JLabel();
	// File
	 FileNameExtensionFilter filter = new FileNameExtensionFilter("esc 파일","esc");
	 
	// Instruments	
	ArrayList<PageInfo> page_piano = new ArrayList<PageInfo>();
	ArrayList<PageInfo> page_guitar = new ArrayList<PageInfo>();
	ArrayList<PageInfo> page_bass = new ArrayList<PageInfo>();
	ArrayList<PageInfo> page_drum = new ArrayList<PageInfo>();
 
	// MusicInfo
	ArrayList<ArrayList<PageInfo>> music_piano = new ArrayList<ArrayList<PageInfo>>();
	ArrayList<ArrayList<PageInfo>> music_guitar = new ArrayList<ArrayList<PageInfo>>();
	ArrayList<ArrayList<PageInfo>> music_bass = new ArrayList<ArrayList<PageInfo>>();
	ArrayList<ArrayList<PageInfo>> music_drum = new ArrayList<ArrayList<PageInfo>>();

	ArrayList<PageInfo> prev_List = new ArrayList<>();
	
	// MusicPlayer
	MidrawInstrument ins_piano = new MidrawInstrument(PIANO);
    MidrawInstrument ins_guitar = new MidrawInstrument(GUITAR);
    MidrawInstrument ins_bass = new MidrawInstrument(BASS);
    MidrawInstrument ins_drum = new MidrawInstrument(DRUM);
    
	MidrawInstrument prev_Player = new MidrawInstrument(select);
	
	
 class MidrawPanel extends JPanel  //악보찍는
 {
	  public void paintComponent(Graphics g)
	  { 
	   
		  Toolkit tk = Toolkit.getDefaultToolkit();
			Image img = tk.getImage(".\\images\\white.jpg");
			g.drawImage(img,0,0,this);
	   
			// 세로 격자
			if(lengthFlag)
			   {
					for(int i=0; i<PAGE_WIDTH/DIVIDE_WIDTH; i++)
				    {
				     g.setColor(Color.black);
				     for(int j=0; j<PAGE_HEIGHT; j+=12)
				     {
				    	 g.drawLine((i*20), j, (i*20), j+6);
				     }
				    }
			   }
			// 가로 격자
			if(widthFlag)
			   {
				Font f = new Font("궁서",Font.CENTER_BASELINE,15);
				g.setFont(f);
				// size 12
				String[] NoteName = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
				//String[] NoteName = {"도","도#","레","레#","미","파","파#","솔","솔#","라","라#","시"};
				// drum name size 14
				String[] drumNoteName = {"Sticks","BassDrum","Acustic Snare","Electric Snare","Low Tom",
						"Low-Mid Tom","Hi-Mid Tom","Closed Hi-Hat","Open Hi-Hat","Crash Cymbal",
						"Ride Cymbal","Ride Bell","Crash Cymbal2","Ride Cymbal2"};
					switch(select)
					{
					case PIANO:
					case GUITAR:	
							    for(int i=0; i<NOTE_HEIGHT; i++)
							    {
							     g.setColor(Color.black);
							     for(int j=0; j<PAGE_WIDTH; j+=12)
							     {
							      g.drawLine(j, (i*(PAGE_HEIGHT/NOTE_HEIGHT)), j+6, (i*(PAGE_HEIGHT/NOTE_HEIGHT)));
							      
							      g.drawString(NoteName[(MidrawInstrument.MAJOR-i)%12], 20, i*(PAGE_HEIGHT/NOTE_HEIGHT)+20);
							     }
							    }
							    break;
					case BASS:	
							    for(int i=0; i<NOTE_HEIGHT; i++)
							    {
							     g.setColor(Color.black);
							     for(int j=0; j<PAGE_WIDTH; j+=12)
							     {
							      g.drawLine(j, (i*(PAGE_HEIGHT/NOTE_HEIGHT)), j+6, (i*(PAGE_HEIGHT/NOTE_HEIGHT)));
							      
							      g.drawString(NoteName[(MidrawInstrument.MAJOR-i-MidrawInstrument.SUBNOTEFORBASS)%12], 
							    		  20, i*(PAGE_HEIGHT/NOTE_HEIGHT)+20);
							     }
							    }
							    break;
					case DRUM:
							    for(int i=0; i<NOTE_HEIGHT_DRUM; i++)
							    {
							     g.setColor(Color.black);
							     for(int j=0; j<PAGE_WIDTH; j+=12)
							     {
							      g.drawLine(j, (i*(PAGE_HEIGHT/NOTE_HEIGHT_DRUM)), j+6, (i*(PAGE_HEIGHT/NOTE_HEIGHT_DRUM)));
							      g.drawString(drumNoteName[i], 20, i*(PAGE_HEIGHT/NOTE_HEIGHT_DRUM)+20);
							     }
							    }						   
							    break;
					}
			   }
	   			// music
				switch (select)
				{
				case PIANO:
					Iterator<PageInfo> i_piano = page_piano.iterator();
					while(i_piano.hasNext())
					{
						PageInfo ip = i_piano.next();
						Point p = ip.getPoint();
						Color c = new Color(55+(PAGE_HEIGHT-p.y)/20*5,0,0);
						g.setColor(c);
						int beat = ip.getBeat();
						
						if(beat>PageInfo.BASE_BEAT*2)
							g.fillOval(p.x - 10 -(numOfPage-1)*PAGE_WIDTH, p.y - 10, beat, 20);
						else
							g.fillOval(p.x - 10 - (numOfPage-1)*PAGE_WIDTH, p.y - 10, PageInfo.BASE_BEAT*2, 20);
					}
					break;
				case GUITAR:
					Iterator<PageInfo> i_guitar = page_guitar.iterator();
					while(i_guitar.hasNext())
					{
						PageInfo ip = i_guitar.next();
						Point p = ip.getPoint();
						Color c = new Color(0,0,55+(PAGE_HEIGHT-p.y)/20*5);
						g.setColor(c);
						int beat = ip.getBeat();
						
						if(beat>PageInfo.BASE_BEAT*2)
							g.fillOval(p.x - 10 -(numOfPage-1)*PAGE_WIDTH, p.y - 10, beat, 20);
						else
							g.fillOval(p.x - 10 - (numOfPage-1)*PAGE_WIDTH, p.y - 10, PageInfo.BASE_BEAT*2, 20);
					}
					break;
				case BASS:
					Iterator<PageInfo> i_bass = page_bass.iterator();
					while(i_bass.hasNext())
					{
						PageInfo ip = i_bass.next();
						Point p = ip.getPoint();
						Color c = new Color(0,55+(PAGE_HEIGHT-p.y)/20*5,0);
						g.setColor(c);
						int beat = ip.getBeat();
						
						if(beat>PageInfo.BASE_BEAT*2)
							g.fillOval(p.x - 10 -(numOfPage-1)*PAGE_WIDTH, p.y - 10, beat, 20);
						else
							g.fillOval(p.x - 10 - (numOfPage-1)*PAGE_WIDTH, p.y - 10, PageInfo.BASE_BEAT*2, 20);
					}
					break;
				case DRUM:
					Iterator<PageInfo> i_drum = page_drum.iterator();
					while(i_drum.hasNext())
					{
						PageInfo ip = i_drum.next();
						Point p = ip.getPoint();
						int i = (PAGE_HEIGHT-p.y)/20*5;
						Color c = new Color(i,i,i);
						g.setColor(c);
						g.fillOval(p.x - 10 - (numOfPage-1)*PAGE_WIDTH, p.y - 10, 20, 20);
					}
					break;
				}
	   
	   
	  }
	 }
 class MidrawPanel2 extends JPanel // preview
	{
		public void paintComponent(Graphics g)
		{
			Toolkit tk = Toolkit.getDefaultToolkit();
			Image img = tk.getImage(".\\images\\preview.jpg");
			g.drawImage(img, 0, 0, this);
			
				// piano
				Iterator<PageInfo> i_piano = page_piano.iterator();
				while(i_piano.hasNext())
				{
					PageInfo ip = i_piano.next();
					Point p = ip.getPoint();
					Color c = new Color(55+(PAGE_HEIGHT-p.y) / (PAGE_HEIGHT/40)*5,0,0);
					g.setColor(c);
					int beat = ip.getBeat();
					
					if(beat>PageInfo.BASE_BEAT)
					{
						g.fillRect(p.x - PREVIEW_WIDTH- (numOfPage-1)*PAGE_WIDTH, 2,
							beat, PREVIEW_HEIGHT);
					}
					else
					{
						g.fillRect(p.x - PREVIEW_WIDTH/2 - (numOfPage-1)*PAGE_WIDTH, 2,
								PREVIEW_WIDTH, PREVIEW_HEIGHT);
					}
				}
				// guitar
				Iterator<PageInfo> i_guitar = page_guitar.iterator();
				while(i_guitar.hasNext())
				{
					PageInfo ip = i_guitar.next();
					Point p = ip.getPoint();
					Color c = new Color(0,0,55+(PAGE_HEIGHT-p.y) / (PAGE_HEIGHT/40)*5);
					g.setColor(c);
					int beat = ip.getBeat();
					
					if(beat>PageInfo.BASE_BEAT)
					{
						g.fillRect(p.x - PREVIEW_WIDTH- (numOfPage-1)*PAGE_WIDTH, 28,
							beat, PREVIEW_HEIGHT);
					}
					else
					{
						g.fillRect(p.x - PREVIEW_WIDTH/2 - (numOfPage-1)*PAGE_WIDTH, 28,
								PREVIEW_WIDTH, PREVIEW_HEIGHT);
					}
				}
				// bass
				Iterator<PageInfo> i_bass = page_bass.iterator();
				while(i_bass.hasNext())
				{
					PageInfo ip = i_bass.next();
					Point p = ip.getPoint();
					Color c = new Color(0,55+(PAGE_HEIGHT-p.y) / (PAGE_HEIGHT/40)*5,0);
					g.setColor(c);
					int beat = ip.getBeat();
					
					if(beat>PageInfo.BASE_BEAT)
					{
						g.fillRect(p.x - PREVIEW_WIDTH- (numOfPage-1)*PAGE_WIDTH, 53,
							beat, PREVIEW_HEIGHT);
					}
					else
					{
						g.fillRect(p.x - PREVIEW_WIDTH/2 - (numOfPage-1)*PAGE_WIDTH, 53,
								PREVIEW_WIDTH, PREVIEW_HEIGHT);
					}
				}
		
				// drum
				Iterator<PageInfo> i_drum = page_drum.iterator();
				while(i_drum.hasNext())
				{
					PageInfo ip = i_drum.next();
					Point p = ip.getPoint();
					int i = (PAGE_HEIGHT-p.y) / (PAGE_HEIGHT/40) *5;
					Color c = new Color(i,i,i);
					g.setColor(c);
					g.fillRect(p.x - PREVIEW_WIDTH/2 - (numOfPage-1)*PAGE_WIDTH, 77,
							PREVIEW_WIDTH, PREVIEW_HEIGHT);
				}
				if(isPlaying)
				{
					g.setColor(Color.ORANGE);
					g.fillRect(bar_x, 0, 10, 200);
					try
					{
						bar_x+=DIVIDE_WIDTH/2;
						Thread.sleep(MusicPlayer.BPM/2);
					}catch(Exception e){}
					
					if(bar_x>=PAGE_WIDTH-10)
					{
						bar_x=0;
						if(numOfPage>=maxPage)
						{
							setStop();
						}
						else
						{
							PageUp();
							left.setEnabled(false);
							right.setEnabled(false);
							drawPanel.repaint();
							left.repaint();
							page.setText(numOfPage.toString());
							page.repaint();
						}
					}
					repaint();
				}
		}
		
	} 
 public MidrawFrame(String str)
 {
	  super(str);
	  super.setSize(1100,925);
	  super.setResizable(false);
	  super.setVisible(true);
	  
	  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  
	  Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	  Dimension frm = super.getSize();
	  int xpos = (int)(screen.getWidth() / 2 - frm.getWidth() / 2);
	  int ypos = (int)(screen.getHeight() /2 - frm.getHeight() / 2);
	  super.setLocation(xpos, ypos);
	  
	  JPanel ButtonPanel = new JPanel(); //버튼 패널  
	  JPanel ButtonCutPanel1 = new JPanel(); //버튼 내부 분할 패널(페이지 넘기기)
	  JPanel ButtonCutPanel2 = new JPanel(); //버튼 내부 분팔 패널(가로 세로 격자)
	  JPanel ButtonCutPanel3 = new JPanel(); //버튼 내부 분팔 패널(가로 세로 격자)
	  
	  piano.addMouseListener(new InstrumentControl());  //악기 리스너 4종류
	  drum.addMouseListener(new InstrumentControl());
	  guitar.addMouseListener(new InstrumentControl());
	  bass.addMouseListener(new InstrumentControl());
	  
	  width.addMouseListener(new LineControl());  //격자 리스너 2종류
	  length.addMouseListener(new LineControl());
	  
	  save.addActionListener(new fileControl());  //파일 저장,열기 리스너 2종류
	  open.addActionListener(new fileControl());
	  
	  left.addActionListener(new PageControl());
	  right.addActionListener(new PageControl());
	  
	  play.addActionListener(new playControl());	// play music
	  eraser.addActionListener(new eraseControl()); // eraser
	  update.addActionListener(new updateControl()); // update
	  
	  //버튼 내부 분할 설정
	  ButtonCutPanel1.setLayout(null);
	  ButtonCutPanel2.setLayout(new GridLayout(1, 2));
	  ButtonCutPanel3.setLayout(new GridLayout(1,1));
	  
	  //버튼 패널 설정
	  ButtonPanel.setSize(100, 800);
	  ButtonPanel.setLayout(new GridLayout(10,1,0,0));  
	  
	  
	  
	  ButtonCutPanel3.add(play);
	  //버튼 설정
	  ButtonPanel.add(piano);
	  piano.setToolTipText("피아노 입니다");
	  ButtonPanel.add(guitar);
	  guitar.setToolTipText("기타 입니다");
	  ButtonPanel.add(bass);
	  bass.setToolTipText("베이스 입니다");
	  ButtonPanel.add(drum);
	  drum.setToolTipText("드럼 입니다");
	
	  play.setToolTipText("악보를 재생함ㅂ니다");
	  page.setToolTipText("현재 페이지를 나타냅니다");
	  
	  left.setBounds(0, 0, 31, 80);
	  page.setBounds(31, 0, 35, 80);
	  page.setHorizontalAlignment(JLabel.CENTER);
	  page.setFont(font);
	  ButtonCutPanel1.setBackground(Color.white);
	  right.setBounds(62, 0, 95, 80);
	  
	  
	  ButtonCutPanel1.add(left);
	  left.setToolTipText("악보를 왼쪽으로 넘깁니다");
	  
	  ButtonCutPanel1.add(page);  
	  
	  ButtonCutPanel1.add(right);  
	  right.setToolTipText("악보를 오른쪽으로 넘깁니다");  
	  
	  
	  ButtonCutPanel2.add(width);
	  
	  ButtonCutPanel2.add(length);  
	  ButtonPanel.add(ButtonCutPanel1);  
	  ButtonPanel.add(ButtonCutPanel2);  
	  ButtonPanel.add(eraser);
	  eraser.setToolTipText("마지막으로 찍은 점을 지웁니다");
	  ButtonPanel.add(update);
	  update.setToolTipText("서버에 저장되어 있는 파일을 가져오고, 서버에 없는 파일을 등록합니다");
	  ButtonPanel.add(save);
	  save.setToolTipText("작성한 악보를 저장합니다");
	  ButtonPanel.add(open);
	  open.setToolTipText("저장되어 있는 악보를 불러옵니다");
	  
	  left.setEnabled(false);
	  page.setEnabled(false);
	  page.setText(numOfPage.toString());
	  
	  piano.setBorderPainted(false);
	  
	  
	  //미리보기 패널 설정
	  PreviewPanel.setSize(1100, 100);  
	  
	  setContentPane(MainPanel);
	  MainPanel.setLayout(null);
	  
	  // KeyEvent
	  addKeyListener(new KeyListener() {
		public void keyTyped(KeyEvent e) {
		}
		public void keyReleased(KeyEvent e) {
			KeyStroke key = KeyStroke.getKeyStroke(e.getKeyChar(),e.getModifiers());
			if(key.getKeyCode()==KeyEvent.VK_BACK_SPACE)
			{
				eraseNote();
			}
			else if(key.getKeyCode()==KeyEvent.VK_SPACE)
			{
				if(!isPlaying)
					musicStart();
			}
		}
		public void keyPressed(KeyEvent e) {
		}
	});
	  
	  //악보 그리기 패널 설정
	  drawPanel.setBounds(0, 0, 1000, 800);
	  drawPanel.addMouseListener(new ClickedControl());
	  
	  ButtonPanel.setBounds(1000, 0, 100, 800);
	  
	  PreviewPanel.setBounds(0, 800, 1065, 100);
	  
	  ButtonCutPanel3.setBounds(1065,800,35,100);
	  //패널 추가
	  MainPanel.add(drawPanel);
	  MainPanel.add(PreviewPanel);
	  MainPanel.add(ButtonPanel);
	  
	  MainPanel.add(ButtonCutPanel3);

	  music_piano.add(page_piano);
	  music_guitar.add(page_guitar);
	  music_bass.add(page_bass);
	  music_drum.add(page_drum);
	  
	  // 저장열기
	  Chooser.addChoosableFileFilter(filter);
	  Image icon = new ImageIcon(".\\images\\Midraw.png").getImage();
	  setIconImage(icon);
 }
 /*************************/
 /**       Function      **/
 /*************************/
 private void setPage(int page)
 {
	 page_piano = music_piano.get(page-1);
	 page_guitar = music_guitar.get(page-1);
	 page_bass = music_bass.get(page-1);
	 page_drum = music_drum.get(page-1);
 }
 private void PageUp()
 {
	 left.setEnabled(true);
		numOfPage++;
		// add new page
		if(music_piano.size() < numOfPage)
		{
			page_piano = new ArrayList<PageInfo>();	
			page_guitar = new ArrayList<PageInfo>();				
			page_bass = new ArrayList<PageInfo>();				
			page_drum = new ArrayList<PageInfo>();
			
			music_piano.add(page_piano);
			music_guitar.add(page_guitar);
			music_bass.add(page_bass);
			music_drum.add(page_drum);
			maxPage++;
		}
		// select page
		else
		{
			setPage(numOfPage);
		}
 }
 private void clickAndPlay(PageInfo p)	// 클릭시 재생
 {
	 prev_List.clear();
	 PageInfo pi = new PageInfo(new Point(0,p.getPoint().y));
	 pi.setBeat(p.getBeat());
	 prev_List.add(pi);
	 prev_Player.setInstrument(select);
	 if(select == DRUM)
		 prev_Player.MakeDrum(prev_List);
	 else
		 prev_Player.Make(prev_List);
	
	 prev_Player.Play();
 }
 private void setPlay()
 {
	 isPlaying = true;
	 
	 left.setEnabled(false);
	 right.setEnabled(false);
	 save.setEnabled(false);
	 open.setEnabled(false);
	 play.setEnabled(false);
	 eraser.setEnabled(false);
	 numOfPage=1;
	 setPage(numOfPage);
	 page.setText(numOfPage.toString());
	 page.repaint();
	 left.setPath(".\\images\\left2.jpg");
	 left.repaint();
	 right.repaint();
	 drawPanel.repaint();
	 PreviewPanel.repaint();
	 
 }
 private void setStop()
 {
	 isPlaying = false;
	 left.setEnabled(true);
	 right.setEnabled(true);
	 save.setEnabled(true);
	 open.setEnabled(true);
	 play.setEnabled(true);
	 eraser.setEnabled(true);
	 if(maxPage>1)
		 left.setPath(".\\images\\left.jpg");
	 left.repaint();
	 right.repaint();
	 bar_x=0;
 }
 private void musicStart()
 {
	 ArrayList<PageInfo> temp = new ArrayList<PageInfo>();	// 페이지별 정보를 하나로 합치기 위해서
	  	
	    // Piano
		Iterator<ArrayList<PageInfo>> it = music_piano.iterator();
		while(it.hasNext())
		{
			ArrayList<PageInfo> ap = it.next();
			temp.addAll(ap);
		}
		ins_piano.Make(temp);
		temp.clear();
		// Guitar
		it = music_guitar.iterator();
		while(it.hasNext())
		{
			ArrayList<PageInfo> ap = it.next();
			temp.addAll(ap);
		}
		ins_guitar.Make(temp);
		temp.clear();
		// Bass
		it = music_bass.iterator();
		while(it.hasNext())
		{
			ArrayList<PageInfo> ap = it.next();
			temp.addAll(ap);
		}
		ins_bass.Make(temp);
		temp.clear();
		// Drum
		it = music_drum.iterator();
		while(it.hasNext())
		{
			ArrayList<PageInfo> ap = it.next();
			temp.addAll(ap);
		}
		ins_drum.MakeDrum(temp);
		temp.clear();
		
		ins_piano.Play();
		ins_guitar.Play();
		ins_bass.Play();
		ins_drum.Play();
		
		// 현재 재생정보
		setPlay();
		PreviewPanel.repaint();
 }
 private void eraseNote()		
 {
	 switch(select)
	  {
	  case PIANO:
		  if(page_piano.size()>0)
		  {
			  page_piano.remove(page_piano.size()-1);
		  }
		  break;
	  case GUITAR:
		  if(page_guitar.size()>0)
		  {
			  page_guitar.remove(page_guitar.size()-1);
		  }
		  break;
	  case BASS:
		  if(page_bass.size()>0)
		  {
			  page_bass.remove(page_bass.size()-1);
		  }
		  break;
	  case DRUM:
		  if(page_drum.size()>0)
		  {
			  page_drum.remove(page_drum.size()-1);
		  }
		  break;
	  }
	  drawPanel.repaint();
	  PreviewPanel.repaint();
 }
 class InstrumentControl extends MouseAdapter // 악기 버튼 리스너
	{
		public void mouseClicked(MouseEvent e)
		{
			JButton bt = (JButton) e.getSource();
			if (bt == piano)
			{
				piano.setBorderPainted(false);
				guitar.setBorderPainted(true);
				bass.setBorderPainted(true);
				drum.setBorderPainted(true);
				select = PIANO;
			}
			else if (bt == guitar)
			{
				piano.setBorderPainted(true);
				guitar.setBorderPainted(false);
				bass.setBorderPainted(true);
				drum.setBorderPainted(true);
				select = GUITAR;
			}
			else if (bt == bass)
			{
				piano.setBorderPainted(true);
				guitar.setBorderPainted(true);
				bass.setBorderPainted(false);
				drum.setBorderPainted(true);
				select = BASS;
			}
			else if (bt == drum)
			{
				piano.setBorderPainted(true);
				guitar.setBorderPainted(true);
				bass.setBorderPainted(true);
				drum.setBorderPainted(false);
				select = DRUM;
			}
			drawPanel.repaint();
			PreviewPanel.repaint();
		}
	}
 class LineControl extends MouseAdapter // 격자 버튼 리스너
	{
		public void mouseClicked(MouseEvent e)
		{
			JButton bt = (JButton) e.getSource();
			if (bt == width)
			{
				drawPanel.repaint();
				if (widthFlag)
				{
					widthFlag = false;
					width.setIcon(widthj);
				}
				else
				{
					widthFlag = true;
					width.setRolloverIcon(widthj);
					width.setRolloverSelectedIcon(widthj);
					width.setIcon(widthg);
				}
			}
			else if (bt == length)
			{
				drawPanel.repaint();
				if (lengthFlag)
				{
					lengthFlag = false;
					length.setIcon(lengthj);
				}
				else
				{
					lengthFlag = true;
					length.setRolloverIcon(lengthj);
					length.setRolloverSelectedIcon(lengthj);
					length.setIcon(lengthg);
				}
			}
		}
	}
 class ClickedControl extends MouseAdapter   //점 찍기 리스너
 {
	 int release_x;
	 int y;
	 int x;
	 int beat;
	 PageInfo p;
	  public void mouseReleased(MouseEvent e)
	  {
		  if(!isPlaying)
		  {
			  release_x = e.getX();
			  beat = release_x - x;
			  if(beat <= PageInfo.BASE_BEAT)
			  {
				  beat = PageInfo.BASE_BEAT;
			  }
	
			  System.out.println("x "+ (x+(numOfPage-1)*PAGE_WIDTH) +" y " +y + " beat "+beat);
			  
			  p = new PageInfo(x+(numOfPage-1)*PAGE_WIDTH,y);
			  p.setBeat(beat);
				switch(select)
				{
				case PIANO:
					page_piano.add(p);
					break;
				case GUITAR:
					page_guitar.add(p);
					break;
				case BASS:
					page_bass.add(p);
					break;
				case DRUM:
					page_drum.add(p);
					break;
				}
				clickAndPlay(p);
				drawPanel.repaint();
				PreviewPanel.repaint();
		  }
	  }
	  public void mousePressed(MouseEvent e)
	  {
		    x = e.getX();
			y = e.getY();	
	  }
 }
 class PageControl implements ActionListener	// 페이지 넘기기 버튼
 {
	 public void actionPerformed(ActionEvent e)
	 {
		JButton temp = (JButton)e.getSource();
		// 페이지 증감
		if(temp == left)
		{
			numOfPage--;
			
			// select page
			setPage(numOfPage);
		}
		else if(temp == right)
		{
			PageUp();
		}
		// 이미지 처리
		if(numOfPage==1)
		{
			left.setPath(".\\images\\left2.jpg");
			left.setEnabled(false);
		}
		else
		{
			left.setPath(".\\images\\left.jpg");
		}
		
		drawPanel.repaint();
		PreviewPanel.repaint();
		
		left.repaint();
		page.setText(numOfPage.toString());
		page.repaint();
	 }
 }
 class fileControl implements ActionListener  //파일 버튼 리스너
 {
	public void actionPerformed(ActionEvent e) {
		JButton temp = (JButton)e.getSource();
		Chooser.setFileFilter(filter);
		if(temp == save)
		{
			int ret = Chooser.showSaveDialog(MainPanel);
			
			if(ret==JFileChooser.APPROVE_OPTION)
			{
				
				File saveFile = Chooser.getSelectedFile();
				String path = saveFile.getPath();
				path += ".esc";
				System.out.println(path);
				System.out.println("Serialize");
				try{
					FileOutputStream fileStream = new FileOutputStream(new File(path));
					ObjectOutputStream os = new ObjectOutputStream(fileStream);
					os.writeObject(music_piano);
					os.writeObject(music_guitar);
					os.writeObject(music_bass);
					os.writeObject(music_drum);
					os.close();
				}catch(Exception ex) {
					ex.printStackTrace();
					System.out.println("------ERROR------");
					System.out.println("      SAVE");
				}
			}
		}
		else if(temp == open)
		{
			int ret = Chooser.showOpenDialog(MainPanel);
			
			if(ret == JFileChooser.APPROVE_OPTION)
			{
				File selectedFile = Chooser.getSelectedFile();
				String path = selectedFile.getPath();
				System.out.println("Restore");
				try{
					FileInputStream fileStream = new FileInputStream(new File(path));
					ObjectInputStream is = new ObjectInputStream(fileStream);
					music_piano = (ArrayList<ArrayList<PageInfo>>) is.readObject();
					music_guitar = (ArrayList<ArrayList<PageInfo>>) is.readObject();
					music_bass = (ArrayList<ArrayList<PageInfo>>) is.readObject();
					music_drum = (ArrayList<ArrayList<PageInfo>>) is.readObject();
					is.close();
				}
				catch(Exception ex) {
					ex.printStackTrace();
					System.out.println("------ERROR------");
					System.out.println("      OPEN");
				}
				numOfPage = 1;
				
				left.setPath(".\\images\\left2.jpg");
				left.setEnabled(false);
				left.repaint();
				page.setText(numOfPage.toString());
				page.repaint();
				
				page_piano = music_piano.get(numOfPage-1);
				page_guitar = music_guitar.get(numOfPage-1);
				page_bass = music_bass.get(numOfPage-1);
				page_drum = music_drum.get(numOfPage-1);
				
				maxPage = music_piano.size();
				if(music_guitar.size()>maxPage)
					maxPage = music_guitar.size();
				if(music_bass.size()>maxPage)
					maxPage = music_bass.size();
				if(music_drum.size()>maxPage)
					maxPage = music_drum.size();
				
				drawPanel.repaint();
				PreviewPanel.repaint();
			}		
		}
	}
 }
 class playControl implements ActionListener	// 재생 버튼 리스너
  {
	  public void actionPerformed(ActionEvent e)
	  {
		  	musicStart();
	  }
  }
 class eraseControl implements ActionListener  // 지우개 버튼 리스너
  {
	  public void actionPerformed(ActionEvent e)
	  {
		  eraseNote();
	  }
  }
 class updateControl implements ActionListener
 {
	 public void actionPerformed(ActionEvent e)
	 {
		 ClientMainFrame client=new ClientMainFrame(thisFrame);
	 }
 }
}

	
class IB extends JButton
{
 String path="";
 IB(String p)
 {
  path = p;
 }
 public void setPath(String p)
 {
  path=p;
 }
 public void paintComponent(Graphics g)
 {
  Image img = new ImageIcon(path).getImage();
  g.drawImage(img, 0, 0, this);
 }
}

class PageInfo implements Serializable
{
	static final int BASE_BEAT = 10;
	private Point point = new Point();
	private int beat = BASE_BEAT;
	// set
	public void setBeat(int _beat)
	{
		beat = _beat;
	}
	public void setPoint(Point _point)
	{
		point = _point;
	}
	//get
	public int getBeat()
	{
		return beat;
	}
	public Point getPoint()
	{
		return point;
	}
	// constructor
	public PageInfo(Point point)
	{
		setPoint(point);
	}
	public PageInfo(int x, int y)
	{
		point.x = x;
		point.y = y;
	}
	public PageInfo(Point point, int beat)
	{
		setPoint(point);
		setBeat(beat);
	}
}
