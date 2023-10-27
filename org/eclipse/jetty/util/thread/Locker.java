/*    */ package org.eclipse.jetty.util.thread;
/*    */ 
/*    */ import java.util.concurrent.locks.Condition;
/*    */ import java.util.concurrent.locks.ReentrantLock;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Locker
/*    */ {
/* 36 */   private final ReentrantLock _lock = new ReentrantLock();
/* 37 */   private final Lock _unlock = new Lock();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Lock lock()
/*    */   {
/* 46 */     this._lock.lock();
/* 47 */     return this._unlock;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public Lock lockIfNotHeld()
/*    */   {
/* 57 */     return lock();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isLocked()
/*    */   {
/* 65 */     return this._lock.isLocked();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Condition newCondition()
/*    */   {
/* 73 */     return this._lock.newCondition();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public class Lock
/*    */     implements AutoCloseable
/*    */   {
/*    */     public Lock() {}
/*    */     
/*    */ 
/* 84 */     public void close() { Locker.this._lock.unlock(); }
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   public class UnLock extends Locker.Lock {
/* 89 */     public UnLock() { super(); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\thread\Locker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */