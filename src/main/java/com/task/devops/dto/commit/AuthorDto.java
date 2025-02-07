package com.task.devops.dto.commit;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public record AuthorDto(
        String name,
        String email,
        String date
) {
    public String getFormattedDate() {
        if (date == null || date.isEmpty()) {
            return "";
        }
        ZonedDateTime zdt = ZonedDateTime.parse(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy 'at' hh:mm:ss a");
        return zdt.format(formatter)
                .replaceFirst("1 ", "1st ")
                .replaceFirst("2 ", "2nd ")
                .replaceFirst("3 ", "3rd ");
    }
}
