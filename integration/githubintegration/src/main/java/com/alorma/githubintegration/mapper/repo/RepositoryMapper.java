package com.alorma.githubintegration.mapper.repo;

import android.support.annotation.NonNull;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

/**
 * Created by a557114 on 08/09/2015.
 */
public class RepositoryMapper implements BaseMapper<Repo, GitskariosRepository> {
    @NonNull
    @Override
    public GitskariosRepository toCore(@NonNull Repo repo) {
        GitskariosRepository gitskariosRepository = new GitskariosRepository();

        gitskariosRepository.owner = repo.owner.login;
        gitskariosRepository.name = repo.name;
        gitskariosRepository.isPrivate = repo.isPrivate;
        gitskariosRepository.description = repo.description;
        gitskariosRepository.forks_count = repo.forks_count;
        gitskariosRepository.stargazers_count = repo.stargazers_count;
        gitskariosRepository.html_url = repo.html_url;

        return gitskariosRepository;
    }
}
