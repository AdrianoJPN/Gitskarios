package com.alorma.gitlabintegration.mapper;

import android.support.annotation.NonNull;

import com.alorma.gitlabsdk.security.bean.GitlabProject;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

/**
 * Created by a557114 on 09/09/2015.
 */
public class RepoMapper implements BaseMapper<GitlabProject,GitskariosRepository> {
    @NonNull
    @Override
    public GitskariosRepository toCore(@NonNull GitlabProject gitlabProject) {
        GitskariosRepository gitskariosRepository = new GitskariosRepository();

        gitskariosRepository.id = gitlabProject.id;
        gitskariosRepository.full_name = gitlabProject.path_with_namespace;
        gitskariosRepository.owner = gitskariosRepository.full_name.split("/")[0];
        gitskariosRepository.name = gitskariosRepository.full_name.split("/")[1];
        gitskariosRepository.description = gitlabProject.description;
        gitskariosRepository.isPrivate = gitlabProject.visibility_level == 0 || gitlabProject.visibility_level == 10;

        return gitskariosRepository;
    }
}
