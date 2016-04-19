
import java.io.*;
import java.net.*;
import java.util.*;
public class Client
{
    

  public static void main(String[] args) throws Exception {

	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
   

    ClientMachine client = new ClientMachine();
    client.initializeClient();
    
    br.readLine();        //wait for user action
    client.searchServer();
    
     
 
    System.out.println("1");
    
    client.connectToServer();
    System.out.println("2");
    client.startService();
  
}}

class ClientMachine
{
	
	String name;                 //general information
	InetAddress ip;
	boolean detected;
    InetAddress group; 
    int port ;
    
    String serverName;           //server information 
    InetAddress serverIp;
    int serverPort;
    
    Socket clientSocket;          //socket information for tcp-ip
	PrintWriter sendToServer;
	static File[] oldListRoot;
	
	void initializeClient() throws IOException
	{
		BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
		
		System.out.println("Enter name for client");
		this.name = br.readLine();
		this.ip = InetAddress.getLocalHost();
		this.detected = false;
		this.group = InetAddress.getByName("237.0.0.1");
		this.port = 9000;
		
		
	}
	
	void searchServer()
	{
		try
		{
		  MulticastSocket socket = new MulticastSocket(port);
          socket.setInterface(InetAddress.getLocalHost());
          socket.joinGroup(group);

          DatagramPacket packet = new DatagramPacket(new byte[100], 100);
          
          {System.out.println("*");
              socket.receive(packet);
            
              serverName = new String( packet.getData(), 0,
                      packet.getLength() );
  
              System.out.println("Found " + serverName);
              
              serverIp = packet.getAddress();
             
              System.out.println("IP "+ serverIp);
              
              serverPort = packet.getPort();
              
              System.out.println("Port"+ serverPort);
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
		
}
	
	
	
	
	
	void connectToServer()throws Exception
	{
		
		clientSocket = new Socket(serverIp,serverPort);
		 sendToServer = new PrintWriter(clientSocket.getOutputStream(), true);
		
		 
	}
	
	void startService()
	{
		  oldListRoot = File.listRoots();
		
		sendToServer.println(this.name);
		
		new Thread(new Runnable(){
			
			public void run(){
			  while (true) {
	                
	                   
	                
	                try{
	                if (File.listRoots().length > oldListRoot.length) {
	                	 sendToServer.println("inserted");
	                    oldListRoot = File.listRoots();
	                    

	                } else if (File.listRoots().length < oldListRoot.length) {
	                	 sendToServer.println("removed");

	                    oldListRoot = File.listRoots();

	                }
	                else
	                {
	                	 Thread.sleep(1000);
	                }
	                }
	                catch(Exception e)
	                {}

	            }
			}
	    }).start();
	    
			
			
			
		}
		
			
					
						

					
						
				
		
	
	
}