package com.dms.tareosoft.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dms.tareosoft.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executors;

public class UtilsMethods {
    static String TAG = UtilsMethods.class.getSimpleName();

    private static Toast toast;
    private static MediaPlayer mediaPlayer;
    /**
     * Esconde el teclado virtual
     * @param context es el contexto correcto
     */
    public static void hideKeyboard(AppCompatActivity context){
        InputMethodManager inputMethodManager = (InputMethodManager)  context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
    }

    public static void hideKeyboardFromFragment(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    /**
     * Copia la base de datos (ROOM) hacia la carpeta "Downloads" (DB_ALMACEN)
     */
    public static void copyAppDbToDownloadFolder(Context context) {
        String[] databasefiles = new String []{"DB_ALMACEN","DB_ALMACEN-shm","DB_ALMACEN-wal"};
        Executors.newSingleThreadExecutor().execute(() -> {
            for (String currentfile : databasefiles) {
                try {
                    File backupDB = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), currentfile); // for example "my_data_backup.db"
                    File currentDB = context.getApplicationContext().getDatabasePath(currentfile); //databaseName=your current application database name, for example "my_data.db"
                    if (currentDB.exists()) {
                        FileInputStream fis = new FileInputStream(currentDB);
                        FileOutputStream fos = new FileOutputStream(backupDB);
                        fos.getChannel().transferFrom(fis.getChannel(), 0, fis.getChannel().size());
                        fis.close();
                        fos.close();
                        Log.d("Database successfully", "copied " + currentfile + " to download folder");
                        new Handler(Looper.getMainLooper()).post(() -> showToast(context,"Base de datos copiada"));
                    } else Log.d("Copying Database", " fail, database not found");
                } catch (IOException e) {
                    Log.d("Copying Database", "fail copying" + currentfile + "reason:", e);
                    new Handler(Looper.getMainLooper()).post(() -> showToast(context,"Base de datos vacía"));
                }
            }
        });
    }

    /**
     * Muestra un toast único a través de la aplicación
     * @param context es el contexto correcto
     * @param message es el menaje a mostrar
     */
    public static void showToast(Context context, String message) {
        if (toast == null){
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(message);
            toast.show();
        }
    }


    /**
     * Verifica conexión a internet
     * @param context es el contexto correcto
     * @return true - hay conexión
     *         false - no hay conexión
     */
    public static boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        if (activeNetworkInfo != null) { // connected to the internet
            return true;
        } else {
            return false;
        }
    }

    /**
     * Devuelve el diálogo de error de conexión en el que la acción es configurable
     * @param context es el contexto correcto
     * @param iButton es la acción a realizar
     */
    public static CustomDialog getInternetError(Context context, CustomDialog.IButton iButton){
        return new CustomDialog.Builder(context)
                .setMessage(context.getString(R.string.movil_sin_conexion))
                .setIcon(R.drawable.ic_warning)
                .setCancelable(false)
                .setPositiveButtonLabel(context.getString(R.string.reintentar))
                .setPositiveButtonlistener(iButton)
                .setTheme(R.style.AppTheme_Dialog_Error)
                .build();
    }

    public static CustomDialog showDetailedErrorDialog(Context context, ArrayList<String> list){

        SimpleRecyclerViewAdapter adapter = new SimpleRecyclerViewAdapter(context, list);

        return new CustomDialog.Builder(context)
                .setType(CustomDialog.DIALOG_TYPE.LIST)
                .setAdapter(adapter)
                .setTitle(context.getString(R.string.title_validation))
                .setMessage(context.getString(R.string.message_validation))
                .setIcon(R.drawable.ic_warning)
                .setNegativeButtonLabel("Cerrar")
                .setTheme(R.style.AppTheme_Dialog_Error)
                .build();
    }

    public static CustomDialog showConfirmDialog(Context context, String titulo, String mensaje, CustomDialog.IButton positiveButton){
        return new CustomDialog.Builder(context)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setIcon(R.drawable.ic_warning)
                .setPositiveButtonLabel(context.getString(R.string.label_yes))
                .setNegativeButtonLabel(context.getString(R.string.label_no))
                .setPositiveButtonlistener(positiveButton)
                .build();
    }

    public static CustomDialog showConfirmDialog(Context context, String titulo, String mensaje,
                                                 CustomDialog.IButton positiveButton,
                                                 CustomDialog.IButton negativeButton){
        return new CustomDialog.Builder(context)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setIcon(R.drawable.ic_warning)
                .setPositiveButtonLabel(context.getString(R.string.label_yes))
                .setNegativeButtonLabel(context.getString(R.string.label_no))
                .setPositiveButtonlistener(positiveButton)
                .setNegativeButtonlistener(negativeButton)
                .build();
    }

    // GET MAC
    public static String getMac() {
        String resultMac = "0123456789";
        try {
            ArrayList<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : all) {
                if (!networkInterface.getName().equalsIgnoreCase("wlan0"))
                    continue;
                byte[] bytes = networkInterface.getHardwareAddress();
                if (bytes == null)
                    return resultMac;
                StringBuilder stringBuilder = new StringBuilder();
                for (byte bite : bytes) {
                    stringBuilder.append(String.format("%02x", bite));
                }
                if (stringBuilder.length() > 0)
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                resultMac = stringBuilder.toString().replace(":", "");
            }
            return resultMac;
        } catch (Exception e) {
            return resultMac;
        }
    }
    //IMEI GET
    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)) {
            String imei = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (!TextUtils.isEmpty(imei)) {
                return imei;
            } else {
                return getMac();
            }
        } else if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)) {
            String imei = tm.getImei();
            if (!TextUtils.isEmpty(imei)) {
                return imei;
            } else {
                return getMac();
            }
        } else {
            String imei = tm.getDeviceId();
            if (!TextUtils.isEmpty(imei)) {
                return imei;
            } else {
                return getMac();
            }
        }
    }

    public static void playSound(Context context, int sound) {
        if (context == null)
            return;
        try {
            mediaPlayer = MediaPlayer.create(context, sound);
            mediaPlayer.setVolume(0.09f, 0.09f);
            Log.d(TAG, "playSound isPlaying: " + mediaPlayer.isPlaying());
            if (mediaPlayer.isPlaying()) {
                stopMediaPlayer();
            }
            mediaPlayer.setOnPreparedListener(mp -> {
                Log.d(TAG, "playSound setOnPreparedListener");
                mp.start();
            });
            mediaPlayer.setOnCompletionListener(mp -> {
                Log.d(TAG, "playSound setOnCompletionListener");
                stopMediaPlayer();
            });
        } catch (Exception ex) {
            Log.d(TAG, "playSound " + ex.getMessage());
            stopMediaPlayer();
            mediaPlayer = null;
        }
    }

    private static void stopMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
    }
}