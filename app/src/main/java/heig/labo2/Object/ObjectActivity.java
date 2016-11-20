package heig.labo2.Object;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
 * @author Fabien Salathe
 */
public class ObjectActivity extends AppCompatActivity {

    private Button JSONSendButton, XMLSendButton;

    private EditText name, firstName, middleName, phone;

    private TextView jsonResponse;

    private TextView xmlResponse;

    // Gender selection
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


        JSONSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    // Name
                    jsonObject.put("name", name.getText().toString());
                    // First name
                    jsonObject.put("firstname", firstName.getText().toString());
                    // Middle name
                    jsonObject.put("middlename", middleName.getText().toString());
                    // Gender
                    jsonObject.put("gender", getGenderChoice());
                    // Phone
                    jsonObject.put("phone", phone.getText());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CommunicationEventListener cel = new CommunicationEventListener() {
                    @Override
                    public boolean handleServerResponse(String response) {
                        jsonResponse.setText(response);
                        return true;
                    }
                };
                JSONObjectSender jsonObjectSender = new JSONObjectSender(jsonObject, getString(R.string.url_json), cel);
                new Thread(jsonObjectSender).start();
            }
        });

        XMLSendButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        StringBuilder sb = new StringBuilder();
                        // Constant Header
                        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                        sb.append("<!DOCTYPE directory SYSTEM \"http://sym.dutoit.email/directory.dtd\">");
                        sb.append("<directory>");

                        sb.append("<person>");
                        // Name
                        sb.append("<name>" + name.getText() + "</name>");
                        // First name
                        sb.append("<firstname>" + firstName.getText() + "</firstname>");
                        // Middle name
                        sb.append("<middlename>" + middleName.getText() + "</middlename>");
                        // Gender
                        sb.append("<gender>" + getGenderChoice() + "</gender>");
                        //phone
                        sb.append("<phone type=" + "\"home\"" + ">" + phone.getText() + "</phone>");

                        sb.append("</person>");
                        sb.append("</directory>");

                        CommunicationEventListener cel = new CommunicationEventListener() {
                            @Override
                            public boolean handleServerResponse(String response) {
                                xmlResponse.setText(response);
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
