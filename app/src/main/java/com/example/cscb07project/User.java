package com.example.cscb07project;

public abstract class User {
    String username;
    boolean isStoreOwner;

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (!username.equals(other.username))
            return false;
        return true;
    }

    public String getUsername() {
        return username;
    }

    public boolean getIsStoreOwner() {
        return isStoreOwner;
    }

    public void setIsStoreOwner(boolean isStoreOwner) {
        this.isStoreOwner = isStoreOwner;
    }
}
