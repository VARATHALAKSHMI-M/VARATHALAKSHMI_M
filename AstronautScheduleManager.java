package exercise2;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.logging.*;
// Simple console-based Astronaut Daily Schedule Organizer
public class AstronautScheduleManager {
    private static final Logger logger = Logger.getLogger("AstronautSchedule");
    private final List<Task> tasks = new ArrayList<>();
    private final DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");
    // Factory inner
    static class TaskFactory {
        static Task create(String desc, String start, String end, String priority) {
            return new Task(desc, start, end, priority);
        }
    }
    // Observer-like simple notifier
    interface ConflictListener { void onConflict(Task existing, Task newTask); }
    private final List<ConflictListener> listeners = new ArrayList<>();
    public void addListener(ConflictListener l) { listeners.add(l); }
    public void addTask(Task t) {
        // validation
        try {
            LocalTime s = LocalTime.parse(t.start, tf);
            LocalTime e = LocalTime.parse(t.end, tf);
            if (!s.isBefore(e)) { System.out.println("Error: Start must be before End."); return; }
            for (Task ex : tasks) {
                if (overlap(s,e, LocalTime.parse(ex.start, tf), LocalTime.parse(ex.end, tf))) {
                    for (ConflictListener l : listeners) l.onConflict(ex, t);
                    System.out.println("Error: Task conflicts with existing task '"+ex.description+"'.");
                    return;
                }
            }
            tasks.add(t);
            tasks.sort(Comparator.comparing(x -> LocalTime.parse(x.start, tf)));
            logger.info("Task added: " + t.description);
            System.out.println("Task added successfully. No conflicts.");
        } catch (DateTimeParseException ex) {
            System.out.println("Error: Invalid time format.");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unexpected error", ex);
        }
    }
    private boolean overlap(LocalTime s1, LocalTime e1, LocalTime s2, LocalTime e2) {
        return !s1.isAfter(e2) && !s2.isAfter(e1) && (s1.isBefore(e2) && s2.isBefore(e1));
    }
    public void removeTask(String desc) {
        Optional<Task> found = tasks.stream().filter(t -> t.description.equalsIgnoreCase(desc)).findFirst();
        if (found.isPresent()) { tasks.remove(found.get()); System.out.println("Task removed successfully."); }
        else System.out.println("Error: Task not found.");
    }
    public void viewTasks() {
        if (tasks.isEmpty()) { System.out.println("No tasks scheduled for the day."); return; }
        for (Task t : tasks) {
            System.out.println(t.start + " - " + t.end + ": " + t.description + " [" + t.priority + "]");
        }
    }
    // Task entity
    static class Task {
        final String description; final String start; final String end; final String priority;
        Task(String d, String s, String e, String p) {
            this.description = Objects.requireNonNull(d);
            this.start = Objects.requireNonNull(s);
            this.end = Objects.requireNonNull(e);
            this.priority = Objects.requireNonNull(p);
        }
    }
    // Simple console UI (no while(true) - using Scanner with a command loop until 'exit')
    public static void main(String[] args) {
        AstronautScheduleManager mgr = new AstronautScheduleManager();
        mgr.addListener((existing, nw) -> System.out.println("[ConflictListener] New task conflicts with: " + existing.description));
        Scanner sc = new Scanner(System.in);
        System.out.println("Astronaut Schedule Manager - commands: add, remove, view, exit");
        String line;
        boolean running = true;
        while (running) {
            System.out.print("> ");
            if (!sc.hasNextLine()) break;
            line = sc.nextLine().trim();
            if (line.equalsIgnoreCase("exit")) { running = false; continue; }
            try {
                if (line.startsWith("add")) {
                    // format: add|Description|HH:mm|HH:mm|Priority
                    String[] parts = line.split("\\|", -1);
                    if (parts.length != 5) { System.out.println("Usage: add|Description|HH:mm|HH:mm|Priority"); continue; }
                    Task t = TaskFactory.create(parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim());
                    mgr.addTask(t);
                } else if (line.startsWith("remove")) {
                    String[] p = line.split("\\|", -1);
                    if (p.length != 2) { System.out.println("Usage: remove|Description"); continue; }
                    mgr.removeTask(p[1].trim());
                } else if (line.equalsIgnoreCase("view")) mgr.viewTasks();
                else System.out.println("Unknown command");
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Error in main loop", ex);
            }
        }
        sc.close();
        System.out.println("Exiting Astronaut Schedule Manager.");
    }
}
