package com.pao.laboratory03.bonus;

public class DuplicateTaskException extends RuntimeException {
    public DuplicateTaskException(String id) {
        super("Task ul '" + id + "' exista deja");
    }
}