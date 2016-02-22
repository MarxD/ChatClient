package Thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

import Frame.ChatClient;

public class ClientThread implements Runnable
{
	ChatClient client;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private String strReceive,strKey;
	private Thread threadTalk;
	private StringTokenizer st;
	public ClientThread(Socket s,ChatClient client)throws IOException
	{
	   this.socket=s;
	   in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
	   threadTalk=new Thread(this);
	   this.client = client;
	   threadTalk.start();
	   
	}
	public void run()
	{
		while(true)
		{
			synchronized(this)
			{
				try
				{
					strReceive=in.readLine();
					st=new StringTokenizer(strReceive,"|");
					strKey=st.nextToken();
					if(strKey.equals("talk"))
					{
						String strTalk=st.nextToken();
						strTalk=client.textViewTalk.getText()+"\r\n"+strTalk;
						client.textViewTalk.setText(strTalk);
					}
					else if(strKey.equals("online"))
					{
						String strOnline;
						while(st.hasMoreTokens())
						{
						 strOnline=st.nextToken();
						 client.listOnline.addItem(strOnline);
						}
					}
					else if(strKey.equals("remove"))
					{
						String strRemove;
						while(st.hasMoreTokens())
						{
							strRemove=st.nextToken();
							client.listOnline.removeItem(strRemove);
						}
					}
					else if(strKey.equals("warning"))
					{
						String strwarning=st.nextToken();
						client.popWindows(strwarning,"Warning");
					}
					Thread.sleep(1000);
				}
				catch(Exception e)
				{
					
				}
			}
	   }
	}
}