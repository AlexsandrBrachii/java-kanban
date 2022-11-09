import java.util.ArrayList;
import java.util.HashMap;

public class Epic {


    ArrayList<Integer> idSubTask;

    String name;            // название
    String description;     // описание
    String status;          // статус


    public Epic() {
    }

    ;

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

    @Override
    public String toString() {
        return "Epic{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", idSubTask='" + idSubTask + '\'' +
                '}';
    }
}
