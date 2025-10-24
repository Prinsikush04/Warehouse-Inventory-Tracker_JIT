
 import java.util.Objects;

public class Product {
    private final String id;
    private final String name;
    private int quantity;
    private final int reorderThreshold;

    public Product(String id, String name, int initialQuantity, int reorderThreshold) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id cannot be empty");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name cannot be empty");
        if (reorderThreshold < 0) throw new IllegalArgumentException("reorderThreshold cannot be negative");
        if (initialQuantity < 0) throw new IllegalArgumentException("quantity cannot be negative");

        this.id = id;
        this.name = name;
        this.quantity = initialQuantity;
        this.reorderThreshold = reorderThreshold;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    // All modifications must go through Warehouse methods
    protected int getQuantity() { return quantity; }
    protected void setQuantity(int newQuantity) { this.quantity = newQuantity; }

    public int getReorderThreshold() { return reorderThreshold; }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", reorderThreshold=" + reorderThreshold +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product p = (Product) o;
        return id.equals(p.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

