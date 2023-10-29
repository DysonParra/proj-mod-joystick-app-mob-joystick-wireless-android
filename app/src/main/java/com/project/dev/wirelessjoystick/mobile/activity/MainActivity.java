/*
 * @fileoverview    {MainActivity}
 *
 * @version         2.0
 *
 * @author          Dyson Arley Parra Tilano <dysontilano@gmail.com>
 *
 * @copyright       Dyson Parra
 * @see             github.com/DysonParra
 *
 * History
 * @version 1.0     Implementation done.
 * @version 2.0     Documentation added.
 */
package com.project.dev.wirelessjoystick.mobile.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.project.dev.wirelessjoystick.mobile.R;

import com.project.dev.joystick.name.generic.type.GenericJoystickClient;
import com.project.dev.joystick.name.nintendo.NintendoJoystick;
import com.project.dev.joystick.name.poly.PolyJoystick;
import static com.project.dev.joystick.name.generic.GenericJoystick.JOYSTICK_TYPE_CLIENT;
import static com.project.dev.joystick.name.generic.GenericJoystick.JOYSTICK_TYPE_LOCAL;
import static com.project.dev.joystick.name.generic.GenericJoystick.JOYSTICK_TYPE_SERVER;

/**
 * TODO: Description of {@code MainActivity}.
 *
 * @author Dyson Parra
 * @since 11
 */
public class MainActivity extends AppCompatActivity {

    /*
     * Variables asociadas con elementos la vista.
     */
    private EditText editTextIpAddress, editTextPort;
    private Spinner spnJoystickType;
    private Spinner spnJoystickName;
    private Button btnCreateConnect;

    /*
     * Variables locales.
     */
    private GenericJoystickClient genericJoystick;                      // Usada para probar la conexión con el servidor con el joystick.
    private String ipAddress = "";                                      // Indica la ip del servidor.
    private int serverPort = 0;                                         // Indica el puerto de conexión al servidor.
    private static final String[] JOYSTICK_TYPES = {
        JOYSTICK_TYPE_CLIENT,
        JOYSTICK_TYPE_SERVER,
        JOYSTICK_TYPE_LOCAL
    };
    private static final String[] JOYSTICK_TYPES_SPN_TEXT = {
        "Joystick cliente",
        "Joystick servidor",
        "Joystick local"
    };
    private static final String[] JOYSTICK_NAMES = {
        PolyJoystick.JOYSTICK_NAME,
        NintendoJoystick.JOYSTICK_NAME
    };

    /**
     * Invocado cuando se crea el activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Crea instancia del activity y la asocia con la vista.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Asocia variables locales con elementos de la vista.
        editTextIpAddress = findViewById(R.id.editTextAddress);
        editTextPort = findViewById(R.id.editTextPort);
        spnJoystickType = findViewById(R.id.spnJoystickType);
        spnJoystickName = findViewById(R.id.spnJoystickName);
        btnCreateConnect = findViewById(R.id.btnCreateConnect);

        // Se indica a StrictMode que en su política deathreads no tenga en cuenta los accesos a la red.
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        // Inicializa el joystick cliente.
        try {
            genericJoystick = new GenericJoystickClient();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        // Asigna valores al array con los textos del joystick.
        for (int i = 0; i < JOYSTICK_TYPES.length; i++) {
            switch (JOYSTICK_TYPES[i]) {
                case JOYSTICK_TYPE_SERVER:
                    JOYSTICK_TYPES_SPN_TEXT[i] = getString(R.string.joystickTypeServer);
                    break;
                case JOYSTICK_TYPE_LOCAL:
                    JOYSTICK_TYPES_SPN_TEXT[i] = getString(R.string.joystickTypeLocal);
                    break;
                case JOYSTICK_TYPE_CLIENT:
                    JOYSTICK_TYPES_SPN_TEXT[i] = getString(R.string.joystickTypeClient);
                    break;
            }
        }

        // Asigna valores a los spinner con el tipo y el nombre del joystick.
        ArrayAdapter<String> spnJoystickTypeAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, JOYSTICK_TYPES_SPN_TEXT);
        spnJoystickType.setAdapter(spnJoystickTypeAdapter);

        ArrayAdapter<String> spnJoystickNameAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, JOYSTICK_NAMES);
        spnJoystickName.setAdapter(spnJoystickNameAdapter);

        // Asigna valores por defecto a la dirección ip y el puerto de conexión.
        editTextIpAddress.setGravity(Gravity.CENTER);
        editTextPort.setGravity(Gravity.CENTER);
        //editTextIpAddress.setText("192.168.1.60");
        //editTextPort.setText("1025");
        //editTextIpAddress.setText("192.168.1.2");
        //editTextPort.setText("2");

        // Comportamiento del spinner con el tipo de joystick.
        spnJoystickType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (JOYSTICK_TYPES[i]) {                                // Evalúa el tipo de joystick seleccionado.
                    case JOYSTICK_TYPE_CLIENT:                              // Cliente.
                        editTextIpAddress.setEnabled(true);                 // Habilita la escritura de la dirección ip.
                        editTextIpAddress.setVisibility(View.VISIBLE);      // Muestra la escritura de la dirección ip.
                        editTextPort.setEnabled(true);                      // Habilita la escritura del puerto de conexión.
                        editTextPort.setVisibility(View.VISIBLE);           // Muestra la escritura del puerto de conexión.
                        spnJoystickName.setEnabled(false);                  // Deshabilita el spinner para seleccionar nombre del joystick.
                        spnJoystickName.setVisibility(View.INVISIBLE);      // Oculta el spinner para seleccionar nombre del joystick.
                        btnCreateConnect.setText(R.string.btnConnect);      // Pone el texto conectar en el botón.
                        break;                                              // Sale del case.

                    case JOYSTICK_TYPE_SERVER:                              // Servidor.
                    case JOYSTICK_TYPE_LOCAL:                               // Local.
                        editTextIpAddress.setEnabled(false);                // Deshabilita la escritura de la dirección ip.
                        editTextIpAddress.setVisibility(View.INVISIBLE);    // Oculta la escritura de la dirección ip.
                        editTextPort.setEnabled(false);                     // Deshabilita la escritura del puerto de conexión.
                        editTextPort.setVisibility(View.INVISIBLE);         // Oculta la escritura del puerto de conexión.
                        spnJoystickName.setEnabled(true);                   // Habilita el spinner para seleccionar nombre del joystick.
                        spnJoystickName.setVisibility(View.VISIBLE);        // Muestra el spinner para seleccionar nombre del joystick.
                        btnCreateConnect.setText(R.string.btnCreate);       // Pone el texto crear en el botón.
                        break;                                              // Sale del case.
                }
            }

            // Invocado si no se selecciona ningún elemento (No necesario).
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Comportamiento del botón crear joysticks.
        btnCreateConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Evalúa el tipo de joystick a crear.
                switch (JOYSTICK_TYPES[(int) spnJoystickType.getSelectedItemId()]) {
                    // Cliente.
                    case JOYSTICK_TYPE_CLIENT:
                        ipAddress = String.valueOf(editTextIpAddress.getText().toString());

                        if ("".equals(ipAddress))
                            Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.noIpAddressMsg), Toast.LENGTH_SHORT).show();
                        else if ("".equals(String.valueOf(editTextPort.getText())))
                            Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.noPortMsg), Toast.LENGTH_SHORT).show();
                        else {
                            try {
                                serverPort = Integer.parseInt(String.valueOf(editTextPort.getText()));
                                genericJoystick.setServerIpAddress(ipAddress);
                                genericJoystick.setServerPort(serverPort);

                                // Prueba la conexión con el servidor.
                                if (genericJoystick.testServerConnection())
                                    startJoystickActivity(JOYSTICK_TYPE_CLIENT, null, ipAddress, serverPort);
                                else
                                    Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.invalidServerMsg), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.unknownErrorClientMsg), Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;

                    // Servidor.
                    case JOYSTICK_TYPE_SERVER:
                        startJoystickActivity(JOYSTICK_TYPE_SERVER, JOYSTICK_NAMES[(int) spnJoystickName.getSelectedItemId()], null, 0);
                        break;

                    // Local.
                    case JOYSTICK_TYPE_LOCAL:
                        startJoystickActivity(JOYSTICK_TYPE_LOCAL, JOYSTICK_NAMES[(int) spnJoystickName.getSelectedItemId()], null, 0);
                        break;
                }
            }
        });
    }

    /**
     * FIXME: Description of {@code startJoystickActivity}. Inicia el frame con el joystick.
     *
     * @param joystickType es el tipo de joystick que se creará (Local, cliente o servidor).
     * @param joystickName es el nombre del joystick que se creará.
     * @param ipAddress    Es la dirección ip donde se conectará el joystick cliente en caso de ser
     *                     de este tipo.
     * @param serverPort   Es puerto de conexión donde se conectará el joystick cliente en caso de
     *                     ser de este tipo.
     */
    public void startJoystickActivity(String joystickType, String joystickName, String ipAddress, int serverPort) {
        // Crea un intent para iniciar el activity con el joystick.
        Intent intent = new Intent(MainActivity.this, JoystickActivity.class);

        // Manda información al activity con el joystick.
        intent.putExtra("joystickType", joystickType);
        intent.putExtra("joystickName", joystickName);
        intent.putExtra("ipAddress", ipAddress);
        intent.putExtra("serverPort", serverPort);

        // Inicia el activity.
        startActivity(intent);
    }
}
