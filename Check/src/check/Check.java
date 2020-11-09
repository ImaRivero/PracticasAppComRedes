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
import java.util.concurrent.locks.*;
class Check
{
 public static void main(String[] args)
 {
  ReentrantLock l = new ReentrantLock();
  Condition inc = l.newCondition();
  Condition dec = l.newCondition();
  Shared s = new Shared();
  MyThread t1 = new MyThread("t1",l,s,inc,dec);
  MyThread t2 = new MyThread("t2",l,s,inc,dec);
  t1.start();
  t2.start();
 }
} 