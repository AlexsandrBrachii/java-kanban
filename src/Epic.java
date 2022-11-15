import java.util.ArrayList;
import java.util.HashMap;

public class Epic {

    private String name;            // название
    private String description;     // описание
    private String status;          // статус
    ArrayList<Integer> idSubTask;

    public Epic() {
    }

    public Epic(String name, String description, String status, ArrayList<Integer> idSubTask) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.idSubTask = idSubTask;
    }


    public Epic(String name, String description, String status) {
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

    @Override
    public String toString() {
        return "Epic{" +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status='" + this.getStatus() + '\'' +
                ", idSubTask='" + idSubTask + '\'' +
                '}';
    }
}
