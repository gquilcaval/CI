package com.dms.tareosoft.util;

import android.util.Patterns;

public class TextUtil {

    public static String capitalize(String text){
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    public static boolean validarURL(String urlWebService){
        if( Patterns.WEB_URL.matcher( urlWebService ).matches() ){
            return true;
        }
        return false;
    }

    public static int convertToInt(String value) {
        if(value.isEmpty()) return 0;
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException e) {
            return 0;
        } catch(NullPointerException e) {
            return 0;
        }
    }
    //TODO VALIDAR DOUBLE
    public static int convertToDouble(String value) {
        if(value.isEmpty()) return 0;
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException e) {
            return 0;
        } catch(NullPointerException e) {
            return 0;
        }
    }

    public static String toString(int value) {
        if(value > 0){
            return ""+value;
        }else{
            return "";
        }
    }

    public static Long convertIntToLong(int maximoMargen) {
        return (long) ((maximoMargen * 60) * 1000);
    }
}
