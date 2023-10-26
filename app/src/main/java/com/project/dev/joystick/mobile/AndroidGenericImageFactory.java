/*
 * @fileoverview    {AndroidGenericImageFactory}
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

import com.project.dev.joystick.factory.GenericImageAbstractFactory;
import com.project.dev.joystick.graphic.GenericImage;

import lombok.Data;

/**
 * TODO: Definición de {@code AndroidGenericImageFactory}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
//@AllArgsConstructor
//@Builder
@Data
//@NoArgsConstructor
public class AndroidGenericImageFactory extends GenericImageAbstractFactory {

    private Context context = null;                                                 // Contexto de la fábrica de imágenes genéricas.

    /**
     * TODO: Definición de {@code AndroidGenericImageFactory}.
     *
     * @param context es el contexto de la fábrica de imágenes genéricas.
     * @throws java.lang.Exception
     */
    public AndroidGenericImageFactory(Context context) throws Exception {
        this.context = context;
    }

    /**
     * FIXME: Definición de {@code makeGenericImage}. Fabrica una imagen genérica con solo la ruta.
     *
     * @param path es la ruta de la imagen.
     * @return una imagen genérica.
     * @throws java.lang.Exception
     */
    @Override
    public GenericImage makeGenericImage(String path) throws Exception {
        return new AndroidGenericImage(context, path);
    }
}
