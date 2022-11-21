package tasks;

public class Task {

    private String name;
    private String description;
    private String status;
    private Integer id;

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(Integer id, String name, String description, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Task{" +
                ", id='" + id + '\'' +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

