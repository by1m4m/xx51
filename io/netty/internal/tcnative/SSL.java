/*     */ package io.netty.internal.tcnative;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SSL
/*     */ {
/*     */   public static final int SSL_PROTOCOL_NONE = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int SSL_PROTOCOL_SSLV2 = 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int SSL_PROTOCOL_SSLV3 = 2;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int SSL_PROTOCOL_TLSV1 = 4;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int SSL_PROTOCOL_TLSV1_1 = 8;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int SSL_PROTOCOL_TLSV1_2 = 16;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int SSL_PROTOCOL_TLSV1_3 = 32;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int SSL_PROTOCOL_TLS = 62;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int SSL_PROTOCOL_ALL = 63;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int SSL_CVERIFY_IGNORED = -1;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int SSL_CVERIFY_NONE = 0;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int SSL_CVERIFY_OPTIONAL = 1;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int SSL_CVERIFY_REQUIRED = 2;
/*     */   
/*     */ 
/*     */ 
/*  67 */   public static final int SSL_OP_CIPHER_SERVER_PREFERENCE = ;
/*  68 */   public static final int SSL_OP_NO_SSLv2 = NativeStaticallyReferencedJniMethods.sslOpNoSSLv2();
/*  69 */   public static final int SSL_OP_NO_SSLv3 = NativeStaticallyReferencedJniMethods.sslOpNoSSLv3();
/*  70 */   public static final int SSL_OP_NO_TLSv1 = NativeStaticallyReferencedJniMethods.sslOpNoTLSv1();
/*  71 */   public static final int SSL_OP_NO_TLSv1_1 = NativeStaticallyReferencedJniMethods.sslOpNoTLSv11();
/*  72 */   public static final int SSL_OP_NO_TLSv1_2 = NativeStaticallyReferencedJniMethods.sslOpNoTLSv12();
/*  73 */   public static final int SSL_OP_NO_TLSv1_3 = NativeStaticallyReferencedJniMethods.sslOpNoTLSv13();
/*  74 */   public static final int SSL_OP_NO_TICKET = NativeStaticallyReferencedJniMethods.sslOpNoTicket();
/*     */   
/*  76 */   public static final int SSL_OP_NO_COMPRESSION = NativeStaticallyReferencedJniMethods.sslOpNoCompression();
/*     */   
/*     */   public static final int SSL_MODE_CLIENT = 0;
/*     */   
/*     */   public static final int SSL_MODE_SERVER = 1;
/*     */   public static final int SSL_MODE_COMBINED = 2;
/*  82 */   public static final long SSL_SESS_CACHE_OFF = NativeStaticallyReferencedJniMethods.sslSessCacheOff();
/*  83 */   public static final long SSL_SESS_CACHE_SERVER = NativeStaticallyReferencedJniMethods.sslSessCacheServer();
/*     */   
/*     */   public static final int SSL_SELECTOR_FAILURE_NO_ADVERTISE = 0;
/*     */   
/*     */   public static final int SSL_SELECTOR_FAILURE_CHOOSE_MY_LAST_PROTOCOL = 1;
/*  88 */   public static final int SSL_ST_CONNECT = NativeStaticallyReferencedJniMethods.sslStConnect();
/*  89 */   public static final int SSL_ST_ACCEPT = NativeStaticallyReferencedJniMethods.sslStAccept();
/*     */   
/*  91 */   public static final int SSL_MODE_ENABLE_PARTIAL_WRITE = NativeStaticallyReferencedJniMethods.sslModeEnablePartialWrite();
/*  92 */   public static final int SSL_MODE_ACCEPT_MOVING_WRITE_BUFFER = NativeStaticallyReferencedJniMethods.sslModeAcceptMovingWriteBuffer();
/*  93 */   public static final int SSL_MODE_RELEASE_BUFFERS = NativeStaticallyReferencedJniMethods.sslModeReleaseBuffers();
/*     */   
/*  95 */   public static final int SSL_MAX_PLAINTEXT_LENGTH = NativeStaticallyReferencedJniMethods.sslMaxPlaintextLength();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */   public static final int SSL_MAX_RECORD_LENGTH = NativeStaticallyReferencedJniMethods.sslMaxRecordLength();
/*     */   
/*     */ 
/* 105 */   public static final int X509_CHECK_FLAG_ALWAYS_CHECK_SUBJECT = NativeStaticallyReferencedJniMethods.x509CheckFlagAlwaysCheckSubject();
/* 106 */   public static final int X509_CHECK_FLAG_NO_WILD_CARDS = NativeStaticallyReferencedJniMethods.x509CheckFlagDisableWildCards();
/* 107 */   public static final int X509_CHECK_FLAG_NO_PARTIAL_WILD_CARDS = NativeStaticallyReferencedJniMethods.x509CheckFlagNoPartialWildCards();
/* 108 */   public static final int X509_CHECK_FLAG_MULTI_LABEL_WILDCARDS = NativeStaticallyReferencedJniMethods.x509CheckFlagMultiLabelWildCards();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 147 */   public static final int SSL_SENT_SHUTDOWN = NativeStaticallyReferencedJniMethods.sslSendShutdown();
/* 148 */   public static final int SSL_RECEIVED_SHUTDOWN = NativeStaticallyReferencedJniMethods.sslReceivedShutdown();
/*     */   
/* 150 */   public static final int SSL_ERROR_NONE = NativeStaticallyReferencedJniMethods.sslErrorNone();
/* 151 */   public static final int SSL_ERROR_SSL = NativeStaticallyReferencedJniMethods.sslErrorSSL();
/* 152 */   public static final int SSL_ERROR_WANT_READ = NativeStaticallyReferencedJniMethods.sslErrorWantRead();
/* 153 */   public static final int SSL_ERROR_WANT_WRITE = NativeStaticallyReferencedJniMethods.sslErrorWantWrite();
/* 154 */   public static final int SSL_ERROR_WANT_X509_LOOKUP = NativeStaticallyReferencedJniMethods.sslErrorWantX509Lookup();
/* 155 */   public static final int SSL_ERROR_SYSCALL = NativeStaticallyReferencedJniMethods.sslErrorSyscall();
/* 156 */   public static final int SSL_ERROR_ZERO_RETURN = NativeStaticallyReferencedJniMethods.sslErrorZeroReturn();
/* 157 */   public static final int SSL_ERROR_WANT_CONNECT = NativeStaticallyReferencedJniMethods.sslErrorWantConnect();
/* 158 */   public static final int SSL_ERROR_WANT_ACCEPT = NativeStaticallyReferencedJniMethods.sslErrorWantAccept();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native int version();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native String versionString();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static native int initialize(String paramString);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long newMemBIO()
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native String getLastError();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long newSSL(long paramLong, boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native int getError(long paramLong, int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native int bioWrite(long paramLong1, long paramLong2, int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long bioNewByteBuffer(long paramLong, int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native void bioSetByteBuffer(long paramLong1, long paramLong2, int paramInt, boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native void bioClearByteBuffer(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native int bioFlushByteBuffer(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native int bioLengthByteBuffer(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native int bioLengthNonApplication(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native int sslPending(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native int writeToSSL(long paramLong1, long paramLong2, int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native int readFromSSL(long paramLong1, long paramLong2, int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native int getShutdown(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native void setShutdown(long paramLong, int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native void freeSSL(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native void freeBIO(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native int shutdownSSL(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native int getLastErrorNumber();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native String getCipherForSSL(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native String getVersion(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native int doHandshake(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native int isInInit(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native String getNextProtoNegotiated(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native String getAlpnSelected(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native byte[][] getPeerCertChain(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native byte[] getPeerCertificate(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native String getErrorString(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long getTime(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long getTimeout(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native long setTimeout(long paramLong1, long paramLong2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native void setVerify(long paramLong, int paramInt1, int paramInt2);
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
/*     */   public static native void clearOptions(long paramLong, int paramInt);
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
/*     */   public static native int setMode(long paramLong, int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native int getMode(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native int getMaxWrapOverhead(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static native String[] getCiphers(long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static boolean setCipherSuites(long ssl, String ciphers)
/*     */     throws Exception
/*     */   {
/* 510 */     return setCipherSuites(ssl, ciphers, false);
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
/*     */ 
/*     */   public static native boolean setCipherSuites(long paramLong, String paramString, boolean paramBoolean)
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
/*     */   public static native byte[] getSessionId(long paramLong);
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
/*     */   public static native int getHandshakeCount(long paramLong);
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
/*     */   public static native void clearError();
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
/*     */   public static native void setTlsExtHostName(long paramLong, String paramString);
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
/*     */   public static native void setHostNameValidation(long paramLong, int paramInt, String paramString);
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
/*     */   public static native String[] authenticationMethods(long paramLong);
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
/*     */   public static native void setCertificateChainBio(long paramLong1, long paramLong2, boolean paramBoolean);
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
/*     */   public static native void setCertificateBio(long paramLong1, long paramLong2, long paramLong3, String paramString)
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
/*     */   public static native long loadPrivateKeyFromEngine(String paramString1, String paramString2)
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
/*     */   public static native long parsePrivateKey(long paramLong, String paramString)
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
/*     */   public static native void freePrivateKey(long paramLong);
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
/*     */   public static native long parseX509Chain(long paramLong)
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
/*     */   public static native void freeX509Chain(long paramLong);
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
/*     */   public static native void enableOcsp(long paramLong);
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
/*     */   public static void setKeyMaterialServerSide(long ssl, long chain, long key)
/*     */     throws Exception
/*     */   {
/* 711 */     setKeyMaterial(ssl, chain, key);
/*     */   }
/*     */   
/*     */   public static native void setKeyMaterial(long paramLong1, long paramLong2, long paramLong3)
/*     */     throws Exception;
/*     */   
/*     */   @Deprecated
/*     */   public static native void setKeyMaterialClientSide(long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5)
/*     */     throws Exception;
/*     */   
/*     */   public static native void setOcspResponse(long paramLong, byte[] paramArrayOfByte);
/*     */   
/*     */   public static native byte[] getOcspResponse(long paramLong);
/*     */   
/*     */   public static native void fipsModeSet(int paramInt)
/*     */     throws Exception;
/*     */   
/*     */   public static native String getSniHostname(long paramLong);
/*     */   
/*     */   public static native String[] getSigAlgs(long paramLong);
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\internal\tcnative\SSL.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */