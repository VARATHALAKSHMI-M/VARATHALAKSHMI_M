package exercise2;
import java.util.*;
// Virtual Classroom Manager - simplified
public class VirtualClassroomManager {
    static class Classroom {
        final String name; final Set<String> students = new HashSet<>(); final List<String> assignments = new ArrayList<>();
        Classroom(String name){ this.name = name; }
    }
    private final Map<String, Classroom> classes = new HashMap<>();
    public void addClassroom(String name){ classes.putIfAbsent(name, new Classroom(name)); System.out.println("Classroom ["+name+"] has been created."); }
    public void addStudent(String studentId, String className){
        Classroom c = classes.get(className); if (c==null){ System.out.println("Class not found"); return; }
        c.students.add(studentId); System.out.println("Student ["+studentId+"] has been enrolled in ["+className+"].");
    }
    public void scheduleAssignment(String className, String details){
        Classroom c = classes.get(className); if (c==null){ System.out.println("Class not found"); return; }
        c.assignments.add(details); System.out.println("Assignment for ["+className+"] has been scheduled.");
    }
    public void submitAssignment(String studentId, String className, String details){
        Classroom c = classes.get(className); if (c==null){ System.out.println("Class not found"); return; }
        if (!c.students.contains(studentId)) { System.out.println("Student not enrolled in class"); return; }
        System.out.println("Assignment submitted by Student ["+studentId+"] in ["+className+"]. Details: " + details);
    }
    public static void main(String[] args){
        VirtualClassroomManager vm = new VirtualClassroomManager();
        Scanner sc = new Scanner(System.in);
        System.out.println("Commands: add_classroom|name, add_student|id|class, schedule_assignment|class|details, submit_assignment|id|class|details, exit");
        boolean run=true;
        while (run && sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equalsIgnoreCase("exit")) { run=false; continue; }
            String[] p = line.split("\\|", -1);
            switch(p[0]) {
                case "add_classroom": vm.addClassroom(p[1]); break;
                case "add_student": vm.addStudent(p[1], p[2]); break;
                case "schedule_assignment": vm.scheduleAssignment(p[1], p[2]); break;
                case "submit_assignment": vm.submitAssignment(p[1], p[2], p[3]); break;
                default: System.out.println("Unknown command");
            }
        }
        sc.close();
    }
}
