import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Warehouse {
    private final String name;
    // Thread-safe map for concurrency
    private final Map<String, Product> inventory = new ConcurrentHashMap<>();
    private final List<StockObserver> observers = Collections.synchronizedList(new ArrayList<>());

    public Warehouse(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void addObserver(StockObserver observer) {
        if (observer == null) return;
        observers.add(observer);
    }

    public void removeObserver(StockObserver observer) {
        observers.remove(observer);
    }

    private void notifyLowStock(Product product) {
        // Copy observers to avoid ConcurrentModification
        List<StockObserver> snapshot;
        synchronized (observers) {
            snapshot = new ArrayList<>(observers);
        }
        for (StockObserver obs : snapshot) {
            try {
                obs.onLowStock(name, product);
            } catch (Exception e) {
                System.err.println("Observer failed: " + e.getMessage());
            }
        }
    }

    public void addProduct(Product product) {
        Objects.requireNonNull(product, "product cannot be null");
        if (inventory.putIfAbsent(product.getId(), product) != null) {
            throw new IllegalArgumentException("Product with id " + product.getId() + " already exists.");
        }
    }

    public void receiveShipment(String productId, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Shipment qty must be positive");
        Product p = inventory.get(productId);
        if (p == null) throw new NoSuchElementException("Product not found: " + productId);
        synchronized (p) {
            int newQty = p.getQuantity() + qty;
            p.setQuantity(newQty);
        }
        // No low-stock notify when receiving shipment (unless you want notify on clearing)
    }

    public void fulfillOrder(String productId, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Order qty must be positive");
        Product p = inventory.get(productId);
        if (p == null) throw new NoSuchElementException("Product not found: " + productId);

        synchronized (p) {
            int current = p.getQuantity();
            if (qty > current) {
                throw new IllegalStateException("Insufficient stock for product " + productId +
                        ". Requested: " + qty + ", Available: " + current);
            }
            p.setQuantity(current - qty);

            if (p.getQuantity() < p.getReorderThreshold()) {
                notifyLowStock(p);
            }
        }
    }

    public Optional<Product> getProduct(String id) {
        Product p = inventory.get(id);
        return p == null ? Optional.empty() : Optional.of(p);
    }

    public Collection<Product> listProducts() {
        return Collections.unmodifiableCollection(inventory.values());
    }

    // Persistence: save to CSV (id,name,quantity,reorderThreshold)
    public void saveInventoryToFile(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Product p : inventory.values()) {
                String line = String.format("%s,%s,%d,%d",
                        escape(p.getId()),
                        escape(p.getName()),
                        p.getQuantity(),
                        p.getReorderThreshold());
                writer.write(line);
                writer.newLine();
            }
        }
    }

    // Load inventory from file (overwrites existing inventory)
    public void loadInventoryFromFile(File file) throws IOException {
        Map<String, Product> loaded = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNo = 0;
            while ((line = reader.readLine()) != null) {
                lineNo++;
                if (line.isBlank()) continue;
                String[] parts = splitCsv(line);
                if (parts.length != 4) {
                    System.err.println("Skipping invalid line " + lineNo + ": " + line);
                    continue;
                }
                String id = unescape(parts[0]);
                String name = unescape(parts[1]);
                int qty = Integer.parseInt(parts[2]);
                int threshold = Integer.parseInt(parts[3]);
                Product p = new Product(id, name, qty, threshold);
                loaded.put(id, p);
            }
        }
        inventory.clear();
        inventory.putAll(loaded);
    }

    // Simple CSV escaping for commas and newlines
    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace(",", "\\,").replace("\n", "\\n");
    }

    private static String unescape(String s) {
        return s.replace("\\n", "\n").replace("\\,", ",").replace("\\\\", "\\");
    }

    private static String[] splitCsv(String line) {
        List<String> parts = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean escape = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (escape) {
                cur.append(c);
                escape = false;
            } else {
                if (c == '\\') {
                    escape = true;
                } else if (c == ',') {
                    parts.add(cur.toString());
                    cur = new StringBuilder();
                } else {
                    cur.append(c);
                }
            }
        }
        parts.add(cur.toString());
        return parts.toArray(new String[0]);
    }
}
