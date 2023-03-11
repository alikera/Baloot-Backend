package org.Baloot;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class DataGetter {

    private String endPoint = "http://5.253.25.110:5000/api";
    private String getUsersAddr = "users";
    private String getCommoditiesAddr = "commodities";
    private String getProvidersAddr = "providers";
    private String getCommentsAddr = "comments";

    public void getDataFromServer() throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        getRequest(httpClient, getUsersAddr);
        getRequest(httpClient, getCommoditiesAddr);
        getRequest(httpClient, getProvidersAddr);
        getRequest(httpClient, getCommentsAddr);
    }

    public void getRequest(HttpClient httpClient, String address) throws IOException {
        HttpGet httpGet = new HttpGet(endPoint + address);
        HttpResponse response = httpClient.execute(httpGet);

        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity());

        System.out.println(statusCode);
        System.out.println(responseBody);
    }
}
