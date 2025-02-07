package com.task.devops.service;

import com.task.devops.cache.BranchCache;
import com.task.devops.client.GitClient;
import com.task.devops.dto.commit.CommitDto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GitRepoService {

    @Value("${git.repo.url}")
    private String repoUrl;

    private static final Logger log = LoggerFactory.getLogger(GitRepoService.class);

    private final GitClient gitClient;
    private final BranchCache branchCache;

    public GitRepoService(GitClient gitClient, BranchCache branchCache) {
        this.gitClient = gitClient;
        this.branchCache = branchCache;
    }

    public void setCommitCache(String branch) {
        if(branchCache.isAlreadyCached(repoUrl, branch)) {
            return ;
        }

        log.info("==== Cache not found, Start Caching for branch :: {}", branch);

        int initPage = 1;
        int initPerPage = 100;

        List<CommitDto> commits;
        do {
            commits = gitClient.getCommits(branch, initPage, initPerPage);
            initPage++;
        } while (!commits.isEmpty());

        Map<String, Map<String, Integer>> totalCommitsCachePerRepo = new HashMap<>();
        Map<String, Integer> totalCommitsPerBranch = new HashMap<>();

        totalCommitsPerBranch.put(branch, --initPage);
        totalCommitsCachePerRepo.put(repoUrl, totalCommitsPerBranch);
        branchCache.setTotalCommitsCache(totalCommitsCachePerRepo);

        log.info("Branch Cache : {}", branchCache.getTotalCommitsCache().get(repoUrl).toString());
    }

    public int getTotalCommitsPagePerBranch(String branchName) {
        Map<String, Integer> commitsPerBranchMap = branchCache.getTotalCommitsCache().get(repoUrl);
        return commitsPerBranchMap.get(branchName) - 1;
    }
}
