package exercise1.structural;
// Adapter pattern demo: adapt legacy printer to modern interface
class LegacyPrinter {
    public void printText(String text) { System.out.println("LegacyPrinter: " + text); }
}
interface ModernPrinter {
    void print(String msg);
}
class PrinterAdapter implements ModernPrinter {
    private final LegacyPrinter legacy;
    PrinterAdapter(LegacyPrinter legacy) { this.legacy = legacy; }
    public void print(String msg) { legacy.printText(msg); }
}
public class AdapterPatternDemo {
    public static void main(String[] args) {
        ModernPrinter printer = new PrinterAdapter(new LegacyPrinter());
        printer.print("Adapter Pattern bridging old and new.");
    }
}
