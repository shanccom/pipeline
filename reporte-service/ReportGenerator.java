import java.util.concurrent.atomic.AtomicInteger;

public class ReportGenerator {
    private final AtomicInteger approved = new AtomicInteger();
    private final AtomicInteger failed = new AtomicInteger();
    private final AtomicInteger errors = new AtomicInteger();

    public ReportStats addRecord(AcademicRecord record) {
        if (!record.isValid()) {
            errors.incrementAndGet();
        } else if ("APROBADO".equalsIgnoreCase(record.getStatus())) {
            approved.incrementAndGet();
        } else if ("DESAPROBADO".equalsIgnoreCase(record.getStatus())) {
            failed.incrementAndGet();
        } else {
            errors.incrementAndGet();
        }
        return snapshot();
    }

    public ReportStats snapshot() {
        return new ReportStats(approved.get(), failed.get(), errors.get());
    }

    public static class ReportStats {
        private final int approved;
        private final int failed;
        private final int errors;

        public ReportStats(int approved, int failed, int errors) {
            this.approved = approved;
            this.failed = failed;
            this.errors = errors;
        }

        public int getApproved() {
            return approved;
        }

        public int getFailed() {
            return failed;
        }

        public int getErrors() {
            return errors;
        }
    }
}
