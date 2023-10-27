/*    */ package io.netty.handler.ssl;
/*    */ 
/*    */ import io.netty.buffer.ByteBufAllocator;
/*    */ import io.netty.internal.tcnative.SSL;
/*    */ import java.security.PrivateKey;
/*    */ import java.security.cert.X509Certificate;
/*    */ import javax.net.ssl.X509KeyManager;
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
/*    */ class OpenSslKeyMaterialProvider
/*    */ {
/*    */   private final X509KeyManager keyManager;
/*    */   private final String password;
/*    */   
/*    */   OpenSslKeyMaterialProvider(X509KeyManager keyManager, String password)
/*    */   {
/* 36 */     this.keyManager = keyManager;
/* 37 */     this.password = password;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   X509KeyManager keyManager()
/*    */   {
/* 44 */     return this.keyManager;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   OpenSslKeyMaterial chooseKeyMaterial(ByteBufAllocator allocator, String alias)
/*    */     throws Exception
/*    */   {
/* 52 */     X509Certificate[] certificates = this.keyManager.getCertificateChain(alias);
/* 53 */     if ((certificates == null) || (certificates.length == 0)) {
/* 54 */       return null;
/*    */     }
/*    */     
/* 57 */     PrivateKey key = this.keyManager.getPrivateKey(alias);
/* 58 */     PemEncoded encoded = PemX509Certificate.toPEM(allocator, true, certificates);
/* 59 */     long chainBio = 0L;
/* 60 */     long pkeyBio = 0L;
/* 61 */     long chain = 0L;
/* 62 */     long pkey = 0L;
/*    */     try {
/* 64 */       chainBio = ReferenceCountedOpenSslContext.toBIO(allocator, encoded.retain());
/* 65 */       chain = SSL.parseX509Chain(chainBio);
/*    */       OpenSslKeyMaterial keyMaterial;
/*    */       OpenSslKeyMaterial keyMaterial;
/* 68 */       if ((key instanceof OpenSslPrivateKey)) {
/* 69 */         keyMaterial = ((OpenSslPrivateKey)key).toKeyMaterial(chain);
/*    */       } else {
/* 71 */         pkeyBio = ReferenceCountedOpenSslContext.toBIO(allocator, key);
/* 72 */         pkey = key == null ? 0L : SSL.parsePrivateKey(pkeyBio, this.password);
/* 73 */         keyMaterial = new DefaultOpenSslKeyMaterial(chain, pkey);
/*    */       }
/*    */       
/*    */ 
/*    */ 
/* 78 */       chain = 0L;
/* 79 */       pkey = 0L;
/* 80 */       return keyMaterial;
/*    */     } finally {
/* 82 */       SSL.freeBIO(chainBio);
/* 83 */       SSL.freeBIO(pkeyBio);
/* 84 */       if (chain != 0L) {
/* 85 */         SSL.freeX509Chain(chain);
/*    */       }
/* 87 */       if (pkey != 0L) {
/* 88 */         SSL.freePrivateKey(pkey);
/*    */       }
/* 90 */       encoded.release();
/*    */     }
/*    */   }
/*    */   
/*    */   void destroy() {}
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\OpenSslKeyMaterialProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */