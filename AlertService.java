public class AlertService implements StockObserver {
    @Override
    public void onLowStock(String warehouseName, Product product) {
        // Simple console alert; could be extended to email/SMS, etc.
        System.out.printf("Restock Alert [%s]: Low stock for %s (ID: %s) — only %d left! Threshold=%d%n",
                warehouseName,
                product.getName(),
                product.getId(),
                product.getQuantity(),
                product.getReorderThreshold());
    }
}

 