package Frame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import Thread.ClientThread;

public class ChatClient extends JFrame implements ActionListener
{
	JFrame frame=new JFrame("JAVA聊天室");
	JPanel panelLogin,panelBack,panelTalk;
	JLabel labelServerIP,labelName,labelPassword,labelTalk,labelTo;
	public JTextArea textViewTalk;
	JScrollPane jsp;
	JTextField textTalk,textServerIP,textName;
	JPasswordField textPassword;
	JButton buttonLogin,buttonTalk, buttonReg;
	public JComboBox listOnline;
	GridBagLayout gl;
	BorderLayout bdl;
	GridBagConstraints gbc;
	JDialog dialogLogin;
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	String strSend,strReceive,strKey,strStatus;
	private StringTokenizer st;
	
	public ChatClient()
	{
		setContainer();
	}

	/**
	 * 初始化容器
	 */
	private void setContainer() 
	{
		gl=new GridBagLayout();
		bdl=new BorderLayout();
		gbc=new GridBagConstraints();
		panelBack=(JPanel)getContentPane();
		panelBack.setLayout(bdl);
		panelLogin=new JPanel();
		panelLogin.setLayout(gl);
		labelServerIP=new JLabel("服务器IP：");
		labelName=new JLabel("    用户名：");
		labelPassword=new JLabel("        密码：");
		Font font=new Font("",Font.PLAIN,14);
		textServerIP=new JTextField(12);
		textName=new JTextField(12);
		textPassword=new JPasswordField(12);
		textServerIP.setFont(font);
		textName.setFont(font);
		buttonLogin=new JButton("登录");
		buttonTalk=new JButton("发送");
		buttonReg=new JButton("注册");
		labelTalk=new JLabel("消息:");
		labelTo=new JLabel("To:");
		textTalk=new JTextField(30);
		panelTalk=new JPanel();
		textViewTalk=new JTextArea(18,40);
		textViewTalk.setFont(new Font("",Font.BOLD,16));
		jsp=new JScrollPane(textViewTalk);
		listOnline=new JComboBox();
		textViewTalk.setForeground(Color.blue);
		textViewTalk.setEditable(false);
		buttonLogin.addActionListener(this);
		buttonTalk.addActionListener(this);
		buttonReg.addActionListener(this);
	    listOnline.addItem("All");
	    panelTalk.add(labelTalk);
	    panelTalk.add(textTalk);
	    panelTalk.add(labelTo);
	    panelTalk.add(listOnline);
	    panelTalk.add(buttonTalk);
	    panelBack.add(jsp);
	    panelBack.add("South",panelTalk);
	    panelBack.setBackground(new Color(240,248,255));
	    buttonTalk.setEnabled(false);
	    frame.getContentPane().add(panelBack);
	    frame.setSize(600,450);
	    frame.setLocation(100,100);
	    frame.setVisible(true);
	    frame.setResizable(false);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    dialogLogin=new JDialog(this,"登录",true);
	    dialogLogin.getContentPane().setLayout(new FlowLayout());
	    dialogLogin.getContentPane().add(labelServerIP);
	    dialogLogin.getContentPane().add(textServerIP);
	    dialogLogin.getContentPane().add(labelName);
	    dialogLogin.getContentPane().add(textName);
	    dialogLogin.getContentPane().add(labelPassword);
	    dialogLogin.getContentPane().add(textPassword);
	    dialogLogin.getContentPane().add(buttonLogin);
	    dialogLogin.getContentPane().add(buttonReg);
	    dialogLogin.setBounds(300,300,250,180);
	    dialogLogin.getContentPane().setBackground(new Color(240,248,255));
	    dialogLogin.addWindowListener(new WindowListener()//设置监听，关闭登录窗口时退出程序
	    {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
			
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	    dialogLogin.setResizable(false);
	    dialogLogin.setVisible(true);
	}


	public static void main(String[]args)
	{
	   new ChatClient();
	}
	
	/**
	 * 连接
	 */
	private void connectServer()
	{
	  	try
	  	{
	  		socket=new Socket(textServerIP.getText(),8888);
	  		in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
	  		out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);	
	 	}
		catch(ConnectException e)
		{
			JOptionPane.showMessageDialog(this,"连接服务器失败！","ERROR",JOptionPane.INFORMATION_MESSAGE);
			textServerIP.setText("");
			System.out.println(e);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	
	}


	/**
	 * 弹窗方法
	 * @param strWarning  警告信息
	 * @param strTitle 弹窗标题
	 */
	public void popWindows(String strWarning,String strTitle)
	{
		JOptionPane.showMessageDialog(this,strWarning,strTitle,JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * 登录方法
	 * @throws IOException
	 */
	private void initLogin()throws IOException
	{
		strReceive=in.readLine();
		st=new StringTokenizer(strReceive,"|");
		strKey=st.nextToken();
		if(strKey.equals("login"))
		{
			strStatus=st.nextToken();
			if(strStatus.equals("succeed"))
			{
				buttonLogin.setEnabled(false);
				buttonTalk.setEnabled(true);
				panelLogin.setVisible(false);
				dialogLogin.dispose();
				new ClientThread(socket,this);
				out.println("init|online");
			}
	    popWindows(strKey+""+strStatus+"!","Login");
	  }
	if(strKey.equals("warning"))
	  {
		strStatus=st.nextToken();
		popWindows(strStatus,"Register");
	  }
	}

	/**
	 * 事件处理
	 */
	public void actionPerformed(ActionEvent evt)
	{
		Object obj=evt.getSource();
		try
		{
			if(obj==buttonLogin)
			{
				if((textServerIP.getText().length()>0)&&(textName.getText().length()>0)&&(new String(textPassword.getPassword()).length()>0))
				{
					connectServer();
					strSend="login|"+textName.getText()+"|"+String.valueOf(textPassword.getPassword());
					out.println(strSend);
					initLogin();
				}
				else
				{
					popWindows("请输入完整信息","登录失败");
				}
			}
			else if(obj==buttonReg)
			{
				if((textName.getText().length()>0)&&(new String(textPassword.getPassword()).length()>0))
				{
					connectServer();
					strSend="reg|"+textName.getText()+"|"+String.valueOf(textPassword.getPassword());
					out.println(strSend);
					initLogin();
				}
				else
				{
					popWindows("请输入正确注册信息", "注册错误");
				}
			}
			else if(obj==buttonTalk)
			{
				if(textTalk.getText().length()>0)
				{
					out.println("talk|"+textTalk.getText()+"|"+textName.getText()+"|"+listOnline.getSelectedItem().toString());
					textTalk.setText("");
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
}


