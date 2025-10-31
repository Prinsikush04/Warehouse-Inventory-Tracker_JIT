 import java.util.*;

public class WarehouseManager {
    private Map<String, Warehouse> warehouses = new HashMap<>();

    public void addWarehouse(String name, StockObserver observer) {
        if (warehouses.containsKey(name)) {
            System.out.println("❌ Warehouse already exists!");
            return;
        }
        Warehouse wh = new Warehouse(name);
        wh.addObserver(observer);
        warehouses.put(name, wh);
        System.out.println("🏗️ Warehouse created: " + name);
    }

    public Warehouse getWarehouse(String name) {
        return warehouses.get(name);
    }

    public void showAllWarehouses() {
        if (warehouses.isEmpty()) {
            System.out.println("📦 No warehouses created yet.");
            return;
        }
        System.out.println("\n========= List of Warehouses =========");
        for (String name : warehouses.keySet()) {
            System.out.println("🏭 " + name);
        }
    }
}
