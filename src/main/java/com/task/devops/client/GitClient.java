package com.task.devops.client;

import com.task.devops.dto.branch.BranchDto;
import com.task.devops.dto.commit.CommitDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "gitClient", url = "${git.repo.url}")
public interface GitClient {

    /** Fetching All Details */
//    @GetMapping("/branches")
//    List<Map<String, Object>> getBranches();
//
//    @GetMapping("/commits?sha={branch}")
//    List<Map<String, Object>> getCommits(@PathVariable("branch") String branch);

    @GetMapping("/branches")
    List<BranchDto> getBranches();

    @GetMapping("/commits")
    List<CommitDto> getCommits(@RequestParam(value = "sha") String branch,
                               @RequestParam(value = "page", required = false) Integer page,
                               @RequestParam(value = "per_page", required = false) Integer perPage);
}

