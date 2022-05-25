package com.example.twitterapi;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

/*
A fazer:
 - implementar botao de like para o tweet buscado
   https://developer.twitter.com/en/docs/twitter-api/tweets/likes/api-reference/post-users-id-likes
 */
public class TwitterAPIAcess {
    // This method is typically for developers that need read-only access to public information.
    // https://developer.twitter.com/en/docs/authentication/oauth-2-0
    private static final String BEARER_TOKEN = "AAAAAAAAAAAAAAAAAAAAAJiJVgEAAAAA2vj8l7tv%2F3fgGZwM8tauu%2BhIl2c%3DFwiwpZr2pdyHnUu6GS7CKQ9Q3OQRWxb8KInMFTKKcZzWzGY1KI";
    private static final String TAG = "MainActivity";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String authHeaderValue = "Bearer " + BEARER_TOKEN;
        connection.setRequestProperty("Authorization", authHeaderValue);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": com " + urlSpec);
            }
            int bytesLidos = 0;
            byte[] buffer = new byte[1024];
            while ((bytesLidos = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesLidos);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public DadosTweet buscaTweet(String tweet_id) {
        DadosTweet item = new DadosTweet();
        try {
            String url = Uri.parse("https://api.twitter.com/2/tweets/"+tweet_id)
                    .buildUpon()
                    .build().toString();
            Log.i(TAG, "buscaTweet: Tweet recebido (url): " + url);
            String jsonString = getUrlString(url);
            Log.i(TAG, "buscaTweet: JSON recebido: " + jsonString);
            JSONObject corpoJson = new JSONObject(jsonString);
            parseItem(item, corpoJson);

            Log.i(TAG, "buscaTweet: Tweet recebido: " + item.getText());

        } catch (IOException ioe) {
            Log.e(TAG, "Falha da busca de itens", ioe);
        } catch (JSONException e) {
            Log.e(TAG, "Falha no análise (parse) do JSON", e);
        }

        return item;
    }

    private void parseItem(DadosTweet item, JSONObject corpoJson)
            throws IOException, JSONException {
        JSONObject dataJsonObject = corpoJson.getJSONObject("data");
        item.setId(dataJsonObject.getString("id"));
        item.setText(dataJsonObject.getString("text"));
    }
}
