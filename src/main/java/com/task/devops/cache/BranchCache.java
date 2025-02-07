package com.task.devops.cache;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BranchCache {

    private Map<String, Map<String, Integer>> totalCommitsCache = new HashMap<>();

    public Map<String, Map<String, Integer>> getTotalCommitsCache() {
        return totalCommitsCache;
    }

    public void setTotalCommitsCache(Map<String, Map<String, Integer>> totalCommitsCache) {
        this.totalCommitsCache = totalCommitsCache;
    }

    public boolean isAlreadyCached(String repoUrl, String branchName) {
        Map<String, Integer> commitsMap = this.getTotalCommitsCache().get(repoUrl);
        if(commitsMap != null && !commitsMap.isEmpty()) {
            int commitCount = commitsMap.get(branchName);
            return commitCount > 0;
        }
        return false;
    }
}
