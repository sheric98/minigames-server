package database;

import user.User;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Database {
    long key = 0;
    HashMap<Long, User> users = new HashMap<>();
    private final Lock keyGenLock = new ReentrantLock();
    private static Database instance = null;

    public static Database getGlobalDB() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public long generateKey() {
        keyGenLock.lock();
        long retKey = this.key;
        key++;
        keyGenLock.unlock();
        return retKey;
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public User getUser(long id) {
        return users.get(id);
    }
}
