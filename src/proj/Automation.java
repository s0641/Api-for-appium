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


public class Automation
{
	 static String token = "";
	 Automation(String token)
	 {
		 Automation.token = token;
	 }
	 
		public static void POSTRequest(String token,ArrayList<Integer> autoId,String cloudURL,int book,int no,String file) throws IOException {
			int count=0;
			try
			{
				Scanner sc=new Scanner(System.in);
				for(int i=0;i<no;i++)
				{
					
		    final String POST_PARAMS = "{\"token\":\"" + token +"\",\"automationId\": "+ autoId.get(i) + ",\"test_suite\":\""+file+"\"}"; 
		    //System.out.println(POST_PARAMS);
            URL obj = new URL (cloudURL+"/"+"api"+"/"+"initautomation");
            System.out.println("");
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
		            res.append(inputLine);
		        } input.close();
		        //print result
		        System.out.println("Automation initiated successfully");
		        count++;
		        
		    } else {
		    	count++;
		        System.out.println("POST NOT WORKED");
		        Release.POSTRequest(token,autoId,cloudURL,no);
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
				
					Service.POSTRequest(token,autoId,cloudURL,book,no);
				
			}
		
		}
}
