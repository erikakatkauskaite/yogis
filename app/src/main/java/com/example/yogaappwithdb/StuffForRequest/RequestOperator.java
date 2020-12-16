package com.example.yogaappwithdb.StuffForRequest;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class RequestOperator extends Thread {
    public interface RequestOperatorListener{
        void success (Challenge publication);
        void failed (int responseCode);
    }

    private RequestOperatorListener listener;
    private int responseCode;
    public static int challenges;

    public void setListener (RequestOperatorListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void run()
    {
        super.run();
        try {
            Challenge publication = request();

            if(publication != null)
            {
                success(publication);
            }
            else
            {
                failed(responseCode);
            }
        }
        catch (IOException e)
        {
            failed(-1);
        }
        catch (JSONException e)
        {
            failed(-2);
        }
    }

    // Getting JSON string
    private Challenge request() throws IOException, JSONException
    {
        //URL
        URL object = new URL("https://my-json-server.typicode.com/erikakatkauskaite/chalenges/challenges");

        //Executor
        HttpsURLConnection connection = (HttpsURLConnection) object.openConnection();

        //Determine what method will be used (GET, POST, PUT, DELETEE)
        connection.setRequestMethod("GET");

        //Determine the content type. In this case it is a JSON variable
        connection.setRequestProperty("Content-Type", "application/json");

        //Make request and receive response
        responseCode = connection.getResponseCode();
        Log.i("Response code", String.valueOf(responseCode));
        InputStreamReader inputStreamReader;

        // If response is okay, use InputStream
        // If not, use ErrorStream
        if(responseCode == 200)
        {
            inputStreamReader = new InputStreamReader(connection.getInputStream());
        }
        else
        {
            inputStreamReader = new InputStreamReader(connection.getErrorStream());
        }
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String inputLine;
        StringBuffer responseStringBuffer = new StringBuffer();

        while ((inputLine = bufferedReader.readLine()) != null)
        {
            responseStringBuffer.append(inputLine);
        }

        bufferedReader.close();

        Log.i("TEST", responseStringBuffer.toString());

        if(responseCode == 200)
        {
            return parsingJsonObject(responseStringBuffer.toString());
        }
        else return  null;
    }

    // Actions with JSON string
    public Challenge parsingJsonObject(String response) throws JSONException
    {
        //attempts to create a json object of achieving a response
        JSONObject object = new JSONObject(response);
        // creating challenges array
        JSONArray array = object.getJSONArray("challenges");
        Challenge post = new Challenge();
        int l = array.length(); //array length
        int min = 1;
        int max = l;
        int randomIndex = (int)(Math.random() * (max - min + 1) + min);
        for(int i = 0 ; i<array.length();i++)
        {
            JSONObject e = array.getJSONObject(i);
            if(e.optInt("id") ==randomIndex)
            {
                post.setId(e.optInt("id", 0));
                post.setTitle( e.getString("title"));

            }
            post.setTotal(l-1);
        }
        Log.i("Array length", String.valueOf(l));
        return post;
    }

    private void failed(int code)
    {
        if(listener != null)
        {
            listener.failed(code);
        }
    }

    private void success(Challenge publication)
    {
        if(listener!=null)
        {
            listener.success(publication);
        }
    }
    public int allChallenges()
    {
        return challenges;
    }

}