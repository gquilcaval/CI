package com.dms.tareosoft.util;

import android.util.Log;

import com.dms.tareosoft.data.PreferenceManager;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class DateUtil {
    private static String TAG = DateUtil.class.getSimpleName();
//+ TimeUnit.MINUTES.toMillis(45);

    /**
     * Obtiene la hora actual del Equipo Móvil
     */
    public static String obtenerFechaHoraEquipo(String format) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static Long longFechaHoraEquipo() {
        return System.currentTimeMillis();
    }

    public static String dateToStringFormat(Date date, String format) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(date);
    }

    public static long stringToLongFormat(String dateTime, String format) {
        try {
            return new SimpleDateFormat(format, Locale.getDefault()).parse(dateTime).getTime();
        } catch (Exception ex) {
            return 0L;
        }
    }

    public static String longToStringFormat(Long milliseconds, String format) {
        Date date = new Date(milliseconds);
        Format simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public static String changeStringDateTimeFormat(String fechaHora, String formatOld, String newFormat) {
        try {
            Date date = new SimpleDateFormat(formatOld, Locale.getDefault()).parse(fechaHora);
            SimpleDateFormat nuevoFormato = new SimpleDateFormat(newFormat, Locale.getDefault());
            return nuevoFormato.format(date);
        } catch (ParseException ex) {
            return "";
        }
    }

    /*
     * Validaciones con fechas
     * */
    public static boolean esMenorQue(String formatFecha1, String fecha1, String formatFecha2, String fecha2) {
        if (stringToLongFormat(fecha1, formatFecha1) < stringToLongFormat(fecha2, formatFecha2)) {
            return true;
        }
        return false;
    }

    public static boolean esMayorQue(String formatFecha1, String fecha1, String formatFecha2, String fecha2) {
        if (stringToLongFormat(fecha1, formatFecha1) > stringToLongFormat(fecha2, formatFecha2)) {
            return true;
        }
        return false;
    }

    public static int validarRangoMinutos(String formatFechaInicio, String fechaInicio,
                                          String formatFechaFin, String fechaFin,
                                          int minutosMinimos, int minutosMaximos) {
        long minimo = TimeUnit.MINUTES.toMillis(minutosMinimos);
        long maximo = TimeUnit.MINUTES.toMillis(minutosMaximos);
        long duracion = stringToLongFormat(fechaFin, formatFechaFin) - stringToLongFormat(fechaInicio, formatFechaInicio);

        if (duracion < minimo) {
            return Constants.ERROR_MENOR_MINIMO;
        }
        if (duracion > maximo) {
            return Constants.ERROR_MAYOR_MAXIMO;
        }

        return Constants.VALOR_CORRECTO;
    }

    //TODO ?DEBERIA SER LA FECHA SINCRONIZADA
    public static long obtenerRestoFechaHora(int cantidadDias) {
        Calendar time = Calendar.getInstance();
        //cal.setTime(dateInstance);
        time.add(Calendar.DATE, -cantidadDias);
        //Date dateBefore30Days = cal.getTime();
        // Date currentTime = Calendar.getInstance().getTime();
        return time.getTime().getTime();
    }

    /*
     * Otros
     * */
    public static String formatMonth(int number) {
        if (number <= 9) {
            return "0" + number;
        } else {
            return "" + number;
        }
    }

    public static int minutesToHour(int tiempo) {
        return tiempo / 60;
    }

    public static boolean ComprobarFechas(String current, String initial, String end) {
        Log.e(TAG, "ComprobarFechas current: " + current + ", initial: " + initial + ", end: " + end);
        boolean result = false;
        try {
            Date mCurrent = fromStringToDate(current,
                    new SimpleDateFormat(Constants.F_DATE_RESULTADO));
            Date mInitial = fromStringToDate(initial,
                    new SimpleDateFormat(Constants.F_DATE_RESULTADO));
            Date mEnd = fromStringToDate(end,
                    new SimpleDateFormat(Constants.F_DATE_RESULTADO));

            Log.e(TAG, "ComprobarFechas mCurrent: " + mCurrent.toString() + ", mInitial: " + mInitial.toString() + ", mEnd: " + mEnd.toString());
            if (mCurrent.equals(mInitial))
                return true;

            if (mCurrent.after(mInitial) && mCurrent.before(mEnd))
                return true;

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static Date fromStringToDate(String value, SimpleDateFormat format)
            throws ParseException {
        return format.parse(value);
    }

    public static String setDateFormat(String dateString, String formatInt, String formatOut) {
        String fechaFormato = null;
        try {
            SimpleDateFormat formatInt1 = new SimpleDateFormat(formatInt);
            SimpleDateFormat formatOut1 = new SimpleDateFormat(formatOut);
            Date date = formatInt1.parse(dateString);
            fechaFormato = formatOut1.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fechaFormato;
    }

    public static Date stringToDate(String dateTime) throws ParseException {
        return new SimpleDateFormat(Constants.F_DATE_RESULTADO).parse(dateTime);
    }

    public static String setFormatDate(String date, String format) {
        String fechaFormato = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            Date d = simpleDateFormat.parse(date);
            fechaFormato = simpleDateFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fechaFormato;
    }

    public static boolean tiempoLimiteExcedido(String fechaTareo, String StringCurrent, int tiempoValides) {
        Log.e(TAG, "tiempoLimiteExcedido fechaTareo: " + fechaTareo + ", StringCurrent: " + StringCurrent + ", tiempoValides: " + tiempoValides);
        try {
            Date dateFechaTareo = stringToDate(setDateFormat(fechaTareo,
                    Constants.F_DATE_TIME_TAREO,
                    Constants.F_DATE_RESULTADO));
            Date dateCurrent = stringToDate(StringCurrent);
            Log.e(TAG, "tiempoLimiteExcedido dateFechaTareo: " + dateFechaTareo);
            Log.e(TAG, "tiempoLimiteExcedido dateCurrent: " + dateCurrent);
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.setTime(dateFechaTareo);
            cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) + tiempoValides);
            Log.e(TAG, "tiempoLimiteExcedido cal: " + cal.getTime());
            if (dateCurrent.after(cal.getTime())) {
                return true;
            }
            return false;
        } catch (ParseException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
