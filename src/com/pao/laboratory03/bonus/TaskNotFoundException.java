package com.pao.laboratory03.bonus;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String id) {
        super("Task-ul '" + id + "' nu a fost gasit");
    }
}