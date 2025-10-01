package exercise1.creational;
// Factory pattern demo: shapes
interface Shape { void draw(); }
class Circle implements Shape { public void draw() { System.out.println("Drawing Circle"); } }
class Square implements Shape { public void draw() { System.out.println("Drawing Square"); } }
class ShapeFactory {
    public static Shape create(String type) {
        switch(type.toLowerCase()) {
            case "circle": return new Circle();
            case "square": return new Square();
            default: throw new IllegalArgumentException("Unknown shape: "+type);
        }
    }
}
public class FactoryPatternDemo {
    public static void main(String[] args) {
        Shape s1 = ShapeFactory.create("circle"); s1.draw();
        Shape s2 = ShapeFactory.create("square"); s2.draw();
    }
}
