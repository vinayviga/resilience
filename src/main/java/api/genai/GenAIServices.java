package api.genai;


import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.path.json.JsonPath;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class GenAIServices {
    public static JsonPath reqresPost(Entry<String, String> body) throws ClientProtocolException, IOException {
        // Define the URL
        String url = "https://reqres.in/api/users";
        
        // Create a map for the request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", body.getKey());
        requestBody.put("job", body.getValue());
        
            // Create an instance of ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();

            // Convert the request body map to JSON string
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);

            // Create an instance of HttpClient
            CloseableHttpClient httpClient = HttpClients.createDefault();

            // Create an instance of HttpPost
            HttpPost httpPost = new HttpPost(url);

            // Set the Content-Type header
            httpPost.setHeader("Content-Type", "application/json");

            // Set the request body
            StringEntity stringEntity = new StringEntity(requestBodyJson, ContentType.APPLICATION_JSON);
            httpPost.setEntity(stringEntity);

            // Execute the request
            CloseableHttpResponse response = httpClient.execute(httpPost);

            
         // Extract and print the response body content
            HttpEntity responseEntity = response.getEntity();
            String responseBody = EntityUtils.toString(responseEntity);
            System.out.println(responseBody);
            JsonPath path = new JsonPath(responseBody);
            // Close the HttpClient
            httpClient.close();
        
        
		return path;
    }
    public static void reqresDelete(String id)
    {
    	// Define the URL
        String url = "https://reqres.in/api/users/"+id;

        try {
            // Create an instance of HttpClient
            CloseableHttpClient httpClient = HttpClients.createDefault();

            // Create an instance of HttpDelete
            HttpDelete httpDelete = new HttpDelete(url);

            // Execute the request
            CloseableHttpResponse response = httpClient.execute(httpDelete);

            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode==204)
            	System.out.println("Response Status Code: "+statusCode+", success");
            else
            	System.out.println("unable to delete");

            // Close the HttpClient
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
