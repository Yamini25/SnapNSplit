package com.example.sony.snapnsplit;

/**
 * Created by sony on 10-03-2016.
 */
public class Config {

    //Address of our scripts of the CRUD
    public static final String URL_UPDATE_EMP = "http://192.168.94.1/Android/CRUD/updateEmp.php";

    //Keys that will be used to send the request to php scripts
    public static final String KEY_EMP_ID = "id";
    public static final String KEY_EMP_NAME = "name";


    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";

    //employee id to pass with intent
    public static final String EMP_ID = "emp_id";

}
