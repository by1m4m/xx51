/*    */ package org.eclipse.jetty.util.component;
/*    */ 
/*    */ import java.io.FileWriter;
/*    */ import java.io.Writer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileNoticeLifeCycleListener
/*    */   implements LifeCycle.Listener
/*    */ {
/* 34 */   private static final Logger LOG = Log.getLogger(FileNoticeLifeCycleListener.class);
/*    */   
/*    */   private final String _filename;
/*    */   
/*    */   public FileNoticeLifeCycleListener(String filename)
/*    */   {
/* 40 */     this._filename = filename;
/*    */   }
/*    */   
/*    */   private void writeState(String action, LifeCycle lifecycle) {
/*    */     try {
/* 45 */       Writer out = new FileWriter(this._filename, true);Throwable localThrowable3 = null;
/*    */       try {
/* 47 */         out.append(action).append(" ").append(lifecycle.toString()).append("\n");
/*    */       }
/*    */       catch (Throwable localThrowable1)
/*    */       {
/* 45 */         localThrowable3 = localThrowable1;throw localThrowable1;
/*    */       }
/*    */       finally {
/* 48 */         if (localThrowable3 != null) try { out.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else out.close();
/*    */       }
/*    */     } catch (Exception e) {
/* 51 */       LOG.warn(e);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public void lifeCycleStarting(LifeCycle event)
/*    */   {
/* 58 */     writeState("STARTING", event);
/*    */   }
/*    */   
/*    */ 
/*    */   public void lifeCycleStarted(LifeCycle event)
/*    */   {
/* 64 */     writeState("STARTED", event);
/*    */   }
/*    */   
/*    */ 
/*    */   public void lifeCycleFailure(LifeCycle event, Throwable cause)
/*    */   {
/* 70 */     writeState("FAILED", event);
/*    */   }
/*    */   
/*    */ 
/*    */   public void lifeCycleStopping(LifeCycle event)
/*    */   {
/* 76 */     writeState("STOPPING", event);
/*    */   }
/*    */   
/*    */ 
/*    */   public void lifeCycleStopped(LifeCycle event)
/*    */   {
/* 82 */     writeState("STOPPED", event);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\component\FileNoticeLifeCycleListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */