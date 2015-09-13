package com.alorma.githubintegration.mapper.repo;

import android.support.annotation.NonNull;

import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.githubintegration.mapper.user.UserMapper;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.bean.dto.GitskariosPermissions;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;

/**
 * Created by a557114 on 08/09/2015.
 */
public class RepositoryMapper implements BaseMapper<Repo, GitskariosRepository> {
    @NonNull
    @Override
    public GitskariosRepository toCore(@NonNull Repo repo) {
        GitskariosRepository gitskariosRepository = new GitskariosRepository();

        gitskariosRepository.owner = new UserMapper().toCore(repo.owner);
        gitskariosRepository.owner_id = repo.owner.id;
        gitskariosRepository.name = repo.name;
        gitskariosRepository.isPrivate = repo.isPrivate;
        gitskariosRepository.description = repo.description;
        gitskariosRepository.forks_count = repo.forks_count;
        gitskariosRepository.stargazers_count = repo.stargazers_count;
        gitskariosRepository.subscribers_count = repo.subscribers_count;
        gitskariosRepository.html_url = repo.html_url;
        gitskariosRepository.default_branch = repo.default_branch;
        gitskariosRepository.external_url = repo.svn_url;
        gitskariosRepository.has_downloads = repo.has_downloads;
        gitskariosRepository.has_wiki = repo.has_wiki;
        gitskariosRepository.has_issues = repo.has_issues;
        gitskariosRepository.has_merge_request = repo.has_issues;
        gitskariosRepository.homepage = repo.homepage;
        gitskariosRepository.created_at = repo.created_at;
        if ((repo.parent != null)) {
            gitskariosRepository.parent = toCore(repo.parent);
        }

        if (repo.permissions != null) {
            gitskariosRepository.gitskariosPermissions = new GitskariosPermissions();
            gitskariosRepository.gitskariosPermissions.admin = repo.permissions.admin;
            gitskariosRepository.gitskariosPermissions.push = repo.permissions.push;
            gitskariosRepository.gitskariosPermissions.pull = repo.permissions.pull;
        }
        return gitskariosRepository;
    }
}
