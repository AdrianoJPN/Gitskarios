package com.alorma.gitskarios.core;

import android.support.annotation.NonNull;

/**
 * Created by a557114 on 08/09/2015.
 */
public abstract class BaseMapper<K, T> {

    @NonNull
    public abstract T toCore(@NonNull K k);

}
