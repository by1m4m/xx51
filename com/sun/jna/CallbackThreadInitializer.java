/*    */ package com.sun.jna;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CallbackThreadInitializer
/*    */ {
/*    */   private boolean daemon;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private boolean detach;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private String name;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private ThreadGroup group;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public CallbackThreadInitializer()
/*    */   {
/* 42 */     this(true);
/*    */   }
/*    */   
/*    */ 
/*    */   public CallbackThreadInitializer(boolean daemon)
/*    */   {
/* 48 */     this(daemon, false);
/*    */   }
/*    */   
/*    */   public CallbackThreadInitializer(boolean daemon, boolean detach) {
/* 52 */     this(daemon, detach, null);
/*    */   }
/*    */   
/*    */   public CallbackThreadInitializer(boolean daemon, boolean detach, String name) {
/* 56 */     this(daemon, detach, name, null);
/*    */   }
/*    */   
/*    */   public CallbackThreadInitializer(boolean daemon, boolean detach, String name, ThreadGroup group) {
/* 60 */     this.daemon = daemon;
/* 61 */     this.detach = detach;
/* 62 */     this.name = name;
/* 63 */     this.group = group;
/*    */   }
/*    */   
/*    */ 
/* 67 */   public String getName(Callback cb) { return this.name; }
/*    */   
/* 69 */   public ThreadGroup getThreadGroup(Callback cb) { return this.group; }
/*    */   
/* 71 */   public boolean isDaemon(Callback cb) { return this.daemon; }
/*    */   
/*    */   public boolean detach(Callback cb)
/*    */   {
/* 75 */     return this.detach;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\CallbackThreadInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */