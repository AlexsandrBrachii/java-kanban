

public class SubTask {

    String name;            // название
    String description;     // описание
    String status;          // статус
    Integer id;

    public SubTask() {
    }

    public SubTask(String name, String description, String status, Integer id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    @Override
    public String toString() {
        return ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", idEpic='" + id + '\'' +
                '}';
    }
}
