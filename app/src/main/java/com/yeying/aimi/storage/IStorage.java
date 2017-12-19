package com.yeying.aimi.storage;

import android.content.SharedPreferences;


public interface IStorage {
    /**
     * Object serialize the properties of the object into DataOutputStream.
     *
     * @param out the output data stream.
     */
     void ser(SharedPreferences sp);

    /**
     * Object de-serialize the DataInputStream into the object.
     *
     * @param in the input data stream.
     */
     void unSer(SharedPreferences sp);

    /**
     * @param sp
     */
     void del(SharedPreferences sp);

    /**
     * Object identifier
     *
     * @return object identifier
     */
     String getIdentifer();


}
