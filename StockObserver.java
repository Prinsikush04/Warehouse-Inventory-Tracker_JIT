public interface StockObserver {
    /**
     * Called when a product's stock is below its reorder threshold.
     * @param warehouseName name of the warehouse where the event happened
     * @param product the product that is low
     */
    void onLowStock(String warehouseName, Product product);
}

 