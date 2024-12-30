package com.dms.tareosoft.data.source.remote;

import retrofit2.http.Path;

public class Urls {

    /*: Maestros*/
    public static final String DOWNLOAD_USUARIOS = "usuarios/";
    public static final String DOWNLOAD_CONCEPTOS_TAREO = "conceptos-tareo/";
    public static final String DOWNLOAD_CLASE_TAREO = "clase-tareo/";
    public static final String DOWNLOAD_TURNO = "turno/";
    public static final String DOWNLOAD_UNIDAD_MEDIDA = "unidadmedida/";
    public static final String DOWNLOAD_PERFILES = "perfil/";
    public static final String DOWNLOAD_TAREO_XUSUARIO = "tareo/";
    public static final String DOWNLOAD_DETALLETAREO_XUSUARIO = "detalle-tareo/";


    public static final String DOWNLOAD_EMPLEADOS = "empleados/";
    public static final String DOWNLOAD_NIVELES_TAREO = "niveles-tareo/";
    public static final String DOWNLOAD_GRUPO = "grupo/";
    public static final String DOWNLOAD_PARAMETROS = "parametros/";
    public static final String DOWNLOAD_CLAVE_DINAMICA = "clave-dinamica/";
    public static final String DOWNLOAD_CULTIVO = "cultivo/";
    public static final String DOWNLOAD_PARCELA = "parcela/";
    public static final String DOWNLOAD_BONO = "bono/";

    /*: Validar servicio*/
    public static final String VALIDAR_CONEXION = "conexion-bd/validar/";
    public static final String DOWNLOAD_FECHA_HORA = "parametros/fecha-hora-servidor/";
    //public static final String DOWNLOAD_DIAS_VIGENCIA = "parametros/dias-vigencia";

    /* Batch */
    public static final String UPLOAD_TAREOS = "tareo/Descarga/";
    public static final String UPLOAD_RESULTADOS = "resultados-empleados/descarga/";
    public static final String UPLOAD_RESULTADOS_TAREO = "resultado-tareo/";
    public static final String UPLOAD_CONTROLES = "control-empleado/descarga/";

    /* En linea */
    /*: Login*/
    public static final String USUARIO_LOGIN = "usuarios/validar/";

    /*: Tareo*/
    public static final String LISTAR_TAREOS = "tareo/resumen/";
    public static final String GUARDAR_TAREO = "tareo/";
    public static final String GUARDAR_DETALLE_TAREO = "tareo/DetalleTareo/";
    public static final String TAREO = "tareo/codigoTareo/{codTareo}/";
    public static final String ACTUALIZAR_TAREO = "tareo/{codTareo}/";

    //AllEmpleados
    //todo verificar la url debe ser real
    public static final String LISTAR_ALL_EMPLEADOS = "tareo/resumen/";
    public static final String VALIDAR_EMPLEADO = "empleados/trabajador/{codEmpleado}";
    public static final String VALIDAR_EMPLEADO_POR_PLANILLA = "empleados/{codPlanilla}/trabajador/{codEmpleado}";
    public static final String DELETE_TAREO = "tareo/{codigoTareo}";
    public static final String CREAR_NEW_DETALE_TAREO = "tareo/resumen/";
    public static final String CONSULTAR_TURNO = "turno/{idTurno}";
    public static final String CLASE_TAREO = "clase-tareo/{codTareo}";
    public static final String DOWNLOAD_CONCEPTOS_X_NIVEL = "conceptos-tareo/identificadorNivel/{identificadorNivel}/idConceptoPadre/{ordenNivel}";

    public static final String DETALLE_ID_EMPLEADO_INI = "detalle-tareo/idEmpleado/{idEmpleado}/estadoIni/{status}";
    public static final String DETALLE_ID_EMPLEADO_FIN = "detalle-tareo/idEmpleado/{idEmpleado}/estadoFin/{status}";
    public static final String DETALLE_ID_TAREO_INI = "detalle-tareo/idTareo/{codTareo}/estadoIni/{status}";
    public static final String DETALLE_ID_TAREO_FIN = "detalle-tareo/idTareo/{codTareo}/estadoFin/{status}";

    public static final String EMPLEADO_ID_EMPLEADO_INI = "empleados/idEmpleado/{idEmpleado}/estadoIni/{status}";
    public static final String EMPLEADO_ID_EMPLEADO_FIN = "empleados/idEmpleado/{idEmpleado}/estadoFin/{status}";
    public static final String EMPLEADO_ID_TAREO_INI = "empleados/idTareo/{codTareo}/estadoIni/{status}";
    public static final String EMPLEADO_ID_TAREO_FIN = "empleados/idTareo/{codTareo}/estadoFin/{status}";


    //Asistencia
    public static final String GUARDAR_MARCACION = "marcacion/";

    //Incidencia
    public static final String GUARDAR_INCIDENCIA = "incidencia/";

    //Acceso
    public static final String GUARDAR_ACCESO = "Acceso/Insert/";
}