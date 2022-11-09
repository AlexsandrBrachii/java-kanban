
public class Task {


    String name;            // название
    String description;     // описание
    String status;          // статус


    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }


    public Task() {
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
