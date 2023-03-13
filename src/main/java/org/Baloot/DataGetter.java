package org.Baloot;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.Baloot.Database.Database;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class DataGetter {

    private String baseEndpoint = "http://5.253.25.110:5000/api/";
    private String getUsersEndpoint = "users";
    private String getCommoditiesEndpoint = "commodities";
    private String getProvidersEndpoint = "providers";
    private String getCommentsEndpoint = "comments";

    public void getDataFromServer(Database db) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        HttpClient httpClient = HttpClientBuilder.create().build();

        String usersData = getRequest(httpClient, getUsersEndpoint);
        User[] users = mapper.readValue(usersData, User[].class);

        String commoditiesData = getRequest(httpClient, getCommoditiesEndpoint);
        Commodity[] commodities = mapper.readValue(commoditiesData, Commodity[].class);

        String providersData = getRequest(httpClient, getProvidersEndpoint);
        Provider[] providers = mapper.readValue(providersData, Provider[].class);

        String commentsData = getRequest(httpClient, getCommentsEndpoint);
        Comment[] comments = mapper.readValue(commentsData, Comment[].class);

        db.insertInitialData(users, providers, commodities, comments);
    }

    public String getRequest(HttpClient httpClient, String address) throws IOException {
        HttpGet httpGet = new HttpGet(baseEndpoint + address);
        HttpResponse response = httpClient.execute(httpGet);

        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity());

        System.out.println(statusCode);
        System.out.println(responseBody);

        return responseBody;
    }
}
