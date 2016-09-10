package br.com.pcpleao.meudeputadofederal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Placido on 28/10/2015.
 */


public class Deputado implements Parcelable {
    public String nomeParlamentar;
    public String partido;
    public String uf;
    public String telefone;
    public String email;
    public String filtro1;
    public String filtro2;
    public String idDeputado;
    public String tipoCongressista;


    public Deputado(String mNomeParlamentar, String mPartido, String mUF, String mTelefone, String mEmail,
                    String mFiltro1, String mFiltro2,  String mIddeputado,String mTipoCongressista)
    {
        this.nomeParlamentar = mNomeParlamentar;
        this.partido = mPartido;
        this.uf = mUF;
        this.telefone = mTelefone;
        this.email = mEmail;
        this.filtro1 = mFiltro1;
        this.filtro2 = mFiltro2;
        this.idDeputado = mIddeputado;
        this.tipoCongressista = mTipoCongressista;
    }

    public String getNomeParlamentar() {
        return nomeParlamentar;
    }

    public String getPartido() {
        return partido;
    }

    public String getUf() {
        return uf;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getFiltro1() {
        return filtro1;
    }

    public String getFiltro2() {
        return filtro2;
    }

    public String getIdDeputado() {
        return idDeputado;
    }

    public String getTipoCongressista() {
        return tipoCongressista;
    }

    private Deputado(Parcel in){
        nomeParlamentar = in.readString();
        partido = in.readString();
        uf = in.readString();
        telefone = in.readString();
        email = in.readString();
        filtro1 = in.readString();
        filtro2 = in.readString();
        idDeputado = in.readString();
        tipoCongressista = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(nomeParlamentar);
        parcel.writeString(partido);
        parcel.writeString(uf);
        parcel.writeString(telefone);
        parcel.writeString(email);
        parcel.writeString(filtro1);
        parcel.writeString(filtro2);
        parcel.writeString(idDeputado);
        parcel.writeString(tipoCongressista);
    }

    public static final Parcelable.Creator<Deputado> CREATOR
            = new Parcelable.Creator<Deputado>() {
        public Deputado createFromParcel(Parcel in) {
            return new Deputado(in);
        }

        public Deputado[] newArray(int size) {
            return new Deputado[size];
        }
    };
}