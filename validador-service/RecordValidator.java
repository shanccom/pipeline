import java.util.ArrayList;
import java.util.List;

public class RecordValidator {
    public AcademicRecord validate(AcademicRecord record) {
        List<String> errors = new ArrayList<>();

        if (isBlank(record.getId())) {
            errors.add("id vacío");
        }
        if (isBlank(record.getName())) {
            errors.add("nombre vacío");
        }
        if (isBlank(record.getCourse())) {
            errors.add("curso vacío");
        }
        if (Double.isNaN(record.getAverage()) || record.getAverage() < 0 || record.getAverage() > 20) {
            errors.add("promedio fuera de rango");
        }

        if (errors.isEmpty()) {
            record.setValid(true);
            record.setError("");
        } else {
            record.setValid(false);
            record.setError(String.join("; ", errors));
        }

        return record;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
