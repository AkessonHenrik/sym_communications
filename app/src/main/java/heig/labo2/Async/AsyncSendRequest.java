package heig.labo2.Async;

import android.os.AsyncTask;

import java.io.*;
import java.net.*;

import heig.labo2.utils.CommunicationEventListener;

/**
 * Class used to send asynchronous requests to the rest api
 * Extends AsyncTask to simplify usage
 * requires a CommunicationEventListener that it will use once the communication is over
 * @author Henrik Akesson
 * @author Fabien Salathe
 */

class AsyncSendRequest extends AsyncTask<String, Void, String> {

    private final CommunicationEventListener eventListener;

    AsyncSendRequest(CommunicationEventListener eventListener) {
        this.eventListener = eventListener;
    }

    String sendRequest(String request, String url) throws Exception {

        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();

        httpURLConnection.setRequestProperty("Content-Type", "text/plain");

        httpURLConnection.setDoOutput(true);
        httpURLConnection.setChunkedStreamingMode(0);

        OutputStream out = new BufferedOutputStream(httpURLConnection.getOutputStream());
        out.write(request.getBytes());
        out.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        String line;
        String response = "";
        while((line = in.readLine()) != null) {
            response += (line + '\n');
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