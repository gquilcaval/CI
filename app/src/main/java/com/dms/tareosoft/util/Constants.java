package com.dms.tareosoft.util;

public class Constants {

    //:: Respuesta General
    public static final int API_SUCCESS = 1;

    //:: Preference Manager

    //  Configuracion (CF)
    public static final String PREFS_GEN_MODO_TRABAJO = "GEN_ModoTrabajo";
    public static final String PREF_GEN_DIAS_VIGENCIA = "GEN_DiasVigencia";
    public static final String PREF_GEN_FECHA_SERVIDOR = "GEN_FechaHoraServidor";
    public static final String PREFS_GEN_MAXIMA_DIFERENCIA = "GEN_MaximaDiferencia";
    public static final String PREFS_GEN_TIMEOUT = "GEN_TimeOut";
    public static final String PREFS_GEN_SOUND = "GEN_SonidoLectura";
    public static final String PREFS_GEN_ERRORES = "GEN_ErroresDetallados";
    public static final String PREF_GEN_WEBSERVICE = "GEN_WebService";
    public static final String PREF_DURACION_REFRIGERIO = "GEN_DuracionRefrigerio";
    public static final String PREF_SYNC_EXITOSA = "GEN_Sync_exitosa";

    //  Flags
    public static final String PREFS_FLG_NUEVAIP = "FLG_NuevoServicio";//Verifica si se ha modificado el servicio web
    public static final String PREFS_FLG_VALIDARWS = "FLG_ValidacionWS"; //Verifica si se hizo la descarga de maestro de forma correcta (Al momento de reiniciar la actividad)

    //  Configuracion: Marca de tiempo (TM)
    public static final String PREFS_TM_MARGEN_TIEMPO = "TM_DiferenciaTiempo";
    public static final String PREFS_VIGENCIA_TAREO = "TM_Vigencia_Tareo";
    public static final String PREFS_TM_ULIIMO_INTENTO = "TM_FechaValidacionWs";

    public static final String PREFS_TM_CAMBIO_MANUAL = "FLG_CambioManual";
    public static final String PREFS_TM_TIEMPO_VALIDADO = "FLG_TiempoValidadoWs";
    public static final String PREFS_TM_FECHA_DESFASADA = "FLG_FechaDesfasada";

    //  Configuracion: Tareo (CT)
    public static final String PREFS_UNIDAD_MEDIDA = "CT_UnidadMedida";
    public static final String PREFS_TURNO = "CT_Turno";
    public static final String PREFS_CLASE_TAREO = "CT_ClaseTareo";
    public static final String PREFS_VALIDA_DESCARGA = "CT_valida_descarga";
    public static final String PREFS_VALIDA_DESCARGA_EXITOSA = "CT_valida_descarga_exitosa";

    //  Configuracion: Ajustes (AJ)
    public static final String PREFS_AJUSTES_UM = "AJ_UnidadMedida";
    public static final String PREFS_FECHA_INICIO = "AJ_FechaInicio";
    public static final String PREFS_HORA_INICIO = "AJ_HoraInicio";
    public static final String PREFS_FECHA_FIN = "AJ_FechaFin";
    public static final String PREFS_HORA_FIN = "AJ_HoraFin";
    public static final String PREFS_VIGENCIA = "AJ_VigenciaDescarga";
    public static final String PREF_COD_ADMIN = "AJ_UsuarioAdmin";
    public static final String PREFS_REGISTRAR_PERSONA = "AJ_RegistrarPersona";
    public static final String PREFS_REGISTRAR_TAREO_NOT_EMPLOYER = "AJ_RegistrarTareoNotEmployer";
    public static final String PREFS_TIME_WORKER = "AJ_TimeWorker";
    public static final String PREFS_MODULO_ASISTENCIA = "AJ_ModuloAsistencia";
    public static final String PREFS_MODULO_INCIDENCIA = "AJ_ModuloIncidencia";
    public static final String PREFS_MODULO_ACCSO = "AJ_ModuloAcceso";

    //  Datos Usuario (DU )
    public static final String PREF_COD_USUARIO = "DU_Codigo";
    public static final String PREF_NOMBRE_USUARIO = "DU_Nombre";
    public static final String PREF_COD_PERFIL = "DU_CodigoPerfil";
    public static final String PREF_PERFIL_USUARIO = "DU_Perfil";

    // Días de diferencia para la fecha de inicio
    public static final String PREFS_CANT_DIAS_INICIO = "CF_CantDiasIni";

    //:: Network
    public static final String URL_SECURITY = "http://";
    public static final String DEFAULT_URL = "172.16.3.15/WSTareoAndroid/api/V1/";

    //:: Formato Fechas
    public static final String F_FECHAHORA_WS = "yyyy-MM-dd HH:mm:ss";
    public static final String F_LECTURA = "yyyy/MM/dd HH:mm:ss";
    public static final String F_DATE_LECTURA = "yyyy/MM/dd";
    public static final String F_DATE_LECTURA2 = "yyyy-MM-dd";
    public static final String F_TIME_LECTURA = "HH:mm:ss";
    public static final String F_TIME_SEND = "HHmmss";
    public static final String F_DATE_TAREO = "yyyyMMdd";
    public static final String F_TIME_TAREO = "HH:mm:ss";
    public static final String F_DATE_TIME_TAREO = "yyyyMMdd HH:mm:ss";
    public static final String F_DATE_RESULTADO = "yyyyMMddHHmmss";


    //:: Otros
    //  Scanner Urobo
    public static final String SCANNER_ACTION = "android.intent.ACTION_DECODE_DATA";
    public static final String SCANNER_STRING = "barcode_string";
    //  Honeywell
    public static final String SCANNER_STRING_HONEY = "data"; //

    public static final String GUION = ">";

    // Extras (Parametros entre activities) TODO: validar que no se destruyan al girar la pantalla
    public static final String EXT_CODTAREO = "CodigoTareo";
    public static final String EXT_TAREO = "StatusTareo";
    public static final String EXT_TITLE_TAREO = "TitleTareo";
    public static final String EXT_CODCONCEPTO1 = "Concepto1";
    public static final String EXT_CODCONCEPTO2 = "Concepto2";
    public static final String EXT_FECHAINICIO = "FechaInicio";
    public static final String EXT_TOTALTRABAJADORES = "Asignados";

    // Codigo Validaciones de Rango
    public static final int ERROR_MENOR_MINIMO = -1;
    public static final int ERROR_MAYOR_MAXIMO = 0;
    public static final int VALOR_CORRECTO = 1;

    // Tipo de Resultados
    public static final int RESULT_POR_EMPLEADO = 1;
    public static final int RESULT_POR_TAREO = 0;
    public static final int INTENT_SETTINGS = 2;

    //Valores por defecto
    public static String sinFecha = "Sin fecha";

    public static final class Intents {
        public static final int FOR_RESULT_NEW_TAREO = 100;
    }
    public static final class Intent {
        public static final String CLASE = "clase";
    }

    // SPLASH
    public static final String PREF_IS_REGISTERED = "PREF_IS_REGISTERED";
}