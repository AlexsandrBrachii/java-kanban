package tasks;


import java.time.LocalDateTime;
import java.util.ArrayList;


public class Epic extends Task {

    public ArrayList<Integer> idSubTask;
    private LocalDateTime endTime;

    public Epic(String name, String description, Status status, ArrayList<Integer> idSubTask) {
        super(name, description, status);
        this.idSubTask = idSubTask;
    }

    public Epic(Integer id, String name, String description, Status status, ArrayList<Integer> idSubTask) {
        super(id, name, description, status);
        this.idSubTask = idSubTask;
    }

    public Epic(Integer id, String name, String description, Status status, Integer duration, LocalDateTime startTime, LocalDateTime endTime) {
        super(id, name, description, status, duration, startTime);
        this.endTime = endTime;
    }

    public Epic(Integer id, String name, String description, Status status, ArrayList<Integer> idSubTask, Integer duration, LocalDateTime startTime, LocalDateTime endTime) {
        super(id, name, description, status, duration, startTime);
        this.idSubTask = idSubTask;
        this.endTime = endTime;
    }

    public ArrayList<Integer> getIdSubTask() {
        return idSubTask;
    }

    public void setIdSubTask(ArrayList<Integer> idSubTask) {
        this.idSubTask = idSubTask;
    }

    @Override
    public Integer getDuration() {
        return super.getDuration();
    }

    @Override
    public void setDuration(Integer duration) {
        super.setDuration(duration);
    }

    @Override
    public LocalDateTime getStartTime() {
        return super.getStartTime();
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        super.setStartTime(startTime);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {

        return "Epic{" +
                "id='" + getId() + '\'' +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status='" + this.getStatus() + '\'' +
                ", idSubTask='" + idSubTask + '\'' +
                ", duration='" + this.getDuration() + '\'' +
                ", startTime='" + this.getStartTime() + '\'' +
                ", endTime='" + this.getEndTime() + '\'' +
                '}';
    }


}

