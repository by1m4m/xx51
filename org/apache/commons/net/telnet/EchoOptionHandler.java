/*    */ package org.apache.commons.net.telnet;
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
/*    */ public class EchoOptionHandler
/*    */   extends TelnetOptionHandler
/*    */ {
/*    */   public EchoOptionHandler(boolean initlocal, boolean initremote, boolean acceptlocal, boolean acceptremote)
/*    */   {
/* 39 */     super(1, initlocal, initremote, acceptlocal, acceptremote);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public EchoOptionHandler()
/*    */   {
/* 49 */     super(1, false, false, false, false);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\telnet\EchoOptionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */