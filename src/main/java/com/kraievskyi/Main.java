package com.kraievskyi;

import com.kraievskyi.service.FileService;
import com.kraievskyi.service.impl.FileServiceImpl;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String filename = "src/main/resources/example.txt";
        FileService fileService = new FileServiceImpl();
        List<String> results = fileService.processQueries(fileService.readDataFile(filename));
        results.forEach(System.out::println);
    }
}
