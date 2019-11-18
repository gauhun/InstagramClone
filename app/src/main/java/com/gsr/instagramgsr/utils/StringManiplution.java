package com.gsr.instagramgsr.utils;

public class StringManiplution {

    public static String expandUsername(String username){
        return username.replace(".", " ");
    }

    public static String condenseUsername(String username){
        return username.replace(" ", ".");
    }
}
