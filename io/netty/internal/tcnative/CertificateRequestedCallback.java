package io.netty.internal.tcnative;

@Deprecated
public abstract interface CertificateRequestedCallback
{
  public static final byte TLS_CT_RSA_SIGN = 1;
  public static final byte TLS_CT_DSS_SIGN = 2;
  public static final byte TLS_CT_RSA_FIXED_DH = 3;
  public static final byte TLS_CT_DSS_FIXED_DH = 4;
  public static final byte TLS_CT_ECDSA_SIGN = 64;
  public static final byte TLS_CT_RSA_FIXED_ECDH = 65;
  public static final byte TLS_CT_ECDSA_FIXED_ECDH = 66;
  
  public abstract void requested(long paramLong1, long paramLong2, long paramLong3, byte[] paramArrayOfByte, byte[][] paramArrayOfByte1)
    throws Exception;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\internal\tcnative\CertificateRequestedCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */