package in.ravir;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AdventOfCode {

    public static void main(String[] args) throws IOException {
        LocalDate startDate = LocalDate.of(2024, 12, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 26);
        LocalDate currentDate = LocalDate.now();

        if (isDateInRange(currentDate, startDate, endDate)) {
            // run today's code by getting the date from the system
            var today = java.time.LocalDate.now().getDayOfMonth();
            runCode(today);
        } else {
            // run all day codes
            log.info("AOC 2024 ended or not started yet, running all days");
            for (int i = 1; i <= 25; i++) {
                runCode(i);
            }
        }
    }

    private static void runCode(int today) {
        String className = String.format("in.ravir.day%02d.Day%02d", today, today);

        try {
            long startTime = System.currentTimeMillis();
            Class<?> clazz = Class.forName(className);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            clazz.getMethod("setInputLines", List.class).invoke(instance, getInputLines(today));
            clazz.getMethod("part1").invoke(instance);
            clazz.getMethod("part2").invoke(instance);
            long endTime = System.currentTimeMillis();
            log.info("Time taken: {}ms", endTime - startTime);
        } catch (Exception e) {
            log.warn("No code for today {}", today, e);
        }
    }

    private static boolean isDateInRange(LocalDate currentDate, LocalDate startDate, LocalDate endDate) {
        return !currentDate.isBefore(startDate) && !currentDate.isAfter(endDate);
    }

    private static List<String> getInputLines(int day) throws IOException {
        String filePath = String.format("inputs/day%02d.txt", day);
        try (var inputStream = AdventOfCode.class.getClassLoader().getResourceAsStream(filePath)) {
            assert inputStream != null;
            try (var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.toList());
            }
        }
    }
}
