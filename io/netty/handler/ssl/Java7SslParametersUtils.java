/*    */ package io.netty.handler.ssl;
/*    */ 
/*    */ import java.security.AlgorithmConstraints;
/*    */ import javax.net.ssl.SSLParameters;
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
/*    */ final class Java7SslParametersUtils
/*    */ {
/*    */   static void setAlgorithmConstraints(SSLParameters sslParameters, Object algorithmConstraints)
/*    */   {
/* 33 */     sslParameters.setAlgorithmConstraints((AlgorithmConstraints)algorithmConstraints);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\Java7SslParametersUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */