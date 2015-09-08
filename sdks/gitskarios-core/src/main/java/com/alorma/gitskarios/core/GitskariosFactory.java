package com.alorma.gitskarios.core;

import com.alorma.gitskarios.core.client.BaseClient;

/**
 * Created by a557114 on 08/09/2015.
 */
public interface GitskariosFactory<T extends BaseClient<K>, K, Z> {

    BaseDataSource<T, K, Z> create();

}
