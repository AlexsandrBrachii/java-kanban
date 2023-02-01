package ru.yandex.practicum.brachii.kanban.tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {


    private Integer idEpic;

    public SubTask(Integer id, String name, String description, Status status, Integer idEpic) {
        super(id, name, description, status);
        this.idEpic = idEpic;
    }

    public SubTask(String name, String description, Status status, Integer idEpic, Duration duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.idEpic = idEpic;
    }

    public SubTask(Integer id, String name, String description, Status status, Integer idEpic, Duration duration, LocalDateTime startTime) {
        super(id, name, description, status, duration, startTime);
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

        return "SubTask{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", idEpic='" + getIdEpic() + '\'' +
                ", duration='" + getDuration() + '\'' +
                ", startTime='" + getStartTime() +
                '}';
    }
}

