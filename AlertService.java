  public class AlertService implements StockObserver {
    @Override
    public void onLowStock(String warehouseName, Product product, int threshold) {
        System.out.println("⚠️ [ALERT] Warehouse: " + warehouseName 
            + " | Low stock for " + product.getName() 
            + " (Remaining: " + product.getQuantity() 
            + ", Threshold: " + threshold + ")");
    }
}

 