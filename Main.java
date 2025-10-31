  import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        WarehouseManager manager = new WarehouseManager();
        AlertService alertService = new AlertService();

        while (true) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Create New Warehouse");
            System.out.println("2. Add Product to Warehouse");
            System.out.println("3. Receive Shipment");
            System.out.println("4. Fulfill Order");
            System.out.println("5. Show Warehouse Products");
            System.out.println("6. Show All Warehouses");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Warehouse Name: ");
                    String wname = sc.nextLine();
                    manager.addWarehouse(wname, alertService);
                    break;

                case 2:
                    System.out.print("Enter Warehouse Name: ");
                    String whAdd = sc.nextLine();
                    Warehouse wAdd = manager.getWarehouse(whAdd);
                    if (wAdd == null) {
                        System.out.println("❌ Warehouse not found!");
                        break;
                    }
                    System.out.print("Enter Product ID: ");
                    String id = sc.nextLine();
                    System.out.print("Enter Product Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Initial Quantity: ");
                    int qty = sc.nextInt();
                    System.out.print("Enter Threshold: ");
                    int threshold = sc.nextInt();
                    sc.nextLine();
                    wAdd.addProduct(new Product(id, name, qty), threshold);
                    break;

                case 3:
                    System.out.print("Enter Warehouse Name: ");
                    String wRec = sc.nextLine();
                    Warehouse rec = manager.getWarehouse(wRec);
                    if (rec == null) {
                        System.out.println("❌ Warehouse not found!");
                        break;
                    }
                    System.out.print("Enter Product ID: ");
                    String pid = sc.nextLine();
                    System.out.print("Enter Quantity Received: ");
                    int qRec = sc.nextInt();
                    sc.nextLine();
                    rec.receiveShipment(pid, qRec);
                    break;

                case 4:
                    System.out.print("Enter Warehouse Name: ");
                    String wFul = sc.nextLine();
                    Warehouse ful = manager.getWarehouse(wFul);
                    if (ful == null) {
                        System.out.println("❌ Warehouse not found!");
                        break;
                    }
                    System.out.print("Enter Product ID: ");
                    String fid = sc.nextLine();
                    System.out.print("Enter Quantity to Fulfill: ");
                    int fqty = sc.nextInt();
                    sc.nextLine();
                    ful.fulfillOrder(fid, fqty);
                    break;

                case 5:
                    System.out.print("Enter Warehouse Name: ");
                    String wShow = sc.nextLine();
                    Warehouse show = manager.getWarehouse(wShow);
                    if (show == null) {
                        System.out.println("❌ Warehouse not found!");
                        break;
                    }
                    show.showAllProducts();
                    break;

                case 6:
                    manager.showAllWarehouses();
                    break;

                case 7:
                    System.out.println("🚪 Exiting System. Goodbye!");
                    sc.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("❌ Invalid choice! Try again.");
            }
        }
    }
}
