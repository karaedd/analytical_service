package com.kraievskyi.service.impl;

import com.kraievskyi.model.DataLine;
import com.kraievskyi.service.FileService;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FileServiceImpl implements FileService {
    public List<List<DataLine>> readDataFile(String fileName) {
        List<DataLine> data = new ArrayList<>();
        List<DataLine> queries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split(EMPTY_LINE);
                String type = parts[0];
                if (type.equals(TYPE_C)) {
                    data.add(parseDataLine(parts));
                }
                if (type.equals(TYPE_D)) {
                    queries.add(parseDataLine(parts));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Arrays.asList(queries, data);
    }

    private DataLine parseDataLine(String[] parts) {
        String serviceId;
        String variationId = "";
        String questionTypeId = "";
        String categoryId = "";
        String subcategoryId = "";
        LocalDate startDate;
        LocalDate endDate;
        long time = 0;

        int dotIndex = parts[1].indexOf(DOT);
        if (dotIndex != -1) {
            serviceId = parts[1].substring(0, dotIndex);
            variationId = parts[1].substring(dotIndex + 1);
        } else {
            serviceId = parts[1];
        }

        if (!STAR.equals(parts[2])) {
            dotIndex = parts[2].indexOf(DOT);
            if (dotIndex != -1) {
                questionTypeId = parts[2].substring(0, dotIndex);
                categoryId = parts[2].substring(dotIndex + 1);
                dotIndex = categoryId.indexOf(DOT);
                if (dotIndex != -1) {
                    subcategoryId = categoryId.substring(dotIndex + 1);
                    categoryId = categoryId.substring(0, dotIndex);
                }
            } else {
                questionTypeId = parts[2];
            }
        }

        if (parts[4].contains(HYPHEN)) {
            String[] splitStartEndDate = parts[4].split(HYPHEN);
            startDate = LocalDate.parse(splitStartEndDate[0], DATE_FORMATTER);
            endDate = LocalDate.parse(splitStartEndDate[1], DATE_FORMATTER);
        } else {
            startDate = LocalDate.parse(parts[4], DATE_FORMATTER);
            endDate = LocalDate.parse(parts[4], DATE_FORMATTER);
        }

        if (TYPE_C.equals(parts[0])) {
            time = Long.parseLong(parts[5]);
        }

        return DataLine.builder()
                .serviceId(serviceId)
                .variationId(variationId)
                .questionTypeId(questionTypeId)
                .categoryId(categoryId)
                .subCategoryId(subcategoryId)
                .responseType(parts[3])
                .dateFrom(startDate)
                .dateTo(endDate)
                .time(time).build();
    }

    public List<String> processQueries(List<List<DataLine>> objects) {
        List<String> results = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
        List<Future<String>> futures = new ArrayList<>();
        List<DataLine> queryList = objects.get(0);
        List<DataLine> dataList = objects.get(1);
        for (DataLine query : queryList) {
            Future<String> future = executorService.submit(() -> {
                long average = Math.round(dataList.stream()
                        .filter(line -> line.getDateFrom().compareTo(query.getDateFrom()) >= 0
                                && line.getDateFrom().compareTo(query.getDateTo()) <= 0)
                        .filter(line -> line.getResponseType().equals(query.getResponseType()))
                        .filter(line -> Objects.equals(query.getQuestionTypeId(), "")
                                || Objects.equals(query.getQuestionTypeId(),
                                line.getQuestionTypeId()))
                        .filter(line -> Objects.equals(query.getServiceId(), "")
                                || Objects.equals(query.getServiceId(), line.getServiceId()))
                        .mapToLong(DataLine::getTime)
                        .average()
                        .orElse(-1));
                return average < 0 ? HYPHEN : Long.toString(average);
            });
            futures.add(future);
        }
        for (Future<String> future : futures) {
            try {
                results.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
        return results;
    }
}
