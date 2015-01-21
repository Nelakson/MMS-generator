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

public class Test
{	
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
	  
	public static void main(String[] args)
	{
		//ArrayListt plus string
		String[] arr = {"AKO", "Me", "you", "THem"};
		ArrayList<String> al= new ArrayList<String>(Arrays.asList(arr));
		System.out.println(al);
		System.out.println(al + "haahha"+ "\n\n");

		//Split for null, empty, one Element, and many element string
		String ttn = null;
		//String[] deln = ttn.split(",");
		//System.out.println(deln);
		String tt0 = ",";
		String[] del0 = tt0.split(",");
		System.out.println(del0.length);
		String tt1 = "ae";
		String[] del1 = tt1.split(",");
		System.out.println(del1[0]);
		String ttm = "ae,artrg,afref,a";
		String[] delm = ttm.split(",");
		System.out.println(delm[0]+"\n\n");


		//Base64 decoding and encoding
		//Decoding
		String b64 = "ICAgICAgICAgICAgIMKk2KPZhtinINmI2KrYs9in2KTZhNin2KrZisKkDQogICAgICAgICAgXyBfIF8gXyBfIF8gXyBfIF8gXyBfIF8NCg0K2YXYp9iw2Kcg2KPZgdi52YQg2KjYsNin2YPYsdipINiq2LLYr9in2K8g2KjZgyDYp9i02KrZitin2YLYp9ifDQrZhdin2LDYpyDYo9mB2LnZhCDYqNmC2YTYqCDZitmG2KjYtiDYqNit2KjZgyDYo9i02YjYp9mC2KfYnw0K2YXYp9iw2Kcg2KPZgtmI2YQg2YHYrdio2YfYpyDYr9in2KEg2YjZh9iw2Kcg2KfZhNiv2KfYoSDYo9i02K8g2KXZitmE2KfZhdin2J8NCtiv2KfYoSDYo9i32LnZhdmG2Yog2KfZhNi02YLYp9ihINmI2LnZhNmF2YbZiiDZg9mEINij2YbZiNin2Lkg2KfZhNio2YPYp9ih2J8NCtmF2KfYsNinINij2YHYudmEINmE2KfZhdix2KPYqSDYo9i52LfZitiq2YfYpyDYp9mE2K3YqCDZiNmF2Kcg2YbYp9io2YbZiiDZhdmGINit2KjZh9inINil2YTYpyDYp9mE2LnYsNin2KjYnw0K2YXYp9iw2Kcg2KPZgdi52YQg2KjZg9ifDQrZitinINii2K7YsSDYp9mE2K3YqCDZitinINiy2YfYsdipINin2YTYtNio2KfYqNifDQrZhdin2LDYpyDYo9mB2LnZhCDZh9mD2LDYpyDYtNin2KHYqiDYp9mE2KPZgtiv2KfYsdifDQrZhNmF2KfYsNinINin2YTYrtmI2YEg2YjYp9mE2KPZhdixINmK2KrYt9mE2Kgg2LXYqNix2Kcg2YjYpdmK2YXYp9mG2KfYnw0K2YTZhdin2LDYpyDYp9mE2K7ZiNmBINmI2KPZhtinINij2KzYqNiqINi52YTZiSDZgtiv2LEg2KfZhNiz2KTYp9mE2J8=";
		String dec = new String(DatatypeConverter.parseBase64Binary(b64));
		System.out.println("decoded" + " = " + dec + "\n\n");
		//Encoding
		String myName = "Nelson Akoku Ebot Eno Akpa";
		String enc_dec = DatatypeConverter.printBase64Binary(dec.getBytes());
		System.out.println(enc_dec + "\n\n");
		String enc_myName = DatatypeConverter.printBase64Binary(myName.getBytes());
		System.out.println(enc_myName + "\n\n");
		
		//Test random file generator
		File dir = new File(System.getProperty("user.dir")+"/txt");
		File[] files = dir.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".txt");
		    }
		});
		
		int r;
        for (int i=0;i < 10;i++)
        {
        	r = (int)(Math.random()*files.length);
            System.out.println(files[r]);
        }
        
        //Math.random() test
        for(int k=0;k<10;k++)
        {
        	double rand = Math.random()*5;
        	System.out.println((int)rand + "__" + rand);
        }
        
        //Test random address generator
        String s;
        for(int a=0;a<6;a++)
        {
        	try
        	{
        		s = choose(new File(System.getProperty("user.dir")+"/sender_addresses.txt"));
        		System.out.println(s);
        	}
        	catch(IOException e){}
        	
          	try
        	{
        		s = choose(new File(System.getProperty("user.dir")+"/sender_addresses.txt"));
        		System.out.println(s+"\n");
        	}
        	catch(IOException e){}
        }        
        
        //Test IP address accessing.
        try
        {
            InetAddress iAddress = InetAddress.getLocalHost();
            String currentIp = iAddress.getHostAddress();
            System.out.println("Current IP address : " +currentIp); //gives only host address
        } catch (Exception e) {
            e.printStackTrace();
        }
        
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
                System.out.println(i.getHostAddress());
            }
        }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        //Test reading and editing a pcap file
        FileInputStream in = null;
        FileOutputStream out = null;
        try
        {
            in = new FileInputStream("outputStream0.pcap");
            out = new FileOutputStream("edited.pcap");
            int c;
            LinkedList<Integer> cTemp = new LinkedList<Integer>();
            LinkedList<Integer> cc = new LinkedList<Integer>();

            int[] ipLess = {0,0,0,0,0,0,0,0};
            int[] ipv4 = ipLess;
            int[] srcNdest = {172,21,0,70,172,21,0,70};
            int[] new_srcNdest = {8,3,1,2,92,0,0,0};
            
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
            	System.out.print(cTemp.get(cp)+".");
            }
            out.close();
            for(int ccs=0;ccs<cc.size();ccs++) System.out.print(cc.get(ccs)+".");
            System.out.println("\n");
            System.out.println(cTemp.size()==cc.size());
        } 
        catch (IOException ex1)
        {ex1.printStackTrace();} 
        
/*        try
        {
        	File file = new File("outputStream0.pcap");
        	Reader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "ISO-8859-1"));
        	//TODO remember to close the file streams.
        	
        	File efile = new File("edited.pcap");
			// if file doesnt exists, then create it
			if (!efile.exists()) {
				efile.createNewFile();
			}
 
			FileWriter fw = new FileWriter(efile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);			
		
            int current = 0;
            int[] ipv4 = new int[8];
            int[] srcNdest = {172,21,0,70,172,21,0,70};
            
            do
            {
                current = br.read();
                ipv4[0] = ipv4[1];
                ipv4[1] = ipv4[2];
                ipv4[2] = ipv4[3];
                ipv4[3] = ipv4[4];
                ipv4[4] = ipv4[5];
                ipv4[5] = ipv4[6];
                ipv4[6] = ipv4[7];
                ipv4[7] = current;
                
                bw.write(current);              
                System.out.print(current+".");
            }while(current != -1);
            bw.close();
            br.close();
        }
        catch (IOException ex1)
        {ex1.printStackTrace();} 
	*/
	}
}