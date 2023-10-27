/*    */ package org.eclipse.jetty.client;
/*    */ 
/*    */ import java.nio.ByteBuffer;
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
/*    */ public abstract interface ContentDecoder
/*    */ {
/*    */   public abstract ByteBuffer decode(ByteBuffer paramByteBuffer);
/*    */   
/*    */   public static abstract class Factory
/*    */   {
/*    */     private final String encoding;
/*    */     
/*    */     protected Factory(String encoding)
/*    */     {
/* 53 */       this.encoding = encoding;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     public String getEncoding()
/*    */     {
/* 61 */       return this.encoding;
/*    */     }
/*    */     
/*    */ 
/*    */     public boolean equals(Object obj)
/*    */     {
/* 67 */       if (this == obj) return true;
/* 68 */       if (!(obj instanceof Factory)) return false;
/* 69 */       Factory that = (Factory)obj;
/* 70 */       return this.encoding.equals(that.encoding);
/*    */     }
/*    */     
/*    */ 
/*    */     public int hashCode()
/*    */     {
/* 76 */       return this.encoding.hashCode();
/*    */     }
/*    */     
/*    */     public abstract ContentDecoder newContentDecoder();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\ContentDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */