package com.pao.laboratory03.exercise;

import java.util.HashMap;
import java.util.Map;

public class Student {
    private String name;
    private int age;
    private Map<Subject, Double> grades;
    public Student(String name, int age) {
        if (age < 18 || age > 60) {
            throw new InvalidStudentException("Varsta invalida, trebuie sa fie intre 18 si 60 de ani.");
        }

        this.name = name;
        this.age = age;
        this.grades = new HashMap<>();
    }

    public String getName(){ return name;};
    public int getAge(){ return age;};
    public Map<Subject, Double> getGrades() { return grades;};

    public void addGrade(Subject subject, Double g)
    {
        if(g<1 || g>10) {
            throw new InvalidGradeException("Nota nu este valida, trebuie sa fie intre 1 si 10");
        }
            grades.put(subject,g);
    }
    public double getAverage() {
        if (grades.isEmpty())
            return 0;
        double sum = 0;
        for (double g : grades.values())
        {
            sum += g;
        }
        return sum / grades.size();
    }

    @Override
    public String toString() {
        return "Student{name='" + name + "', age=" + age + ", avg=" + String.format("%.2f", getAverage()) + "}";
    }

}
