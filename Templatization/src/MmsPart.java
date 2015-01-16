import java.io.RandomAccessFile;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.ArrayList;
import java.util.*;
import java.util.LinkedHashMap;
import java.util.List;
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

public class MmsPart
{
	/** Identifier of the part*/
	private String partId;
	
	/** Part data */
	private byte[] partContent;
	
	/** Part content type*/
	private String partType;
	
	/** Part content tranfer encoding*/
	private String contentTransferEncoding;
	
	/** Part content file Content-Location/path*/
	private String contentLocation;
	
	/** Charset of the text component */
	private String partCharset;
	
	/** Part content path/filename */
	private String path;
	
	/**There are two regions in the SMIL description (Text and Image regions)
	 * This attribute is set to true if the part is of the text context class
	 * It is set to false if the part is a video or image.
	 *  */
	private boolean textRegion;
	
	
	
	/** 
	 * Creates an MmsPart object 
	 */
	public MmsPart(){
		this.partId = null;
		this.partContent = null;
		this.contentTransferEncoding = null;
		this.contentLocation = null;
		this.partType = MmsMessage.CTYPE_UNKNOWN;
		this.partCharset = MmsMessage.CHARSET_UTF8;
	}
	
	public MmsPart(JSONObject obj, String contentClass) throws FileNotFoundException, MmsContentException
	{
		if(contentClass.equals("text")) textPart(obj);
		if(contentClass.equals("image")) imagePart(obj);
	}

	private void textPart(JSONObject txt) throws FileNotFoundException, MmsContentException
	{
		if(!txt.get("path").toString().equals(""))
		{
			this.partCharset = MmsMessage.CHARSET_UTF8;
			this.partType = MmsMessage.CTYPE_TEXT_PLAIN + "; charset="+ this.partCharset;
			this.contentTransferEncoding = MmsMessage.ENCODING_BASE64; 
			this.path = txt.get("path").toString();
			setPartContent(txt.get("path").toString());
			this.textRegion = true; 
			//this.partContent = txt.get("text").toString().getBytes();
		}
		else
		{
			this.partId = null;
			this.partContent = null;
			this.contentTransferEncoding = null;
			this.contentLocation = null;
			this.partType = MmsMessage.CTYPE_UNKNOWN;
			this.partCharset = MmsMessage.CHARSET_UTF8;
		}
	}
	
	private void imagePart(JSONObject img) throws FileNotFoundException, MmsContentException
	{
		if(!img.get("path").toString().equals(""))
		{
			this.partType = MmsMessage.CTYPE_IMAGE_JPEG;
			this.contentTransferEncoding = MmsMessage.ENCODING_BASE64;
			this.path = img.get("path").toString();
			setPartContent(img.get("path").toString());
			this.textRegion = false;
		}
		else
		{
			this.partId = null;
			this.partContent = null;
			this.contentTransferEncoding = null;
			this.contentLocation = null;
			this.partType = MmsMessage.CTYPE_UNKNOWN;
			this.partCharset = MmsMessage.CHARSET_UTF8;
		}
	}
	
	
	/**
	 * To get the region to which a part belongs.
	 */
	public boolean inTextRegion()
	{
		return this.textRegion;
	}
	
	/**
	 * Returns the part path
	 */
	public String getPartPath()
	{
		return this.path;
	}
	
	/**
	 * checks if part has identifier.
	 */
	public boolean hasId()
	{
		return this.partId != null;
	}
	/**
	 * Sets the part identifier.
	 * @param id part identifier
	 */
	public void setPartId(String id)
	{
		this.partId = id;
	}
	/**
	 * Returns the part identifier.
	 * @return part identifier
	 */
	public String getPartId(){
		if (this.partId == null) return "";
		else return this.partId;
	}
	
	public boolean hasContent()
	{
		return this.partContent != null;
	}
	/**
	 * Sets the part data.
	 * @param buffer part data buffer
	 */
	public void setPartContent(byte buffer[])
	{
		partContent = buffer;
	}
	
	/**
	 * Sets the part data from a file.
	 * @param f part data file
	 * @throws FileNotFoundException 
	 * @throws MmsContentException part data too big
	 */
	public void setPartContent(File f) throws FileNotFoundException, MmsContentException
	{
		RandomAccessFile file = new RandomAccessFile(f, "r");
		
		byte buf[] = null;
		try{
			long fileSize = file.length();
		
			if (fileSize > Integer.MAX_VALUE)
				throw new MmsContentException("MMS Part content file ("+ f +") too big.");
		
			buf = new byte[(int)fileSize];
			file.read(buf);
		}catch(IOException e){}
		
		setPartContent(buf);
	}
	
	/**
	 * Sets the part data from a file name.
	 * This implementation calls setPartContent(new File(filePath));
	 * @param filePath path of the part data file
	 * @throws FileNotFoundException
	 * @throws MmsContentException
	 */
	public void setPartContent(String filePath) throws FileNotFoundException, MmsContentException{
		setPartContent(new File(filePath));
	}
	
	public boolean hasContentType()
	{
		return this.partType != null;
	}
	/**
	 * Sets the part content type.
	 * @param contentType part content type
	 */
	public void setPartContentType(String contentType){
		this.partType = contentType;
	}
	
	public boolean hasTransferEncoding()
	{
		return this.contentTransferEncoding != null;
	}
	/**
	 * Sets the part transfer encoding.
	 * @param transEnc part transfer encoding
	 */
	public void setPartTransferEncoding(String transEnc)
	{
		this.contentTransferEncoding = transEnc;
	}
	
	/**
	 * Sets the charset for the content.
	 * Valid only in case of text/ content type.
	 * @param charset charset
	 */
	public void setPartCharset(String charset)
	{
		this.partCharset = charset;
	}
	
	/**
	 * Returns the buffer containing the part data.
	 * @return part data buffer
	 */
	public byte[] getPartContent()
	{
		return this.partContent;
	}
	
	/**
	 * Returns the part content type.
	 * @return part content type
	 */
	public String getPartContentType()
	{
		return this.partType;
	}
	
	public boolean hasLocation()
	{
		return this.contentLocation != null;
	}
	
	/**
	 * Returns the charset for the content.
	 * Valid only in case of text/ content type.
	 * @return charset
	 */
	public String getPartCharset()
	{
		return this.partCharset;
	}
	
	public String getTransferEncoding()
	{
		return this.contentTransferEncoding;
	}
	
	public String decodeBase64(String b64)
	{
		return new String(DatatypeConverter.parseBase64Binary(b64));
	}
	public String encodeBase64(byte[] bytes)
	{
		return DatatypeConverter.printBase64Binary(bytes);	
	}
	
	public String toString()
	{
		String part = "";
		if(this.hasContent() && this.hasContentType())
		{
			part = "\n"+MmsMessage.PART_BOUNDARY+"\n";
			if(this.hasId()) part = part + ("Content-ID: "+ this.partId + "\n"); 
			if(this.hasContentType()) part = part + ("Content-Type: "+ this.partType + "\n"); 
			if(this.hasTransferEncoding()) part = part + ("Content-Transfer-Encoding: "+ this.contentTransferEncoding + "\n"); 
			if(this.hasLocation()) part = part + ("Content-Location: "+ this.contentLocation + "\n");
			if(this.hasContent()) part = part + ("\n"+encodeBase64(this.partContent)+"\n");
		}
		return part;
	}
}