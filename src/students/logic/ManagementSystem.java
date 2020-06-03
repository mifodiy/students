package students.logic;

import java.util.*;

public class ManagementSystem
{
    private List<Group> groups;
    private Collection <Student> students;

    private static ManagementSystem instance;

    private ManagementSystem(){
        loadGroups();
        loadStudents();
    }

    public static synchronized ManagementSystem getInstance(){
        if(instance == null){
            instance = new ManagementSystem();
        }
        return instance;
    }

    public void loadGroups() {
        if(groups == null){
            groups = new ArrayList<Group>();
        }else {
            groups.clear();
        }

        Group g = null;

        g = new Group();
        g.setGroupId(1);
        g.setNameGroup("Первая");
        g.setCurator("Доктор Борменталь");
        g.setSpeciality("Создание собачек из человеков");
        groups.add(g);

        g = new Group();
        g.setGroupId(2);
        g.setNameGroup("Вторая");
        g.setCurator("Профессор Преображенский");
        g.setSpeciality("Создание человеков из собачек");
        groups.add(g);
    }

    public void loadStudents() {
        if(students == null){
            students = new TreeSet<Student>();
        }else {
            students.clear();
        }

        Student s = null;
        Calendar c = Calendar.getInstance();

        s = new Student();
        s.setStudentId(1);
        s.setFirstName("Иван");
        s.setPatronymic("Сергеевич");
        s.setSurName("Степанов");
        s.setSex('М');
        c.set(1990, 3, 20);
        s.setDateOfBirth(c.getTime());
        s.setGroupId(2);
        s.setEducationYear(2006);
        students.add(s);

        s = new Student();
        s.setStudentId(2);
        s.setFirstName("Наталья");
        s.setPatronymic("Андреевна");
        s.setSurName("Чичикова");
        s.setSex('Ж');
        c.set(1990, 6, 10);
        s.setDateOfBirth(c.getTime());
        s.setGroupId(2);
        s.setEducationYear(2006);
        students.add(s);

        s = new Student();
        s.setStudentId(3);
        s.setFirstName("Петр");
        s.setPatronymic("Викторович");
        s.setSurName("Сушкин");
        s.setSex('М');
        c.set(1991, 3, 12);
        s.setDateOfBirth(c.getTime());
        s.setEducationYear(2006);
        s.setGroupId(1);
        students.add(s);

        s = new Student();
        s.setStudentId(4);
        s.setFirstName("Вероника");
        s.setPatronymic("Сергеевна");
        s.setSurName("Ковалева");
        s.setSex('Ж');
        c.set(1991, 7, 19);
        s.setDateOfBirth(c.getTime());
        s.setEducationYear(2006);
        s.setGroupId(1);
        students.add(s);
    }

    private void removeStudentsFromGroup(Group group, int year) {
        Collection<Student> tmp = new TreeSet<Student>();
        for (Student si : students){
            if (si.getGroupId() != group.getGroupId() || si.getEducationYear() != year){
                tmp.add(si);
            }
        }
        students = tmp;
    }

    private void moveStudentsToGroup(Group oldGroup, int oldYear, Group newGroup, int newYear) {
        for (Student si : students){
            if (si.getGroupId() == oldGroup.getGroupId() && si.getEducationYear() == oldYear){
                si.setGroupId(newGroup.getGroupId());
                si.setEducationYear(newYear);
            }
        }

    }

    private void deleteStudent(Student student) {
        Student delStudent = null;
        for (Student si : students){
            if (si.getStudentId() == student.getStudentId()){
                delStudent = si;
                break;
            }
        }
        students.remove(delStudent);
    }

    private void updateStudent(Student student) {
        Student updStudent = null;

        for (Student si : students){
            if (si.getStudentId() == student.getStudentId()){
                updStudent = si;
                break;
            }
        }

        updStudent.setFirstName(student.getFirstName());
        updStudent.setPatronymic(student.getPatronymic());
        updStudent.setSurName(student.getSurName());
        updStudent.setSex(student.getSex());
        updStudent.setDateOfBirth(student.getDateOfBirth());
        updStudent.setGroupId(student.getGroupId());
        updStudent.setEducationYear(student.getEducationYear());
    }

    private void insertStudent(Student student) {
        students.add(student);
    }

    private Collection<Student> getStudentsFromGroup(Group group, int year) {
        Collection<Student> l = new TreeSet<Student>();
        for (Student si : students){
            if(si.getGroupId() == group.getGroupId() && si.getEducationYear() == year){
                l.add(si);
            }
        }
        return l;
    }

    public List<Group> getGroups(){
        return groups;
    }

    public Collection<Student> getAllStudents(){
        return students;
    }

    public static void printString(Object s){
        System.out.println(new String(s.toString()));
    }

    public static void printString(){
        System.out.println();
    }
}
