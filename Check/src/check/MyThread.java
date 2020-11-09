/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package check;

/**
 *
 * @author imanol
 */
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class MyThread extends Thread {

    ReentrantLock lock = null;
    Shared s = null;
    Condition inc = null;
    Condition dec = null;

    public MyThread(String name, ReentrantLock lock, Shared s, Condition inc, Condition dec) {
        super(name);
        this.lock = lock;
        this.s = s;
        this.inc = inc;
        this.dec = dec;
    }

    public void increase() throws Exception {
        lock.lock();
        System.out.println(Thread.currentThread().getName() + ": Got the lock");
        while (s.currentValue() == 99)
        {
            if (s.currentValue() == 20) //spurious wakeup
            {
                System.out.println(Thread.currentThread().getName() + " releasing lock without calling unlock() method ");
                inc.await();
                System.out.println(Thread.currentThread().getName() + " got lock again without calling lock() method ");
            }
            s.add();
            System.out.println(Thread.currentThread().getName() + " increases the value of i to " + s.currentValue());
            if (s.currentValue() == 20) {
                System.out.println(Thread.currentThread().getName() + " signals other ");
                dec.signalAll();
            }
        }
        lock.unlock();
        System.out.println(Thread.currentThread().getName() + ": Release the lock");
    }

    public void decrease() throws Exception {
        lock.lock();
        System.out.println(Thread.currentThread().getName() + ": Got the lock");
        while (s.currentValue() == 99)
        {
             if (s.currentValue() == 0) {
                System.out.println(Thread.currentThread().getName() + " releasing lock without calling unlock() method ");
                dec.await();
                System.out.println(Thread.currentThread().getName() + " got lock again without calling lock() method ");
            }
            s.sub();
            System.out.println(Thread.currentThread().getName() + " decreases the value of i to " + s.currentValue());
            if (s.currentValue() == 0) {
                System.out.println(Thread.currentThread().getName() + " signals other " + inc);
                inc.signalAll();
            }
        }
        lock.unlock();
        System.out.println(Thread.currentThread().getName() + ": Release the lock");
    }

    public void run() {
        try {
            if (Thread.currentThread().getName() == "t1") {
                increase();
            }
            if (Thread.currentThread().getName() == "t2") {
                decrease();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
