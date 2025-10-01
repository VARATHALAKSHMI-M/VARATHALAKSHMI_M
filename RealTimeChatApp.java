package exercise2;
import java.util.*;
import java.util.concurrent.*;
// Simplified in-process chat rooms using Observer-like subscriptions
public class RealTimeChatApp {
    static class Room {
        final String id; final Map<String, Client> clients = new ConcurrentHashMap<>();
        Room(String id){ this.id=id; }
        void join(Client c){ clients.put(c.name, c); broadcast("[system] " + c.name + " joined"); }
        void leave(Client c){ clients.remove(c.name); broadcast("[system] " + c.name + " left"); }
        void broadcast(String msg){ clients.values().forEach(c -> c.receive(msg)); }
    }
    static class Client {
        final String name;
        Client(String name){ this.name=name; }
        void receive(String msg){ System.out.println("[" + name + "] " + msg); }
    }
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    public void createRoom(String id){ rooms.putIfAbsent(id, new Room(id)); System.out.println("Room " + id + " created"); }
    public void join(String roomId, String user){ Room r = rooms.get(roomId); if (r==null) { System.out.println("No such room"); return; } r.join(new Client(user)); }
    public void send(String roomId, String user, String msg){ Room r = rooms.get(roomId); if (r==null){ System.out.println("No such room"); return; } r.broadcast("["+user+"]: " + msg); }
    public static void main(String[] args){
        RealTimeChatApp app = new RealTimeChatApp();
        Scanner sc = new Scanner(System.in);
        System.out.println("Chat commands: create|roomId, join|roomId|user, send|roomId|user|msg, exit");
        boolean run=true;
        while (run && sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equalsIgnoreCase("exit")) { run=false; continue; }
            String[] p = line.split("\\|", -1);
            switch(p[0]) {
                case "create": app.createRoom(p[1]); break;
                case "join": app.join(p[1], p[2]); break;
                case "send": app.send(p[1], p[2], p[3]); break;
                default: System.out.println("Unknown");
            }
        }
        sc.close();
    }
}
