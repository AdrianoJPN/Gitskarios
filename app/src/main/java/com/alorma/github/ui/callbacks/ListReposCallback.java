package com.alorma.github.ui.callbacks;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.githubintegration.mapper.repo.list.ListRepositoryMapper;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;
import com.alorma.gitskarios.core.client.BaseClient;

import java.util.List;

/**
 * Created by a557114 on 08/09/2015.
 */
public class ListReposCallback extends TransitionalCallback<List<Repo>, List<GitskariosRepository>> {

    public ListReposCallback(BaseClient.OnResultCallback<List<GitskariosRepository>> callback) {
        super(callback);
    }

    @Override
    protected BaseMapper<List<Repo>, List<GitskariosRepository>> geMapper() {
       return new ListRepositoryMapper();
    }
}
