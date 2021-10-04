package proj;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * This utility class provides an abstraction layer for sending multipart HTTP POST requests to a web server.
 *
 * @author www.codejava.net
 *
 */
public class MultipartUtility {
	private final String boundary;
	private static final String LINE_FEED = "\r\n";
	private HttpURLConnection httpConn;
	private String charset;
	private OutputStream outputStream;
	private PrintWriter writer;
	public OutputStream getOutputStream()
	{
		return outputStream;
	}
	/**
	 * This constructor initializes a new HTTP POST request with content type is set to multipart/form-data
	 *
	 * @param requestURL 
	 * @param charset
	 * @throws IOException
	 */
	public MultipartUtility(URL requestURL, String charset) throws IOException {
		this.charset = charset;
		// creates a unique boundary based on time stamp
		boundary = "===" + System.currentTimeMillis() + "===";
		httpConn = (HttpURLConnection) requestURL.openConnection();
		httpConn.setConnectTimeout(0);
		httpConn.setReadTimeout(0);
		
		httpConn.setUseCaches(false);
		httpConn.setDoOutput(true); // indicates POST method
		httpConn.setDoInput(true);
		httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		httpConn.setChunkedStreamingMode(-1);
		outputStream = httpConn.getOutputStream();
		// outputStream = new FileOutputStream("D:/javaMultipart.txt");
		writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
	}
	/**
	 * Adds a form field to the request
	 *
	 * @param name
	 *            field name
	 * @param value
	 *            field value
	 */
	public void addFormField(String name, String value) {
		writer.append("--" + boundary).append(LINE_FEED);
		writer.append("Content-Disposition: form-data; name=\"" + name + "\"").append(LINE_FEED);
		writer.append("Content-Type: text/plain; charset=" + charset).append(LINE_FEED);
		writer.append(LINE_FEED);
		writer.append(value).append(LINE_FEED);
		writer.flush();
	}
	/**
	 * Adds a upload file section to the request
	 *
	 * @param fieldName
	 *            name attribute in <input type="file" name="..." />
	 * @param uploadFile
	 *            a File to be uploaded
	 * @throws IOException
	 */
	public void addFilePart(String fieldName, File uploadFile, Boolean showProgress) throws IOException {
		String fileName = uploadFile.getName();
		System.out.println("The file You want to upload is= "+fileName);
		writer.append("--" + boundary).append(LINE_FEED);
		writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"").append(LINE_FEED);
		writer.append("Content-Type: " + "text/xml").append(LINE_FEED);
		writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
		writer.append(LINE_FEED);
		writer.flush();
		FileInputStream inputStream = new FileInputStream(uploadFile);
		byte[] buffer = new byte[1024];
		int bytesRead = -1;
		long totalBytesRead = 0;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
			totalBytesRead += bytesRead;
			if (showProgress)
				showProgressBar(totalBytesRead, uploadFile.length());
			outputStream.flush();
			writer.flush();
		}
		outputStream.flush();
		inputStream.close();
		// writer.append(LINE_FEED);
		writer.flush();
	}
	/**
	 * Adds a header field to the request.
	 *
	 * @param name
	 *            - name of the header field
	 * @param value
	 *            - value of the header field
	 */
	public void addHeaderField(String name, String value) {
		writer.append(name + ": " + value).append(LINE_FEED);
		writer.flush();
	}
	/**
	 * Completes the request and receives response from the server.
	 *
	 * @return a list of Strings as response in case the server returned status OK, otherwise an exception is thrown.
	 * @throws IOException
	 */
	public String finish() throws IOException {
		StringBuilder response = new StringBuilder();
		writer.append(LINE_FEED).flush();
		writer.append("--" + boundary + "--").append(LINE_FEED);
		writer.flush();
		writer.close();
		// checks server's status code first
		int status = httpConn.getResponseCode();
		//System.out.println(status);
		if (status == HttpURLConnection.HTTP_OK) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();
			httpConn.disconnect();
		} else {
			throw new IOException("Server returned non-OK status: " + status);
		}
		return response.toString();
	}
	private void showProgressBar(long current, long total) {
		long progressOutOf65 = current * 65 / total;
		String bar = "";
		for (int i = 0; i < progressOutOf65; i += 1) {
			bar += "=";
		}
		for (long i = progressOutOf65; i < 65; i += 1) {
			bar += " ";
		}
		int percent = (int) (current * 100 / total);
		//System.out.println("File uploaded successfully");
		//System.out.print("|" + bar + "|(" + percent + "%) \r");
		if (percent < 0)
			System.err.println(current + "  " + total);
	}
}