import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class JsonUtils {
    private static final Pattern SIMPLE_JSON_PATTERN = Pattern.compile(
            "\"(\\w+)\"\\s*:\\s*(\"(.*?)\"|true|false|null|-?\\d+(?:\\.\\d+)?)");

    private JsonUtils() {
    }

    // Conversión simple para uso educativo (no reemplaza un parser JSON real).
    public static String toJson(AcademicRecord record) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"id\":\"").append(escape(record.getId())).append("\",");
        sb.append("\"name\":\"").append(escape(record.getName())).append("\",");
        sb.append("\"course\":\"").append(escape(record.getCourse())).append("\",");
        sb.append("\"average\":").append(record.getAverage()).append(",");
        sb.append("\"status\":\"").append(escape(record.getStatus())).append("\",");
        sb.append("\"valid\":").append(record.isValid()).append(",");
        sb.append("\"error\":\"").append(escape(record.getError())).append("\"");
        sb.append("}");
        return sb.toString();
    }

    public static AcademicRecord fromJson(String json) {
        AcademicRecord record = new AcademicRecord();
        if (json == null || json.trim().isEmpty()) {
            record.setValid(false);
            record.setError("JSON vacío");
            return record;
        }

        Map<String, String> map = parseSimpleJson(json);
        record.setId(map.getOrDefault("id", ""));
        record.setName(map.getOrDefault("name", ""));
        record.setCourse(map.getOrDefault("course", ""));
        record.setStatus(map.getOrDefault("status", ""));
        record.setError(map.getOrDefault("error", ""));

        String averageValue = map.get("average");
        record.setAverage(parseDouble(averageValue));

        String validValue = map.get("valid");
        record.setValid(validValue == null || Boolean.parseBoolean(validValue));

        return record;
    }

    private static Map<String, String> parseSimpleJson(String json) {
        Map<String, String> result = new HashMap<>();
        Matcher matcher = SIMPLE_JSON_PATTERN.matcher(json);
        while (matcher.find()) {
            String key = matcher.group(1);
            String rawValue = matcher.group(2);
            String value;
            if (rawValue.startsWith("\"")) {
                value = unescape(matcher.group(3));
            } else {
                value = rawValue;
            }
            result.put(key, value);
        }
        return result;
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String unescape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\\"", "\"").replace("\\\\", "\\");
    }

    private static double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return Double.NaN;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            return Double.NaN;
        }
    }
}
