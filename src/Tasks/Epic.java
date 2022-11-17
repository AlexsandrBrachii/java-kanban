package Tasks;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {

    private ArrayList<Integer> idSubTask;

    public Epic() {
    }

    public Epic(String name, String description, String status, Integer id, ArrayList<Integer> idSubTask) {
        super(name, description, status, id);
        this.idSubTask = idSubTask;
    }

    public Epic(String name, String description, String status, ArrayList<Integer> idSubTask) {
        super(name, description, status);
        this.idSubTask = idSubTask;
    }

    public Epic(String name, String description, String status) {
        super(name, description, status);
    }

    public ArrayList<Integer> getIdSubTask() {
        return idSubTask;
    }

    public void setIdSubTask(ArrayList<Integer> idSubTask) {
        this.idSubTask = idSubTask;
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

