package org.enovatelab.localhackdaykano;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public ListView listView;
    public School[] schools;
    public MyAdapter myAdapter;
    public JSONObject jObject;
    public JSONArray jArray;
    public School school;
    public String url;
    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url = "https://mlh-hackaton.herokuapp.com/api/schools";
//        url = "https://api.github.com/search/users?q=language:java+location:lagos";
        new GetSchools().execute(url);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.input:
                Intent i = new Intent(this, SendingActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    class GetSchools extends AsyncTask<String, JSONObject, String>{

        @Override
        protected String doInBackground(String... strings) {

            GetRequest getRequest = new GetRequest();
            String response = null;

            try{
                response = getRequest.run(strings[0]);
            }catch (IOException e){
                e.printStackTrace();
            }

            try {
               //  JSONObject jsonObject = new JSONObject(response);
                // jArray = jsonObject.getJSONArray("school");
                 jArray = new JSONArray(response);
                schools = new School[jArray.length()];

                for (int i = 0; i < jArray.length(); i++){
                    jObject = jArray.getJSONObject(i);
                    School school = new School();

                    school.setSchoolName(jObject.getString("SchoolName"));
                    school.setSchoolLocation(jObject.getString("SchoolLocation"));
                    school.setSchoolType(jObject.getString("SchoolType"));
                    school.setSchoolContact(jObject.getString("SchoolContact"));

                    schools[i] = school;
                }

            }catch (JSONException j){
                j.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            listView = findViewById(R.id.listView);

            myAdapter = new MyAdapter() {
                @Override
                public int getCount() {
                    return schools.length;
                }

                @Override
                public Object getItem(int position) {
                    return schools[position];
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }
            };

            listView.setAdapter(myAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:234" + school.getSchoolContact()));
                    if (ActivityCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);
                    }else {
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private abstract class MyAdapter extends BaseAdapter{
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.view_model, parent, false);
            }

            school = (School) getItem(position);

            ((TextView) convertView.findViewById(R.id.schoolName)).setText(school.getSchoolName());
            ((TextView) convertView.findViewById(R.id.schoolLocation)).setText(school.getSchoolLocation());
            ((TextView) convertView.findViewById(R.id.schoolType)).setText(school.getSchoolType());
            ((TextView) convertView.findViewById(R.id.schoolContact)).setText(school.getSchoolContact());

            return convertView;
        }
    }
}
