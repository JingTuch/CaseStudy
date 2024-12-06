public class Student {
    private String name;
    private String studentNumber;
    private String password;

    // Constructor
    public Student(String name, String studentNumber, String password) {
        this.name = name;
        this.studentNumber = studentNumber;
        this.password = password;
    }

    // Getter methods
    public String getStudentNumber() {
        return studentNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    // Setter methods
    public void setName(String name) {
        this.name = name;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Student{ Name: " + name + ", Student Number: " + studentNumber + ", Password: " + password + " }";
    }
}
