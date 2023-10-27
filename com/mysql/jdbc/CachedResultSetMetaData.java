/*    */ package com.mysql.jdbc;
/*    */ 
/*    */ import java.sql.ResultSetMetaData;
/*    */ import java.util.Map;
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
/*    */ public class CachedResultSetMetaData
/*    */ {
/* 30 */   Map<String, Integer> columnNameToIndex = null;
/*    */   
/*    */ 
/*    */   Field[] fields;
/*    */   
/*    */ 
/* 36 */   Map<String, Integer> fullColumnNameToIndex = null;
/*    */   
/*    */   ResultSetMetaData metadata;
/*    */   
/*    */   public Map<String, Integer> getColumnNameToIndex()
/*    */   {
/* 42 */     return this.columnNameToIndex;
/*    */   }
/*    */   
/*    */   public Field[] getFields() {
/* 46 */     return this.fields;
/*    */   }
/*    */   
/*    */   public Map<String, Integer> getFullColumnNameToIndex() {
/* 50 */     return this.fullColumnNameToIndex;
/*    */   }
/*    */   
/*    */   public ResultSetMetaData getMetadata() {
/* 54 */     return this.metadata;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\CachedResultSetMetaData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */