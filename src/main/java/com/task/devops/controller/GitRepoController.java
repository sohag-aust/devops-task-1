package com.task.devops.controller;

import com.task.devops.cache.BranchCache;
import com.task.devops.client.GitClient;
import com.task.devops.dto.branch.BranchDto;
import com.task.devops.dto.commit.CommitDto;
import com.task.devops.service.GitRepoService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j
@Controller
public class GitRepoController {

    @Value("${git.repo.url}")
    private String repoUrl;

    private static final Logger log = LoggerFactory.getLogger(GitRepoController.class);

    private final GitClient gitClient;
    private final GitRepoService gitRepoService;
    private final BranchCache branchCache;

    public GitRepoController(GitClient gitClient, GitRepoService gitRepoService, BranchCache branchCache) {
        this.gitClient = gitClient;
        this.gitRepoService = gitRepoService;
        this.branchCache = branchCache;
    }

    @GetMapping("/branches")
    public String getBranches(Model model) {
        model.addAttribute("branches", gitClient.getBranches());
        return "branches";
    }

    @GetMapping("/commits/{branch}")
    public String getCommits(@PathVariable String branch,
                             @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                             @RequestParam(value = "per_page", required = false, defaultValue = "30") Integer size,
                             Model model) {

        if(!branchCache.isAlreadyCached(repoUrl, branch)) {
            log.info("==== Cache not found, Start Caching for branch :: {}", branch);
            gitRepoService.setCommitCache(branch);
        }

        int totalCommitsPage = gitRepoService.getTotalCommitsPagePerBranch(branch);
        int totalCommits = totalCommitsPage * 100;
        log.info("Total Max Commits at branch : {} is : {}", branch, totalCommits);

        int totalPages = (int) Math.ceil((double) (totalCommits) / size);
        log.info("Total Pages at branch : {} is : {}", branch, totalPages);

        int start = (page - 1) * size;
        int end = Math.min(start + size, totalCommits);

        log.info("Page : {}", page);
        log.info("Size : {}", size);
        List<CommitDto> allCommitsPerPage = gitClient.getCommits(branch, page, size);

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<CommitDto> commitPage = new PageImpl<>(allCommitsPerPage, pageable, totalCommits);

        model.addAttribute("commits", commitPage.getContent());
        model.addAttribute("page", commitPage);
        model.addAttribute("branchName", branch);

        return "commits";
    }
}
