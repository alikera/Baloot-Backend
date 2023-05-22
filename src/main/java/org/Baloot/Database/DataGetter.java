package org.Baloot.Database;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.Baloot.Entities.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class DataGetter {
    private String baseEndpoint = "http://5.253.25.110:5000/api/";
    private String getUsersEndpoint = "users";
    private String getCommoditiesEndpoint = "v2/commodities";
    private String getProvidersEndpoint = "v2/providers";
    private String getCommentsEndpoint = "comments";
    private String getDiscountCodesEndpoint = "discount";

    public DataGetter() {
        try {
            getDataFromServer();
        }
        catch (IOException e) {}
    }

    private void getDataFromServer() throws IOException {
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

        String discountCodesData = getRequest(httpClient, getDiscountCodesEndpoint);
        DiscountCode[] discountCodes = mapper.readValue(discountCodesData, DiscountCode[].class);

        try {
            Database.insertInitialData(users, providers, commodities, comments, discountCodes);
        }catch (Exception e){

        }
    }

    private String getRequest(HttpClient httpClient, String address) throws IOException {
        HttpGet httpGet = new HttpGet(baseEndpoint + address);
        HttpResponse response = httpClient.execute(httpGet);

        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity());

        return responseBody;
    }
}
