package com.task.devops.dto.commit;

public record CommitDto(
        String sha,
        CommitDetailsDto commit
) { }
