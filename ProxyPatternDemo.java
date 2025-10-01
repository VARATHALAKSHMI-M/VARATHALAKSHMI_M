package exercise1.structural;
// Proxy pattern demo: access control to a resource
interface DataService {
    String readData();
}
class RealDataService implements DataService {
    public String readData() { return "Sensitive Data: [42, 73]"; }
}
class DataServiceProxy implements DataService {
    private final RealDataService real = new RealDataService();
    private final boolean authorized;
    DataServiceProxy(boolean authorized) { this.authorized = authorized; }
    public String readData() {
        if (!authorized) return "Access denied";
        return real.readData();
    }
}
public class ProxyPatternDemo {
    public static void main(String[] args) {
        DataService ds1 = new DataServiceProxy(true);
        System.out.println(ds1.readData());
        DataService ds2 = new DataServiceProxy(false);
        System.out.println(ds2.readData());
    }
}
