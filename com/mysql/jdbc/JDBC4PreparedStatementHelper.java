/*    */ package com.mysql.jdbc;
/*    */ 
/*    */ import java.io.Reader;
/*    */ import java.sql.NClob;
/*    */ import java.sql.RowId;
/*    */ import java.sql.SQLException;
/*    */ import java.sql.SQLXML;
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
/*    */ public class JDBC4PreparedStatementHelper
/*    */ {
/*    */   static void setRowId(PreparedStatement pstmt, int parameterIndex, RowId x)
/*    */     throws SQLException
/*    */   {
/* 41 */     throw SQLError.notImplemented();
/*    */   }
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
/*    */   static void setNClob(PreparedStatement pstmt, int parameterIndex, NClob value)
/*    */     throws SQLException
/*    */   {
/* 57 */     if (value == null) {
/* 58 */       pstmt.setNull(parameterIndex, 2011);
/*    */     } else {
/* 60 */       pstmt.setNCharacterStream(parameterIndex, value.getCharacterStream(), value.length());
/*    */     }
/*    */   }
/*    */   
/*    */   static void setNClob(PreparedStatement pstmt, int parameterIndex, Reader reader) throws SQLException {
/* 65 */     pstmt.setNCharacterStream(parameterIndex, reader);
/*    */   }
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
/*    */   static void setNClob(PreparedStatement pstmt, int parameterIndex, Reader reader, long length)
/*    */     throws SQLException
/*    */   {
/* 83 */     if (reader == null) {
/* 84 */       pstmt.setNull(parameterIndex, 2011);
/*    */     } else {
/* 86 */       pstmt.setNCharacterStream(parameterIndex, reader, length);
/*    */     }
/*    */   }
/*    */   
/*    */   static void setSQLXML(PreparedStatement pstmt, int parameterIndex, SQLXML xmlObject) throws SQLException
/*    */   {
/* 92 */     if (xmlObject == null) {
/* 93 */       pstmt.setNull(parameterIndex, 2009);
/*    */     }
/*    */     else {
/* 96 */       pstmt.setCharacterStream(parameterIndex, ((JDBC4MysqlSQLXML)xmlObject).serializeAsCharacterStream());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\JDBC4PreparedStatementHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */