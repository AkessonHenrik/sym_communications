package heig.labo2.async;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import heig.labo2.utils.AsyncSendRequest;
import heig.labo2.utils.CommunicationEventListener;
import heig.labo2.R;

/**
 * Activity associated to the asynchronous requests.
 * When the user presses the send button, a AsyncSendRequest is created and launched
 * Once it has communicated successfully with the server, the CommunicationEventListener
 * updated the response text with server's response
 *
 * @author Henrik Akesson
 */
public class AsyncActivity extends AppCompatActivity {
    EditText requestText;
    TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async);
        requestText = (EditText) findViewById(R.id.requestText);
        responseText = (TextView) findViewById(R.id.responseText);

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommunicationEventListener evl = new CommunicationEventListener() {
                    public boolean handleServerResponse(final String response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                responseText.setText(response);
                            }
                        });
                        return true;
                    }
                };

                AsyncSendRequest asyncSendRequest = new AsyncSendRequest(evl);
                try {
                    asyncSendRequest.execute(requestText.getText().toString(), getString(R.string.url_txt));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}