package proj;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Scanner;
import org.json.JSONObject;
import java.util.regex.Matcher; 
import java.util.regex.Pattern; 

public class AuthenticationApi {
	static String token="";
	public static boolean isValid(String email) 
    { 
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                            "[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                            "A-Z]{2,7}$"; 
                              
        Pattern pat = Pattern.compile(emailRegex); 
        if (email == null) 
            return false; 
        return pat.matcher(email).matches(); 
    } 
	public static boolean isURLValid(String url) 
    { 
        try { 
            new URL(url).toURI(); 
            return true; 
        } 
        catch (Exception e) { 
            return false; 
        } 
    } 
      
	static String email, cloudURL;
	public static void main(String[] args) {
		try {
			Scanner sc=new Scanner(System.in);
			do
			{
			System.out.println("Enter the email id");
			email=sc.nextLine();
			}
			while(!isValid(email));
			
			System.out.println("");
			System.out.println("Enter the API access key");
			String api=sc.nextLine();
			System.out.println("");
			
			do
			{
			System.out.println("Enter the url for getting token");
			cloudURL=sc.nextLine();
			}
			while(!isURLValid(cloudURL));
			System.out.println("");
            URL url = new URL (cloudURL+"/"+"api"+"/"+"access");   //This is the url of the api
            String encoding = Base64.getEncoder().encodeToString((email+":"+api).getBytes("UTF-8"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");        //we are using post method
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Basic " + encoding);
            InputStream content = (InputStream)connection.getInputStream();
            BufferedReader input   = 
                new BufferedReader (new InputStreamReader (content));
            String line = "";
            while ((line = input.readLine()) != null) {
            	//System.out.println(line);                     //we are getting the api response
            	JSONObject jsonObject = new JSONObject(line); 
            	token = jsonObject.getJSONObject("result").getString("token");
                System.out.println("Your Api access token is= "+token);  //getting the token in a string
            	UploadFile.POSTRequest(token,cloudURL);
            } 
		}catch(Exception e) {
            e.printStackTrace();
        }
	}
}
