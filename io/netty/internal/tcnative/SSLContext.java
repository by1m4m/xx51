/*     */ package io.netty.internal.tcnative;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SSLContext
/*     */ {
/*     */   public static native long make(int paramInt1, int paramInt2)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native int free(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native void setContextId(long paramLong, String paramString);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native void setOptions(long paramLong, int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native int getOptions(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native void clearOptions(long paramLong, int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static boolean setCipherSuite(long ctx, String ciphers)
/*     */     throws Exception
/*     */   {
/* 117 */     return setCipherSuite(ctx, ciphers, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native boolean setCipherSuite(long paramLong, String paramString, boolean paramBoolean)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native boolean setCertificateChainFile(long paramLong, String paramString, boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native boolean setCertificateChainBio(long paramLong1, long paramLong2, boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native boolean setCertificate(long paramLong, String paramString1, String paramString2, String paramString3)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native boolean setCertificateBio(long paramLong1, long paramLong2, long paramLong3, String paramString)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long setSessionCacheSize(long paramLong1, long paramLong2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long getSessionCacheSize(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long setSessionCacheTimeout(long paramLong1, long paramLong2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long getSessionCacheTimeout(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long setSessionCacheMode(long paramLong1, long paramLong2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long getSessionCacheMode(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long sessionAccept(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long sessionAcceptGood(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long sessionAcceptRenegotiate(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long sessionCacheFull(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long sessionCbHits(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long sessionConnect(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long sessionConnectGood(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long sessionConnectRenegotiate(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long sessionHits(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long sessionMisses(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long sessionNumber(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long sessionTimeouts(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long sessionTicketKeyNew(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long sessionTicketKeyResume(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long sessionTicketKeyRenew(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long sessionTicketKeyFail(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setSessionTicketKeys(long ctx, SessionTicketKey[] keys)
/*     */   {
/* 424 */     if ((keys == null) || (keys.length == 0)) {
/* 425 */       throw new IllegalArgumentException("Length of the keys should be longer than 0.");
/*     */     }
/* 427 */     byte[] binaryKeys = new byte[keys.length * 48];
/* 428 */     for (int i = 0; i < keys.length; i++) {
/* 429 */       SessionTicketKey key = keys[i];
/* 430 */       int dstCurPos = 48 * i;
/* 431 */       System.arraycopy(key.name, 0, binaryKeys, dstCurPos, 16);
/* 432 */       dstCurPos += 16;
/* 433 */       System.arraycopy(key.hmacKey, 0, binaryKeys, dstCurPos, 16);
/* 434 */       dstCurPos += 16;
/* 435 */       System.arraycopy(key.aesKey, 0, binaryKeys, dstCurPos, 16);
/*     */     }
/* 437 */     setSessionTicketKeys0(ctx, binaryKeys);
/*     */   }
/*     */   
/*     */   private static native void setSessionTicketKeys0(long paramLong, byte[] paramArrayOfByte);
/*     */   
/*     */   public static native boolean setCACertificateBio(long paramLong1, long paramLong2);
/*     */   
/*     */   public static native void setVerify(long paramLong, int paramInt1, int paramInt2);
/*     */   
/*     */   public static native void setCertVerifyCallback(long paramLong, CertificateVerifier paramCertificateVerifier);
/*     */   
/*     */   public static native void setCertRequestedCallback(long paramLong, CertificateRequestedCallback paramCertificateRequestedCallback);
/*     */   
/*     */   public static native void setCertificateCallback(long paramLong, CertificateCallback paramCertificateCallback);
/*     */   
/*     */   public static native void setSniHostnameMatcher(long paramLong, SniHostNameMatcher paramSniHostNameMatcher);
/*     */   
/*     */   public static native void setNpnProtos(long paramLong, String[] paramArrayOfString, int paramInt);
/*     */   
/*     */   public static native void setAlpnProtos(long paramLong, String[] paramArrayOfString, int paramInt);
/*     */   
/*     */   public static native void setTmpDHLength(long paramLong, int paramInt);
/*     */   
/*     */   public static native boolean setSessionIdContext(long paramLong, byte[] paramArrayOfByte);
/*     */   
/*     */   public static native int setMode(long paramLong, int paramInt);
/*     */   
/*     */   public static native int getMode(long paramLong);
/*     */   
/*     */   public static native void enableOcsp(long paramLong, boolean paramBoolean);
/*     */   
/*     */   public static native void disableOcsp(long paramLong);
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\internal\tcnative\SSLContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */