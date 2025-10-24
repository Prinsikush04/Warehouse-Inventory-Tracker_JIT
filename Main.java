import java.io.File;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Create manager and a warehouse
            WarehouseManager manager = new WarehouseManager();
            Warehouse wh = manager.createWarehouse("MainWarehouse");

            // Attach an alert service
            AlertService alertService = new AlertService();
            wh.addObserver(alertService);

            // Add new product: Laptop (reorder threshold 5)
            Product laptop = new Product("P100", "Laptop", 0, 5);
            wh.addProduct(laptop);

            // Receive shipment of 10 units
            wh.receiveShipment("P100", 10);
            System.out.println("After shipment: " + wh.getProduct("P100").get());

            // Fulfill 6 orders
            wh.fulfillOrder("P100", 6); // remaining = 4 -> should trigger alert
            System.out.println("After fulfilling 6 orders: " + wh.getProduct("P100").get());

            // Demonstrate persistence
            File f = new File("inventory_main.csv");
            wh.saveInventoryToFile(f);
            System.out.println("Inventory saved to " + f.getAbsolutePath());

            // Load into a new warehouse to verify persistence
            Warehouse wh2 = manager.createWarehouse("LoadedWarehouse");
            wh2.addObserver(alertService);
            wh2.loadInventoryFromFile(f);
            System.out.println("Loaded warehouse products:");
            wh2.listProducts().forEach(System.out::println);

            // Bonus: multithreaded simulation (concurrent shipments & orders)
            System.out.println("\nStarting multithreaded simulation...");
            ExecutorService exec = Executors.newFixedThreadPool(6);
            Runnable orderTask = () -> {
                try {
                    wh.fulfillOrder("P100", 1);
                    System.out.println("Order fulfilled by " + Thread.currentThread().getName());
                } catch (Exception e) {
                    System.err.println("Order failed: " + e.getMessage());
                }
            };
            Runnable shipTask = () -> {
                try {
                    wh.receiveShipment("P100", 2);
                    System.out.println("Shipment processed by " + Thread.currentThread().getName());
                } catch (Exception e) {
                    System.err.println("Shipment failed: " + e.getMessage());
                }
            };

            // Schedule a mix of tasks
            for (int i = 0; i < 5; i++) exec.submit(orderTask);
            for (int i = 0; i < 3; i++) exec.submit(shipTask);

            exec.shutdown();
            exec.awaitTermination(5, TimeUnit.SECONDS);

            System.out.println("Final product state: " + wh.getProduct("P100").get());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

 
