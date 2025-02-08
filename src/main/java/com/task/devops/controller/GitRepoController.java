package com.task.devops.controller;

import com.task.devops.client.GitClient;
import com.task.devops.dto.commit.CommitDto;
import com.task.devops.service.GitRepoService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
public class GitRepoController {

    private static final Logger log = LoggerFactory.getLogger(GitRepoController.class);

    private final GitClient gitClient;
    private final GitRepoService gitRepoService;

    public GitRepoController(GitClient gitClient, GitRepoService gitRepoService) {
        this.gitClient = gitClient;
        this.gitRepoService = gitRepoService;
    }

    @GetMapping("/branches")
    public String getBranches(Model model) {
        model.addAttribute("branches", gitClient.getBranches());
        return "branches";
    }

    @GetMapping("/commits")
    public String getCommits(@RequestParam(value = "branchName") String branchName,
                             @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                             @RequestParam(value = "per_page", required = false, defaultValue = "30") Integer size,
                             Model model) {

        gitRepoService.setCommitCache(branchName);

        int totalCommitsPage = gitRepoService.getTotalCommitsPagePerBranch(branchName);
        int totalCommits = totalCommitsPage * 100;
        log.info("Total Max Commits at branch : {} is : {}", branchName, totalCommits);

        int totalPages = (int) Math.ceil((double) (totalCommits) / size);
        log.info("Total Pages at branch : {} is : {}", branchName, totalPages);

        List<CommitDto> allCommitsPerPage = gitClient.getCommits(branchName, page, size);

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<CommitDto> commitPage = new PageImpl<>(allCommitsPerPage, pageable, totalCommits);

        model.addAttribute("commits", commitPage.getContent());
        model.addAttribute("page", commitPage);
        model.addAttribute("branchName", branchName);

        return "commits";
    }
}
