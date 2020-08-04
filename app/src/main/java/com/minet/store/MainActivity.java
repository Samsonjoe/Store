package com.minet.store;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         listView =(ListView)findViewById(R.id.listView);
         mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
         listView.setAdapter(mAdapter);
        new Connection().execute();
    }

    class Connection extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            String host ="http://10.0.2.2/store/cars.php";

            try {

                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(host));
                HttpResponse response = client.execute(request);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuffer stringBuffer = new StringBuffer("");

                String line = "";
                while ((line = reader.readLine()) != null){
                    stringBuffer.append(line);
                    break;
                }
                reader.close();
                result = stringBuffer.toString();

            }catch (Exception e){
                return new  String("There is an Exception "+e.getMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
          //  Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            //Parsing json data here
            try {
                JSONObject jsonResult = new JSONObject(result);
                int success = jsonResult.getInt("success");
                if (success == 1){
                    //Toast.makeText(getApplicationContext(), "Okay there cars in Rongai", Toast.LENGTH_SHORT).show();
                    JSONArray cars = jsonResult.getJSONArray("cars");
                    for(int i=0; i < cars.length(); i++){
                        JSONObject car = cars.getJSONObject(i);
                        int id = car.getInt("id");
                        String name = car.getString("name");
                        double price = car.getDouble("price");
                        String description = car.getString("description");
                        String line = id + "-" + name + "-" +price;
                        mAdapter.add(line);

                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "There us no car yet!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
