package com.alorma.gitlabintegration.mapper;

import android.support.annotation.NonNull;

import com.alorma.gitlabsdk.security.bean.GitlabProject;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a557114 on 09/09/2015.
 */
public class ListReposMapper implements BaseMapper<List<GitlabProject>, List<GitskariosRepository>> {

    private final RepoMapper repoMapper;

    public ListReposMapper() {
        repoMapper = new RepoMapper();
    }

    @NonNull
    @Override
    public List<GitskariosRepository> toCore(@NonNull List<GitlabProject> gitlabProjects) {
        List<GitskariosRepository> repositories = new ArrayList<>(gitlabProjects.size());

        for (GitlabProject gitlabProject : gitlabProjects) {
            repositories.add(repoMapper.toCore(gitlabProject));
        }

        return repositories;
    }
}
