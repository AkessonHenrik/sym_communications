package heig.labo2.utils;

import android.os.AsyncTask;

import java.io.*;
import java.net.*;

/**
 * Class used to send asynchronous requests to the rest api
 * Extends AsyncTask to simplify usage
 * requires a CommunicationEventListener that it will use once the communication is over
 * @author Henrik Akesson
 * @author Fabien Salathe
 */

public class AsyncSendRequest extends AsyncTask<String, Void, String> {

    private final CommunicationEventListener eventListener;

    public AsyncSendRequest(CommunicationEventListener eventListener) {
        this.eventListener = eventListener;
    }

    String sendRequest(String request, String url) throws Exception {
        String response = "";
        while(response.isEmpty()) {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();

                httpURLConnection.setRequestProperty("Content-Type", "text/plain");

                httpURLConnection.setDoOutput(true);
                httpURLConnection.setChunkedStreamingMode(0);

                OutputStream out = new BufferedOutputStream(httpURLConnection.getOutputStream());
                out.write(request.getBytes());
                out.flush();

                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    response += (line + '\n');
                }
            } catch (Exception e) {
                Thread.sleep(3000);
            }
        }
        return response;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String response =  sendRequest(params[0], params[1]);
            return response.split("\n")[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error";
    }

    @Override
    protected void onPostExecute(String result) {
        this.eventListener.handleServerResponse(result);
    }
}