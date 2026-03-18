package com.pao.laboratory03.bonus;
import java.util.*;

/**
 * Structuri de date interne:
 *   - Map<String, Task> tasksById — toate task-urile, indexate după id
 *   - Map<Priority, List<Task>> tasksByPriority — grupate pe prioritate
 *   - List<String> auditLog — jurnal cu TOATE operațiile efectuate
 *
 * Metode:
 *   a) Task addTask(String title, Priority priority)
 *      → generează id automat ("T001", "T002", ...)
 *      → adaugă în ambele map-uri
 *      → logează: "[ADD] T001: 'Fix bug' (HIGH)"
 *
 *   b) void assignTask(String taskId, String assignee)
 *      → aruncă TaskNotFoundException dacă id-ul nu există
 *      → logează: "[ASSIGN] T001 → Ana"
 *
 *   c) void changeStatus(String taskId, Status newStatus)
 *      → aruncă TaskNotFoundException dacă id-ul nu există
 *      → aruncă InvalidTransitionException dacă tranziția nu e permisă
 *        (folosește Status.canTransitionTo())
 *      → logează: "[STATUS] T001: TODO → IN_PROGRESS"
 *
 *   d) List<Task> getTasksByPriority(Priority priority)
 *      → returnează lista din map (sau listă goală)
 *
 *   e) Map<Status, Long> getStatusSummary()
 *      → returnează câte task-uri sunt pe fiecare status
 *      → exemplu: {TODO=3, IN_PROGRESS=2, DONE=5, CANCELLED=1}
 *
 *   f) List<Task> getUnassignedTasks()
 *      → task-uri cu assignee == null
 *
 *   g) void printAuditLog()
 *      → afișează toate intrările din jurnal
 *
 *   h) double getTotalUrgencyScore(int baseDays)
 *      → suma scorurilor de urgență ale task-urilor care NU sunt DONE/CANCELLED
 *      → folosește Priority.calculateScore(baseDays)
 **/

public class TaskService {
    private static TaskService instance;
    private Map<String, Task> tasksById = new HashMap<>();
    private Map<Priority, List<Task>> tasksByPriority = new HashMap<>();
    private List<String> auditLog = new ArrayList<>();

    private int counter = 1;
    private TaskService() {}

    public static TaskService getInstance() {
        if (instance == null)
        {
            instance = new TaskService();
        }
        return instance;
    }
    public String generateId()
    {
       return String.format("T%03d", counter++);
    }

    public Task addTask(String title, Priority priority)
    {
       String id=generateId();
       if(tasksById.containsKey(id))
           throw new DuplicateTaskException(id);
       Task task= new Task(id, title, priority);
        tasksById.put(id, task);
        /// testez daca contine prioritatea asta in map-ul care tine cont de prioritati
        if (!tasksByPriority.containsKey(priority)) {
            tasksByPriority.put(priority, new ArrayList<>());
        }
        tasksByPriority.get(priority).add(task);
        auditLog.add("[ADD] " + id + ": '" + title + "' (" + priority + ")");

        return task;
    }
    private Task getTask(String id) {
        Task task = tasksById.get(id);
        if (task == null) {
            throw new TaskNotFoundException(id);
        }
        return task;
    }

    public void assignTask(String taskId, String assignee)
    {
        if(!tasksById.containsKey(taskId))
            throw new TaskNotFoundException(taskId);
        Task task = getTask(taskId);
        task.setAssignee(assignee);
        auditLog.add("[ASSIGN] " + taskId + " → " + assignee);
    }

    public void changeStatus(String taskId, Status newStatus)
    {

        if(!tasksById.containsKey(taskId))
            throw new TaskNotFoundException(taskId);
        Task task=getTask(taskId);
        if(!task.getStatus().canTransitionTo(newStatus))
            throw new InvalidTransitionException(task.getStatus(),newStatus);

        auditLog.add("[STATUS] " + taskId + ": " + task.getStatus() + " → " + newStatus);
        task.setStatus(newStatus);
    }

    public List<Task> getUnassignedTasks() {
        List<Task> result = new ArrayList<>();

        for (Task t : tasksById.values()) {
            if (t.getAssignee() == null) {
                result.add(t);
            }
        }

        return result;
    }

    public void printAuditLog() {
        for (String log : auditLog) {
            System.out.println(log);
        }
    }

    public double getTotalUrgencyScore(int baseDays) {
        double total = 0;

        for (Task t : tasksById.values()) {
            if (t.getStatus() != Status.DONE && t.getStatus() != Status.CANCELLED) {
                total += t.getPriority().calculateScore(baseDays);
            }
        }

        return total;
    }


    public List<Task> getTasksByPriority(Priority priority) {
        List<Task> result = tasksByPriority.get(priority);

        if (result == null) {
            return new ArrayList<>();
        }

        return result;
    }

    public Map<Status, Long> getStatusSummary() {
        Map<Status, Long> summary = new HashMap<>();

        for (Status status : Status.values()) {
            summary.put(status, 0L);
        }

        for (Task t : tasksById.values()) {
            Status status = t.getStatus();
            summary.put(status, summary.get(status) + 1);
        }

        return summary;
    }
}
