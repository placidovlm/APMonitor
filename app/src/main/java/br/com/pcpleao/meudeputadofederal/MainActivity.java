package br.com.pcpleao.meudeputadofederal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DeputadoAdapter mDeputadoAdapter;
    public String mCongressista = "senador";
    private String mUF = "ESTADOS";
    private String mPartido = "PARTIDOS";
    private Spinner spinnerUF;
    private Spinner spinnerPartido;
    private Boolean ufFisrtRun = true;
    private Boolean partidoFisrtRun = true;

    String mAssunto = "Aprove o Pedido de Impeachment da Presidente Dilma";
    String mTexto = "Excelentíssimo Senhor Senador" +
            ",\n"+
            "\n" +
            "Aprove o Pedido de Impeachment da Presidente Dilma\n" +
            "\n" +
            "Estamos ao lado da população, indignados com tanta bandalheira!\n" +
            "E, assim como a maioria dos brasileiros, defendemos que a presidente seja afastada o mais rápido possível, através do seu impeachment!";



    //Deputado ap1 = new Deputado("192.168.0.1", "Arris Router", null, "");
    //ArrayList<Deputado> deputadoArray = new ArrayList<Deputado>(Arrays.asList(ap1, ap2, ap3, ap4, ap5, ap6, ap7, ap8, ap9, ap10, ap11, ap12, ap13, ap14));

    ArrayList<Deputado> deputadoArray = new ArrayList<Deputado>();


    public void onItemSelected(AdapterView<?> parent, View view, int pos,
                               long id) {

        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.uf_spinner)
        {

            if (!ufFisrtRun) {
                mUF = parent.getItemAtPosition(pos).toString();
                LoadDeputadoTask loadDeputado = new LoadDeputadoTask();
                loadDeputado.execute();
            }
            ufFisrtRun = false;
        }
        else if(spinner.getId() == R.id.partido_spinner)
        {

            if (!partidoFisrtRun) {
                mPartido = parent.getItemAtPosition(pos).toString();
                LoadDeputadoTask loadDeputado = new LoadDeputadoTask();
                loadDeputado.execute();
            }
            partidoFisrtRun = false;
        }


    }



    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }



    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButtonCamara:
                if (checked) {

                    mCongressista ="deputado";
                    LoadDeputadoTask loadDeputado = new LoadDeputadoTask();
                    loadDeputado.execute();
                }
                break;
            case R.id.radioButtonSenado:
                if (checked){
                    mCongressista  = "senador";
                    LoadDeputadoTask loadDeputado = new LoadDeputadoTask();
                    loadDeputado.execute();
                }
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Toast toast2 = Toast.makeText(getApplicationContext(), "Escolha SENADO ou CÂMARA", Toast.LENGTH_SHORT);
        //toast2.show();

        final Button button = (Button) findViewById(R.id.button_enviar);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Integer qtEmail = 0;
                String mMail = "";
                for(Deputado mIntegrante : deputadoArray) {
                    mMail = mMail + mIntegrante.getEmail()+";";
                    qtEmail++;
                    //Toast toast2 = Toast.makeText(getApplicationContext(),mIntegrante.getEmail() , Toast.LENGTH_SHORT);
                    //toast2.show();
                }
                if (qtEmail>100){
                    String mAviso = "Mais de 100 remetentes! Pode ser reconhecido como SPAM. Filtre por Partido e UF.";
                    Toast toast2 = Toast.makeText(getApplicationContext(),mAviso , Toast.LENGTH_LONG);
                    toast2.show();
                    return;


                }

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",mMail, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, mAssunto);
                emailIntent.putExtra(Intent.EXTRA_TEXT, mTexto);
                startActivity(Intent.createChooser(emailIntent, "Enviar e-mail :"));

            }
        });


        spinnerUF = (Spinner) findViewById(R.id.uf_spinner);
        List<String> listaUF = new ArrayList<String>();

        //Adicionando estados no ArrayList
        listaUF.add("ESTADOS");
        listaUF.add("AC");listaUF.add("AL");listaUF.add("AM");listaUF.add("AP");listaUF.add("BA");listaUF.add("CE");listaUF.add("DF");
        listaUF.add("ES");listaUF.add("GO");listaUF.add("MA");listaUF.add("MG");listaUF.add("MS");listaUF.add("MT");listaUF.add("PA");
        listaUF.add("PB");listaUF.add("PE");listaUF.add("PI");listaUF.add("PR");listaUF.add("RJ");listaUF.add("RN");listaUF.add("RO");
        listaUF.add("RR");listaUF.add("RS");listaUF.add("SC");listaUF.add("SE");listaUF.add("SP");listaUF.add("TO");

        ArrayAdapter<String> ufAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaUF);
        //ufAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUF.setAdapter(ufAdapter);
        // Spinner item selection Listener
        //addListenerOnSpinnerItemSelection();
        spinnerUF.setOnItemSelectedListener(this);



        spinnerPartido = (Spinner) findViewById(R.id.partido_spinner);
        List<String> listaPartido = new ArrayList<String>();

        //Adicionando Partidos no ArrayList
        listaPartido.add("PARTIDOS");
        listaPartido.add("DEM");listaPartido.add("PCdoB");listaPartido.add("PDT");listaPartido.add("PEN");
        listaPartido.add("PHS");listaPartido.add("PMB");listaPartido.add("PMDB");listaPartido.add("PMN");
        listaPartido.add("PP");listaPartido.add("PPS");listaPartido.add("PR");listaPartido.add("PRB");
        listaPartido.add("PROS");listaPartido.add("PSB");listaPartido.add("PSC");listaPartido.add("PSD");
        listaPartido.add("PSDB");listaPartido.add("PSL");listaPartido.add("PSOL");listaPartido.add("PT");
        listaPartido.add("PTB");listaPartido.add("PTC");listaPartido.add("PTdoB");listaPartido.add("PTN");
        listaPartido.add("PV");listaPartido.add("REDE");listaPartido.add("S.PART.");listaPartido.add("SD");


        ArrayAdapter<String> partidoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaPartido);
        //partidoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPartido.setAdapter(partidoAdapter);
        // Spinner item selection Listener
        //addListenerOnSpinnerItemSelection();
        spinnerPartido.setOnItemSelectedListener(this);




        mDeputadoAdapter = new DeputadoAdapter(this, deputadoArray);

        LoadDeputadoTask loadDep = new LoadDeputadoTask();
        loadDep.execute();

        ListView listView = (ListView) findViewById(R.id.listViewAP);
        listView.setAdapter(mDeputadoAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Deputado movieTitle = mDeputadoAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class).putExtra(Intent.EXTRA_TEXT, movieTitle);

                startActivity(intent);
            }
        });
    }


    // Add spinner data
    public void addListenerOnSpinnerItemSelection() {


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

    public class DeputadoAdapter extends ArrayAdapter<Deputado> {
        private Context context;
        private final String LOG_TAG = LoadDeputadoTask.class.getSimpleName();

        public DeputadoAdapter(Context context, ArrayList<Deputado> aps) {
            super(context, 0, aps);
            this.context = context;
        }


        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            Deputado dep = getItem(position);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_item_ap, parent, false);

            TextView nomeParlamentarTextView = (TextView) rowView.findViewById(R.id.nome_parlamentar_textview);
            TextView deputadoTextView  = (TextView) rowView.findViewById(R.id.partido_textview);
            TextView ufTextView  = (TextView) rowView.findViewById(R.id.uf_textview);
            ImageView fotoImageView = (ImageView) rowView.findViewById(R.id.foto_imageview);
            ImageView votoImageView = (ImageView) rowView.findViewById(R.id.voto_imageview);


            nomeParlamentarTextView.setText(dep.nomeParlamentar);
            deputadoTextView.setText(dep.partido);
            ufTextView.setText((dep.uf));

            if (dep.filtro2.startsWith("F")) {
                votoImageView.setImageResource(R.drawable.thumbsup);
            } else {
                if (dep.filtro2.startsWith("C")) {
                    votoImageView.setImageResource(R.drawable.thumbsdown);
                } else {
                    votoImageView.setImageResource(R.drawable.questionmark);
                }
            }

            final String IMAGES_EXTENSION = ".jpg";
            String imageFullPath;

            if (mCongressista == "deputado"){
                 imageFullPath = "http://www.camara.gov.br/internet/deputado/bandep/"  + dep.idDeputado + IMAGES_EXTENSION;
            } else {
                 imageFullPath = "http://www.senado.gov.br/senadores/img/fotos-oficiais/senador"  + dep.idDeputado + IMAGES_EXTENSION;
            }


            Log.v(LOG_TAG, imageFullPath);
            Picasso.with(context).load(imageFullPath).into(fotoImageView);

            return rowView;
        }

    }


    public class LoadDeputadoTask extends AsyncTask<Void,Void,Deputado[]> {

        private final String LOG_TAG = LoadDeputadoTask.class.getSimpleName();

        private Deputado[] getDepDataFromJson(String DepJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String DEP_TOTAL = "total_rows";
            final String DEP_ROW = "rows";
            final String DEP_VALUE = "value";
            final String NOME_PARLAMENTAR = "NomeParlamentar";
            final String PARTIDO = "Partido";
            final String UF ="UF";
            final String TELEFONE ="Telefone";
            final String EMAIL ="CorreioEletronico";
            final String FILTRO1 ="Filtro1";
            final String FILTRO2 ="Filtro2";
            final String ID_DEPUTADO ="IdDeputado";


            JSONObject apJson = new JSONObject(DepJsonStr);
            String mTotal = apJson.getString(DEP_TOTAL);
            //mProgress.setMax(Integer.parseInt(mTotal)) ;
            JSONArray apArray = apJson.getJSONArray(DEP_ROW);

            Deputado[] resultAP = new Deputado[apArray.length()];
            for(int i = 0; i < apArray.length(); i++) {

                // Get the JSON object representing row
                JSONObject rowObject = apArray.getJSONObject(i);

                // Get the JSON object representing value
                JSONObject valueObject = rowObject.getJSONObject(DEP_VALUE);


                Deputado mDep = new Deputado(valueObject.getString(NOME_PARLAMENTAR), valueObject.getString(PARTIDO),
                        valueObject.getString(UF),valueObject.getString(TELEFONE),valueObject.getString(EMAIL),
                        valueObject.getString(FILTRO1),valueObject.getString(FILTRO2),
                        String.valueOf(valueObject.getInt(ID_DEPUTADO)),mCongressista);
                resultAP[i] = mDep ;
            }

            return resultAP;

        }


        @Override
        protected Deputado[] doInBackground(Void... params){

            //if (params.length == 0) {
               // return null;
            //}

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String depJsonStr = null;


            try {
                // Construct the URL for the themoviedb query

                String mURL;


                if (mUF == "ESTADOS" && mPartido == "PARTIDOS") {
                        mURL = "/_design/list/_view/list";
                } else {

                        if (mUF != "ESTADOS" && mPartido != "PARTIDOS") {
                            mURL = "/_design/list/_view/list_UF_Partido?key=[\"" + mUF + "\",\"" + mPartido + "\"]";
                        } else {

                            if (mUF != "ESTADOS") {
                                mURL = "/_design/list/_view/list_UF?key=\"" + mUF + "\"";
                            } else {
                                mURL = "/_design/list/_view/list_Partido?key=\"" + mPartido + "\"";
                            }
                        }
                }





                final String DEPUTADO_BASE_URL = "https://couchdb-430337.smileupps.com/" + mCongressista + mURL;

                Uri builtUri = Uri.parse(DEPUTADO_BASE_URL).buildUpon().build();
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
                depJsonStr = buffer.toString();

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
                return getDepDataFromJson(depJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            //Toast toast = Toast.makeText(getApplicationContext(), "Carregando lista de deputados federais", Toast.LENGTH_SHORT);
            //toast.show();


        }



        @Override
        protected void onPostExecute(Deputado[] result) {


            if (result != null) {
                mDeputadoAdapter.clear();
                for (Deputado movieItem : result) {
                    mDeputadoAdapter.add(movieItem);
                }
            } else
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Erro ao carregar a lista de congressistas" , Toast.LENGTH_LONG);
                    toast.show();
                }
        }


    }


}
