/*    */ package org.eclipse.jetty.util.preventers;
/*    */ 
/*    */ import java.lang.reflect.Method;
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
/*    */ public class GCThreadLeakPreventer
/*    */   extends AbstractLeakPreventer
/*    */ {
/*    */   public void prevent(ClassLoader loader)
/*    */   {
/*    */     try
/*    */     {
/* 50 */       Class<?> clazz = Class.forName("sun.misc.GC");
/* 51 */       Method requestLatency = clazz.getMethod("requestLatency", new Class[] { Long.TYPE });
/* 52 */       requestLatency.invoke(null, new Object[] { Long.valueOf(9223372036854775806L) });
/*    */     }
/*    */     catch (ClassNotFoundException e)
/*    */     {
/* 56 */       LOG.ignore(e);
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 60 */       LOG.warn(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\preventers\GCThreadLeakPreventer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */