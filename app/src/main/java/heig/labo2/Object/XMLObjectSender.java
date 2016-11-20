package heig.labo2.object;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import heig.labo2.utils.CommunicationEventListener;

/**
 * Runnable class that sends XML objects to the server
 * The object to send is given at creation
 *
 * @author Henrik Akesson
 */
public class XMLObjectSender implements Runnable {
    private String url;
    private String content;
    private CommunicationEventListener cel;

    public XMLObjectSender(String content, String url, CommunicationEventListener cel) {
        this.content = content;
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
            httpURLConnection.setRequestProperty("Content-Type", "application/xml");
            httpURLConnection.setRequestProperty("Accept", "application/xml");
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();
            OutputStream os = httpURLConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(content);
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
                cel.handleServerResponse("Worked");
            } else {
                cel.handleServerResponse("Error");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
