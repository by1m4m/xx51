/*    */ package com.maxmind.db;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class ClosedDatabaseException
/*    */   extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   ClosedDatabaseException()
/*    */   {
/* 13 */     super("The MaxMind DB has been closed.");
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\db\ClosedDatabaseException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */