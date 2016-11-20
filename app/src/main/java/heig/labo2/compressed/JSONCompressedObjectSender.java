package heig.labo2.compressed;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import heig.labo2.utils.CommunicationEventListener;

/**
 * Sends a compressed POST request of application/json type
 *
 * @author Henrik Akesson
 */
public class JSONCompressedObjectSender implements Runnable {
    private String url;
    private JSONObject jsonObject;
    private CommunicationEventListener cel;

    public JSONCompressedObjectSender(JSONObject jsonObject, String url, CommunicationEventListener cel) {
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
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("X-Content-Encoding", "deflate");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();

            Deflater deflater = new Deflater(Deflater.DEFLATED, true);
            DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(httpURLConnection.getOutputStream(), deflater, Deflater.BEST_COMPRESSION);
            deflaterOutputStream.write(jsonObject.toString().getBytes("UTF-8"));
            deflaterOutputStream.finish();

            String response = "";

            int HttpResult = httpURLConnection.getResponseCode();

            if (HttpResult == HttpURLConnection.HTTP_OK) {

                InputStream inputStream = httpURLConnection.getInputStream();
                InflaterInputStream inflaterInputStream = new InflaterInputStream(inputStream, new Inflater(true));
                InputStreamReader inputStreamReader = new InputStreamReader(inflaterInputStream);
                BufferedReader br = new BufferedReader(inputStreamReader);

                String line;

                while ((line = br.readLine()) != null) {
                    response += (line + "\n");
                }
                br.close();

                System.out.println(response);
                JSONObject responseObject = new JSONObject(response);
                cel.handleServerResponse(responseObject.get("name") + ", " + responseObject.get("firstname"));
            } else {
                cel.handleServerResponse("Error");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
