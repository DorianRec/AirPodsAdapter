package com.example.airpodsadapter;

import android.os.Parcel;
import android.os.Parcelable;

public class BluetoothDeviceParcelable implements Parcelable {
    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<BluetoothDeviceParcelable> CREATOR = new Parcelable.Creator<BluetoothDeviceParcelable>() {
        public BluetoothDeviceParcelable createFromParcel(Parcel in) {
            return new BluetoothDeviceParcelable(in);
        }

        public BluetoothDeviceParcelable[] newArray(int size) {
            return new BluetoothDeviceParcelable[size];
        }
    };

    /* everything below here is for implementing Parcelable */
    private int mData;

    // example constructor that takes a Parcel and gives you an object populated with it's values
    public BluetoothDeviceParcelable(Parcel in) {
        mData = in.readInt();
    }

    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }
}
