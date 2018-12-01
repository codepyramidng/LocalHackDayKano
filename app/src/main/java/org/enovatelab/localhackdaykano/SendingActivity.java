package org.enovatelab.localhackdaykano;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SendingActivity extends AppCompatActivity {

    EditText name, location, type, contact;
    Button submiit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending);

        name = findViewById(R.id.schoolNameEdt);
        location = findViewById(R.id.schoolLocationEdt);
        type = findViewById(R.id.schoolTypeEdt);
        contact = findViewById(R.id.schoolContactEdt);

        submiit = findViewById(R.id.submitBtn);

        submiit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().equals("") || location.getText().equals("") || type.getText().equals("")
                        || contact.getText().equals("")){
                    Toast.makeText(SendingActivity.this, "Everything is needed", Toast.LENGTH_LONG).show();
                }else {
                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("SchoolName", name.getText().toString());
                        jsonObject.put("SchoolLocation", location.getText().toString());
                        jsonObject.put("SchoolType", type.getText().toString());
                        jsonObject.put("SchoolContact", contact.getText().toString());

//                        new MainActivity.GetSchools().execute("https://mlh-hackaton.herokuapp.com/api/schools", jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
