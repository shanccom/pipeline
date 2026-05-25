import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
    public List<AcademicRecord> readAll() throws IOException {
        Path csvPath = resolveCsvPath();
        if (Files.exists(csvPath)) {
            return readFromPath(csvPath);
        }
        // Fallback: intenta leer el CSV desde el classpath si se empaqueta.
        return readFromClasspath();
    }

    private List<AcademicRecord> readFromPath(Path path) throws IOException {
        List<AcademicRecord> records = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                AcademicRecord record = parseLine(line);
                records.add(record);
            }
        }
        return records;
    }

    private List<AcademicRecord> readFromClasspath() throws IOException {
        List<AcademicRecord> records = new ArrayList<>();
        try (InputStream input = CsvReader.class.getResourceAsStream("/academic_records.csv")) {
            if (input == null) {
                return records;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
                String line;
                boolean isHeader = true;
                while ((line = reader.readLine()) != null) {
                    if (isHeader) {
                        isHeader = false;
                        continue;
                    }
                    AcademicRecord record = parseLine(line);
                    records.add(record);
                }
            }
        }
        return records;
    }

    private AcademicRecord parseLine(String line) {
        String[] parts = line.split(",", -1);
        String id = parts.length > 0 ? parts[0].trim() : "";
        String name = parts.length > 1 ? parts[1].trim() : "";
        String course = parts.length > 2 ? parts[2].trim() : "";
        double average = parseAverage(parts.length > 3 ? parts[3].trim() : "");
        return new AcademicRecord(id, name, course, average);
    }

    private double parseAverage(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            return Double.NaN;
        }
    }

    private Path resolveCsvPath() {
        Path current = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
        for (int i = 0; i < 4; i++) {
            Path candidate = current.resolve(Constants.DATA_FILE);
            if (Files.exists(candidate)) {
                return candidate;
            }
            current = current.getParent();
            if (current == null) {
                break;
            }
        }
        return Paths.get(Constants.DATA_FILE);
    }
}
