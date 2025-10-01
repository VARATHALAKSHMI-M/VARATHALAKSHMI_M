package exercise2;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;
// Simplified Smart Office Facility console app
public class SmartOfficeFacility {
    private static final Logger logger = Logger.getLogger("SmartOffice");
    private final Map<Integer, Room> rooms = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public SmartOfficeFacility() { /* background tasks could be scheduled here */ }
    static class Room {
        final int id; int maxCapacity = 10; int occupancy = 0; Booking booking = null;
        boolean lightsOn = false, acOn = false;
        Room(int id) { this.id = id; }
    }
    static class Booking { final LocalTime start; final int durationMinutes; Booking(LocalTime s,int d){start=s;durationMinutes=d;} }
    public void configureRooms(int n) {
        for (int i=1;i<=n;i++) rooms.put(i,new Room(i));
        System.out.println("Office configured with " + n + " meeting rooms.");
    }
    public void setMaxCapacity(int roomId, int cap) {
        Room r = rooms.get(roomId);
        if (r==null) { System.out.println("Room does not exist"); return; }
        if (cap<=0) { System.out.println("Invalid capacity"); return; }
        r.maxCapacity = cap; System.out.println("Room " + roomId + " max capacity set to " + cap);
    }
    public void bookRoom(int id, String timeStr, int minutes) {
        Room r = rooms.get(id);
        if (r==null) { System.out.println("Invalid room number"); return; }
        LocalTime t = LocalTime.parse(timeStr);
        if (r.booking!=null) { System.out.println("Room " + id + " is already booked during this time. Cannot book."); return; }
        r.booking = new Booking(t, minutes);
        // schedule auto-release if not occupied within 5 minutes
        scheduler.schedule(() -> {
            if (r.occupancy < 2) {
                r.booking = null; r.lightsOn=false; r.acOn=false;
                System.out.println("Room " + id + " booking released due to no occupancy.");
            }
        }, 5, TimeUnit.MINUTES);
        System.out.println("Room " + id + " booked from " + t + " for " + minutes + " minutes.");
    }
    public void cancelBooking(int id) {
        Room r = rooms.get(id);
        if (r==null || r.booking==null) { System.out.println("Room " + id + " is not booked. Cannot cancel booking."); return; }
        r.booking = null; System.out.println("Booking for Room " + id + " cancelled successfully.");
    }
    public void addOccupants(int id, int count) {
        Room r = rooms.get(id);
        if (r==null) { System.out.println("Room " + id + " does not exist."); return; }
        r.occupancy = Math.max(0, count);
        if (r.occupancy >= 2) { r.lightsOn = true; r.acOn = true; System.out.println("Room " + id + " is now occupied by " + r.occupancy + " persons. AC and lights turned on."); }
        else { System.out.println("Room " + id + " occupancy insufficient to mark as occupied."); }
        if (r.occupancy==0) { r.lightsOn=false; r.acOn=false; System.out.println("Room " + id + " is now unoccupied. AC and lights turned off."); }
    }
    public static void main(String[] args) {
        SmartOfficeFacility app = new SmartOfficeFacility();
        Scanner sc = new Scanner(System.in);
        System.out.println("Smart Office - commands: configRooms|n, setCap|room|cap, book|room|HH:mm|mins, cancel|room, addOccupant|room|count, exit");
        boolean run=true;
        while (run && sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equalsIgnoreCase("exit")) { run=false; continue; }
            String[] p = line.split("\\|", -1);
            try {
                switch(p[0]) {
                    case "configRooms": app.configureRooms(Integer.parseInt(p[1])); break;
                    case "setCap": app.setMaxCapacity(Integer.parseInt(p[1]), Integer.parseInt(p[2])); break;
                    case "book": app.bookRoom(Integer.parseInt(p[1]), p[2], Integer.parseInt(p[3])); break;
                    case "cancel": app.cancelBooking(Integer.parseInt(p[1])); break;
                    case "addOccupant": app.addOccupants(Integer.parseInt(p[1]), Integer.parseInt(p[2])); break;
                    default: System.out.println("Unknown command");
                }
            } catch (Exception ex) { logger.log(Level.SEVERE, "Error", ex); System.out.println("Invalid command or params"); }
        }
        sc.close();
        app.scheduler.shutdown();
        System.out.println("Exiting Smart Office.");
    }
}
