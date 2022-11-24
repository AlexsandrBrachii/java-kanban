package tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> idSubTask;

    public Epic(Integer id, String name, String description, Status status, ArrayList<Integer> idSubTask) {
        super(id, name, description, status);
        this.idSubTask = idSubTask;
    }

    public Epic(String name, String description, Status status, ArrayList<Integer> idSubTask) {
        super(name, description, status);
        this.idSubTask = idSubTask;
    }

    public Epic(String name, String description, Status status) {
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
                "id='" + getId() + '\'' +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status='" + this.getStatus() + '\'' +
                ", idSubTask='" + idSubTask + '\'' +
                '}';
    }
}

