package proj;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import org.json.JSONObject;

public class UploadFile
{
	public static void POSTRequest(String token,String cloudURL) throws IOException 
	{
		try
		{
		Scanner sc=new Scanner(System.in);
    	URL domain = new URL(cloudURL+"/"+"api"+"/"+"upload_file");
    	MultipartUtility multipartutility = new MultipartUtility(domain, "UTF-8");
    	System.out.println("Enter the file path which you want to upload.");
    	String filePath=sc.nextLine();
    	File file = new File(filePath);
    	multipartutility.addFormField("source_type", "raw");
    	multipartutility.addFormField("token", token);
    	multipartutility.addFormField("filter", "all");
    	multipartutility.addFilePart("file", file, true);
    	String res=multipartutility.finish();
    	JSONObject jsonObject = new JSONObject(res);
    	String filePass;
    	filePass= jsonObject.getJSONObject("result").getString("file");
    	System.out.println("File uploaded Successfully");
    	String pass=filePass;
    	ShowDevices.POSTRequest(token, cloudURL,pass);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}