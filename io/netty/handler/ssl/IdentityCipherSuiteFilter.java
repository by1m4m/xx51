/*    */ package io.netty.handler.ssl;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Set;
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
/*    */ public final class IdentityCipherSuiteFilter
/*    */   implements CipherSuiteFilter
/*    */ {
/* 30 */   public static final IdentityCipherSuiteFilter INSTANCE = new IdentityCipherSuiteFilter(true);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 35 */   public static final IdentityCipherSuiteFilter INSTANCE_DEFAULTING_TO_SUPPORTED_CIPHERS = new IdentityCipherSuiteFilter(false);
/*    */   
/*    */   private final boolean defaultToDefaultCiphers;
/*    */   
/*    */   private IdentityCipherSuiteFilter(boolean defaultToDefaultCiphers)
/*    */   {
/* 41 */     this.defaultToDefaultCiphers = defaultToDefaultCiphers;
/*    */   }
/*    */   
/*    */ 
/*    */   public String[] filterCipherSuites(Iterable<String> ciphers, List<String> defaultCiphers, Set<String> supportedCiphers)
/*    */   {
/* 47 */     if (ciphers == null) {
/* 48 */       return this.defaultToDefaultCiphers ? 
/* 49 */         (String[])defaultCiphers.toArray(new String[0]) : 
/* 50 */         (String[])supportedCiphers.toArray(new String[0]);
/*    */     }
/* 52 */     List<String> newCiphers = new ArrayList(supportedCiphers.size());
/* 53 */     for (String c : ciphers) {
/* 54 */       if (c == null) {
/*    */         break;
/*    */       }
/* 57 */       newCiphers.add(c);
/*    */     }
/* 59 */     return (String[])newCiphers.toArray(new String[0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\IdentityCipherSuiteFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */