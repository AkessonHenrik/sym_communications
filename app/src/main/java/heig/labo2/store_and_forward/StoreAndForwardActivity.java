package heig.labo2.store_and_forward;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import heig.labo2.utils.CommunicationEventListener;
import heig.labo2.R;

/**
 * @author: Henrik Akesson
 */
public class StoreAndForwardActivity extends AppCompatActivity {
    Button sendRequest = null;
    TextView responseText = null;
    EditText requestText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_store_and_forward);
        responseText = (TextView) findViewById(R.id.storeAndForwardResponseText);

        requestText = (EditText) findViewById(R.id.storeAndForwardEditText);

        this.sendRequest = (Button) findViewById(R.id.storeAndForwardButton);

        CommunicationEventListener cel = new CommunicationEventListener() {
            @Override
            public boolean handleServerResponse(final String response) {
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        responseText.setText(response);
                    }
                });
                return false;
            }
        };

        final AsyncStoreAndForwardSendRequest asyncStoreAndForwardSendRequest = AsyncStoreAndForwardSendRequest.getInstance();

        asyncStoreAndForwardSendRequest.setCommunicationEventListener(cel);

        asyncStoreAndForwardSendRequest.setURL(getString(R.string.url_txt));

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestText.getText().toString().isEmpty()) {
                    Toast.makeText(StoreAndForwardActivity.this, "Please enter something at least...", Toast.LENGTH_LONG).show();
                } else {
                    asyncStoreAndForwardSendRequest.addRequest(requestText.getText().toString(), getString(R.string.url_txt));
                }
            }
        });
    }
}