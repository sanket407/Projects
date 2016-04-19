import java.io.*;
import java.net.*;
import java.util.*;

public class Server {


   

	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub

				
		ServerMachine server = new ServerMachine();
		
		server.initializeServer();
		
		server.startServer();
		
		server.acceptConnections();
	}}


class ServerMachine{
	
	InetAddress ip;
	String name;
	int clientCount;
	InetAddress group;
	int port;
	
	ServerSocket serverSocket;
	HashMap  <Socket,String> clientSocketMap;
	
	void initializeServer()throws IOException
	{
		
	
		
		this.ip = InetAddress.getLocalHost(); 
		
		BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
		System.out.println("Enter server name");
		this.name=br.readLine();
		
		this.clientCount = 0;
		this.group = InetAddress.getByName("237.0.0.1");
		this.port = 9000;
		this.clientSocketMap = new HashMap<Socket,String>();
	}
	
	void startServer()throws IOException
	{
		
		 serverSocket = new ServerSocket(this.port);
		
		final byte[] bt = this.name.getBytes();
		 new Thread(new Runnable() {
		        @Override
		        public void run() {
		            try {
		                MulticastSocket socket = new MulticastSocket(port);
		                socket.setInterface(InetAddress.getLocalHost());
		                socket.joinGroup(group);

		                
		                byte index = 0;
		                while(true) {
		                   // System.out.println("svr broadcast");
		                    socket.send(new DatagramPacket(bt, bt.length, group, port));
		                   
		                    Thread.sleep(1*1000);
		                }
		            } catch (Exception e) {
		                e.printStackTrace();
		            }
		        }
		    }).start();
		
	}
	
	
	void acceptConnections()throws IOException{
		
		 new Thread(new Runnable(){
			 
			 public void run(){
			 
				 
				 try
				 {
			 
					 while(true)
					 {
					 Socket clientSocket =  serverSocket.accept();
				
				
			 BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		   
			 String name = br.readLine();
			 System.out.println(name+" connected");
			 clientSocketMap.put(clientSocket,name);
			 startDetecting(clientSocket);
			 
			 Thread.sleep(1000);
			 
			 }}
				 catch (Exception e)
				 {}
				 
			 } 
		 }).start();
		
		 
		
		
	
	}
	
	void startDetecting(Socket clientSocket)
	{
		
		new Thread(new Runnable(){
			
			public void run()
			{
				
				while (true)
				{
					try{
					BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					
					String status = br.readLine();
					
					System.out.println(clientSocketMap.get(clientSocket)+" "+status);
					
					Thread.sleep(1000);}
					
					catch(Exception e){}
					
				}
				
				
				
				
			}
			
			
			
		}).start();
		
		
		
		
	}
	
	
}
