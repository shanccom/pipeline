public class AcademicRecord {
    private String id;
    private String name;
    private String course;
    private double average;
    private String status;
    private boolean valid;
    private String error;

    public AcademicRecord() {
        this.status = "";
        this.valid = true;
        this.error = "";
    }

    public AcademicRecord(String id, String name, String course, double average) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.average = average;
        this.status = "";
        this.valid = true;
        this.error = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "AcademicRecord{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", course='" + course + '\'' +
                ", average=" + average +
                ", status='" + status + '\'' +
                ", valid=" + valid +
                ", error='" + error + '\'' +
                '}';
    }
}
