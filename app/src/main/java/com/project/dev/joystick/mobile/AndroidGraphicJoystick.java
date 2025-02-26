/*
 * @fileoverview    {AndroidGraphicJoystick}
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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.project.dev.joystick.button.GenericButton;
import com.project.dev.joystick.button.GenericButtonGraphicListener;
import com.project.dev.joystick.graphic.GenericImage;
import com.project.dev.joystick.graphic.GraphicJoystick;
import com.project.dev.joystick.name.generic.GenericJoystick;

/**
 * TODO: Description of {@code AndroidGraphicJoystick}.
 *
 * @author Dyson Parra
 * @since Java 17 (LTS), Gradle 7.3
 */
public class AndroidGraphicJoystick extends RelativeLayout implements GraphicJoystick {

    private RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(0, 0);

    private final Activity currentContext;
    private int width;                                                                              // Ancho del joystick.
    private int height;                                                                             // Alto del joystick.

    private final GenericJoystick joystick;                                                         // Joystick parámetro.

    private final GenericImage backgroundImage;                                                     // Imagen de fondo del joystick.

    private RelativeLayout.LayoutParams backGroundParams = new RelativeLayout.LayoutParams(0, 0);
    private final ImageView backgroundComponent;                                                    // Componente con el fondo del joystick.

    private RelativeLayout backGroundLayout;                                                        // Crea un relative layout que tendrá la imagen de fondo.
    private RelativeLayout buttonsLayout;                                                           // Crea un relative layout que tendrá los botones del joystick.

    private final GenericButton[] joystickButtons;                                                  // Botones del joystick.
    private GenericButton button;                                                                   // Cada botón encontrado en el joystick.
    private ImageView buttonComponet;                                                               // Cada botón que irá como componente de la vista.

    /**
     * TODO: Description of {@code AndroidGraphicJoystick}.
     *
     * @param context         es el contexto donde se creará el RelativeLayout.
     * @param genericJoystick es el joystick que tendrá el RelativeLayout.
     * @throws java.lang.Exception
     */
    @SuppressLint("ClickableViewAccessibility")
    public AndroidGraphicJoystick(Context context, GenericJoystick genericJoystick) throws Exception {
        super(context);

        this.currentContext = (Activity) context;
        joystick = genericJoystick;                                                                 // Obtiene el joystick parámetro.
        joystickButtons = joystick.getButtons();

        backGroundLayout = new RelativeLayout(context);                                             // Inicaliza el layout que tendrá el fondo.
        buttonsLayout = new RelativeLayout(context);                                                // Inicaliza el layout que tendrá los botones.

        backgroundImage = joystick.getBackground();                                                 // Obtiene la imagen genérica del fondo.

        width = backgroundImage.getWidth();                                                         // Obtiene el ancho del joystick.
        height = backgroundImage.getHeight();                                                       // Obtiene el alto del joystick.

        backgroundComponent = new ImageView(context);                                               // Inicializa componente que tendrá el fondo del joystick.
        backgroundComponent.setImageBitmap((Bitmap) backgroundImage.getGraphic());                  // Obtiene el gráfico de la imagen genérica del fondo.
        backgroundComponent.setLayoutParams(backGroundParams);
        backGroundParams.width = backgroundImage.getWidth();
        backGroundParams.height = backgroundImage.getHeight();
        params.width = backgroundImage.getWidth();
        params.height = backgroundImage.getHeight();

        buttonsLayout.setLayoutParams(params);
        backGroundLayout.setLayoutParams(params);

        backGroundLayout.addView(backgroundComponent);                                              // Agrega el grafico del fondo al layout del fondo.

        for (int i = 0; i < joystick.getButtonQuantity(); i++) {                                    // Recorre los botones del joystick.
            button = joystickButtons[i];                                                            // Obtiene el botón actual.

            buttonComponet = new ImageView(context);                                                // Crea un nuevo componente para ponerlo en la vista.
            buttonComponet.setAdjustViewBounds(true);

            if (!joystick.getType().equals(GenericJoystick.JOYSTICK_TYPE_SERVER)) {                  // Si el joystick no es un servidor.
                buttonComponet.setOnTouchListener(new OnTouchListener() {
                    final GenericButton currentButton = button;

                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN /*
                                 * -
                                 * || event.getAction() == MotionEvent.ACTION_MOVE
                                 */)
                            currentButton.touchButton();
                        else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                            currentButton.unTouchButton();
                        return true;
                    }
                });
            }
            GenericButtonGraphicListener<ImageView> graphicButtonListener = new GenericButtonGraphicListener<ImageView>(buttonComponet) {
                RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(0, 0);

                @Override
                public void onButtonStateChanged(byte newState, final GenericImage newImage) {
                    currentContext.runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                if (newImage != null) {
                                    view.setImageBitmap((Bitmap) newImage.getGraphic());
                                    view.setLayoutParams(buttonParams);
                                    buttonParams.width = newImage.getWidth();
                                    buttonParams.height = newImage.getHeight();
                                    buttonParams.leftMargin = newImage.getPositionX();
                                    buttonParams.topMargin = newImage.getPositionY();
                                }
                            } catch (Exception ignored) {

                            }
                        }
                    });
                }
            };

            button.setOnGraphicListener(graphicButtonListener);                                                     // Asigna componente al botón actual.
            graphicButtonListener.onButtonStateChanged(BUTTON_RELEASED, button.getButtonImage(BUTTON_RELEASED));

            buttonsLayout.addView(buttonComponet);
        }
        this.addView(backGroundLayout);
        this.addView(buttonsLayout);
        joystick.setOnGraphicListener(this);
    }

    /**
     * FIXME: Description of {@code getJoystick}. Obtiene el joystick del joystick gráfico.
     *
     * @return es el joystick del gráfico.
     */
    @Override
    public final GenericJoystick getJoystick() {
        return joystick;
    }

    /**
     * FIXME: Description of {@code getJoystickWidth}. Obtiene ancho del joystick asociado al
     * joystick gráfico.
     *
     * @return el ancho del joystick gráfico.
     */
    public final int getJoystickWidth() {
        return width;
    }

    /**
     * FIXME: Description of {@code getJoystickHeight}. Obtiene el alto del joystick asociado al
     * joystick gráfico.
     *
     * @return el alto del joystick gráfico.
     */
    public final int getJoystickHeight() {
        return height;
    }

    /**
     * TODO: Description of {@code getLeftMargin}.
     *
     * @return
     */
    public final int getLeftMargin() {
        return params.leftMargin;
    }

    /**
     * TODO: Description of {@code setLeftMargin}.
     *
     * @param leftMargin
     */
    public final void setLeftMargin(int leftMargin) {
        this.params.leftMargin = leftMargin;
    }

    /**
     * TODO: Description of {@code getTopMargin}.
     *
     * @return
     */
    public final int getTopMargin() {
        return params.topMargin;
    }

    /**
     * TODO: Description of {@code setTopMargin}.
     *
     * @param topMargin
     */
    public final void setTopMargin(int topMargin) {
        this.params.topMargin = topMargin;
    }

    /**
     * FIXME: Description of {@code onJoystickScale}. Invocado cuando el joystick escala sus
     * dimensiones.
     *
     */
    @Override
    public void onJoystickScale() {
        backgroundComponent.setImageBitmap((Bitmap) backgroundImage.getGraphic());
        width = backgroundImage.getWidth();                                                         // Obtiene el ancho de la imagen de fondo.
        height = backgroundImage.getHeight();                                                       // Obtiene el alto de la imagen de fondo.
        backGroundParams.width = backgroundImage.getWidth();
        backGroundParams.height = backgroundImage.getHeight();
        params.width = backgroundImage.getWidth();
        params.height = backgroundImage.getHeight();
    }

    /**
     * FIXME: Description of {@code scaleJoystick}. Cambia la resolución de las imágenes de los
     * botones y el joystick.
     *
     * @param percent es el porcentaje de escalado del joystick.
     * @throws java.lang.Exception
     */
    public void scaleJoystick(double percent) throws Exception {
        joystick.scaleImages(percent);
    }

    /**
     * FIXME: Description of {@code scaleJoystickWidth}. Cambia la resolución de las imágenes de los
     * botones y el joystick.
     *
     * @param newWidth es el nuevo ancho del joystick.
     * @throws java.lang.Exception
     */
    public final void scaleJoystickWidth(int newWidth) throws Exception {
        double percent = newWidth / (double) width;
        scaleJoystick(percent);
    }

    /**
     * FIXME: Description of {@code scaleJoystickHeight}. Cambia la resolución de las imágenes de los
     * botones y el joystick.
     *
     * @param newHeight es el nuevo alto del joystick.
     * @throws java.lang.Exception
     */
    public final void scaleJoystickHeight(int newHeight) throws Exception {
        double percent = newHeight / (double) height;
        scaleJoystick(percent);
    }
}
