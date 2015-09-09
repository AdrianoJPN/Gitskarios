package com.alorma.gitskarios.core;

public interface GitskariosFactory<Z> {

    BaseDataSource<Z> create();

    GitskariosFactory<Z> setApiConnection(ApiConnection apiConnection);

}
