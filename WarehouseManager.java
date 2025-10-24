import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WarehouseManager {
    private final Map<String, Warehouse> warehouses = new ConcurrentHashMap<>();

    public Warehouse createWarehouse(String name) {
        Warehouse w = new Warehouse(name);
        if (warehouses.putIfAbsent(name, w) != null) {
            throw new IllegalArgumentException("Warehouse already exists: " + name);
        }
        return w;
    }

    public Warehouse getWarehouse(String name) {
        Warehouse w = warehouses.get(name);
        if (w == null) throw new IllegalArgumentException("No such warehouse: " + name);
        return w;
    }

    public boolean exists(String name) {
        return warehouses.containsKey(name);
    }
}
 