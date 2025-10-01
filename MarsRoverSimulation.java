package exercise2;
import java.util.*;
import java.util.logging.*;
// Mars Rover simulation (grid) - simplified, uses Command pattern for M/L/R
public class MarsRoverSimulation {
    enum Dir { N, E, S, W }
    static class Rover {
        int x,y; Dir dir; final int maxX, maxY;
        Rover(int x,int y,Dir d,int maxX,int maxY){this.x=x;this.y=y;this.dir=d;this.maxX=maxX;this.maxY=maxY;}
        public void move(Set<Point> obstacles) {
            int nx=x, ny=y;
            switch(dir) {
                case N -> ny = y+1;
                case S -> ny = y-1;
                case E -> nx = x+1;
                case W -> nx = x-1;
            }
            Point np = new Point(nx, ny);
            if (nx<0||ny<0||nx>maxX||ny>maxY) { System.out.println("Move blocked: boundary"); return; }
            if (obstacles.contains(np)) { System.out.println("Move blocked: obstacle at " + nx + "," + ny); return; }
            x=nx; y=ny;
        }
        public void left() { dir = switch(dir){case N->Dir.W; case W->Dir.S; case S->Dir.E; case E->Dir.N;}; }
        public void right(){ dir = switch(dir){case N->Dir.E; case E->Dir.S; case S->Dir.W; case W->Dir.N;}; }
        public String status(){ return "("+x+", "+y+", "+dir+")"; }
    }
    static record Point(int x,int y){}
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Mars Rover - init: gridX gridY startX startY startDir(N/E/S/W). Then commands M/L/R as a string. Type exit to quit.");
        if (!sc.hasNextLine()) return;
        String init = sc.nextLine().trim();
        if (init.equalsIgnoreCase("exit")) return;
        String[] parts = init.split("\s+");
        int gx = Integer.parseInt(parts[0]), gy = Integer.parseInt(parts[1]);
        int sx = Integer.parseInt(parts[2]), sy = Integer.parseInt(parts[3]);
        Dir d = Dir.valueOf(parts[4]);
        Rover rover = new Rover(sx, sy, d, gx, gy);
        System.out.println("Provide obstacles as comma-separated pairs like: 2,2;3,5 or empty for none");
        String obsLine = sc.nextLine().trim();
        Set<Point> obs = new HashSet<>();
        if (!obsLine.isEmpty()) {
            for (String s : obsLine.split(";")) {
                String[] a = s.split(","); obs.add(new Point(Integer.parseInt(a[0].trim()), Integer.parseInt(a[1].trim())));
            }
        }
        System.out.println("Enter command string (e.g. MMRMLM):");
        String cmds = sc.nextLine().trim().toUpperCase();
        for (char c : cmds.toCharArray()) {
            switch(c) {
                case 'M' -> rover.move(obs);
                case 'L' -> rover.left();
                case 'R' -> rover.right();
                default -> System.out.println("Ignored: " + c);
            }
        }
        System.out.println("Final Position: " + rover.status());
        sc.close();
    }
}
