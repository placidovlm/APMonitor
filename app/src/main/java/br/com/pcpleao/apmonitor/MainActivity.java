package br.com.pcpleao.apmonitor;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String mStatus = "";
    private AccessPointAdapter mAccessPointAdapter;

    private ProgressBar mProgress;
    private int mProgressStatus = 0;


    AccessPoint ap1 = new AccessPoint("192.168.0.1", "Arris Router", null, "");
    AccessPoint ap2 = new AccessPoint("192.168.0.50", "Intelbras DVR", null, "");
    AccessPoint ap3 = new AccessPoint("192.168.0.51", "PCP01", null, "");
    AccessPoint ap5 = new AccessPoint("192.168.0.52", "iPhone-de-Patricia", null, "");
    AccessPoint ap4 = new AccessPoint("192.168.0.53", "MACBOOKPRO", null, "");
    AccessPoint ap6 = new AccessPoint("192.168.0.55", "", null, "");
    AccessPoint ap7 = new AccessPoint("192.168.0.56", "", null, "");
    AccessPoint ap8 = new AccessPoint("192.168.0.56", "", null, "");
    AccessPoint ap9 = new AccessPoint("192.168.0.58", "", null, "");
    AccessPoint ap10 = new AccessPoint("192.168.0.59", "", null, "");
    AccessPoint ap11 = new AccessPoint("192.168.0.60", "Sony PS4", null, "");
    AccessPoint ap12 = new AccessPoint("192.168.0.61", "", null, "");
    AccessPoint ap13 = new AccessPoint("192.168.0.62", "", null, "");
    AccessPoint ap14 = new AccessPoint("192.168.0.63", "", null, "");
    //ArrayList<AccessPoint> accessPointArray = new ArrayList<AccessPoint>(Arrays.asList(ap1, ap2, ap3, ap4, ap5, ap6, ap7, ap8, ap9, ap10, ap11, ap12, ap13, ap14));
    ArrayList<AccessPoint> accessPointArray = new ArrayList<AccessPoint>();


    private class PingAP extends AsyncTask<Integer, Void, AccessPoint> {
        protected AccessPoint doInBackground(Integer... position) {
            AccessPoint item = mAccessPointAdapter.getItem(position[0]);
            Boolean reachable=false;
            InetAddress address;
            try {
                address = InetAddress.getByName(item.ipAdress);
                reachable = address.isReachable(1000);
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (reachable) {
                item.status = "alive";
            } else {
                item.status = "dead";
            }
            return item;
        }


        protected void onPostExecute(AccessPoint result) {
            //Toast toast = Toast.makeText(getApplicationContext(), result.ipAdress+" -> "+result.status,Toast.LENGTH_SHORT);
            //toast.show();
            mProgressStatus++;
            mProgress.setProgress(mProgressStatus);
            mAccessPointAdapter.notifyDataSetChanged();

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                for (int i=0 ; i< mAccessPointAdapter.getCount() ; i++){
                    new PingAP().execute(i);
                    //apItem.status = mStatus;
                    //accessPointArray.set(i,apItem);
                }
                //mAccessPointAdapter.notifyDataSetChanged();

            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.site_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sites_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        mAccessPointAdapter = new AccessPointAdapter(this,accessPointArray);
        LoadAPTask loadAP = new LoadAPTask();
        loadAP.execute();
        ListView listView = (ListView) findViewById(R.id.listViewAP);
        listView.setAdapter(mAccessPointAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class AccessPointAdapter extends ArrayAdapter<AccessPoint> {
        private Context context;


        public AccessPointAdapter(Context context, ArrayList<AccessPoint> aps) {
            super(context, 0, aps);
            this.context = context;
        }


        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            AccessPoint ap = getItem(position);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_item_ap, parent, false);

            TextView ipAddressTextView = (TextView) rowView.findViewById(R.id.ip_address_textview);
            TextView hostNameTextView  = (TextView) rowView.findViewById(R.id.host_name_textview);
            TextView locationTextView  = (TextView) rowView.findViewById(R.id.location_textview);

            ImageView statusImageView = (ImageView) rowView.findViewById(R.id.status_imageview);

            ipAddressTextView.setText(ap.ipAdress);
            hostNameTextView.setText(ap.hostName);
            locationTextView.setText((ap.location));


            if (ap.status.startsWith("alive")) {
                statusImageView.setImageResource(R.drawable.green_light);
            } else {
                if (ap.status.startsWith("dead")) {
                    statusImageView.setImageResource(R.drawable.red_light);
                } else {
                    statusImageView.setImageResource(R.drawable.yellow_light);
                }
            }

            return rowView;
        }

    }


    public class LoadAPTask extends AsyncTask<Void,Void,AccessPoint[]> {

        private final String LOG_TAG = LoadAPTask.class.getSimpleName();

        private AccessPoint[] getAPDataFromJson(String APJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String AP_TOTAL = "total_rows";
            final String AP_ROW = "rows";
            final String AP_VALUE = "value";
            final String AP_IP_ADDRESS = "ip_address";
            final String AP_HOST_NAME = "host_name";
            final String AP_LOCATION ="location";


            JSONObject apJson = new JSONObject(APJsonStr);
            String mTotal = apJson.getString(AP_TOTAL);
            mProgress.setMax(Integer.parseInt(mTotal)) ;
            JSONArray apArray = apJson.getJSONArray(AP_ROW);

            AccessPoint[] resultAP = new AccessPoint[apArray.length()];
            for(int i = 0; i < apArray.length(); i++) {

                // Get the JSON object representing row
                JSONObject rowObject = apArray.getJSONObject(i);

                // Get the JSON object representing value
                JSONObject valueObject = rowObject.getJSONObject(AP_VALUE);


                AccessPoint mAP = new AccessPoint(valueObject.getString(AP_IP_ADDRESS),
                        valueObject.getString(AP_HOST_NAME),valueObject.getString(AP_LOCATION),"");
                resultAP[i] = mAP ;
            }

            return resultAP;

        }


        @Override
        protected AccessPoint[] doInBackground(Void... params){

            //if (params.length == 0) {
               // return null;
            //}

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String apJsonStr = null;


            try {
                // Construct the URL for the themoviedb query

                final String MOVIES_BASE_URL = "https://couchdb-430337.smileupps.com/" + "ap_icpmao" + "/_design/list/_view/list";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon().build();
                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to themoviedb, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                apJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getAPDataFromJson(apJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(AccessPoint[] result) {
            if (result != null) {
                mAccessPointAdapter.clear();
                for(AccessPoint movieItem : result) {
                    mAccessPointAdapter.add(movieItem);
                }
            }
        }


    }


}
