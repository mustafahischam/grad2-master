package com.sasa.project.grad2.Firebase_DataObjects;

import android.os.Parcel;
import android.os.Parcelable;



public class SDL_DataObjects implements Parcelable {

    public String address,text,type,time,User_name;

//// parceble works with bundle ///////String
//primitives

    // Constructor for loading from a Parcel:
    public SDL_DataObjects(Parcel in) {/// read
        // lazm nafs elorder bta3 bundle
        this.text = in.readString();/// source or destination
        this.time = in.readString();
        this.User_name = in.readString();
        this.address = in.readString();
        this.type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SDL_DataObjects> CREATOR = new Creator<SDL_DataObjects>() {
        @Override
        public SDL_DataObjects createFromParcel(Parcel in) {
            return new SDL_DataObjects(in);
        }

        @Override
        public SDL_DataObjects[] newArray(int size) {
            return new SDL_DataObjects[size];
        }
    };
/// write
    public void writeToParcel(Parcel out, int flags) {

        out.writeString(this.text);
        out.writeString(this.time);
        out.writeString(this.User_name);
        out.writeString(this.address);
        out.writeString(this.type);
    }


    public SDL_DataObjects() { }

}
