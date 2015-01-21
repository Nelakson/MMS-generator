import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.*;
import java.io.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Iterator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Template
{
	/**
	 * Returns a json array of the Mmses in the input file.
	 * @param inputFile is input data file name.
	 **/
	public static JSONArray getRandomInput(String inputFile, int noOfMmses)
	{
		generateRandomInput(inputFile, noOfMmses);
		JSONArray mmses = null;
		try 
		{
			// read the json file
			FileReader reader = new FileReader(inputFile);

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
			
			// get an array from the JSON object
			mmses = (JSONArray) jsonObject.get("mmses");
			//Just for testing how java json works 
			JSONObject mms;
			for(int i=0;i<mmses.size();i++)
			{
				mms = (JSONObject) mmses.get(i);
				JSONObject content = (JSONObject) mms.get("Content");
				JSONObject txt = (JSONObject) content.get("text");
				System.out.println(mms.get("Content").toString());
				System.out.println(content.toString() + "\n\n");
				System.out.println(txt.toString() + "\n\n");
			}
            reader.close();
		}	 
		catch (FileNotFoundException ex1){ex1.printStackTrace();}
		catch (IOException ex2){ex2.printStackTrace();}
		catch (ParseException ex3){ex3.printStackTrace();}
		catch (NullPointerException ex4){ex4.printStackTrace();}
		return mmses;
	}
	
	public static void templateFile(JSONObject mms, String tempFile) throws MmsMessageException, FileNotFoundException, MmsContentException
	{
		MmsMessage msg = new MmsMessage(mms);
		msg.toString();
		System.out.println(msg.toString());
		
		try 
		{  
            // Writing to a file  
            File file = new File(System.getProperty("user.dir")+"/output_mmses/"+ tempFile);  
            //file.createNewFile();  
            FileWriter fileWriter = new FileWriter(file);  
			fileWriter.write("DATA\n");
			/*
			 * More parameters to add which I am still unsure of.
			 * */
			fileWriter.write("Date: " + msg.getDate()+"\n");
			fileWriter.write("From: " + msg.getSender()+"\n");
			fileWriter.write("To: " + msg.getReceiver()+"\n");
			if(msg.hasCC())	fileWriter.write("Cc: " + msg.getCC()+"\n");
			if(msg.hasBCC()) fileWriter.write("Bcc: " + msg.getBCC()+"\n");
			if(msg.hasSubject()) fileWriter.write("Subject: " + msg.getSubject()+"\n");
			fileWriter.write("MIME-Version: "+MmsMessage.MIME_VERSION + "\n");
			fileWriter.write("Content-Type: " +msg.getContentType() +"\n");
			fileWriter.write("X-Mms-3GPP-Version: " +MmsMessage.ThreeGPP_VERSION +"\n");
			fileWriter.write("X-Mms-MMS-Version: " +MmsMessage.MMS_VERSION_1 +"\n");
			fileWriter.write("X-Mms-Transaction-ID: " +"\"testing\"" +"\n");
			fileWriter.write("X-Mms-Message-ID: " +"\"testing\"" +"\n");
			fileWriter.write("X-Mms-Message-Type: " +MmsMessage.MMS_MESSAGE_TYPE_FORWARD_REQUEST +"\n");
			fileWriter.write("X-Mms-Message-Class: " +MmsMessage.MMS_CLASS_PERSONAL +"\n");

			
			//Parts and population of MMS ArrayList
			ArrayList<MmsPart> mmsParts = new ArrayList<MmsPart>();
			for(int i=0;i<msg.noOfParts();i++)
			{
				MmsPart t = msg.getPart(i);
				mmsParts.add(t);
				fileWriter.write(t.toString());
			}
			//Presentation
			Presentation smil = new Presentation(mmsParts);
			fileWriter.write(smil.getPresentationContent());
			
			//End 
			fileWriter.write(MmsMessage.END_SEQUENCE);
			
            fileWriter.flush();  
            fileWriter.close();  
  
        } 
        catch (IOException e)
        {  
            e.printStackTrace();  
        }
		
	}
	
	/*Returns a random line from file*/
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
	

	public static void generateRandomInput(String jsonFilename, int noOfMMSes)
	{
		//parameter noOfMMSes must be greater that zero else error
		String jsonArrayString = "{\n\"mmses\":\n[\n";
		try
		{
			File file = new File(jsonFilename);  
			//file.createNewFile();  
			FileWriter fileWriter = new FileWriter(file);
			for(int i=0;i<noOfMMSes;i++)
			{
				jsonArrayString = jsonArrayString + randMms();
			}
			fileWriter.write(jsonArrayString+"]\n}");
            fileWriter.flush();  
            fileWriter.close();
        } 
        catch (IOException e)
        {  
            e.printStackTrace();  
        }
	}

	public static String randMms()
	{
		final String[] inputParam = new String[9];
		Arrays.fill(inputParam, ""); //Initialize to empty string.
		String randSender;
    	try
    	{
    		randSender = choose(new File(System.getProperty("user.dir")+"/addresses/sender_addresses.txt"));
    		inputParam[0] = randSender;
    		//System.out.println(randSender);
    	}
    	catch(IOException e){}
    	
		String randReceiver;
    	try
    	{
    		randReceiver = choose(new File(System.getProperty("user.dir")+"/addresses/receiver_addresses.txt"));
    		inputParam[1] = randReceiver;
    		//System.out.println(randReceiver);
    	}
    	catch(IOException e){}
    	
    	inputParam[4] = "Its just a Demo; relax.";
		
		File txtDir = new File(System.getProperty("user.dir")+"/txt");
		File[] txtFiles = txtDir.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".txt");
		    }
		});
    	int txtPathIndex = (int)(Math.random()*txtFiles.length);
    	inputParam[5] = txtFiles[txtPathIndex].toString();
    	
		File imgDir = new File(System.getProperty("user.dir")+"/img");
		File[] imgFiles = imgDir.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return (name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg"));
		    }
		});
    	int imgPathIndex = (int)(Math.random()*imgFiles.length);
    	inputParam[6] = imgFiles[imgPathIndex].toString();
    	
        //System.out.println(files[r]);
		
    	return mmsJsonString(inputParam);
	}
	
	private static String mmsJsonString(String[] p)
	{
		String env = "{\n\"From\": \""+p[0]+"\",\n\"To\": \""+p[1]+"\",\n\"Cc\": \""+p[2]+"\",\n\"Bcc\": \""+p[3]+"\",\n\"subject\": \""+p[4]+"\",\n";
		String cont = "\n\"Content\":\n{\n\t\"text\":\n\t{\n\t\t\"path\":\""+p[5]+"\",\n\t},\n\t\"image\":\n\t{\n\t\t\"path\":\""+p[6]+"\",\n\t},\n\t\"audio\":\n\t{\n\t\t\"path\":\""+p[7]+"\",\n\t},\n\t\"video\":\n\t{\n\t\t\"path\":\""+p[8]+"\",\n\t}\n}\n},\n"; 
		return env+cont;
	}
	
	public static void main(String[] args) throws MmsMessageException, FileNotFoundException, MmsContentException
	{
		//JSONArray mmses = readInput("input.json");
		JSONArray mmses = getRandomInput("myJSON_inputs.json", 2);
		
		String filename;
		ArrayList<String> filenames = new ArrayList<String>();
		for(int i=0; i<mmses.size();i++)
		{
			//For mms output file names.
			filename = "mms"+i+".txt";
			filenames.add(filename);
			templateFile((JSONObject)mmses.get(i), filename);		
		}
		
		try 
		{
            File mmsFilenames = new File(System.getProperty("user.dir")+"/output_mms_filenames.txt/");  
            FileWriter writeFilename = new FileWriter(mmsFilenames);
            for(int j=0;j<filenames.size();j++)
            {	
            	writeFilename.write("output_mmses/"+filenames.get(j)+"\n");
            }
	        writeFilename.flush();  
	        writeFilename.close();
		}
        catch (IOException e){e.printStackTrace();}	

	}
}
