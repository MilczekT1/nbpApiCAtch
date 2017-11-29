package pl.konradboniecki.nbp;

import lombok.Cleanup;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class NbpApiHandler {
    
    public static Currency[] getCurrency(Request request){
        try {
            URL url = new URL("http://api.nbp.pl/api/exchangerates/rates/c/"
                                      + request.getCurrency() + "/"
                                      + request.getStartDate() + "/"
                                      + request.getEndDate() + "/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Accept","application/json");
            
            @Cleanup
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream()));
            
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null){
                stringBuilder.append(line + "\n");
            }
            String downloadedData = stringBuilder.toString();
            downloadedData = downloadedData.substring(downloadedData.indexOf("["),downloadedData.length()-2);
            
            JSONArray jsonArray = new JSONArray(downloadedData);
            Currency[] currencies = new Currency[jsonArray.length()];
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                currencies[i] = new Currency(jsonObject.getDouble("bid"),jsonObject.getDouble("ask"));
            }
            return currencies;
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return null;
    }
}
