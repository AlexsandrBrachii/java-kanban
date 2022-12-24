package tasks;

public class SubTask extends Task {


    private static Integer idEpic;


    public SubTask(Integer id, String name, String description, Status status, Integer idEpic) {
        super(id, name, description, status);
        this.idEpic = idEpic;
    }

    public SubTask(String name, String description, Status status, Integer idEpic) {
        super(name, description, status);
        this.idEpic = idEpic;
    }

    public static Integer getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(Integer idEpic) {
        this.idEpic = idEpic;
    }

    @Override

    public String toString() {

        return "SubTask{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", idEpic='" + getIdEpic() + '\'' +
                '}';
    }
}

