package com.task.devops.dto.commit;

public record AuthorDto(
        String name,
        String email,
        String date
) { }
