public class RecordTransformer {
    public AcademicRecord transform(AcademicRecord record) {
        if (!record.isValid()) {
            record.setStatus("ERROR");
            return record;
        }

        if (record.getAverage() >= Constants.PASSING_AVERAGE) {
            record.setStatus("APROBADO");
        } else {
            record.setStatus("DESAPROBADO");
        }
        return record;
    }
}
