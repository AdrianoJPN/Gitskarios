package com.alorma.githubintegration.mapper.repo.list;

import android.support.annotation.NonNull;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.githubintegration.mapper.repo.RepositoryMapper;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a557114 on 08/09/2015.
 */
public class ListRepositoryMapper implements BaseMapper<List<Repo>, List<GitskariosRepository>> {

    private final RepositoryMapper repoMapper;

    public ListRepositoryMapper() {
        repoMapper = new RepositoryMapper();
    }

    @NonNull
    @Override
    public List<GitskariosRepository> toCore(@NonNull List<Repo> repos) {
        List<GitskariosRepository> repositories = new ArrayList<>(repos.size());
        for (Repo repo : repos) {
            repositories.add(repoMapper.toCore(repo));
        }
        return repositories;
    }
}
