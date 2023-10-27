/*    */ package org.apache.commons.net.discard;
/*    */ 
/*    */ import java.io.OutputStream;
/*    */ import org.apache.commons.net.SocketClient;
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
/*    */ public class DiscardTCPClient
/*    */   extends SocketClient
/*    */ {
/*    */   public static final int DEFAULT_PORT = 9;
/*    */   
/*    */   public DiscardTCPClient()
/*    */   {
/* 49 */     setDefaultPort(9);
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
/*    */   public OutputStream getOutputStream()
/*    */   {
/* 63 */     return this._output_;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\discard\DiscardTCPClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */