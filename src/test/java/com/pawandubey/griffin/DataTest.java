package com.pawandubey.griffin;

import com.pawandubey.griffin.model.Parsable;
import com.pawandubey.griffin.model.Post;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class DataTest {
    @Ignore @Test
    public void getLatestPosts() {
        Set<Parsable> posts = createPosts();
        Data.latestPosts.addAll(posts);

        List<String> expectedDates = Arrays.asList("2020-06-09", "2020-06-05", "2020-06-02");

        List<String> datesFromData = Data.datum.getLatestPosts()
                .stream()
                .map(Parsable::getDate)
                .map(localDate -> localDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .collect(Collectors.toList());

        assertEquals(expectedDates, datesFromData);
    }

    private Set<Parsable> createPosts() {
        List<String> publishDates = Arrays.asList("2020-06-05", "2020-06-02", "2020-06-09");
        return publishDates.stream().map(s -> new Post(
                "Title",
                "",
                LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE),
                Paths.get(""),
                "",
                "",
                "",
                "",
                Collections.emptyList()
        )).collect(Collectors.toSet());
    }
}