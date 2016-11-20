package heig.labo2.Object;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import heig.labo2.utils.CommunicationEventListener;

/**
 * Runnable class that sends JSON objects to the server
 * The object to send is given at creation
 * @author Henrik Akesson
 * @author Fabien Salathe
 */
public class JSONObjectSender implements Runnable {
    private String url;
    private JSONObject jsonObject;
    private CommunicationEventListener cel;
    public JSONObjectSender(JSONObject jsonObject, String url, CommunicationEventListener cel) {
        this.jsonObject = jsonObject;
        this.url = url;
        this.cel = cel;
    }

    @Override
    public void run() {
        HttpURLConnection httpURLConnection;
        try {

            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();

            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty( "Content-Type", "application/json" );
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();
            OutputStream os = httpURLConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(jsonObject.toString());
            osw.flush();
            osw.close();

            StringBuilder sb = new StringBuilder();

            int HttpResult = httpURLConnection.getResponseCode();

            if (HttpResult == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));

                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                System.out.println(sb.toString());
                JSONObject responseObject = new JSONObject(sb.toString());
                cel.handleServerResponse(responseObject.get("name") + ", " + responseObject.get("firstname"));
            } else {
                cel.handleServerResponse("Error");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
