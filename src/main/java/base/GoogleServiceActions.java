package base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;

import io.restassured.path.json.JsonPath;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.BufferedReader;
import  java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GoogleServiceActions {
	private static final String APPLICATION_NAME = "Desktop client 1";
	private static final String spreadsheetId = "";
	private static final String CREDENTIALS_FILE_PATH = "credential.json";//you can create your own credential file and paste it in the current folder
	private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);


	private static Credential authorize() throws IOException, GeneralSecurityException
	{
		GsonFactory instance = GsonFactory.getDefaultInstance();

		InputStream in = GoogleServiceActions.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(instance, new InputStreamReader(in));
		List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow
				.Builder(GoogleNetHttpTransport.newTrustedTransport(), instance, clientSecrets, scopes)
				.setDataStoreFactory(new FileDataStoreFactory(new File("tokens")))
				.setAccessType("offline")
				.build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize(APPLICATION_NAME);

		return credential;

	}

	public static Sheets getSheetService() throws IOException, GeneralSecurityException
	{
		Credential credential = authorize();   

		return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), credential)
				.setApplicationName(APPLICATION_NAME)
				.build();
	}

	public static void sheetAppend(List<List<String>> users) throws IOException, GeneralSecurityException {
		Sheets sheetService = getSheetService();
		String range ="employee_list!A2:D11";
		boolean empty = false;
		
		//Reading the data to check if the targeted rows are empty or not
		ValueRange response = sheetService.spreadsheets().values()
				.get(spreadsheetId, range)
				.execute();
		
		List<List<Object>> values = response.getValues();
		if(values == null||values.isEmpty())
		{
			System.out.println("No data found, we can go ahead with writing data");
			empty = true;
			
		}else
		{
			for(List row : values)
			{
				System.out.println(row.get(0)+" "+row.get(1)+" "+row.get(2));
			}
		}
		
		//writing data to excel sheet
		
		for(List<String> user: users)
		{
			System.out.println(user);
			ValueRange appendBody = new ValueRange()
					.setValues(Arrays.asList(Arrays.asList(user.get(0),user.get(1),user.get(2),user.get(3))));
			
			try {
				sheetService.spreadsheets().values().append(spreadsheetId, "employee_list", appendBody)
				.setValueInputOption("USER_ENTERED")
				.setInsertDataOption("INSERT_ROWS")
				.setIncludeValuesInResponse(true)
				.execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	public static void deleteCells(int size) throws IOException, GeneralSecurityException
	{
		Sheets sheetService = getSheetService();
		String range ="employee_list!A2:D"+size;
		
		
		ClearValuesRequest clearValuesRequest = new ClearValuesRequest();
		sheetService.spreadsheets().values().clear(spreadsheetId,range,clearValuesRequest).execute();
		
	}
	
	public static Map<String, String> aiDataGenerator() throws IOException {
		String apiKey = "AIzaSyCgGYcg2wSy-5pLLcNpfrtqZw_twdxE7HQ";// your API key
		String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-latest:generateContent";

		// Create the request body object
		RequestBody requestBody = new RequestBody();
		requestBody.getContents().add(new Content("user", new Part(
				"generate a list of Harry potter characters with their names and single worded corporate job roles in simple json")));
		requestBody.setGenerationConfig(new GenerationConfig(1, 0, 0.95, 8192, new String[] {}));
		requestBody.getSafetySettings().add(new SafetySetting("HARM_CATEGORY_HARASSMENT", "BLOCK_MEDIUM_AND_ABOVE"));
		requestBody.getSafetySettings().add(new SafetySetting("HARM_CATEGORY_HATE_SPEECH", "BLOCK_MEDIUM_AND_ABOVE"));
		requestBody.getSafetySettings()
				.add(new SafetySetting("HARM_CATEGORY_SEXUALLY_EXPLICIT", "BLOCK_MEDIUM_AND_ABOVE"));
		requestBody.getSafetySettings()
				.add(new SafetySetting("HARM_CATEGORY_DANGEROUS_CONTENT", "BLOCK_MEDIUM_AND_ABOVE"));

		// Convert the request body object to JSON
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		String requestBodyJson = mapper.writeValueAsString(requestBody);

		// Send the request
		URL requestUrl = new URL(url + "?key=" + apiKey);
		HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setDoOutput(true);

		try (OutputStream os = connection.getOutputStream()) {
			byte[] input = requestBodyJson.getBytes("utf-8");
			os.write(input, 0, input.length);
		}

		int responseCode = connection.getResponseCode();
		System.out.println("Response code: " + responseCode);

		// Read the response
		StringBuilder response = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
		}

		JsonPath jsonPath = new JsonPath(response.toString());

		String json = jsonPath.getString("candidates.content.parts.text");

		// Find the index of the first opening curly brace {
		int startIndex = json.indexOf("{");
		// Find the index of the last closing curly brace }
		int endIndex = json.lastIndexOf("}");

		if (startIndex != -1 && endIndex != -1) {
			// Extract the substring between the first opening and last closing curly braces
			json = json.substring(startIndex, endIndex + 1);
		}

		// Parse JSON string into a list of maps
		Map<String, String> dataList = mapper.readValue(json, Map.class);

		// Convert list of maps into JSON using Jackson
		String jsonArray = mapper.writeValueAsString(dataList);

		// Print the resulting map
		System.out.println(dataList);

		connection.disconnect();
		return dataList;
	}
       
}

//POJO classes representing the request body
class RequestBody {
	@JsonProperty("contents")
	private java.util.List<Content> contents = new java.util.ArrayList<>();

	@JsonProperty("generationConfig")
	private GenerationConfig generationConfig; // This is the field

	@JsonProperty("safetySettings")
	private java.util.List<SafetySetting> safetySettings = new java.util.ArrayList<>();

	// Getter and setter for contents
	public java.util.List<Content> getContents() {
		return contents;
	}

	public void setContents(java.util.List<Content> contents) {
		this.contents = contents;
	}

	// Getter and setter for generationConfig
	public GenerationConfig getGenerationConfig() {
		return generationConfig;
	}

	public void setGenerationConfig(GenerationConfig generationConfig) {
		this.generationConfig = generationConfig;
	}

	// Getter and setter for safetySettings
	public java.util.List<SafetySetting> getSafetySettings() {
		return safetySettings;
	}

	public void setSafetySettings(java.util.List<SafetySetting> safetySettings) {
		this.safetySettings = safetySettings;
	}
}

class Content {
	@JsonProperty("role")
	private String role;

	@JsonProperty("parts")
	private java.util.List<Part> parts = new java.util.ArrayList<>();

	// Default constructor
	public Content() {
	}

	// Constructor with parameters
	public Content(String role, Part part) {
		this.role = role;
		this.parts.add(part);
	}

	// Getter and setter for role
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	// Getter and setter for parts
	public java.util.List<Part> getParts() {
		return parts;
	}

	public void setParts(java.util.List<Part> parts) {
		this.parts = parts;
	}
}

class Part {
	@JsonProperty("text")
	private String text;

	// Default constructor
	public Part() {
	}

	// Constructor with parameter
	public Part(String text) {
		this.text = text;
	}

	// Getter and setter for text
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}

class GenerationConfig {
	@JsonProperty("temperature")
	private int temperature;

	@JsonProperty("topK")
	private int topK;

	@JsonProperty("topP")
	private double topP;

	@JsonProperty("maxOutputTokens")
	private int maxOutputTokens;

	@JsonProperty("stopSequences")
	private String[] stopSequences;

	// Default constructor
	public GenerationConfig() {
	}

	// Constructor with parameters
	public GenerationConfig(int temperature, int topK, double topP, int maxOutputTokens, String[] stopSequences) {
		this.temperature = temperature;
		this.topK = topK;
		this.topP = topP;
		this.maxOutputTokens = maxOutputTokens;
		this.stopSequences = stopSequences;
	}

	// Getter and setter methods for all fields
	// Omitted for brevity
}

class SafetySetting {
	@JsonProperty("category")
	private String category;

	@JsonProperty("threshold")
	private String threshold;

	// Default constructor
	public SafetySetting() {
	}

	// Constructor with parameters
	public SafetySetting(String category, String threshold) {
		this.category = category;
		this.threshold = threshold;
	}

}


