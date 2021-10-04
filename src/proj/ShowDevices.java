package proj;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;
import java.lang.Thread;
import org.json.JSONArray;
import org.json.JSONObject;


public class ShowDevices 
{
	 static String token = "";
	 static int id = 0;
	 static String no;
	 ShowDevices(String token,int id)
	 {
		 ShowDevices.token = token;
		 ShowDevices.id=id;
	 }
	 private static boolean validateNumber(String nos) 
     {
		 String regex="\\d{1}";
		 Pattern pat = Pattern.compile(regex); 
	        if (no == null) 
	            return false; 
	        return pat.matcher(no).matches(); 
 		
     }
		public static void POSTRequest(String token,String cloudURL,String file) throws IOException{
			try
			{
			System.out.println("Now showing the Devices:");
		    final String POST_PARAMS = "{\"token\":\"" + token +"\",\"duration\":10,\"platform\": \"ios\",\"available_now\": \"true\"}"; 
		    //System.out.println(POST_PARAMS);
		    Scanner sc = new Scanner(System.in);
            URL obj = new URL (cloudURL+"/"+"api"+"/"+"devices");
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
		        String inputLine; 
		        StringBuffer res=new StringBuffer();
		        while ((inputLine = input.readLine()) != null) 
		        {
		            res.append(inputLine);
		            //System.out.println(inputLine);  //we are getting all the ios device list in json format
		        } 
		        input.close();
		        JSONObject jsonObject = new JSONObject(res.toString());
		        int length = jsonObject.getJSONObject("result").getJSONArray("models").length();
		        
		        //we are getting the name and id number of the devices so the user can select
		       ArrayList<Integer> deviceId = new ArrayList<Integer>();
		        for(int i=0;i<length;i++)
		        {
		        	System.out.println((i+1) +"  "+jsonObject.getJSONObject("result").getJSONArray("models").getJSONObject(i).get("full_name"));
		        	deviceId.add(Integer.parseInt(jsonObject.getJSONObject("result").getJSONArray("models").getJSONObject(i).get("id").toString()));
		        }
		        System.out.println("");
		        
		        do
		        {
		        System.out.println("How many devices you want to book");
		        no=sc.nextLine();
		        }
		        while(!validateNumber(no));
		        
		        
		        int fin=Integer.parseInt(no);
		        
		        
		        System.out.println("Enter the phone id you want to book");
		        int[] on=new int[fin];
		        for(int i=0;i<fin;i++) 
		        {
		        	on[i]=sc.nextInt();
		        	on[i] = on[i]-1;
		        }
		        System.out.println("Enter the booking duration");
		    	int book=sc.nextInt();
		    	List<Integer> aa = new ArrayList<Integer>();
		        for(int i=0;i<fin;i++) 
		        {
		        	aa.add(i,deviceId.get(on[i]));
		        }
		        	DeviceBook.POSTRequest(token, aa, cloudURL,book,fin,file);
		    }
		    else 
		    {
		        System.out.println("POST NOT WORKED");  
		    }
			}
			catch(Exception e)
			{
	            e.printStackTrace();
			}
		}
}


