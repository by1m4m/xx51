/*    */ package org.eclipse.jetty.util.component;
/*    */ 
/*    */ import org.eclipse.jetty.util.log.Log;
/*    */ import org.eclipse.jetty.util.log.Logger;
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
/*    */ public class StopLifeCycle
/*    */   extends AbstractLifeCycle
/*    */   implements LifeCycle.Listener
/*    */ {
/* 30 */   private static final Logger LOG = Log.getLogger(StopLifeCycle.class);
/*    */   
/*    */   private final LifeCycle _lifecycle;
/*    */   
/*    */   public StopLifeCycle(LifeCycle lifecycle)
/*    */   {
/* 36 */     this._lifecycle = lifecycle;
/* 37 */     addLifeCycleListener(this);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void lifeCycleStarting(LifeCycle lifecycle) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void lifeCycleStarted(LifeCycle lifecycle)
/*    */   {
/*    */     try
/*    */     {
/* 50 */       this._lifecycle.stop();
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 54 */       LOG.warn(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public void lifeCycleFailure(LifeCycle lifecycle, Throwable cause) {}
/*    */   
/*    */   public void lifeCycleStopping(LifeCycle lifecycle) {}
/*    */   
/*    */   public void lifeCycleStopped(LifeCycle lifecycle) {}
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\component\StopLifeCycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */