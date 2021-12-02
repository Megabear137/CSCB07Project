package com.example.cscb07project;

import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

public class Database implements Contract.Model{

    public static Database database;

    public static User user;
    private static int userIndex;

    public static Store store;
    private static int storeIndex;

    public static ArrayList<Store> stores;
    public static int userCount;
    public static int storeCount;

    private Contract.View view;

    public Database(Contract.View view) {

        stores = new ArrayList<>();
        this.view = view;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserCount");
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userCount = snapshot.getValue(Integer.class);
                Log.i("demo", userCount + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ref.addValueEventListener(listener);

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("StoreCount");
        ValueEventListener listener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storeCount = snapshot.getValue(Integer.class);
                Log.i("demo", storeCount + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ref1.addValueEventListener(listener1);

    }

    public Database(){

    }

    public static Database getInstance() {
        return database;
    }

    public void updateDatabase(){
        FirebaseDatabase.getInstance().getReference().child("Users").child(userIndex + "").setValue(user);
        if(user.isStoreOwner){
            FirebaseDatabase.getInstance().getReference("Stores").child(storeIndex + "").setValue(store);
        }
        else{
            FirebaseDatabase.getInstance().getReference("Stores").setValue(stores);
        }
    }

    public void initializeUser(String username, boolean isLogin) {

        FirebaseDatabase.getInstance().getReference("UserCount").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (int i = 0; i < userCount; i++) {
                    FirebaseDatabase.getInstance().getReference("Users").child(i + "").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.getResult().child("username").getValue(String.class).equals(username)) {
                                DatabaseReference ref = task.getResult().getRef();
                                ValueEventListener listener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.child("isStoreOwner").getValue(Boolean.class)) {
                                            user = snapshot.getValue(StoreOwner.class);
                                            userIndex = Integer.parseInt(snapshot.getKey());
                                            if(isLogin) view.validateLogin(user);
                                            initializeStore(snapshot.child("storeName").getValue(String.class));
                                        } else {
                                            user = snapshot.getValue(Customer.class);
                                            userIndex = Integer.parseInt(snapshot.getKey());
                                            if(isLogin) view.validateLogin(user);
                                            initializeStores();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                };
                                ref.addValueEventListener(listener);
                            }
                        }
                    });
                }
            }
        });
    }

    public void initializeStores(){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Stores");

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stores.clear();
                for(int i = 0; i < storeCount; i++){
                    FirebaseDatabase.getInstance().getReference("Stores").child(i + "").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            stores.add(task.getResult().getValue(Store.class));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ref.addValueEventListener(listener);
    }

    public void initializeStore(String storeName){

        FirebaseDatabase.getInstance().getReference("StoreCount").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                int storeCount = task.getResult().getValue(Integer.class);
                for (int i = 0; i < storeCount; i++) {
                    FirebaseDatabase.getInstance().getReference("Stores").child(i + "").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.getResult().child("name").getValue(String.class).equals(storeName)) {
                                DatabaseReference ref = task.getResult().getRef();
                                ValueEventListener listener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            store = snapshot.getValue(Store.class);
                                            storeIndex = Integer.parseInt(snapshot.getKey());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                };
                                ref.addValueEventListener(listener);
                            }
                        }
                    });
                }
            }
        });
    }

    public void matchPass(String username, String password) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Passwords").child(username);
        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().getValue() != null && task.getResult().getValue(String.class).equals(password)) initializeUser(username, true);
                else if(task.getResult().getValue() != null) view.displayMessage("Incorrect Password");
                else view.displayMessage("User Not Found");
            }
        });
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

    public boolean isCustomer(){
        return !user.isStoreOwner;
    }

    public boolean isStoreOwner(){
        return user.isStoreOwner;
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
    public void addCustomer(String username, String password){

        FirebaseDatabase.getInstance().getReference("Passwords").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.getResult().exists()){
                    userCount++;
                    FirebaseDatabase.getInstance().getReference().child("UserCount").setValue(userCount);

                    Customer customer = new Customer(username);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userCount - 1 + "").setValue(customer);
                    FirebaseDatabase.getInstance().getReference().child("Passwords").child(username).setValue(password);

                    initializeUser(username, false);
                    initializeStores();

                    view.displayMessage("Success");
                    view.validateSignup(customer);
                }
                else{
                    view.displayMessage("Username taken");
                }
            }
        });
    }

    //Adds a new Store Owner to the Database. Returns true if successful, returns false if a user in the database already has the passed in username.
    public void addStoreOwner(String username, String password){

        FirebaseDatabase.getInstance().getReference("Passwords").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.getResult().exists()){
                    userCount++;
                    FirebaseDatabase.getInstance().getReference().child("UserCount").setValue(userCount);

                    StoreOwner storeOwner = new StoreOwner(username);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userCount - 1 + "").setValue(storeOwner);
                    FirebaseDatabase.getInstance().getReference().child("Passwords").child(username).setValue(password);
                    FirebaseDatabase.getInstance().getReference().child("StoreOwners").child(username).setValue("");

                    initializeUser(username, false);

                    view.displayMessage("Success");
                    view.validateSignup(storeOwner);
                }
                else{
                    view.displayMessage("Username taken");
                }
            }
        });

    }


    /*
    Adds a new store to the database and assigns it to the storeOwner whose username matches the passed in username.
    Returns 1 if successful
    four possible failures:
    */
    public void addStore(String storeName, String ownerName, RegisterStoreActivity rsa){

        FirebaseDatabase.getInstance().getReference("StoreOwners").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                boolean storeExists = false;
                for(DataSnapshot snapshot: task.getResult().getChildren()){
                    if(snapshot.getValue(String.class).equals(storeName)){
                        rsa.verifyRegisterStore(0);
                        storeExists = true;
                    }
                }

                if(!storeExists){
                    Store store = new Store(storeName, ownerName);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userIndex + "").child("storeName").setValue(storeName);
                    FirebaseDatabase.getInstance().getReference().child("StoreOwners").child(ownerName).setValue(storeName);
                    FirebaseDatabase.getInstance().getReference().child("Stores").child(storeCount + "").setValue(store);
                    storeCount++;
                    FirebaseDatabase.getInstance().getReference().child("StoreCount").setValue(storeCount);
                    rsa.verifyRegisterStore(1);
                }

            }
        });
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
    public int addProductToCart(String storeName, String productName, int quantity) {
        if(!isCustomer()) {
            return 0;
        }

        Customer customer = (Customer)user;

        for(Order order: customer.getCart()){
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

        Order order = new Order(customer.getUsername(), storeName);
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
        Customer customer = (Customer)user;
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
