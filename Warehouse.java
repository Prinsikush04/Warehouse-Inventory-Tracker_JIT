 import java.util.*;

public class Warehouse {
    private String name;
    private Map<String, Product> products = new HashMap<>();
    private Map<String, Integer> thresholds = new HashMap<>();
    private List<StockObserver> observers = new ArrayList<>();

    public Warehouse(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void addObserver(StockObserver observer) {
        observers.add(observer);
    }

    public void addProduct(Product product, int threshold) {
        if (products.containsKey(product.getId())) {
            System.out.println("❌ Product ID already exists in " + name + "!");
            return;
        }
        products.put(product.getId(), product);
        thresholds.put(product.getId(), threshold);
        System.out.println("✅ Added " + product.getName() + " to warehouse: " + name);
    }

    public void receiveShipment(String productId, int quantity) {
        Product p = products.get(productId);
        if (p != null) {
            p.increaseQuantity(quantity);
            System.out.println("📦 Shipment received for " + p.getName() + 
                               " in " + name + ". New quantity: " + p.getQuantity());
        } else {
            System.out.println("❌ Product ID not found in " + name + "!");
        }
    }

    public void fulfillOrder(String productId, int quantity) {
        Product p = products.get(productId);
        if (p == null) {
            System.out.println("❌ Invalid Product ID in " + name + "!");
            return;
        }

        if (quantity > p.getQuantity()) {
            System.out.println("❌ Insufficient stock to fulfill the order!");
            return;
        }

        p.decreaseQuantity(quantity);
        System.out.println("🛒 Order fulfilled for " + p.getName() + 
                           " from " + name + ". Remaining: " + p.getQuantity());

        int threshold = thresholds.get(productId);
        if (p.getQuantity() < threshold) {
            notifyObservers(p, threshold);
        }
    }

    private void notifyObservers(Product product, int threshold) {
        for (StockObserver observer : observers) {
            observer.onLowStock(name, product, threshold);
        }
    }

    public void showAllProducts() {
        if (products.isEmpty()) {
            System.out.println("📦 No products in warehouse: " + name);
            return;
        }
        System.out.println("\n========== Inventory of " + name + " ==========");
        for (Product p : products.values()) {
            int threshold = thresholds.get(p.getId());
            System.out.println(p + ", Threshold: " + threshold);
        }
    }
}
