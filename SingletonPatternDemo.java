package exercise1.creational;
import java.util.logging.*;
// Thread-safe Singleton Logger wrapper demo
public class SingletonPatternDemo {
    private static volatile SingletonPatternDemo instance;
    private final Logger logger;
    private SingletonPatternDemo() {
        logger = Logger.getLogger("SingletonDemo");
        logger.info("Logger initialized");
    }
    public static SingletonPatternDemo getInstance() {
        if (instance == null) {
            synchronized (SingletonPatternDemo.class) {
                if (instance == null) instance = new SingletonPatternDemo();
            }
        }
        return instance;
    }
    public void log(String msg) { logger.info(msg); }
    public static void main(String[] args) {
        SingletonPatternDemo s1 = SingletonPatternDemo.getInstance();
        SingletonPatternDemo s2 = SingletonPatternDemo.getInstance();
        s1.log("Singleton works. s1 == s2: " + (s1 == s2));
    }
}
