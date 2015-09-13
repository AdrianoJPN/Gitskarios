package com.alorma.gitlabintegration.mapper;

import android.support.annotation.NonNull;

import com.alorma.gitlabsdk.security.bean.GitlabProject;
import com.alorma.gitskarios.core.BaseMapper;
import com.alorma.gitskarios.core.bean.dto.GitskariosRepository;
import com.alorma.gitskarios.core.bean.dto.GitskariosUser;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by a557114 on 09/09/2015.
 */
public class RepoMapper implements BaseMapper<GitlabProject, GitskariosRepository> {
    @NonNull
    @Override
    public GitskariosRepository toCore(@NonNull GitlabProject gitlabProject) {
        GitskariosRepository gitskariosRepository = new GitskariosRepository();

        gitskariosRepository.id = gitlabProject.id;
        gitskariosRepository.full_name = gitlabProject.path_with_namespace;
        gitskariosRepository.default_branch = gitlabProject.default_branch;
        if (gitlabProject.owner != null) {
            gitskariosRepository.owner_id = gitlabProject.owner.id;
        } else if (gitlabProject.namespace != null) {
            gitskariosRepository.owner = new GitskariosUser();
            gitskariosRepository.owner.id = gitlabProject.namespace.owner_id;
            gitskariosRepository.owner.login = gitlabProject.namespace.owner;
        } else {
            gitskariosRepository.owner = new GitskariosUser();
            gitskariosRepository.owner.login = gitskariosRepository.full_name.split("/")[0];
        }
        if (gitskariosRepository.owner == null || gitskariosRepository.owner.login == null) {
            gitskariosRepository.owner = new GitskariosUser();
            gitskariosRepository.owner.login = gitskariosRepository.full_name.split("/")[0];
        }
        gitskariosRepository.name = gitskariosRepository.full_name.split("/")[1];
        gitskariosRepository.description = gitlabProject.description;
        gitskariosRepository.isPrivate = gitlabProject.visibility_level == 0 || gitlabProject.visibility_level == 10;
        gitskariosRepository.external_url = gitlabProject.web_url;
        gitskariosRepository.html_url = gitlabProject.web_url;
        gitskariosRepository.has_issues = gitlabProject.issues_enabled;
        gitskariosRepository.has_wiki = gitlabProject.wiki_enabled;
        gitskariosRepository.has_merge_request = gitlabProject.merge_requests_enabled;
//        gitskariosRepository.created_at = gitlabProject.created_at;
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        gitskariosRepository.created_at = dateTimeFormatter.parseDateTime(gitlabProject.created_at).toDate();
        return gitskariosRepository;
    }
}
