/*
 * @fileoverview {FileName} se encarga de realizar tareas especificas.
 *
 * @version             1.0
 *
 * @author              Dyson Arley Parra Tilano <dysontilano@gmail.com>
 * Copyright (C) Dyson Parra
 *
 * @History v1.0 --- La implementacion de {FileName} fue realizada el 31/07/2022.
 * @Dev - La primera version de {FileName} fue escrita por Dyson A. Parra T.
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
 * TODO: Definición de {@code AndroidGenericImage}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
//@AllArgsConstructor
//@Builder
@Data
//@NoArgsConstructor
public class AndroidGenericImage extends GenericImage {

    private Context context = null;                                                 // Contexto de la imagen.

    /**
     * TODO: Definición de {@code AndroidGenericImage}.
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
     * FIXME: Definición de {@code pathChanged}. Invocado cuando cambia la ruta de la imagen.
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
     * FIXME: Definición de {@code onPathChange}. Invocado cuando se cambia la ruta de la imagen
     * genérica.
     *
     * @throws Exception
     */
    @Override
    public void onPathChange() throws Exception {
        pathChanged();
    }

    /**
     * FIXME: Definición de {@code onWidthChange}. Invocado cuando se cambia el ancho de la imagen
     * genérica.
     */
    @Override
    public void onWidthChange() {

    }

    /**
     * FIXME: Definición de {@code onHeightChange}. Invocado cuando se cambia el lto de la imagen
     * genérica.
     */
    @Override
    public void onHeightChange() {

    }

    /**
     * FIXME: Definición de {@code onPositionXChange}. Invocado cuando se cambian las coordenadas en
     * X de la imagen genérica.
     */
    @Override
    public void onPositionXChange() {

    }

    /**
     * FIXME: Definición de {@code onPositionYChange}. Invocado cuando se cambian las coordenadas en
     * Y de la imagen genérica.
     */
    @Override
    public void onPositionYChange() {

    }
}
