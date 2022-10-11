/*
 * @fileoverview {JoystickActivity} se encarga de realizar tareas especificas.
 *
 * @version             1.0
 *
 * @author              Dyson Arley Parra Tilano <dysontilano@gmail.com>
 * Copyright (C) Dyson Parra
 *
 * @History v1.0 --- La implementacion de {JoystickActivity} fue realizada el 31/07/2022.
 * @Dev - La primera version de {JoystickActivity} fue escrita por Dyson A. Parra T.
 */
package com.project.dev.wirelessjoystick.mobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.project.dev.wirelessjoystick.mobile.R;

import com.project.dev.joystick.exception.JoystickClientConnectionRefusedException;
import com.project.dev.joystick.factory.GraphicJoystickFactory;
import com.project.dev.joystick.graphic.GraphicJoystick;
import com.project.dev.joystick.listener.JoystickClientListener;
import com.project.dev.joystick.listener.JoystickServerListener;
import com.project.dev.joystick.name.generic.GenericJoystick;
import com.project.dev.joystick.name.generic.type.GenericJoystickClient;
import com.project.dev.joystick.name.generic.type.GenericJoystickLocal;
import com.project.dev.joystick.name.generic.type.GenericJoystickServer;
import static com.project.dev.joystick.name.generic.GenericJoystick.JOYSTICK_TYPE_CLIENT;
import static com.project.dev.joystick.name.generic.GenericJoystick.JOYSTICK_TYPE_LOCAL;
import static com.project.dev.joystick.name.generic.GenericJoystick.JOYSTICK_TYPE_SERVER;

import com.project.dev.joystick.mobile.AndroidJoystickViewSetter;

/**
 * TODO: Definición de {@code JoystickActivity}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class JoystickActivity extends Activity {

    /*
     * Variables asociadas con elementos la vista.
     */
    private LinearLayout layout;
    private ScrollView scrollView;

    /* Ancho de la ventana. */
    private int width = 0;
    /* Alto de la ventana. */
    private int height = 0;

    /*
     * Variables locales.
     */
    private String joystickType = "";                                   // Tipo de joystick a utilizar.
    private String joystickName = "";                                   // Nombre del joystick a utilizar.
    private String ipAddress = "";                                      // Ip del servidor.
    private int serverPort = 0;                                         // Puerto de conexión al servidor.
    private GraphicJoystick graphic = null;                             // Joystick grafico asociado a la ventana.
    private final Activity frame = this;                                // Referencia a la ventana.
    private GenericJoystick joystick;                                   // Joystick que ese creará con la información del activiy anterior.
    private Intent mainActivity;                                        // usada para iniciar el activity principal.

    /**
     * Invocado cuando se crea el activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Crea instancia del activity y la asocia con la vista.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Asocia variables locales con elementos de la vista.
        scrollView = findViewById(R.id.scrollView);
        layout = findViewById(R.id.layout);

        // Obtiene los elementos enviados desde el activity "Main" y los asigna a variables locales.
        if (getIntent().getExtras() != null) {
            joystickType = (String) getIntent().getExtras().getSerializable("joystickType");
            joystickName = (String) getIntent().getExtras().getSerializable("joystickName");
            ipAddress = (String) getIntent().getExtras().getSerializable("ipAddress");
            serverPort = (int) getIntent().getExtras().getSerializable("serverPort");
        }

        // Se indica a StrictMode que en su política deathreads no tenga en cuenta los accesos a la red.
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        // Asigna valores a variables locales.
        mainActivity = new Intent(JoystickActivity.this, MainActivity.class);       // Inicializa el intent para iniciar el activity principal.
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);   // Agrega banderas indicando que se cerrará toda la pila de actividades.

        // Obtiene el alto en píxeles del dispositivo.
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        layout.post(new Runnable() {
            public void run() {
                width = layout.getWidth();
                height = layout.getHeight();
                startJoystick();
            }
        });
    }

    /**
     * FIXME: Definición de {@code startJoystick}. Inicializa el joystick con la información
     * obtenida del activity anterior.
     */
    private void startJoystick() {
        AndroidJoystickViewSetter viewSetter = new AndroidJoystickViewSetter(this, scrollView);
        viewSetter.setWith(width);
        GraphicJoystickFactory graphicFactory = new GraphicJoystickFactory(viewSetter);
        try {
            graphic = graphicFactory.makeGraphicJoystick(joystickType, joystickName, ipAddress, serverPort);
            joystick = graphic.getJoystick();                                                       // Obtiene el joystick.

            switch (joystick.getType()) {                                                           // Evalúa el tipo de joystick.
                case JOYSTICK_TYPE_CLIENT:                                                          // Cliente.
                    final GenericJoystickClient client = (GenericJoystickClient) joystick;
                    client.removeButtonActionListeners();
                    client.setOnJoystickClientListener(new JoystickClientListener() {
                        GenericJoystickClient joystick = client;

                        @Override
                        public void onUpdateButtonsStatesTimeOut() {
                            // Crea un mensaje de alerta indicando error de conexión.
                            new AlertDialog.Builder(JoystickActivity.this)
                                    .setTitle(JoystickActivity.this.getString(R.string.errorTitle))
                                    .setMessage(JoystickActivity.this.getString(R.string.errorMsg))
                                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        // Si se minimiza el mensaje sin escoger ninguna opción.
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {

                                        }
                                    })
                                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                        // Si se indica cancelar.
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        // Si se indica Ok.
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Envía petición de desconexión al servidor.
                                            //joystick.disconnectToServer();

                                            // Inicia el activity principal.
                                            startActivity(mainActivity);
                                        }
                                    }).show();
                        }
                    });
                    break;                                                                          // Sale del case.

                case JOYSTICK_TYPE_SERVER:                                                          // Servidor.
                    final GenericJoystickServer server = (GenericJoystickServer) joystick;
                    server.removeButtonActionListeners();
                    server.setOnJoystickServerListener(new JoystickServerListener() {
                        @Override
                        public void onClientConnected() {
                            //Toast.makeText(JoystickActivity.this, JoystickActivity.this.getString(R.string.txtClientConnected), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onClientDisconnected() {
                            //Toast.makeText(JoystickActivity.this, JoystickActivity.this.getString(R.string.txtClientDisconnected), Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;                                                                          // Sale del case.

                case JOYSTICK_TYPE_LOCAL:                                                           // Local.
                    GenericJoystickLocal local = (GenericJoystickLocal) joystick;
                    break;                                                                          // Sale del case.
            }

        } catch (JoystickClientConnectionRefusedException ex) {
            //dispose();
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    /**
     * FIXME: Definición de {@code onKeyDown}. Comportamiento del botón atrás.
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Si el botón es el de atrás.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Si el joystick es un cliente.
            if (joystick.getType().equals(JOYSTICK_TYPE_CLIENT)) {
                // Crea un mensaje de alerta preguntando si desea salir sin guardar.
                new AlertDialog.Builder(this)
                        .setTitle(JoystickActivity.this.getString(R.string.btnBack))
                        .setMessage(JoystickActivity.this.getString(R.string.msgComeBack))
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            // Si se indica si ir atrás.
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Envía petición de desconexión al servidor.
                                ((GenericJoystickClient) joystick).disconnectToServer();
                                // Inicia el activity principal.
                                startActivity(mainActivity);
                            }
                        }).show();
            } else {
                if (joystick.getType().equals(JOYSTICK_TYPE_SERVER))
                    ((GenericJoystickServer) joystick).stopServer();

                startActivity(mainActivity);
            }
        }

        return true;
    }
}
