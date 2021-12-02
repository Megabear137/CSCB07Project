package com.example.cscb07project;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Database implements Contract.Model{

    private static Database database = null;
    private ArrayList<User> users;
    private ArrayList<Store> stores;
    private static int userCount;
    private static int storeCount;
    HashMap<String, String> passwords;

    private Database() {

        users = new ArrayList<>();
        stores = new ArrayList<>();
        passwords = new HashMap<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        //Initializes userCount variable by getting currently stored value in Firebase Database
        ref.child("UserCount").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("demo", "Error getting data");
                } else {
                    userCount = Integer.parseInt(task.getResult().getValue().toString());
                    //Initializes users array list by getting currently stored users in Firebase Database. Also gets hashmap of passwords
                    //store in database
                    for (int i = 0; i < userCount; i++) {
                        int finalI = i;
                        ref.child("Users").child(finalI + "").child("isStoreOwner").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.getResult().getValue(Boolean.class)) {
                                    ref.child("Users").child(finalI + "").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            users.add(task.getResult().getValue(Customer.class));
                                            String username = task.getResult().getValue(Customer.class).getUsername();
                                            ref.child("Passwords").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                    passwords.put(username, task.getResult().getValue(String.class));
                                                }
                                            });
                                        }
                                    });
                                } else {
                                    ref.child("Users").child(finalI + "").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            users.add(task.getResult().getValue(StoreOwner.class));
                                            String username = task.getResult().getValue(Customer.class).getUsername();
                                            ref.child("Passwords").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                    passwords.put(username, task.getResult().getValue(String.class));
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });

                    }

                }
            }
        });

        ref.child("StoreCount").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("demo", "Error getting data");
                } else {
                    storeCount = Integer.parseInt(task.getResult().getValue().toString());
                    //Initializes stores array list by getting currently stored stores in Firebase Database
                    for (int i = 0; i < storeCount; i++) {
                        ref.child("Stores").child(i + "").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                stores.add(task.getResult().getValue(Store.class));
                            }
                        });
                    }

                }
            }
        });
    }

    //Function that is used to get an instance of the database. Getting an instance allows usage of functions in this class
    public static Database getInstance(){
        if(database == null)
            database = new Database();
        return database;
    }

    public void updateDatabase(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").setValue(users);
        ref.child("Stores").setValue(stores);
        ref.child("UserCount").setValue(users.size());
        ref.child("StoreCount").setValue(stores.size());
    }

    public boolean userExists(String username){
        for(User user: users){
            if(user.getUsername().equals(username))
                return true;
        }
        return false;
    }

    public boolean matchPass(String username, String password) {
        for (String user : passwords.keySet()) {
            if (user.equals(username) && password.equals(passwords.get(user))) {
                return true;
            }
        }
        return false;
    }

    public boolean storeExists(String storeName){
        for (Store store: stores){
            if(store.getName().equals(storeName))
                return true;
        }
        return false;
    }

    public boolean productExists(String productName){
        for (Store store: stores){
            for(Product product: store.products){
                if(product.getName().equals(productName)) return true;
            }
        }

        return false;
    }

    public boolean isCustomer(String username){
        for(User user: users){
            if(user.getUsername().equals(username) && !user.isStoreOwner)
                return true;
        }
        return false;
    }

    public boolean isStoreOwner(String username){
        for(User user: users){
            if(user.getUsername().equals(username) && user.isStoreOwner)
                return true;
        }

        return false;
    }

    //Returns Customer/StoreOwner object with matching username stored in Firebase Database. Returns null if no user has
    //matching username.
    public Customer findCustomer(String username){

        for (User user: users){
            if(user.getUsername().equals(username) && isCustomer(username))
                return (Customer)user;
        }

        return null;
    }

    public StoreOwner findStoreOwner(String username){
        for (User user: users){
            if(user.getUsername().equals(username) && !isCustomer(username))
                return (StoreOwner)user;
        }
        return null;
    }

    public Product findProduct(String productName, String storeName){
        for (Store store: stores){
            if(store.getName().equals(storeName)){
                for(Product product: store.products){
                    if(product.getName().equals(productName)) return product;
                }
            }
        }

        return null;

    }

    //Returns Store object with matching storeName stored in Firebase Database. Returns null if no store has
    //matching storeName.
    public Store findStore(String storeName){
        for (Store store: stores){
            if(store.getName().equals(storeName))
                return store;
        }
        return null;
    }

    //Adds a new customer to the Database. Returns true if successful, returns false if a user in the database already has the passed in username.
    public boolean addCustomer(String username, String password){

        if(userExists(username))
            return false;

        userCount++;
        FirebaseDatabase.getInstance().getReference().child("UserCount").setValue(userCount);

        Customer customer = new Customer(username);
        users.add(customer);
        FirebaseDatabase.getInstance().getReference().child("Passwords").child(username).setValue(password);

        updateDatabase();
        return true;
    }

    //Adds a new Store Owner to the Database. Returns true if successful, returns false if a user in the database already has the passed in username.
    public boolean addStoreOwner(String username, String password){
        if(userExists(username))
            return false;

        userCount++;

        StoreOwner storeOwner = new StoreOwner(username);
        users.add(storeOwner);
        FirebaseDatabase.getInstance().getReference().child("Passwords").child(username).setValue(password);
        updateDatabase();
        return true;
    }


    /*
    Adds a new store to the database and assigns it to the storeOwner whose username matches the passed in username.
    Returns 1 if successful
    four possible failures:
    returns -1 if a store in the database already has the passed in storeName
    returns -2 if StoreOwner already has a store
    returns -3 if passed in username belongs to a customer, not a StoreOwner.
    returns -4 if passed in username belongs to no one
    */
    public int addStore(String storeName, String ownerName){

        if(storeExists(storeName)) return -1;
        if(!findStoreOwner(ownerName).getStoreName().equals("")) return -2;
        if(!userExists(ownerName)) return -3;
        if(isCustomer(ownerName)) return -4;

        storeCount++;

        Store store = new Store(storeName, ownerName);
        findStoreOwner(ownerName).setStoreName(storeName);
        stores.add(store);
        updateDatabase();
        return 1;
    }

    //Adds product to store with storeName.
    //returns 1 if successful
    //return 0 if no store has storeName as its name
    public int addProductToStore(String storeName, Product product){
        if(!storeExists(storeName)) return 0;

        findStore(storeName).products.add(product);
        updateDatabase();
        return 1;
    }

    /* Adds a product from the store of specified quantity to cart belonging to specified customer.
        return 1 if successful.
        return 0 if no customer has a matching username
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int addProductToCart(String customerName, String storeName, String productName, int quantity) {
        if(!isCustomer(customerName)) {
            return 0;
        }

        Customer customer = findCustomer(customerName);


        for(Order order: findCustomer(customerName).getCart()){
            if(order.getStoreName().equals(storeName)) {
                if(order.products.containsKey(productName)){
                    int productNum = order.products.get(productName);
                    order.products.replace(productName, productNum + quantity);
                }
                else{
                    order.products.put(productName, quantity);
                }
                updateDatabase();
                return 1;
            }
        }

        Order order = new Order(customerName, storeName);
        order.products.put(productName, quantity);
        customer.cart.add(order);

        updateDatabase();
        return 1;
    }

    /* Deletes one piece of product from cart belonging to specified customer.
    return 1 if successful.
    return 0 if no customer has a matching username.
    return -1 if customer exists but has no more specified product to be deleted.
    Assumption: all products in cart will have count at least 1.

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int deleteProductFromCart(Customer customer, Product product) {
        if(!isCustomer(customer.getUsername())) {
            return 0;
        }

        HashMap<String, String> newCart = customer.getCart();
        String productName = product.getName();
        if (!newCart.containsKey(productName))
            return -1;

        int quantity = Integer.parseInt(Objects.requireNonNull(newCart.get(productName)));
        if(quantity == 1)
            newCart.remove(productName);
        else
            newCart.replace(productName, String.valueOf(quantity-1));
        customer.setCart(newCart);
        updateDatabase();
        return 1;
    }
    */

    /* Make all products from the store in customer's cart into an order. Then add it
        to customer.pendingOrder and store.incomingOrder.
        return 1 if successful.
          return -1 if customer not found.
          return -2 if store not found.
    public int makeOrder(Customer customer, Store store) {
        if (!isCustomer(customer.getUsername()))
            return -1;

        if (findStore(store.getName())==null)
            return -2;

        HashMap<String ,String> oldCart = customer.getCart();
        HashMap<String ,String> newCart = new HashMap<String, String>();
        HashMap<String, String> productsInOrder = new HashMap<String, String>();
        for (String productName: oldCart.keySet()) {
            Product product = store.findProduct(productName);
            if (product!=null && product.getBrand().equals(store.getName()))
                productsInOrder.put(productName, oldCart.get(productName));
            else
                newCart.put(productName, oldCart.get(productName));
        }

        customer.setCart(newCart);
        Order order = new Order(customer.getUsername(), store.getName(), productsInOrder);
        /*ArrayList<Order> newPendingOrders = customer.getPendingOrders();
        newPendingOrders.add(order);
        customer.setPendingOrders(newPendingOrders);
        ArrayList<Order> newIncomingOrders = store.getIncomingOrders();
        newIncomingOrders.add(order);
        store.setIncomingOrders(newIncomingOrders);*/
        /*customer.pendingOrders.add(order);
        store.incomingOrders.add(order);
        updateDatabase();
        return 1;
    }
    */

    /* Move the order from incomingOrder to outgoingOrder and pendingOrder to CompletedOrder. */
    public int fulfillOrder(Order order) {
        Store store = findStore(order.getStoreName());
        Customer customer = findCustomer(order.getCustomerName());
        if (store == null || customer == null) {
            return 0;
        }

        store.incomingOrders.remove(order);
        store.outgoingOrders.add(order);
        customer.pendingOrders.remove(order);
        customer.completedOrders.add(order);
        updateDatabase();
        return 1;
    }

    //Returns Product object in database given a store name and product name. Returns null if
    // store or product does not exist
    public Product findProductInStore(String storeName, String productName){
        for (Store store: stores){
            if(store.getName().equals(storeName)){
                ArrayList<Product> products = store.getProducts();
                for (Product product: products){
                    if (product.getName().equals(productName)) {
                        return product;
                    }
                }
                return null;
            }

        }
        return null;
    }

    //Returns true iff product is found in a store, given the store name and product name
    public boolean productExists(String storeName, String productName){
        if (findProductInStore(storeName,productName) == null) {
            return false;
        }
        return true;
    }

    //Returns 1 upon successfully updating the database with edited product, when given a Product,
    // store name, new name, new brand name, new price.
    //Returns -1 on invalid product, -2 on invalid store name, -3 on invalid product name.
    // -4 on invalid brand name, -5 on invalid price

    public int editProduct(Product product, String storeName, String newName, String newBrand,
                           double newPrice) {
        if (product == null)
            return -1;
        Store store = findStore(storeName);
        if (store == null || storeName == null || storeName.isEmpty())
            return -2;
        if (newName.isEmpty())
            return -3;
        if(newBrand.isEmpty()){
            return -4;
        }
        if(newPrice < 0) {
            return -5;
        }

        store.editProductInfo(product,newName, newBrand,newPrice);
        updateDatabase();
        return 1;


    }




    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public static int getStoreCount() {
        return storeCount;
    }

    int getUserCount(){
        return userCount;
    }
}
