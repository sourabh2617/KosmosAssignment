package com.kosmos.articles.data.remote;

public class ApiConstants {
    public static final String BASE_URL = "https://reqres.in/api/";
    public static final long CONNECT_TIMEOUT = 30000;
    public static final long READ_TIMEOUT = 30000;
    public static final long WRITE_TIMEOUT = 30000;

    private ApiConstants(){
        // Private constructor to hide the implicit one
    }
}
