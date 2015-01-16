import java.io.RandomAccessFile;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.*;
import java.io.File;  
import java.io.FileWriter;  
import java.io.IOException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.text.SimpleDateFormat;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.xml.bind.DatatypeConverter;

public class Presentation
{
	private int Width; //Width(in pixels) of MMS region
	private int Height; //Height(in pixels) of MMS region
	private int SlideDuration; // in milliseconds
	private ArrayList<MmsPart> PresParts; //MMS presentation parts
	/** Presentation content type*/
	private String PresType;
	/** Presentation content tranfer encoding*/
	private String ContentTransferEncoding;
	/** Charset of the presentation */
	private String PresCharset;
	
	public Presentation(ArrayList<MmsPart> mmsParts)
	{
		this.Width = 120;
		this.Height = 120;
		this.SlideDuration = 10000;
		this.PresParts = mmsParts;
		this.PresType = MmsMessage.CTYPE_SMIL+ "; charset="+ MmsMessage.CHARSET_UTF8;
		this.ContentTransferEncoding = MmsMessage.ENCODING_7BIT;
	}
	
	public void setSlideDuration(int d)
	{
		this.SlideDuration = d;
	}
	public int getSlideDuration()
	{
		return this.SlideDuration;
	}	
	
	public void setWidth(int w)
	{
		this.Width = w;
	}
	public int getWidth()
	{
		return this.Width;
	}
	
	public void setHeight(int h)
	{
		this.Height = h;
	}
	public int getHeight()
	{
		return this.Height;
	}
	
	public ArrayList<MmsPart> getPresParts()
	{
		return this.PresParts;
	}
	
	private String layout()
	{
		//Using a portrait layout with same width for image and text region.
		String root = "<root-layout width=\"" + Integer.toString(getWidth()) +"\" height=\"" + Integer.toString(getHeight()) +"\"/>\n";
		String imgRegion = "<region id=\"Image\" width=\"100%\" height=\"75%\" left=\"0\" top=\"0\"/>\n";
		String txtRegion = "<region id=\"Text\" width=\"100%\" height=\"25%\" left=\"0\" top=\"75%\"/>\n";
		return "<layout>\n"+ root + imgRegion + txtRegion + "</layout>\n"; 
	}
	
	private String head()
	{
		return "<head>\n"+ layout() + "</head>\n";  
	}
	
	private String slide(ArrayList<MmsPart> mmsParts)
	{
		String regions = "";
		for(int i=0;i<mmsParts.size();i++)
		{
			if(!mmsParts.get(i).inTextRegion())
			{
				regions = regions + "<img src=\"" + mmsParts.get(i).getPartPath() + "\" region=\"Image\"/>\n";
			}
			else
			{
				regions = regions + "<text src=\"" + mmsParts.get(i).getPartPath() + "\" region=\"Text\"/>\n";
			}
		}
		return "<par dur=\"" + getSlideDuration() + "ms\">\n" + regions + "</par>\n";
	}
	
	private String descHeader()
	{
		String partHeader = "\n"+MmsMessage.PART_BOUNDARY+"\n";
		//if(this.hasId()) part = part + ("Content-ID: "+ this.partId + "\n"); 
		/*if(this.hasContentType())*/ partHeader = partHeader + ("Content-Type: "+ this.PresType + "\n"); 
		/*if(this.hasTransferEncoding())*/ partHeader = partHeader + ("Content-Transfer-Encoding: "+ this.ContentTransferEncoding + "\n"); 
		//if(this.hasLocation()) part = part + ("Content-Location: "+ this.contentLocation + "\n");
		return partHeader;
	}
	
	public String getPresentationContent()
	{
		return descHeader() + "\n<smil>\n"+ head() + "<body>\n"+ slide(this.getPresParts()) + "</body>\n" + "</smil>\n\n";  	
	}
}
