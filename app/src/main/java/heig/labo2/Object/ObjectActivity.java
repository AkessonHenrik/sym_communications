package heig.labo2.object;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import heig.labo2.utils.CommunicationEventListener;
import heig.labo2.R;

/**
 * Activity associated to object sending (both xml and json)
 * The same form is used for both object types, according to the given xml dtd
 *
 * @author Henrik Akesson
 */
public class ObjectActivity extends AppCompatActivity {

    private Button JSONSendButton, XMLSendButton;

    private EditText name, firstName, middleName, phone;

    private TextView jsonResponse;

    private TextView xmlResponse;

    private RadioButton male, female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object);

        name = (EditText) findViewById(R.id.name);
        firstName = (EditText) findViewById(R.id.firstname);
        middleName = (EditText) findViewById(R.id.middlename);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);

        phone = (EditText) findViewById(R.id.phone);

        JSONSendButton = (Button) findViewById(R.id.jsonButton);
        XMLSendButton = (Button) findViewById(R.id.xmlButton);
        jsonResponse = (TextView) findViewById(R.id.jsonResponse);
        xmlResponse = (TextView) findViewById(R.id.xmlResponse);

        // If clicked, a json post request is sent to the server
        JSONSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // We didn't use gson given the simple object that is sent
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name", name.getText().toString());
                    jsonObject.put("firstname", firstName.getText().toString());
                    jsonObject.put("middlename", middleName.getText().toString());
                    jsonObject.put("gender", getGenderChoice());
                    jsonObject.put("phone", phone.getText());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CommunicationEventListener cel = new CommunicationEventListener() {
                    @Override
                    public boolean handleServerResponse(final String response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                jsonResponse.setText(response);
                            }
                        });
                        return true;
                    }
                };
                JSONObjectSender jsonObjectSender = new JSONObjectSender(jsonObject, getString(R.string.url_json), cel);
                new Thread(jsonObjectSender).start();
            }
        });

        // If clicked, a xml post request is sent to the server
        XMLSendButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        StringBuilder sb = new StringBuilder();

                        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                        sb.append("<!DOCTYPE directory SYSTEM \"http://sym.dutoit.email/directory.dtd\">");
                        sb.append("<directory>");
                        sb.append("<person>");
                        sb.append("<name>" + name.getText() + "</name>");
                        sb.append("<firstname>" + firstName.getText() + "</firstname>");
                        sb.append("<middlename>" + middleName.getText() + "</middlename>");
                        sb.append("<gender>" + getGenderChoice() + "</gender>");
                        sb.append("<phone type=" + "\"home\"" + ">" + phone.getText() + "</phone>");
                        sb.append("</person>");
                        sb.append("</directory>");

                        CommunicationEventListener cel = new CommunicationEventListener() {
                            @Override
                            public boolean handleServerResponse(final String response) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        xmlResponse.setText(response);
                                    }
                                });
                                return true;
                            }
                        };
                        XMLObjectSender xmlObjectSender = new XMLObjectSender(sb.toString(), getString(R.string.url_xml), cel);
                        new Thread(xmlObjectSender).start();
                    }
                }

        );

    }

    /**
     * Checks which radio button is checked, and returns the corresponding string
     *
     * @return Selected gender
     */
    public String getGenderChoice() {
        String genderChoice;
        if (male.isChecked()) {
            genderChoice = "male";
        } else if (female.isChecked()) {
            genderChoice = "female";
        } else {
            genderChoice = "other";
        }
        return genderChoice;
    }
}
