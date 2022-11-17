package Tasks;

public class SubTask extends Epic {


    private Integer idEpic;

    public SubTask() {
    }

    public SubTask(String name, String description, String status, Integer idEpic) {
        super(name, description, status);
        this.idEpic = idEpic;
    }

    public Integer getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(Integer idEpic) {
        this.idEpic = idEpic;
    }

    @Override
    public String toString() {
        return ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", idEpic='" + getIdEpic() + '\'' +
                '}';
    }
}

