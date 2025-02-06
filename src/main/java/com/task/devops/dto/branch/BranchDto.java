package com.task.devops.dto.branch;

public record BranchDto(
        String name,
        LatestCommitDto commit
) { }
