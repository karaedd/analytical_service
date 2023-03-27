package com.kraievskyi.service;

import com.kraievskyi.model.DataLine;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

public interface FileService {

    DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder()
            .appendOptional(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            .appendOptional(DateTimeFormatter.ofPattern("d.MM.yyyy"))
            .toFormatter();
    String DOT = ".";
    String HYPHEN = "-";
    String TYPE_C = "C";
    String TYPE_D = "D";
    String STAR = "*";
    String EMPTY_LINE = " ";

    List<List<DataLine>> readDataFile(String fileName);

    List<String> processQueries(List<List<DataLine>> objects);
}
