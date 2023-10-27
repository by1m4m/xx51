/*    */ package org.apache.log4j;
/*    */ 
/*    */ import org.apache.log4j.spi.LoggerFactory;
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
/*    */ class DefaultCategoryFactory
/*    */   implements LoggerFactory
/*    */ {
/*    */   public Logger makeNewLoggerInstance(String name)
/*    */   {
/* 29 */     return new Logger(name);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\log4j\DefaultCategoryFactory.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */