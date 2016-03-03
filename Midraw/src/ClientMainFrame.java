import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;


class ClientMainFrame extends JDialog
{
	JDialog ME=this;
	JFrame MotherFrame;
	JPanel LeftMenu,LeftMainView,LeftContent,LeftCenter;
	JPanel RightMainView,RightMenu,RighrContent,RighrContent2;
	JButton upLoadButton,downButton,connect,clearHistoryButton,LogOutButton;
	ImgButton downLoadingButton,myDirectoryButton;
	JTextField userName;
	static String s_userName;
	
	static JTable SERVER_DATA_LIST;
	static JTable DOWNLODING_TABLE=null;
	static JTable Directory_TABLE=null;
	static String FILE_NAME;
	static long FILE_SIZE;
	static boolean is_connect=false;
	
	FileInputStream IS_selectFile=null;
	
	static final String SERVER_IP="14.45.56.180";
	static final int PORT=1209;
	static final int UPLOAD_PORT=1155;
	
	Socket mainSocket;
	OutputStream out=null;
	InputStream in=null;
	DataOutputStream dos=null;

	
	
	public ClientMainFrame(JFrame frame) {
		// TODO Auto-generated constructor stub
		super(frame,"파일전송",true);
		MotherFrame=frame;
		setLayout(new BorderLayout());
		init();
		
		add(LeftMainView,"Center");
		add(RightMainView,"East");
		setSize(700,500);
		setVisible(true);
	}
	
	public void init()
	{
		//좌측메인
		LeftMainView=new JPanel(new BorderLayout());
		LeftMainView.setSize(500,500);
		
		//좌측상단메뉴
		LeftMenu=new ImgPanel(".\\images\\BackGroundMenu.png", 500, 90);
		LeftMenu.setLayout(null);
		LeftMenu.setPreferredSize(new java.awt.Dimension(500,50));
		LeftMainView.add(LeftMenu,"North");
		
		upLoadButton=new ImgButton(".\\images\\uploadButton.png");
		upLoadButton.setSize(100,50);
		LeftMenu.add(upLoadButton);
		upLoadButton.addActionListener(new ButtonClick());
		
		downButton=new ImgButton(".\\images\\downButton.png");
		downButton.setLocation(100,0);
		downButton.setSize(100, 50);
		LeftMenu.add(downButton);
		downButton.addActionListener(new ButtonClick());
		
		userName=new JTextField();
		userName.setLocation(283,0);
		userName.setSize(100,50);
		LeftMenu.add(userName);
		
		connect=new ImgButton(".\\images\\connect.png");
		connect.setLocation(383,0);
		connect.setSize(100,50);
		LeftMenu.add(connect);
		connect.addActionListener(new ButtonClick());
		
		LogOutButton=new JButton("접속종료");
		LogOutButton.setLocation(383,0);
		LogOutButton.setSize(100,50);
		LogOutButton.addActionListener(new ButtonClick());
		
		//좌측 내용
		LeftContent=new JPanel(new BorderLayout());
		LeftContent.setBackground(Color.white);
		LeftMainView.add(LeftContent,"Center");
		
		String []data_list={"번호","파일명","유저명","시간"};
		DefaultTableModel data_Model=new DefaultTableModel(data_list,0)
		{
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		SERVER_DATA_LIST=new JTable(data_Model);
		SERVER_DATA_LIST.setFillsViewportHeight(true); //JTable 배경색 지정
		SERVER_DATA_LIST.setBackground(Color.white);
		SERVER_DATA_LIST.getColumnModel().getColumn(0).setPreferredWidth(1);
		LeftContent.add(new JScrollPane(SERVER_DATA_LIST));
		
		//우측메인
		RightMainView=new JPanel(new BorderLayout());
		RightMainView.setSize(200,500);
		RightMainView.setPreferredSize(new java.awt.Dimension(200,500));
		RightMainView.setBackground(new Color(241,242,243));
	
		//우측메ㄴ뉴
		RightMenu=new ImgPanel(".\\images\\BackGroundMenu.png", 200, 90);
		RightMenu.setLayout(null);
		RightMenu.setPreferredSize(new java.awt.Dimension(200,50));
		RightMainView.add(RightMenu,"North");
		
		downLoadingButton=new ImgButton(".\\images\\downloadingON.png");
		downLoadingButton.setLocation(0,0);
		downLoadingButton.setSize(100,50);
		downLoadingButton.setBorderPainted(false);
		RightMenu.add(downLoadingButton);
		downLoadingButton.addActionListener(new ButtonClick());
		
		myDirectoryButton=new ImgButton(".\\images\\MyDirectoryOFF.png");
		myDirectoryButton.setLocation(100, 0);
		myDirectoryButton.setSize(100,50);
		RightMenu.add(myDirectoryButton);
		myDirectoryButton.addActionListener(new ButtonClick());
		///
		RighrContent=new JPanel(new BorderLayout());
		RightMainView.add(RighrContent,"Center");
		
		String []downloading_list={"파일명","용량","상태"};
		DefaultTableModel dwModel=new DefaultTableModel(downloading_list,0)
		{
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		DOWNLODING_TABLE=new JTable(dwModel);
		DOWNLODING_TABLE.setFillsViewportHeight(true);
		DOWNLODING_TABLE.setBackground(new Color(241,242,243));
		RighrContent.add(new JScrollPane(DOWNLODING_TABLE),"Center");
		
		clearHistoryButton=new JButton("정리하기");
		RighrContent.add(clearHistoryButton,"South" );
		clearHistoryButton.addActionListener(new ButtonClick());
		
		RighrContent2=new JPanel(new BorderLayout());
	
		
		String []my_list={"파일명","용량"};
		DefaultTableModel myDModel=new DefaultTableModel(my_list,0)
		{
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		Directory_TABLE=new JTable(myDModel);
		Directory_TABLE.setFillsViewportHeight(true);
		Directory_TABLE.setBackground(new Color(241,242,243));

		RighrContent2.add(new JScrollPane(Directory_TABLE));
		
		
	}
	
	public void ConnectSucess()
	{
		if(is_connect)
		{
			LeftMenu.remove(userName);
			LeftMenu.remove(connect);
			LeftMenu.add(LogOutButton);
			LeftMenu.repaint();
		}
		else
		{
			LeftMenu.remove(LogOutButton);
			LeftMenu.add(userName);
			LeftMenu.add(connect);
			LeftMenu.repaint();
		}
	
		
	}
	
	class ButtonClick implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			Object obj=e.getSource();
			if(obj==upLoadButton && is_connect)
			{
				JFileChooser filechooser=new JFileChooser();
				filechooser.setCurrentDirectory(new File(System.getProperty("user.home")+"//"+"Desktop"));
				FileNameExtensionFilter filter=new FileNameExtensionFilter("*.*", "*.*");
				filechooser.addChoosableFileFilter(filter);
				int result = filechooser.showOpenDialog(null);
				if(result==JFileChooser.APPROVE_OPTION)
				{
					FILE_NAME=filechooser.getSelectedFile().getName();
					try {
						IS_selectFile=new FileInputStream(filechooser.getSelectedFile().getPath()); 
						File readFile=new File(filechooser.getSelectedFile().getPath());
						FILE_SIZE=readFile.length();
						System.out.println(FILE_SIZE);
						readFile=null; //파일 사이즈를 위해서 사용
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				
				
				System.out.println("전송");
				Socket transSocket=null;
				try {
					dos.writeUTF("2");
					Thread.sleep(50);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				catch(Exception e2)
				{
					e2.printStackTrace();
				}
				try
				{
					transSocket=new Socket(SERVER_IP,1210);
					//DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
					
				}
				catch(IOException ie)
				{
					ie.printStackTrace();
				}
				FileSend fst;
				fst=new FileSend(SERVER_IP,UPLOAD_PORT,IS_selectFile,transSocket,ME);
				fst.start();	
				}
			}
			else if(obj==connect)
			{
				try
				{
					if(userName.getText().trim().length()!=0)
					{
						mainSocket=new Socket(SERVER_IP,PORT);
						out=mainSocket.getOutputStream();
						in=mainSocket.getInputStream();
						dos=new DataOutputStream(out);
						dos.writeInt(1209);
						Thread.sleep(50);
						dos.writeUTF("1");
						dos.writeUTF("connect;"+userName.getText());
						ListReceiver test=new ListReceiver(mainSocket);
						test.start();
						JOptionPane.showMessageDialog(rootPane, "서버와 연결이 되었습니다.");
						is_connect=true;
						s_userName=userName.getText();
						ConnectSucess(); //로그인관련 창을 지운다
						
					}
					else
					{
						JOptionPane.showMessageDialog(rootPane, "사용자 명을 적어주세요");
					}
				}
				catch(ConnectException ee)
				{
					//ee.printStackTrace();
					JOptionPane.showMessageDialog(rootPane, "서버상태를 확인해주세요.");
				}
				catch(Exception ee)
				{
					ee.printStackTrace();
				}
			}
			else if(downButton==obj  && is_connect)
			{
				int[] rows=SERVER_DATA_LIST.getSelectedRows();
				for(int i=0;i<rows.length;i++)
				{
					
					System.out.println(rows[i]);
					String FileName=SERVER_DATA_LIST.getValueAt(rows[i], 1).toString();
					String FileDate=SERVER_DATA_LIST.getValueAt(rows[i], 3).toString();
					
					Socket fileReceiveSocket=null;
					try
					{
						dos.writeUTF("3");
						Thread.sleep(50);
						fileReceiveSocket=new Socket(SERVER_IP,1211);
						FileReceiver Fr=new FileReceiver(fileReceiveSocket,FileDate,FileName);
						Fr.start();
					}
					catch(IOException IE)
					{
						IE.printStackTrace();
					}
					catch(Exception Ex)
					{
						Ex.printStackTrace();
					}
				}
			}
			else if(myDirectoryButton==obj)
			{
				myDirectoryButton.changeImage(".\\images\\MyDirectoryOn.png");
				myDirectoryButton.setBorderPainted(false);
				downLoadingButton.changeImage(".\\images\\downloadingOFF.png");
				downLoadingButton.setBorderPainted(true);
				
				RightMainView.remove(RighrContent);
				RightMainView.remove(RighrContent2);
				RightMainView.add(RighrContent2,"Center");
				DefaultTableModel model=(DefaultTableModel)ClientMainFrame.Directory_TABLE.getModel();
				
				while(true) //모든 열 삭제
				{
					try
					{
						model.removeRow(0);
					}
					catch(ArrayIndexOutOfBoundsException e2)
					{
						break;
					}
				}
				
				
				if(((JComponent)e.getSource()).isVisible())
				{
				//`	String[] fileList = null;
					ArrayList<String> fileList=new ArrayList<String>();
					ArrayList<String> fileSize=new ArrayList<String>();
					
					
					MyFileList file=new MyFileList();
					file.showFile(fileList, fileSize);
					
					for(int i=0;i<fileList.size();i++)
					{
						String[] list=new String[2];
						list[0]=fileList.get(i);
						list[1]=fileSize.get(i);
					//	System.out.println("sss");
						model.addRow(list);
					}
		
				}
			}
			else if(downLoadingButton==obj)
			{
				
				RightMainView.remove(RighrContent);
				RightMainView.remove(RighrContent2);
				RightMainView.add(RighrContent,"Center");
				myDirectoryButton.changeImage(".\\images\\MyDirectoryOFF.png");
				myDirectoryButton.setBorderPainted(true);
				downLoadingButton.changeImage(".\\images\\downloadingON.png");
				downLoadingButton.setBorderPainted(false);
				RighrContent.repaint();
				
			}
			else if(clearHistoryButton==obj)
			{
				DefaultTableModel model=(DefaultTableModel)ClientMainFrame.DOWNLODING_TABLE.getModel();
				int count=0;
				while(true) //모든 열 삭제
				{
					try
					{
						if(!model.getValueAt(count, 2).equals("전송중"))
							model.removeRow(0);
						else
							count++;
					}
					catch(ArrayIndexOutOfBoundsException e2)
					{
						break;
					}
				}
			}
			else if(!is_connect)
			{
				JOptionPane.showMessageDialog(rootPane, "접속하세요");
			}
			else if(LogOutButton==obj)
			{
				try {
					mainSocket.close();
					is_connect=false;
					ConnectSucess();
					s_userName="";
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		}
	}
}
class FileReceiver extends Thread
{
	Socket socket;
	final static int BUFFER_SIZE=1024*4;;
	InputStream is;
	FileOutputStream fos;
	String FileDate;
	String FileName;
	String SavePath;
	int listSize;
	public FileReceiver(Socket socket,String FileDate,String FileName) {
		// TODO Auto-generated constructor stub
		this.socket=socket;
		this.FileDate=FileDate;
		this.FileName=FileName;
	}
	
	public void addTableData()
	{
		String []arr=new String[3];
		arr[0]=FileName;
		arr[1]="0 Byte";
		arr[2]="전송중";
		DefaultTableModel model=(DefaultTableModel)ClientMainFrame.DOWNLODING_TABLE.getModel();
		model.addRow(arr);
		synchronized (this) {
			listSize=ClientMainFrame.DOWNLODING_TABLE.getRowCount();
		}
	}
	
	
	public void run()
	{
		byte[] buffer;
		buffer=new byte[BUFFER_SIZE];
		int readLen;
		double totalSize=0.0;
		try
		{
			synchronized (this) {
				BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				bw.write(FileName+"\n");
				bw.flush();
				
				//System.out.println(FilePath);
				SavePath="./fileTransfer/";
				File dir=new File(SavePath);
				if(!dir.exists())
				{
					dir.mkdir();
				}
				String FilePath=SavePath+FileName;
				fos=new FileOutputStream(FilePath);
				System.out.println("파일전송중");
				addTableData();

				while(true)
				{
					is=socket.getInputStream();
					readLen=is.read(buffer);
					if(readLen==-1)
					{
						break;
					}
					totalSize+=(double)(readLen/1024.0);
					totalSize=((int)(totalSize*100))/100.0;
					ClientMainFrame.DOWNLODING_TABLE.setValueAt(totalSize+"KB", listSize-1, 1); //용량 증가
					fos.write(buffer,0,readLen);
				}
				ClientMainFrame.DOWNLODING_TABLE.setValueAt("전송완료", listSize-1, 2);
				System.out.println("전송완료");
				fos.close();
				socket.close();
			}
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}


//데이터리스트 로드
class ListReceiver extends Thread
{
	Socket socket;
	DataInputStream in;
	boolean is_end=false;
	DefaultTableModel model=(DefaultTableModel)ClientMainFrame.SERVER_DATA_LIST.getModel();
	public ListReceiver(Socket socket)
	{
		this.socket=socket;
		while(true) //모든 열 삭제
		{
			try
			{
				model.removeRow(0);
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				break;
			}
		}
		
	}
	public void run()
	{
		BufferedReader br;
		String Data="";
		
		int allCount=0;

		
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while(true)
			{
				Data=br.readLine();
				if(Data.equals("end"))
				{
					is_end=true;
					System.out.println("끝");
				}
				else
				{
					if(is_end==true)
					{
						while(true) //모든 열 삭제
						{
							try
							{
								model.removeRow(0);
							}
							catch(ArrayIndexOutOfBoundsException e)
							{
								break;
							}
						}
						allCount=0;
						is_end=false;
					}
					
					allCount++;
					if(Data==null)
						break;
				
					String []dataList=MyUtil.parseMsg(Data);
					Object []inserData=new Object[4];
					inserData [0]=allCount+"";
					inserData [1]=dataList[0];
					inserData [2]=dataList[1];
					inserData [3]=dataList[2];
				
				
					model.addRow(inserData);
				}
			}
			br=null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}

//파일전송
class FileSend extends Thread
{
	JDialog Mother;
	FileInputStream fis;
	OutputStream os;
	int port;
	Socket socket;
	String serverIP;
	final static int BUFFER_SIZE=1024*4;
	
	public FileSend(String serverIP,int port,FileInputStream fis,Socket socket,JDialog Mother) {
		// TODO Auto-generated constructor stu
		this.serverIP=serverIP;
		this.port=port;
		this.fis=fis;
		this.socket=socket;
		this.Mother=Mother;
	}
	
	public void run()
	{
		int readLen;
		byte[] buffer=new byte[BUFFER_SIZE];
		try
		{
				os=socket.getOutputStream();
				synchronized (this) {
				BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				String fileName=ClientMainFrame.FILE_NAME;
				System.out.println(fileName);
				bw.write(fileName+"\n");
				bw.flush();
				
				int totalSize=0;
				while(true)
				{
					readLen=fis.read(buffer);
					if(readLen==-1)
						break;
					System.out.println(readLen+"--Byte");
					os.write(buffer,0,readLen);
					totalSize+=readLen;
				}
				System.out.println(totalSize+"헤헤");
				socket.close();
				fis.close();
				JOptionPane.showMessageDialog(Mother, "전송완료");
			}

		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
}



class ImgPanel extends JPanel
{
	String Path;
	ImgPanel(String Path,int width,int height)
	{
		this.Path=Path;
		setSize(width,height);
		//setLayout(new BorderLayout());
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Image img=new ImageIcon(Path).getImage();
		g.drawImage(img, 0,0,this.getWidth(),this.getHeight(), this);
	}
}

class ImgButton extends JButton
{
	String Path;
	public ImgButton(String Path) {
		// TODO Auto-generated constructor stub
		this.Path=Path;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Image img=new ImageIcon(Path).getImage();
		g.drawImage(img, 0,0,this.getWidth(),this.getHeight(), this);
	}
	
	public void changeImage(String Path)
	{
		this.Path=Path;
		repaint();
	}
}

class MyFileList
{
	final String filePath="./fileTransfer/";
	File f;
	String []list;
	File dir;
	MyFileList()
	{
		f=new File(filePath);
		list=f.list();
		dir=new File(filePath);		
	}
	public void showFile(ArrayList<String> fileList, ArrayList<String> fileSize)
	{
		
		for(int i=0;i<list.length;i++)
		{
			File f2=new File(filePath,list[i]);
			if(!dir.exists())
			{
				dir.mkdir();
			}
			if(!f2.isDirectory())
			{
				fileList.add(list[i]);
				fileSize.add( ((int)((f2.length()/1024.0)*100))/100.0 +" KB");
			}
		}
	}
}