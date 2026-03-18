package com.pao.laboratory03.bonus;

public class Task
{
    /** Sistemul gestionează task-uri. Fiecare task are:
     *   - String id (unic, format "T001", "T002", ...)
     *   - String title
     *   - Status status (enum: TODO, IN_PROGRESS, DONE, CANCELLED)
     *   - Priority priority (enum: LOW, MEDIUM, HIGH, CRITICAL)
     *   - String assignee (persoana responsabilă, poate fi null)*/

    private String id;
    private String title;
    private Status status;
    private Priority priority;
    private String assignee;

    public Task(String id, String title, Priority priority) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.status = Status.TODO;      ///pun initial task-ul ca TODO
    }

    public String getId(){ return id;}
    public String getTitle(){ return title;}
    public Status getStatus(){ return status;}
    public Priority getPriority(){return priority;}
    public String getAssignee(){return assignee;}

    public void setStatus(Status status){
        this.status=status;
    }

    public void setAssignee(String assignee)
    {
        this.assignee=assignee;
    }
    @Override
    public String toString() {
        return "Task{id='" + id + "', title='" + title + "', priority=" + priority + ", status=" + status + ", assignee=" + assignee + "}";
    }
}
