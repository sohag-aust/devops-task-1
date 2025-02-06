package com.task.devops.dto.commit;

public record CommitDetailsDto(
         AuthorDto author,
         String message
) { }
