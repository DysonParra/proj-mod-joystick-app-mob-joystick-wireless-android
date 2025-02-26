/*
 * @fileoverview    {AndroidGenericImage}
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.BitmapFactory;

import java.io.InputStream;

import com.project.dev.joystick.graphic.GenericImage;

import lombok.Data;

/**
 * TODO: Description of {@code AndroidGenericImage}.
 *
 * @author Dyson Parra
 * @since Java 17 (LTS), Gradle 7.3
 */
//@AllArgsConstructor
//@Builder
@Data
//@NoArgsConstructor
public class AndroidGenericImage extends GenericImage {

    private Context context = null;                                                 // Contexto de la imagen.

    /**
     * TODO: Description of {@code AndroidGenericImage}.
     *
     * @param context es el contexto donde se creará la imagen.
     * @param path    es la ruta de la imagen.
     * @throws java.lang.Exception
     */
    public AndroidGenericImage(Context context, String path) throws Exception {
        super(path);
        this.context = context;
        pathChanged();
    }

    /**
     * FIXME: Description of {@code pathChanged}. Invocado cuando cambia la ruta de la imagen.
     *
     * @throws java.lang.Exception
     */
    private void pathChanged() throws Exception {
        InputStream assetInStream = (GenericImage.class.getResource(path)).openStream();
        Bitmap bit = BitmapFactory.decodeStream(assetInStream);

        setWidth(bit.getWidth());
        setHeight(bit.getHeight());

        this.graphic = bit;
    }

    /**
     * FIXME: Description of {@code onPathChange}. Invocado cuando se cambia la ruta de la imagen
     * genérica.
     *
     * @throws Exception
     */
    @Override
    public void onPathChange() throws Exception {
        pathChanged();
    }

    /**
     * FIXME: Description of {@code onWidthChange}. Invocado cuando se cambia el ancho de la imagen
     * genérica.
     *
     */
    @Override
    public void onWidthChange() {

    }

    /**
     * FIXME: Description of {@code onHeightChange}. Invocado cuando se cambia el lto de la imagen
     * genérica.
     *
     */
    @Override
    public void onHeightChange() {

    }

    /**
     * FIXME: Description of {@code onPositionXChange}. Invocado cuando se cambian las coordenadas en
     * X de la imagen genérica.
     *
     */
    @Override
    public void onPositionXChange() {

    }

    /**
     * FIXME: Description of {@code onPositionYChange}. Invocado cuando se cambian las coordenadas en
     * Y de la imagen genérica.
     *
     */
    @Override
    public void onPositionYChange() {

    }
}
