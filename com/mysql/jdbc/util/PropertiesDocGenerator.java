/*    */ package com.mysql.jdbc.util;
/*    */ 
/*    */ import com.mysql.jdbc.ConnectionPropertiesImpl;
/*    */ import java.io.PrintStream;
/*    */ import java.sql.SQLException;
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
/*    */ public class PropertiesDocGenerator
/*    */   extends ConnectionPropertiesImpl
/*    */ {
/*    */   static final long serialVersionUID = -4869689139143855383L;
/*    */   
/*    */   public static void main(String[] args)
/*    */     throws SQLException
/*    */   {
/* 41 */     System.out.println(new PropertiesDocGenerator().exposeAsXml());
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\util\PropertiesDocGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */