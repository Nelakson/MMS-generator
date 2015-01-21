import java.util.*;
import java.io.*;
//import java.util.Base64;
import javax.xml.bind.DatatypeConverter; 
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

public class EditIP
{
    public static int[] getRandomIPs()
    {
    	int[] srcNdestIPs = new int[8]; //Using IPv4.
    	String randSrc_ip = "";
        try
        {
            randSrc_ip = choose(new File(System.getProperty("user.dir")+"/addresses/sender_IPs.txt")).trim();
        }
        catch(IOException e){}
        
        String randDest_ip = "";
        try
        {
            randDest_ip = choose(new File(System.getProperty("user.dir")+"/addresses/receiver_IPs.txt")).trim();
        }
        catch(IOException e){}
        
        String[] srcBytes = randSrc_ip.split("\\.");
        System.out.println(srcBytes.length);

        String[] destBytes = randDest_ip.split("\\.");
        System.out.println(destBytes.length);
        
        for(int i=0;i<srcBytes.length;i++)
        {
        	srcNdestIPs[i] = Integer.parseInt(srcBytes[i]);
        	srcNdestIPs[i+4] = Integer.parseInt(destBytes[i]);
        //	System.out.print(srcNdestIPs[i]+"_"+srcNdestIPs[i+4]+"\n");
        }
        return srcNdestIPs;
    }
    
    public static int[] getHostIP()
    {
    	int[] ip = new int[8];
    	String ip_bytes = "";
    	String tempIP = "";
        String regex = "\\d+"; //regular expression for digits.
        try
        {
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while(e.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                InetAddress i = (InetAddress) ee.nextElement();
                tempIP = i.getHostAddress().trim().replace(".", ""); //remove all full stops.
                System.out.println(i.getHostAddress());
                if(!tempIP.equals("127001") && tempIP.matches(regex))
                {
                	ip_bytes = i.getHostAddress().trim();
                	break;
                }
            }
        }
        }
        catch (Exception e) {e.printStackTrace();}
        String[] splt = ip_bytes.split("\\.");
        for(int i=0;i<splt.length;i++)
        {
        	ip[i] = Integer.parseInt(splt[i]);
        	ip[i+4] = Integer.parseInt(splt[i]);
        }
        System.out.println("MAchine IP: "+ip_bytes);
        
        return ip;
    }
	
	  public static String choose(File f) throws FileNotFoundException
	  {
	     String result = null;
	     Random rand = new Random();
	     int n = 0;
	     for(Scanner sc = new Scanner(f); sc.hasNext(); )
	     {
	        ++n;
	        String line = sc.nextLine();
	        if(rand.nextInt(n) == 0)
	           result = line;         
	     }

	     return result;      
	  }
	  
	  public static void editIP(String originalFile, String newFile)
	  {
	
		  FileInputStream in = null;
	        FileOutputStream out = null;
	        try
	        {
	            in = new FileInputStream(originalFile);
	            //in = new FileInputStream(args[0]);
	            out = new FileOutputStream(newFile);
	            int c;
	            LinkedList<Integer> cTemp = new LinkedList<Integer>();
	            LinkedList<Integer> cc = new LinkedList<Integer>();

	            int[] ipLess = {0,0,0,0,0,0,0,0};
	            int[] ipv4 = ipLess;
	            int[] srcNdest = getHostIP();
	            int[] new_srcNdest = getRandomIPs();         
	            while ((c = in.read()) != -1)
	            {
	                ipv4[0] = ipv4[1];
	                ipv4[1] = ipv4[2];
	                ipv4[2] = ipv4[3];
	                ipv4[3] = ipv4[4];
	                ipv4[4] = ipv4[5];
	                ipv4[5] = ipv4[6];
	                ipv4[6] = ipv4[7];
	                ipv4[7] = c;
	                
	                if(Arrays.equals(srcNdest, ipv4))
	                {
	                	for(int last=0;last<7;last++) cTemp.removeLast(); //remove last seven entries by removing the last element seven times (seven and not eight because the last byte has not yet been added to the linked list).
	                	for(int edit=0;edit<8;edit++) cTemp.add(new_srcNdest[edit]); //Add new src and dest address 
	                	ipv4 = ipLess; //reset array ipv4 to ipLess address.
	                }
	                else
	                {
	                	cTemp.add(c);
	                	//out.write(c);
	                	//System.out.print(c+".");
	                }
	                cc.add(c);
	            }
	            in.close();
	            for(int cp=0;cp<cTemp.size();cp++)
	            {
	            	out.write(cTemp.get(cp));
	            	//System.out.print(cTemp.get(cp)+".");
	            }
	            out.close();
	            //for(int ccs=0;ccs<cc.size();ccs++) System.out.print(cc.get(ccs)+".");
	            System.out.println("\n");
	            System.out.println(cTemp.size()==cc.size());
	        } 
	        catch (IOException ex1){ex1.printStackTrace();}
	  }
	  
	public static void main(String[] args)
	{                 
		editIP("outputStream0.pcap","edited.pcap");
	}
}