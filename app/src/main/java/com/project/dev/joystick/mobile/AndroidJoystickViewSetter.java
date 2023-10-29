/*
 * @fileoverview    {AndroidJoystickViewSetter}
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
package com.project.dev.joystick.mobile;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.project.dev.wirelessjoystick.mobile.R;

import com.project.dev.joystick.factory.GenericImageAbstractFactory;
import com.project.dev.joystick.graphic.GraphicJoystick;
import com.project.dev.joystick.name.generic.GenericJoystick;
import com.project.dev.joystick.name.generic.setter.GenericJoystickGraphicSetter;
import com.project.dev.joystick.name.generic.type.GenericJoystickClient;
import com.project.dev.joystick.name.generic.type.GenericJoystickServer;
import com.project.dev.joystick.setter.GenericJoystickPrintActionSetter;
import com.project.dev.joystick.setter.JoystickViewSetter;

import lombok.Data;

import static com.project.dev.joystick.name.generic.GenericJoystick.JOYSTICK_TYPE_CLIENT;
import static com.project.dev.joystick.name.generic.GenericJoystick.JOYSTICK_TYPE_LOCAL;
import static com.project.dev.joystick.name.generic.GenericJoystick.JOYSTICK_TYPE_SERVER;

/**
 * TODO: Definición de {@code AndroidJoystickViewSetter}.
 *
 * @author Dyson Parra
 * @since 11
 */
//@AllArgsConstructor
//@Builder
@Data
//@NoArgsConstructor
public class AndroidJoystickViewSetter extends JoystickViewSetter<ViewGroup> {

    private Context context;
    private int with = 0;

    /**
     * FIXME: Definición de {@code AndroidJoystickViewSetter}.
     *
     * @param context es el contexto donde se agregara la vista.
     * @param view    es la vista donde se agregará el joystick.
     */
    public AndroidJoystickViewSetter(Context context, ViewGroup view) {
        super(view);
        this.context = context;
    }

    /**
     * FIXME: Definición de {@code addJoystickToView}. Agrega el joystick actual a la vista.
     *
     * @param joystick      es el joystick que se agregará a la vista.
     * @param graphicSetter es quien dirá cuales botones tendrá el joystick.
     * @throws java.lang.Exception Si ocurre un error.
     */
    @Override
    public GraphicJoystick addJoystickToView(GenericJoystick joystick, GenericJoystickGraphicSetter graphicSetter) throws Exception {
        GenericJoystickPrintActionSetter printActionSetter
                = new GenericJoystickPrintActionSetter();                                       // Crea un asignador de acciones a los botones.

        GenericImageAbstractFactory imageFactory = new AndroidGenericImageFactory(context);     // Crea fábrica de imagenes genéricas de android.
        graphicSetter.setGenericJoystickGraphics(imageFactory, joystick);                       // Asigna gráficos al joystick.
        AndroidGraphicJoystick graphicJoystick = new AndroidGraphicJoystick(context, joystick); // Crea un joystick gráfico de android.

        view.addView(graphicJoystick);

        if (with > 0)
            graphicJoystick.scaleJoystickWidth(with);

        TextView joystickInfo = new TextView(context);
        joystickInfo.setTextSize((float) (graphicJoystick.getJoystickWidth() / 100));
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(
                graphicJoystick.getJoystickWidth(),
                (int) (graphicJoystick.getJoystickHeight() + (joystickInfo.getTextSize() * 2)));
        joystickInfo.setGravity(Gravity.BOTTOM);
        joystickInfo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        joystickInfo.setLayoutParams(infoParams);
        graphicJoystick.addView(joystickInfo);
        String title = "";

        switch (joystick.getType()) {                                                           // Evalúa el tipo de joystick.
            case JOYSTICK_TYPE_CLIENT:                                                          // Cliente.
                GenericJoystickClient client = (GenericJoystickClient) joystick;
                title = context.getString(R.string.txtJoystickInfoClient)
                        + "   IP: " + getIpAddress()
                        + "   "
                        + context.getString(R.string.txtJoystickInfoPort)
                        + ": "
                        + client.getServerPort();
                break;                                                                          // Sale del case.

            case JOYSTICK_TYPE_SERVER:                                                          // Servidor.
                GenericJoystickServer server = (GenericJoystickServer) joystick;
                title = context.getString(R.string.txtJoystickInfoServer)
                        + "   IP: " + getIpAddress()
                        + "   "
                        + context.getString(R.string.txtJoystickInfoPort)
                        + ": "
                        + server.getServerPort();
                printActionSetter.addButtonPrintActions(joystick);                              // Agrega acciones de impresion en pantalla al joystick servidor.
                break;                                                                          // Sale del case.

            case JOYSTICK_TYPE_LOCAL:                                                           // Local.
                title = context.getString(R.string.txtJoystickInfoLocal);
                printActionSetter.addButtonPrintActions(joystick);                              // Agrega acciones de impresion en pantalla al joystick local.
                break;                                                                          // Sale del case.
        }

        joystickInfo.setText(title);
        return graphicJoystick;
    }

    /**
     * FIXME: Definición de {@code getIpAddress}. Obtiene la dirección ip del dispositivo.
     *
     * @return
     */
    private String getIpAddress() {

        StringBuilder ip = new StringBuilder();

        try {

            int ipQuantity = 0;
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();

                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ipQuantity++;
                        if (ipQuantity == 1)
                            ip.append(inetAddress.getHostAddress());
                        else
                            ip.append(" (").append(inetAddress.getHostAddress()).append(")");
                    }
                }
            }

        } catch (SocketException e) {
            e.printStackTrace(System.out);
        }

        //System.out.println("'" + ip + "'");
        return ip.toString();
    }
}
