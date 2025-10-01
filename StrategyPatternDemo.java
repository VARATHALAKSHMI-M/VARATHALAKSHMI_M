package exercise1.behavioral;
// Strategy Pattern demo: payment strategies
interface PaymentStrategy {
    void pay(int amount);
}
class CreditCardPayment implements PaymentStrategy {
    public void pay(int amount) { System.out.println("Paid " + amount + " using Credit Card."); }
}
class PayPalPayment implements PaymentStrategy {
    public void pay(int amount) { System.out.println("Paid " + amount + " using PayPal."); }
}
class ShoppingCart {
    private PaymentStrategy strategy;
    void setStrategy(PaymentStrategy s) { this.strategy = s; }
    void checkout(int amount) {
        if (strategy == null) throw new IllegalStateException("Payment strategy not set");
        strategy.pay(amount);
    }
}
public class StrategyPatternDemo {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();
        cart.setStrategy(new CreditCardPayment());
        cart.checkout(300);
        cart.setStrategy(new PayPalPayment());
        cart.checkout(150);
    }
}
