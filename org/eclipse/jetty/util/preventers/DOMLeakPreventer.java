/*    */ package org.eclipse.jetty.util.preventers;
/*    */ 
/*    */ import javax.xml.parsers.DocumentBuilderFactory;
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
/*    */ public class DOMLeakPreventer
/*    */   extends AbstractLeakPreventer
/*    */ {
/*    */   public void prevent(ClassLoader loader)
/*    */   {
/* 44 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*    */     try
/*    */     {
/* 47 */       factory.newDocumentBuilder();
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 51 */       LOG.warn(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\preventers\DOMLeakPreventer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */