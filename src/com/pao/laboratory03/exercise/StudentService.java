package com.pao.laboratory03.exercise;
import java.util.*;

public class StudentService {

    private static StudentService instance;
    private List<Student> students;
    private StudentService()
    {
        students=new ArrayList<>();

    }

    public static StudentService getInstance()
    {
        if(instance==null)
        {
            instance = new StudentService();
        }
        return instance;
    }

    public void addStudent(String name, int age)
    {
        for(Student s:students)
        {
            if(s.getName().equalsIgnoreCase(name))
                throw new RuntimeException(("studentul deja exista"));

        }
        /// daca studentul nu exista deja si nu se declanseaza exceptia, atunci il adauga la lista de studenti
        students.add(new Student(name,age));
    }

    public Student findByName(String name)
    {
        for(Student s: students)
        {
            if(s.getName().equalsIgnoreCase(name))
                return s;
        }
        throw new StudentNotFoundException("studentul nu a fost gasit");
    }

    public void addGrade(String name, Subject s, Double grade)
    {
        Student st= findByName(name);
        st.addGrade(s,grade);
    }

    public void printAllStudents()
    {
        for(Student s: students)
        {
            System.out.println(s);
            /// functia de afisare o sa apeleze automat toString

            /// parcurg map-ul de grades si folosesc entrySet ca sa obtin perechile key,value
            for(Map.Entry<Subject,Double> entry: s.getGrades().entrySet()){
                System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
            }
        }

    }

    public void printTopStudents()
    {
        /// transform lista intr-un flux si apoi o sortez descrescator interschimband ordinea parametrilor
        /// la afisare se foloseste implicit toString suprascris
        students.stream().sorted((a,b)->Double.compare(b.getAverage(),a.getAverage())).forEach(System.out::println);
    }

    public Map<Subject, Double> getAveragePerSubject()
    {
        Map<Subject, Double> temp=new HashMap<>();
        for (Subject subject : Subject.values()) {
            double sum = 0;
            int count = 0;
            for (Student s : students) {
                Double grade = s.getGrades().get(subject);
                if (grade != null) {
                    sum += grade;
                    count++;
                }
            }
            if (count > 0) {
                temp.put(subject, sum / count);
            }
        }
        return temp;
    }


}
