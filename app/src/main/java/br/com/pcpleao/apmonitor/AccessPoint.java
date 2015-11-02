package br.com.pcpleao.apmonitor;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Placido on 28/10/2015.
 */


public class AccessPoint implements Parcelable {
    public String ipAdress;
    public String hostName;
    public String location;
    public String status ;


    public AccessPoint (String mIpAdress, String mHostName, String mLocation, String mStatus)
    {
        this.ipAdress = mIpAdress;
        this.hostName = mHostName;
        this.location = mLocation;
        this.status = mStatus;
    }

    public String getIpAdress() {
        return ipAdress;
    }

    public String getHostName() {
        return hostName;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }


    private AccessPoint(Parcel in){
        ipAdress = in.readString();
        hostName = in.readString();
        location = in.readString();
        status = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(ipAdress);
        parcel.writeString(hostName);
        parcel.writeString(location);
        parcel.writeString(status);
    }

    public static final Parcelable.Creator<AccessPoint> CREATOR
            = new Parcelable.Creator<AccessPoint>() {
        public AccessPoint createFromParcel(Parcel in) {
            return new AccessPoint(in);
        }

        public AccessPoint[] newArray(int size) {
            return new AccessPoint[size];
        }
    };
}