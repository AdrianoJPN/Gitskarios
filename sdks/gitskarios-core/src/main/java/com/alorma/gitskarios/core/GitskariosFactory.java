package com.alorma.gitskarios.core;

public interface GitskariosFactory<K, Z> {

    BaseDataSource<K, Z> create();

}
