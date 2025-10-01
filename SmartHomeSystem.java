package exercise2;
import java.util.*;
import java.util.logging.*;
// Simple Smart Home System: devices control via central hub. Factory + Observer + Proxy usage simplified.
public class SmartHomeSystem {
    interface Device { int id(); String status(); void perform(String cmd); }
    static class Light implements Device {
        private final int id; private boolean on=false;
        Light(int id){this.id=id;}
        public int id(){return id;}
        public String status(){return on?"On":"Off";}
        public void perform(String cmd){
            if (cmd.equalsIgnoreCase("turnOn")) on=true;
            else if (cmd.equalsIgnoreCase("turnOff")) on=false;
        }
    }
    static class Thermostat implements Device {
        private final int id; private int temp=70;
        Thermostat(int id){this.id=id;}
        public int id(){return id;}
        public String status(){return "Temp="+temp;}
        public void perform(String cmd){ if (cmd.startsWith("set:")) temp=Integer.parseInt(cmd.split(":")[1]); }
    }
    static class DeviceFactory {
        public static Device create(String type, int id) {
            return switch(type.toLowerCase()) {
                case "light" -> new Light(id);
                case "thermostat" -> new Thermostat(id);
                default -> throw new IllegalArgumentException("Unknown device");
            };
        }
    }
    // Hub
    private final Map<Integer, Device> devices = new HashMap<>();
    public void addDevice(Device d){ devices.put(d.id(), d); System.out.println("Device added: " + d.id()); }
    public void perform(int id, String cmd){
        Device d = devices.get(id); if (d==null){ System.out.println("No device"); return; }
        d.perform(cmd); System.out.println("Performed " + cmd + " on " + id);
    }
    public void status(){ devices.values().forEach(d -> System.out.println(d.id()+": " + d.status())); }
    public static void main(String[] args){
        SmartHomeSystem hub = new SmartHomeSystem();
        Scanner sc = new Scanner(System.in);
        System.out.println("SmartHome commands: add|type|id, perform|id|cmd, status, exit");
        boolean run=true;
        while (run && sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equalsIgnoreCase("exit")) { run=false; continue; }
            String[] p = line.split("\\|", -1);
            try {
                switch(p[0]) {
                    case "add": hub.addDevice(DeviceFactory.create(p[1], Integer.parseInt(p[2])); break;
                    case "perform": hub.perform(Integer.parseInt(p[1]), p[2]); break;
                    case "status": hub.status(); break;
                    default: System.out.println("Unknown");
                }
            } catch (Exception ex) { System.out.println("Error: " + ex.getMessage()); }
        }
        sc.close();
    }
}
