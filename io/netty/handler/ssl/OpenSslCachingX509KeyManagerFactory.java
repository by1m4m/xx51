/*    */ package io.netty.handler.ssl;
/*    */ 
/*    */ import java.security.InvalidAlgorithmParameterException;
/*    */ import java.security.KeyStore;
/*    */ import java.security.KeyStoreException;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.security.UnrecoverableKeyException;
/*    */ import javax.net.ssl.KeyManager;
/*    */ import javax.net.ssl.KeyManagerFactory;
/*    */ import javax.net.ssl.KeyManagerFactorySpi;
/*    */ import javax.net.ssl.ManagerFactoryParameters;
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
/*    */ public final class OpenSslCachingX509KeyManagerFactory
/*    */   extends KeyManagerFactory
/*    */ {
/*    */   public OpenSslCachingX509KeyManagerFactory(KeyManagerFactory factory)
/*    */   {
/* 41 */     super(new KeyManagerFactorySpi()
/*    */     {
/*    */       protected void engineInit(KeyStore keyStore, char[] chars) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException
/*    */       {
/* 45 */         OpenSslCachingX509KeyManagerFactory.this.init(keyStore, chars);
/*    */       }
/*    */       
/*    */       protected void engineInit(ManagerFactoryParameters managerFactoryParameters)
/*    */         throws InvalidAlgorithmParameterException
/*    */       {
/* 51 */         OpenSslCachingX509KeyManagerFactory.this.init(managerFactoryParameters);
/*    */       }
/*    */       
/*    */       protected KeyManager[] engineGetKeyManagers()
/*    */       {
/* 56 */         return OpenSslCachingX509KeyManagerFactory.this.getKeyManagers();
/*    */       }
/* 58 */     }, factory.getProvider(), factory.getAlgorithm());
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\OpenSslCachingX509KeyManagerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */