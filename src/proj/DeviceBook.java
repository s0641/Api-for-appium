package proj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.json.JSONObject;

public class DeviceBook
{
	 static String token = "";
	 static int aID=0;
	  DeviceBook(String token)
	 {
		 DeviceBook.token = token;
	 }
		public static void POSTRequest(String token,List<Integer> aa,String cloudURL,int book,int no,String file) throws IOException {
			 int count=0;
			  	ArrayList<Integer> autoId = new ArrayList<Integer>();
			try {
		    	
		    	Scanner sc=new Scanner(System.in);
		    	for(int i=0;i<no;i++)
		    	{
		    		final String POST_PARAMS = "{\"token\":\"" + token +"\",\"devices\": ["+ aa.get(i) +"],\"booking_duration\":\""+book+"\",\"automation_type\": \"XCUITest\"}"; 
		    		//System.out.println(POST_PARAMS);
					
		    		URL obj = new URL (cloudURL+"/"+"api"+"/"+"automationbooking");
		    		HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
		    		postConnection.setRequestMethod("POST");
		    		postConnection.setRequestProperty("Content-Type", "application/json");
		    		postConnection.setDoOutput(true);
		    		OutputStream os = postConnection.getOutputStream();
		    		os.write(POST_PARAMS.getBytes());
		    		os.flush();
		    		os.close();
		    		int responseCode = postConnection.getResponseCode();
		    
		    		if (responseCode ==200) { //success

		    			BufferedReader input = new BufferedReader(new InputStreamReader(
		    					postConnection.getInputStream()));
		    			String inputLine="";
		    			StringBuffer res=new StringBuffer();
		    			while ((inputLine = input.readLine()) != null) {
		    				res.append(inputLine);
		    			} input.close();
		    			
		    			System.out.println("Device is booked successfully for automation");
		    			JSONObject jsonObject = new JSONObject(res.toString());
		    			aID = jsonObject.getJSONObject("data").getInt("automationId");// generating automation id
		    			autoId.add(jsonObject.getJSONObject("data").getInt("automationId"));
		    			count++;
		    			System.out.println("Your automation id for the device you booked= "+aID);
		    			System.out.println("");
		    		}
		    		
		    		else 
		    		{
		    			count++;
		    			System.out.println("POST NOT WORKED");
		    			Release.POSTRequest(token,autoId,cloudURL,no);        // if some issue then device can be released
		    		}
		    	}
		    }
		    catch(Exception e)
		    {
		    	e.printStackTrace();
		    	Release.POSTRequest(token,autoId,cloudURL,no);
		    }
		if(count==no)
		{
				Automation.POSTRequest(token,autoId,cloudURL,book,no,file);
		}
		
		
	}
}