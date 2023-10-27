/*    */ package org.apache.http.impl.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.apache.http.io.SessionInputBuffer;
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
/*    */ public class IdentityInputStream
/*    */   extends InputStream
/*    */ {
/*    */   private final SessionInputBuffer in;
/* 58 */   private boolean closed = false;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public IdentityInputStream(SessionInputBuffer in)
/*    */   {
/* 67 */     if (in == null) {
/* 68 */       throw new IllegalArgumentException("Session input buffer may not be null");
/*    */     }
/* 70 */     this.in = in;
/*    */   }
/*    */   
/*    */   public int available() throws IOException {
/* 74 */     if ((!this.closed) && (this.in.isDataAvailable(10))) {
/* 75 */       return 1;
/*    */     }
/* 77 */     return 0;
/*    */   }
/*    */   
/*    */   public void close() throws IOException
/*    */   {
/* 82 */     this.closed = true;
/*    */   }
/*    */   
/*    */   public int read() throws IOException {
/* 86 */     if (this.closed) {
/* 87 */       return -1;
/*    */     }
/* 89 */     return this.in.read();
/*    */   }
/*    */   
/*    */   public int read(byte[] b, int off, int len) throws IOException
/*    */   {
/* 94 */     if (this.closed) {
/* 95 */       return -1;
/*    */     }
/* 97 */     return this.in.read(b, off, len);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\io\IdentityInputStream.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */