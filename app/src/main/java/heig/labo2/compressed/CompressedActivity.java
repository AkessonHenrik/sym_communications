package heig.labo2.compressed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import heig.labo2.R;
import heig.labo2.utils.CommunicationEventListener;


/**
 * Activity associated with sending Compressed JSON objects to the server
 *
 * @author Henrik Akesson
 */
public class CompressedActivity extends AppCompatActivity {
    private EditText name, firstname;
    private TextView compressedResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compressed);
        this.name = (EditText) findViewById(R.id.compressedName);
        this.firstname = (EditText) findViewById(R.id.compressedFirstName);
        Button compressedButton = (Button) findViewById(R.id.compressedButton);
        this.compressedResponse = (TextView) findViewById(R.id.compressedResponse);
        compressedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name", name.getText().toString());
                    jsonObject.put("firstname", firstname.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CommunicationEventListener cel = new CommunicationEventListener() {
                    @Override
                    public boolean handleServerResponse(final String response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                compressedResponse.setText(response);
                            }
                        });
                        return true;
                    }
                };
                JSONCompressedObjectSender jsonCompressedObjectSender = new JSONCompressedObjectSender(jsonObject, getString(R.string.url_json), cel);
                new Thread(jsonCompressedObjectSender).start();
            }
        });
    }
}
