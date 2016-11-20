package heig.labo2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import heig.labo2.async.AsyncActivity;
import heig.labo2.compressed.CompressedActivity;
import heig.labo2.object.ObjectActivity;
import heig.labo2.store_and_forward.StoreAndForwardActivity;


/**
 * Main Activity, used to launch the 4 specified activities
 *
 * @author Henrik Akesson
 */
public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);

        findViewById(R.id.asyncButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, AsyncActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.storeAndForwardButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, StoreAndForwardActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.compressedButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, CompressedActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.objectButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, ObjectActivity.class);
                startActivity(intent);
            }
        });

    }
}
