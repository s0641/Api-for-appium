package proj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONObject;


public class Release
{
	 static String token = "";
	 Release(String token)
	 {
		 Release.token = token;
	 }
		public static void POSTRequest(String token,ArrayList<Integer> autoId,String cloudURL,int n) throws IOException {
			Scanner sc=new Scanner(System.in);

		    final String POST_PARAMS = "{\"token\":\"" + token +"\",\"automationId\": "+ autoId + "}"; 
		    //System.out.println(POST_PARAMS);
		    
            URL obj = new URL (cloudURL+"/"+"api"+"/"+"automationrelease");
		    
		    HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
		    postConnection.setRequestMethod("POST");
		    postConnection.setRequestProperty("Content-Type", "application/json");
		    postConnection.setDoOutput(true);
		    OutputStream os = postConnection.getOutputStream();
		    os.write(POST_PARAMS.getBytes());
		    os.flush();
		    os.close();
		    int responseCode = postConnection.getResponseCode();
		    //System.out.println("POST Response Code :  " + responseCode);
		    //System.out.println("POST Response Message : " + postConnection.getResponseMessage());
		    if (responseCode ==200) { //success
		    	
		        BufferedReader input = new BufferedReader(new InputStreamReader(
		            postConnection.getInputStream()));
		        String inputLine="";
		        StringBuffer res=new StringBuffer();
		        while ((inputLine = input.readLine()) != null) {
		            res.append(inputLine);      // the device is released
		        } input.close();
		        //print result
		        System.out.println("The Device is released Successfully");
		         
		        
		    } else {
		        System.out.println("POST NOT WORKED");
		    }
			
		
		}
}
