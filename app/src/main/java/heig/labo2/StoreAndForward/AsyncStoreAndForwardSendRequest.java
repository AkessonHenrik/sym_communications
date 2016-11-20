package heig.labo2.StoreAndForward;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import heig.labo2.utils.CommunicationEventListener;


class AsyncStoreAndForwardSendRequest extends AsyncTask<String, Void, String> {
    private static AsyncStoreAndForwardSendRequest asfsr;
    private CommunicationEventListener cel;
    private List<String> requests = new LinkedList<>();
    private String url;

    private AsyncStoreAndForwardSendRequest() {

    }

    static AsyncStoreAndForwardSendRequest getInstanceOfAsyncStoreAndForwardSendRequest() {
        if (asfsr == null) {
            asfsr = new AsyncStoreAndForwardSendRequest();
        }
        return asfsr;
    }

    void setCommunicationEventListener(CommunicationEventListener cel) {
        this.cel = cel;
    }

    void addRequest(String request) {
        requests.add(request);
    }

    void setURL(String url) {
        this.url = url;
    }

    @Override
    protected String doInBackground(String... params) {
        boolean ok = false;
        while (!ok) {
            for (String request : requests) {
                try {

                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();

                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.setRequestProperty("Content-Type", "text/plain");
                    httpURLConnection.setRequestProperty("Accept", "text/plain");
                    httpURLConnection.setRequestMethod("POST");

                    httpURLConnection.connect();
                    ok = true;

                    OutputStream os = httpURLConnection.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                    osw.write(request);
                    osw.flush();
                    osw.close();

                    int HttpResult = httpURLConnection.getResponseCode();

                    if (HttpResult == HttpURLConnection.HTTP_OK) {

                        BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));

                        String line;
                        String response = "";
                        while ((line = br.readLine()) != null) {
                            response += line + "\n";
                        }
                        br.close();

                        System.out.println(response);
                        requests.remove(request);
                        cel.handleServerResponse(response.split("\n")[0]);
                    } else {
                        cel.handleServerResponse("Error");
                    }

                    if (!ok) {
                        try {
                            Thread.sleep(60000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (SocketTimeoutException socketTimeoutException) {
                    ok = false;
                } catch (Exception e) {

                }
            }
        }
        return "Worked";
    }
}
