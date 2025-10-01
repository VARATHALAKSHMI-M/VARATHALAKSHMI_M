package exercise2;
import java.util.*;
// Simplified rocket launch simulator
public class RocketLaunchSimulator {
    static class Rocket {
        String stage = "Pre-Launch"; int fuel = 100; int altitude = 0; int speed = 0;
        boolean checksPassed = false;
        public void startChecks() { checksPassed = true; System.out.println("All systems are 'Go' for launch."); }
        public void launch() {
            if (!checksPassed) { System.out.println("Cannot launch: checks not passed"); return; }
            stage = "Stage 1"; System.out.println("Launch started");
        }
        public void tick() {
            if (stage.startsWith("Stage")) {
                fuel -= 10; altitude += 10; speed += 1000;
                System.out.println("Stage: " + stage + ", Fuel: " + fuel + "%, Altitude: " + altitude + " km, Speed: " + speed + " km/h");
                if (fuel <= 50 && stage.equals("Stage 1")) { System.out.println("Stage 1 complete. Separating stage. Entering Stage 2."); stage = "Stage 2"; }
                if (altitude >= 100) { System.out.println("Orbit achieved! Mission Successful."); stage = "Orbit"; }
                if (fuel <= 0) { System.out.println("Mission Failed due to insufficient fuel."); stage = "Failed"; }
            } else System.out.println("No active flight stage.");
        }
    }
    public static void main(String[] args) {
        Rocket r = new Rocket();
        Scanner sc = new Scanner(System.in);
        System.out.println("Commands: start_checks, launch, fast_forward|seconds, exit");
        boolean run=true;
        while (run && sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equalsIgnoreCase("exit")) { run=false; continue; }
            String[] p = line.split("\\|", -1);
            switch(p[0]) {
                case "start_checks": r.startChecks(); break;
                case "launch": r.launch(); break;
                case "fast_forward": int secs = Integer.parseInt(p[1]); for (int i=0;i<secs;i++) r.tick(); break;
                default: System.out.println("Unknown");
            }
        }
        sc.close();
    }
}
