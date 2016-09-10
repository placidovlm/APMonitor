package br.com.pcpleao.meudeputadofederal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private String mTel;
    private String mMail;
    private String mNomeDeuptado;
    private String mCongressista;
    private String mAssunto = "Aprovem o Pedido de Impeachment da Presidente Dilma";
    private String mTexto;



    public void ligaDeputado(View v){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mTel, null));
        startActivity(intent);
    }

    public void enviaEmail(View v){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",mMail, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, mAssunto);
        emailIntent.putExtra(Intent.EXTRA_TEXT, mTexto);
        startActivity(Intent.createChooser(emailIntent, "Enviar e-mail :"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ImageButton mTelefoneButton = (ImageButton) findViewById(R.id.telefoneImageButton);
        mTelefoneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mTel, null));
                startActivity(intent);
            }
        });

        final ImageButton mEmailButton = (ImageButton) findViewById(R.id.emailImageButton);
        mEmailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",mMail, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, mAssunto);
                emailIntent.putExtra(Intent.EXTRA_TEXT, mTexto);
                startActivity(Intent.createChooser(emailIntent, "Enviar e-mail :"));
            }
        });

        Intent intent = this.getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {

            Deputado myDep = intent.getExtras().getParcelable(Intent.EXTRA_TEXT);

            mTel = "(061) "+ myDep.getTelefone();
            mMail = myDep.getEmail();
            mNomeDeuptado = myDep.getNomeParlamentar();
            mCongressista = myDep.getTipoCongressista();

            if (mCongressista.equals("deputado")) {
                mTel = "(061) " + myDep.getTelefone();
            }else{
                mTel = myDep.getTelefone();
                mTel = "(061) " + mTel.substring(4,mTel.length());
            }

            mTexto = "Excelentíssimo Senhor " + mCongressista.toUpperCase() +  " " + mNomeDeuptado +
                    ",\n"+
                    "\n" +
                    "Aprove o Pedido de Impeachment da Presidente Dilma\n" +
                    "\n" +
                    "Estamos ao lado da população, indignados com tanta bandalheira!\n" +
                    "E, assim como a maioria dos brasileiros, defendemos que a presidente seja afastada o mais rápido possível, através do seu impeachment!";

            ((TextView) this.findViewById(R.id.detailNomeParlamentartextView)).setText(myDep.nomeParlamentar);
            ((TextView) DetailActivity.this.findViewById(R.id.detailPartidoTextView)).setText(myDep.getPartido());
            ((TextView) DetailActivity.this.findViewById(R.id.detailUFTextView)).setText(myDep.getUf());
            ((TextView) DetailActivity.this.findViewById(R.id.telefoneTextView)).setText(mTel);
            ((TextView) DetailActivity.this.findViewById(R.id.emailTextView)).setText(myDep.getEmail());
            ((TextView) DetailActivity.this.findViewById(R.id.textViewTipoCongressista)).setText(mCongressista.toUpperCase());

            ImageView mImageView = (ImageView) this.findViewById(R.id.detailFotoImageView);


            final String IMAGES_EXTENSION = ".jpg";
            String imageFullPath;


            if (mCongressista.equals("deputado")){

                imageFullPath = "http://www.camara.gov.br/internet/deputado/bandep/"  + myDep.idDeputado + IMAGES_EXTENSION;
            } else {
                imageFullPath = "http://www.senado.gov.br/senadores/img/fotos-oficiais/senador"  + myDep.idDeputado + IMAGES_EXTENSION;
            }

            Picasso.with(DetailActivity.this).load(imageFullPath).into(mImageView);

        }


    }



}


