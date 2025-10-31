
 public class Product {
    private String id;
    private String name;
    private int quantity;

    public Product(String id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }

    public void increaseQuantity(int amount) {
        if (amount > 0) {
            quantity += amount;
        }
    }

    public void decreaseQuantity(int amount) {
        if (amount > 0 && amount <= quantity) {
            quantity -= amount;
        }
    }

    @Override
    public String toString() {
        return "Product ID: " + id + ", Name: " + name + ", Quantity: " + quantity;
    }
}

