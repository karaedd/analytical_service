package com.kraievskyi.service.impl;

import com.kraievskyi.model.DataLine;
import com.kraievskyi.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileServiceImplTest {
    private static final String FILE_NAME = "src/main/resources/example.txt";
    private FileService fileService;
    private List<List<DataLine>> expectedData;

    @BeforeEach
    public void setUp() {
        fileService = new FileServiceImpl();
        expectedData = new ArrayList<>();
        expectedData.add(Arrays.asList(
                new DataLine("1", "1", "8", "", "", "P",
                        LocalDate.parse("2012-01-01"),
                        LocalDate.parse("2012-12-01"), 0L),
                new DataLine("1", "", "", "", "", "P",
                        LocalDate.parse("2012-10-08"),
                        LocalDate.parse("2012-11-20"), 0L),
                new DataLine("3", "", "10", "", "", "P",
                        LocalDate.parse("2012-12-01"),
                        LocalDate.parse("2012-12-01"), 0L)));
        expectedData.add(Arrays.asList(
                new DataLine("1", "1", "8", "15", "1", "P",
                        LocalDate.parse("2012-10-15"),
                        LocalDate.parse("2012-10-15"), 83L),
                new DataLine("1", "", "10", "1", "", "P",
                        LocalDate.parse("2012-12-01"),
                        LocalDate.parse("2012-12-01"), 65L),
                new DataLine("1", "1", "5", "5", "1", "P",
                        LocalDate.parse("2012-11-01"),
                        LocalDate.parse("2012-11-01"), 117L),
                new DataLine("3", "", "10", "2", "", "N",
                        LocalDate.parse("2012-10-02"),
                        LocalDate.parse("2012-10-02"), 100L)));
    }

    @Test
    void testReadDataFile() {
        List<List<DataLine>> actualData = fileService.readDataFile(FILE_NAME);
        assertEquals(expectedData, actualData);
    }

    @Test
    void testProcessQueries() {
        List<String> expectedResults = Arrays.asList("83", "100", "-");
        List<String> actualResults = fileService.processQueries(expectedData);
        assertEquals(expectedResults, actualResults);
    }
}
