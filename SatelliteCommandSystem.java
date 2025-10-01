package exercise2;
import java.util.*;
// Simple Satellite Command System - command-line simulator
public class SatelliteCommandSystem {
    static class Satellite {
        enum Orientation { North, South, East, West }
        private Orientation orientation = Orientation.North;
        private boolean panelsActive = false;
        private int dataCollected = 0;
        public void rotate(String dir) { orientation = Orientation.valueOf(dir); System.out.println("Orientation: " + orientation); }
        public void activatePanels(){ panelsActive = true; System.out.println("Panels activated"); }
        public void deactivatePanels(){ panelsActive = false; System.out.println("Panels deactivated"); }
        public void collectData(){ if (panelsActive) { dataCollected += 10; System.out.println("Data Collected: " + dataCollected); } else System.out.println("Cannot collect data: panels inactive"); }
        public String status(){ return "Orientation:"+orientation+" Panels:"+(panelsActive?"Active":"Inactive")+" Data:"+dataCollected; }
    }
    public static void main(String[] args) {
        Satellite sat = new Satellite();
        Scanner sc = new Scanner(System.in);
        System.out.println("Satellite commands: rotate|Direction, activatePanels, deactivatePanels, collectData, status, exit");
        boolean run=true;
        while (run && sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equalsIgnoreCase("exit")) { run=false; continue; }
            String[] p = line.split("\\|", -1);
            switch(p[0]) {
                case "rotate": sat.rotate(p[1]); break;
                case "activatePanels": sat.activatePanels(); break;
                case "deactivatePanels": sat.deactivatePanels(); break;
                case "collectData": sat.collectData(); break;
                case "status": System.out.println(sat.status()); break;
                default: System.out.println("Unknown");
            }
        }
        sc.close();
    }
}
