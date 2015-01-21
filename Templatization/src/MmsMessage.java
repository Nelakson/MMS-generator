import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MmsMessage {
	/*
	 * =========================================================================
	 * Header Value Constants
	 * =========================================================================
	 */
	public static final String MIME_VERSION = "1.0";
	public static final String ThreeGPP_VERSION = "6.5.0";
	public static final String MMS_VERSION = "1.2";
	public static final String MMS_VERSION_1 = "1.0";

	public static final String CHARSET_US_ASCII = "us-ascii";
	public static final String CHARSET_UTF8 = "utf-8";

	public static final String ENCODING_BASE64 = "base64";
	public static final String ENCODING_7BIT = "7bit";
	
	public static final String MMS_MESSAGE_TYPE_FORWARD_REQUEST = "MM4_forward_REQ";
	public static final String MMS_MESSAGE_TYPE_FORWARD_RESPONSE = "MM4_forward_RES";

	public static final String MMS_DELIVERY_REPORT_YES = "Yes";
	public static final String MMS_DELIVERY_REPORT_NO = "No";

	public static final String MMS_CLASS_PERSONAL = "Personal";
	public static final String MMS_CLASS_ADVERTISEMENT = "Advertisement";
	public static final String MMS_CLASS_INFORMATIONAL = "Informational";
	public static final String MMS_CLASS_AUTO = "Auto";

	public static final String MMS_PRIORITY_LOW = "Low";
	public static final String MMS_PRIORITY_NORMAL = "Normal";
	public static final String MMS_PRIORITY_HIGH = "High";

	public static final String MMS_READ_REPLY_YES = "Yes";
	public static final String MMS_READ_REPLY_NO = "No";

	public static final String MMS_RESPONSE_STATUS_OK = "OK";
	public static final String MMS_RESPONSE_STATUS_ERROR_UNSPECIFIED = "Error-unspecified";
	public static final String MMS_RESPONSE_STATUS_ERROR_SERVICE_DENIED = "Error-service-denied";
	public static final String MMS_RESPONSE_STATUS_ERROR_MESSAGE_FORMAT_CORRUPT = "Error-message-format-corrupt";
	public static final String MMS_RESPONSE_STATUS_ERROR_SENDING_ADDRESS_UNRESOLVED = "Error-sending-address-unresolved";
	public static final String MMS_RESPONSE_STATUS_ERROR_MESSAGE_NOT_FOUND = "Error-message-not-found";
	public static final String MMS_RESPONSE_STATUS_ERROR_NETWORK_PROBLEM = "Error-network-problem";
	public static final String MMS_RESPONSE_STATUS_ERROR_CONTENT_NOT_ACCEPTED = "Error-contant-not-accepted";
	public static final String MMS_RESPONSE_STATUS_ERROR_UNSUPPORTED_MESSAGE = "Error-unsupported-message";

	public static final String MMS_SENDER_VISIBILITY_HIDE = "Hide";
	public static final String MMS_SENDER_VISIBILITY_SHOW = "Show";

	public static final String MMS_ADDRESS_TYPE_MOBILE_NUMBER = "/TYPE=PLMN";
	public static final String MMS_ADDRESS_TYPE_MAIL = "";
	public static final String MMS_ADDRESS_TYPE_IPV4 = "/TYPE=IPV4";
	public static final String MMS_ADDRESS_TYPE_IPV6 = "/TYPE=IPV6";

	/*
	 * =========================================================================
	 * CONTENT TYPE CONSTANTS
	 * =========================================================================
	 */
	public static final String CTYPE_UNKNOWN = "*/*";
	public static final String CTYPE_TEXT = "text/*";
	public static final String CTYPE_TEXT_PLAIN = "text/plain";
	public static final String CTYPE_SMIL = "application/smil";
	public static final String CTYPE_TEXT_HTML = "text/html";
	public static final String CTYPE_TEXT_WML = "text/wnd.vap.wml";
	public static final String CTYPE_IMAGE = "image/*";
	public static final String CTYPE_IMAGE_JPEG = "image/jpeg";
	public static final String CTYPE_IMAGE_GIF = "image/gif";
	public static final String CTYPE_IMAGE_TIFF = "image/tiff";
	public static final String CTYPE_IMAGE_PNG = "image/png";
	public static final String CTYPE_IMAGE_VND_WAP_WBMP = "image/vnd.wap.wbmp";
	public static final String CTYPE_MULTIPART = "multipart/*";
	public static final String CTYPE_MULTIPART_MIXED = "multipart/mixed";
	public static final String CTYPE_APPLICATION_MULTIPART_MIXED = "application/vnd.wap.multipart.mixed";
	//The two lines below are very closely related. A change on one should be effected on the other.
	public static final String PART_BOUNDARY = "------=My_MMS_Demo_Part_distinguisher";
	public static final String CTYPE_APPLICATION_MULTIPART_RELATED = "multipart/related; type=\"application/smil\"; .boundary=\""+ PART_BOUNDARY.substring(2)+"\"";
	public static final String END_SEQUENCE = PART_BOUNDARY+"--\n\n.\nQUIT\n";
	/*
	 * =========================================================================
	 * CLASS VARIABLES
	 * =========================================================================
	 */
	private String mmsMessageType;
	private String mmsTransactionId;
	private String mmsVersion;
	private String mmsDate;
	private String mmsFrom;
	private ArrayList<String> mmsTo;
	private ArrayList<String> mmsCC;
	private ArrayList<String> mmsBCC;
	private String mmsSubject;
	private String mmsClass;

	private Date mmsExpiryTime;
	private Boolean mmsExpiryTimeAbsolute;

	private Date mmsDeliveryTime;
	private Boolean mmsDeliveryTimeAbsolute;

	private String mmsPriority;
	private String mmsSenderVisibility;
	private Boolean mmsDeliveryReport;
	private Boolean mmsReadReply;
	
	private String mmsContentType;
	private String mmsContentTransferEncoding;
	
	private String mmsResponseStatus;
	private String mmsResponseText;
	private String mmsMessageID;

	private ArrayList<MmsPart> mmsParts;
	
	public MmsMessage(JSONObject mms) throws MmsMessageException, FileNotFoundException, MmsContentException
	{
		if (mms.containsKey("")) {

		}
		mmsFrom = inputSender(mms);
		mmsTo = inputReceivers(mms);
		mmsCC = inputCC(mms);
		mmsBCC = inputBCC(mms);
		mmsParts = inputParts(mms);
		setDate(new Date()); //Today's date
		setSubject(mms);
		setContentType(CTYPE_APPLICATION_MULTIPART_RELATED);

	}

	public MmsMessage()
	{
		this.mmsTo = new ArrayList<String>();
		this.mmsCC = new ArrayList<String>();
		this.mmsBCC = new ArrayList<String>();
		this.mmsParts = new ArrayList<MmsPart>();
	}

	public boolean hasDate()
	{
		return mmsDate != null;
	}
	public void setDate(Date date)
	{
		DateFormat dt = new SimpleDateFormat("EEE, dd MM yyy HH:mm:ss Z");
		mmsDate = dt.format(date);
	}
	public String getDate()
	{
		return mmsDate;
	}
    
	public void setMessageType(String msgType)
	{
		this.mmsMessageType = msgType;
	}
	public String getMessageType()
	{
		return this.mmsMessageType;
	}

	public void setTransactionId(String transId)
	{
		this.mmsTransactionId = transId;
	}
	public String getTransactionId()
	{
		return this.mmsTransactionId;
	}

	public void setVersion(String version)
	{
		this.mmsVersion = version;
	}
	public String getVersion()
	{
		return this.mmsVersion;
	}

	public boolean hasSender()
	{
		return mmsFrom != null;
	}
	public String inputSender(JSONObject mms) throws MmsMessageException {
		// Assuming type = PLMN
		if (!mms.containsKey("From"))
			throw new MmsMessageException("Input file error. No sender field");
		if (mms.get("From").equals("") || mms.get("From") == null)
			throw new MmsMessageException("No sender address specified.");
		else
			return mms.get("From").toString() + MMS_ADDRESS_TYPE_MOBILE_NUMBER;
	}
	public String getSender()
	{
		return this.mmsFrom;
	}
	
	public boolean hasReceiver()
	{
		return mmsTo != null;
	}
	public ArrayList<String> inputReceivers(JSONObject mms)throws MmsMessageException
	{
		ArrayList<String> receiversList;
		String[] receivers;
		if (!mms.containsKey("To"))
			throw new MmsMessageException("Input file error. No receiver field");
		if (mms.get("To").equals("") || mms.get("To") == null)
			throw new MmsMessageException("No receiver address specified.");
		else
		{
			receivers = mms.get("To").toString().split(",");
			for (int i = 0; i < receivers.length; i++)
			{
				receivers[i] = receivers[i] + MMS_ADDRESS_TYPE_MOBILE_NUMBER; // Assuming type = PLMN
			}
			receiversList = new ArrayList<String>(Arrays.asList(receivers));
			// TODO: Still need to check whether or not the type(PLMN) needs to
			// be added as suffix to all recipient addresses.
			// For now we've added the suffix to all recipients.
			return receiversList;
		}
	}
	public String getReceiver()
	{
		String receivers = "";
		for(int i=0;i<mmsTo.size();i++)
		{
			receivers = receivers + mmsTo.get(i)+",";
		}
		return receivers.substring(0, receivers.length()-1);
	}

	public boolean hasCC()
	{
		return mmsCC != null;
	}
	public ArrayList<String> inputCC(JSONObject mms) throws MmsMessageException
	{
		ArrayList<String> ccList = null;
		String[] cc = mms.get("Cc").toString().split(",");
		if(mms.containsKey("Cc"))
		{
			if (cc.length > 0 && !mms.get("Cc").toString().equals(",") && !mms.get("Cc").toString().equals(""))
			{
				for (int i = 0; i < cc.length; i++)
				{
					cc[i] = cc[i] + MMS_ADDRESS_TYPE_MOBILE_NUMBER; // Assuming type = PLMN
				}
				ccList = new ArrayList<String>(Arrays.asList(cc));
		}
		}
		// TODO: Still need to check whether the type(PLMN) needs to be added as
		// suffix to all recipient addresses.
		// For now we've added the suffix to all recipients.
		return ccList;
	}
	public String getCC()
	{
		String receivers = "";
		for(int i=0;i<mmsCC.size();i++)
		{
			receivers = receivers + mmsCC.get(i)+",";
		}
		return receivers.substring(0, receivers.length()-1);
	}

	public boolean hasBCC()
	{
		return mmsBCC != null;
	}
	public ArrayList<String> inputBCC(JSONObject mms)throws MmsMessageException
	{
		ArrayList<String> bccList = null;
		String[] bcc = mms.get("Bcc").toString().split(",");
		if(mms.containsKey("Bcc"))
		{
			if (bcc.length > 0 && !mms.get("Bcc").toString().equals(",") && !mms.get("Bcc").toString().equals(""))
			{
				for (int i = 0; i < bcc.length; i++)
				{
					bcc[i] = bcc[i] +MMS_ADDRESS_TYPE_MOBILE_NUMBER; // Assuming type = PLMN
				}
				bccList = new ArrayList<String>(Arrays.asList(bcc));
			}
		}
		// TODO: Still need to check whether the type(PLMN) needs to be added as
		// suffix to all recipient addresses.
		// For now we've added the suffix to all recipients.
		return bccList;
	}
	public String getBCC()
	{
		String receivers = "";
		for(int i=0;i<mmsBCC.size();i++)
		{
			receivers = receivers + mmsBCC.get(i)+",";
		}
		return receivers.substring(0, receivers.length()-1);
	}

	public boolean hasSubject()
	{
		return mmsSubject != null;
	}
	public void setSubject(JSONObject mms)
	{
		if(mms.containsKey("Subject"))	mmsSubject = mms.get("Subject").toString();
	}
	public String getSubject()
	{
		return mmsSubject;
	}
	
	public ArrayList<MmsPart> inputParts(JSONObject mms) throws MmsMessageException, FileNotFoundException, MmsContentException
	{
		ArrayList<MmsPart> myParts = new ArrayList<MmsPart>();
		if(!mms.containsKey("Content"))throw new MmsMessageException("Input must have some content");
		else 
		{
			JSONObject content = (JSONObject) mms.get("Content");
			if(content.containsKey("text"))
			{
				JSONObject txt = (JSONObject) content.get("text");
				MmsPart txt_part = new MmsPart(txt, "text");
				myParts.add(txt_part);
			}
			
			if(content.containsKey("image"))
			{
				JSONObject img = (JSONObject) content.get("image");
				MmsPart img_part = new MmsPart(img, "image");
				myParts.add(img_part);
			}
		}
		//JSONObject img = (JSONObject) content.get("image");
		//JSONObject audio = (JSONObject) content.get("audio");
		//JSONObject video = (JSONObject) content.get("video");

		return myParts;
	}
	public MmsPart getPart(int i)
	{
		return mmsParts.get(i);
	}
	public int noOfParts()
	{
		return this.mmsParts.size();
	}

	public void setClass(String cls) {
		this.mmsClass = cls;
	}

	public void setExpiryTime(Date expTime) {
		this.mmsExpiryTime = expTime;
	}

	public void setExpiryTimeAbsolute(Boolean expTimeAbs) {
		this.mmsExpiryTimeAbsolute = expTimeAbs;
	}

	public void setDeliveryTime(Date delTime) {
		this.mmsDeliveryTime = delTime;
	}

	public void setDeliveryTimeAbsolute(Boolean delTimeAbs) {
		this.mmsDeliveryTimeAbsolute = delTimeAbs;
	}

	public void setPriority(String pr) {
		this.mmsPriority = pr;
	}

	public void setSenderVisibility(String sv) {
		this.mmsSenderVisibility = sv;
	}

	public void setDeliveryReport(Boolean dr) {
		this.mmsDeliveryReport = dr;
	}

	public void setReadReply(Boolean rr) {
		this.mmsReadReply = rr;
	}

	public boolean hasContentType()
	{
		return mmsContentType != null;
	}
	public void setContentType(String ct)
	{
		this.mmsContentType = ct;
	}
	public String getContentType()
	{
		return mmsContentType;
	}
	
	public void setResponseStatus(String rs) {
		this.mmsResponseStatus = rs;
	}

	public void setResponseText(String rt) {
		this.mmsResponseText = rt;
	}

	public void setMessageId(String mId) {
		this.mmsMessageID = mId;
	}

}
