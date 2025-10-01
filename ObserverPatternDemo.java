package exercise1.behavioral;
import java.util.*;
import java.util.logging.*;
// Simple Observer pattern demo
interface Observer {
    void update(String message);
}
class ConsoleObserver implements Observer {
    private final String name;
    ConsoleObserver(String name) { this.name = name; }
    public void update(String message) {
        System.out.println(name + " received: " + message);
    }
}
class Subject {
    private final List<Observer> observers = new ArrayList<>();
    void subscribe(Observer o) { observers.add(o); }
    void unsubscribe(Observer o) { observers.remove(o); }
    void notifyObservers(String msg) {
        for (Observer o : observers) o.update(msg);
    }
}
public class ObserverPatternDemo {
    public static void main(String[] args) {
        Subject subject = new Subject();
        subject.subscribe(new ConsoleObserver("Alice"));
        subject.subscribe(new ConsoleObserver("Bob"));
        subject.notifyObservers("Observer Pattern Demo Running");
    }
}
