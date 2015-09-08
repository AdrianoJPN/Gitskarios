package com.alorma.gitskarios.core;

import android.support.annotation.NonNull;

/**
 * Created by a557114 on 08/09/2015.
 */
public interface BaseMapper<K, T> {

    @NonNull
    T toCore(@NonNull K k);

}
