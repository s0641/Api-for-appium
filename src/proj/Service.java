package proj;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;


public class Service
{
	 static String token = "";
	 Service(String token)
	 {
		 Service.token = token;
	 }
		public static void POSTRequest(String token,ArrayList<Integer> autoId,String cloudURL,int book,int no) throws IOException {
			int count=0;
			Scanner sc=new Scanner(System.in);
			try
			{
			for(int i=0;i<no;i++)
			{
		    final String POST_PARAMS = "{\"token\":\"" + token +"\",\"automationId\": "+ autoId.get(i) + ",\"startDeviceLogs\": \"true\",\"startPerformanceData\": \"true\",\"startSessionRecording\": \"true\"}"; 
		    //System.out.println(POST_PARAMS);
            URL obj = new URL (cloudURL+"/"+"api"+"/"+"startdeviceservices");
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
		        System.out.println("Device logs, Session recording, Performance started successfully"); 
		        // result in json format
		       count++;
		        
		        
		    } else
		    {
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
				for(int i=0;i<no;i++)
				{
					System.out.println("Do you want to release the device with automation id " + String.valueOf(autoId.get(i)) + "? (Y/N)");
			        String dec=sc.nextLine();
			        if(dec.equals("Y"))
			        {
			        	Release.POSTRequest(token,autoId,cloudURL,no);
			        }
			        else
			        {
			        	try {
							TimeUnit.MINUTES.sleep(book);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
			        }
				}
			}
		
		}
}
