/*     */ package io.netty.internal.tcnative;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ public abstract class CertificateVerifier
/*     */ {
/*  35 */   public static final int X509_V_OK = ;
/*  36 */   public static final int X509_V_ERR_UNSPECIFIED = NativeStaticallyReferencedJniMethods.x509vErrUnspecified();
/*  37 */   public static final int X509_V_ERR_UNABLE_TO_GET_ISSUER_CERT = NativeStaticallyReferencedJniMethods.x509vErrUnableToGetIssuerCert();
/*  38 */   public static final int X509_V_ERR_UNABLE_TO_GET_CRL = NativeStaticallyReferencedJniMethods.x509vErrUnableToGetCrl();
/*  39 */   public static final int X509_V_ERR_UNABLE_TO_DECRYPT_CERT_SIGNATURE = NativeStaticallyReferencedJniMethods.x509vErrUnableToDecryptCertSignature();
/*  40 */   public static final int X509_V_ERR_UNABLE_TO_DECRYPT_CRL_SIGNATURE = NativeStaticallyReferencedJniMethods.x509vErrUnableToDecryptCrlSignature();
/*  41 */   public static final int X509_V_ERR_UNABLE_TO_DECODE_ISSUER_PUBLIC_KEY = NativeStaticallyReferencedJniMethods.x509vErrUnableToDecodeIssuerPublicKey();
/*  42 */   public static final int X509_V_ERR_CERT_SIGNATURE_FAILURE = NativeStaticallyReferencedJniMethods.x509vErrCertSignatureFailure();
/*  43 */   public static final int X509_V_ERR_CRL_SIGNATURE_FAILURE = NativeStaticallyReferencedJniMethods.x509vErrCrlSignatureFailure();
/*  44 */   public static final int X509_V_ERR_CERT_NOT_YET_VALID = NativeStaticallyReferencedJniMethods.x509vErrCertNotYetValid();
/*  45 */   public static final int X509_V_ERR_CERT_HAS_EXPIRED = NativeStaticallyReferencedJniMethods.x509vErrCertHasExpired();
/*  46 */   public static final int X509_V_ERR_CRL_NOT_YET_VALID = NativeStaticallyReferencedJniMethods.x509vErrCrlNotYetValid();
/*  47 */   public static final int X509_V_ERR_CRL_HAS_EXPIRED = NativeStaticallyReferencedJniMethods.x509vErrCrlHasExpired();
/*  48 */   public static final int X509_V_ERR_ERROR_IN_CERT_NOT_BEFORE_FIELD = NativeStaticallyReferencedJniMethods.x509vErrErrorInCertNotBeforeField();
/*  49 */   public static final int X509_V_ERR_ERROR_IN_CERT_NOT_AFTER_FIELD = NativeStaticallyReferencedJniMethods.x509vErrErrorInCertNotAfterField();
/*  50 */   public static final int X509_V_ERR_ERROR_IN_CRL_LAST_UPDATE_FIELD = NativeStaticallyReferencedJniMethods.x509vErrErrorInCrlLastUpdateField();
/*  51 */   public static final int X509_V_ERR_ERROR_IN_CRL_NEXT_UPDATE_FIELD = NativeStaticallyReferencedJniMethods.x509vErrErrorInCrlNextUpdateField();
/*  52 */   public static final int X509_V_ERR_OUT_OF_MEM = NativeStaticallyReferencedJniMethods.x509vErrOutOfMem();
/*  53 */   public static final int X509_V_ERR_DEPTH_ZERO_SELF_SIGNED_CERT = NativeStaticallyReferencedJniMethods.x509vErrDepthZeroSelfSignedCert();
/*  54 */   public static final int X509_V_ERR_SELF_SIGNED_CERT_IN_CHAIN = NativeStaticallyReferencedJniMethods.x509vErrSelfSignedCertInChain();
/*  55 */   public static final int X509_V_ERR_UNABLE_TO_GET_ISSUER_CERT_LOCALLY = NativeStaticallyReferencedJniMethods.x509vErrUnableToGetIssuerCertLocally();
/*  56 */   public static final int X509_V_ERR_UNABLE_TO_VERIFY_LEAF_SIGNATURE = NativeStaticallyReferencedJniMethods.x509vErrUnableToVerifyLeafSignature();
/*  57 */   public static final int X509_V_ERR_CERT_CHAIN_TOO_LONG = NativeStaticallyReferencedJniMethods.x509vErrCertChainTooLong();
/*  58 */   public static final int X509_V_ERR_CERT_REVOKED = NativeStaticallyReferencedJniMethods.x509vErrCertRevoked();
/*  59 */   public static final int X509_V_ERR_INVALID_CA = NativeStaticallyReferencedJniMethods.x509vErrInvalidCa();
/*  60 */   public static final int X509_V_ERR_PATH_LENGTH_EXCEEDED = NativeStaticallyReferencedJniMethods.x509vErrPathLengthExceeded();
/*  61 */   public static final int X509_V_ERR_INVALID_PURPOSE = NativeStaticallyReferencedJniMethods.x509vErrInvalidPurpose();
/*  62 */   public static final int X509_V_ERR_CERT_UNTRUSTED = NativeStaticallyReferencedJniMethods.x509vErrCertUntrusted();
/*  63 */   public static final int X509_V_ERR_CERT_REJECTED = NativeStaticallyReferencedJniMethods.x509vErrCertRejected();
/*  64 */   public static final int X509_V_ERR_SUBJECT_ISSUER_MISMATCH = NativeStaticallyReferencedJniMethods.x509vErrSubjectIssuerMismatch();
/*  65 */   public static final int X509_V_ERR_AKID_SKID_MISMATCH = NativeStaticallyReferencedJniMethods.x509vErrAkidSkidMismatch();
/*  66 */   public static final int X509_V_ERR_AKID_ISSUER_SERIAL_MISMATCH = NativeStaticallyReferencedJniMethods.x509vErrAkidIssuerSerialMismatch();
/*  67 */   public static final int X509_V_ERR_KEYUSAGE_NO_CERTSIGN = NativeStaticallyReferencedJniMethods.x509vErrKeyUsageNoCertSign();
/*  68 */   public static final int X509_V_ERR_UNABLE_TO_GET_CRL_ISSUER = NativeStaticallyReferencedJniMethods.x509vErrUnableToGetCrlIssuer();
/*  69 */   public static final int X509_V_ERR_UNHANDLED_CRITICAL_EXTENSION = NativeStaticallyReferencedJniMethods.x509vErrUnhandledCriticalExtension();
/*  70 */   public static final int X509_V_ERR_KEYUSAGE_NO_CRL_SIGN = NativeStaticallyReferencedJniMethods.x509vErrKeyUsageNoCrlSign();
/*  71 */   public static final int X509_V_ERR_UNHANDLED_CRITICAL_CRL_EXTENSION = NativeStaticallyReferencedJniMethods.x509vErrUnhandledCriticalCrlExtension();
/*  72 */   public static final int X509_V_ERR_INVALID_NON_CA = NativeStaticallyReferencedJniMethods.x509vErrInvalidNonCa();
/*  73 */   public static final int X509_V_ERR_PROXY_PATH_LENGTH_EXCEEDED = NativeStaticallyReferencedJniMethods.x509vErrProxyPathLengthExceeded();
/*  74 */   public static final int X509_V_ERR_KEYUSAGE_NO_DIGITAL_SIGNATURE = NativeStaticallyReferencedJniMethods.x509vErrKeyUsageNoDigitalSignature();
/*  75 */   public static final int X509_V_ERR_PROXY_CERTIFICATES_NOT_ALLOWED = NativeStaticallyReferencedJniMethods.x509vErrProxyCertificatesNotAllowed();
/*  76 */   public static final int X509_V_ERR_INVALID_EXTENSION = NativeStaticallyReferencedJniMethods.x509vErrInvalidExtension();
/*  77 */   public static final int X509_V_ERR_INVALID_POLICY_EXTENSION = NativeStaticallyReferencedJniMethods.x509vErrInvalidPolicyExtension();
/*  78 */   public static final int X509_V_ERR_NO_EXPLICIT_POLICY = NativeStaticallyReferencedJniMethods.x509vErrNoExplicitPolicy();
/*  79 */   public static final int X509_V_ERR_DIFFERENT_CRL_SCOPE = NativeStaticallyReferencedJniMethods.x509vErrDifferntCrlScope();
/*  80 */   public static final int X509_V_ERR_UNSUPPORTED_EXTENSION_FEATURE = NativeStaticallyReferencedJniMethods.x509vErrUnsupportedExtensionFeature();
/*  81 */   public static final int X509_V_ERR_UNNESTED_RESOURCE = NativeStaticallyReferencedJniMethods.x509vErrUnnestedResource();
/*  82 */   public static final int X509_V_ERR_PERMITTED_VIOLATION = NativeStaticallyReferencedJniMethods.x509vErrPermittedViolation();
/*  83 */   public static final int X509_V_ERR_EXCLUDED_VIOLATION = NativeStaticallyReferencedJniMethods.x509vErrExcludedViolation();
/*  84 */   public static final int X509_V_ERR_SUBTREE_MINMAX = NativeStaticallyReferencedJniMethods.x509vErrSubtreeMinMax();
/*  85 */   public static final int X509_V_ERR_APPLICATION_VERIFICATION = NativeStaticallyReferencedJniMethods.x509vErrApplicationVerification();
/*  86 */   public static final int X509_V_ERR_UNSUPPORTED_CONSTRAINT_TYPE = NativeStaticallyReferencedJniMethods.x509vErrUnsupportedConstraintType();
/*  87 */   public static final int X509_V_ERR_UNSUPPORTED_CONSTRAINT_SYNTAX = NativeStaticallyReferencedJniMethods.x509vErrUnsupportedConstraintSyntax();
/*  88 */   public static final int X509_V_ERR_UNSUPPORTED_NAME_SYNTAX = NativeStaticallyReferencedJniMethods.x509vErrUnsupportedNameSyntax();
/*  89 */   public static final int X509_V_ERR_CRL_PATH_VALIDATION_ERROR = NativeStaticallyReferencedJniMethods.x509vErrCrlPathValidationError();
/*  90 */   public static final int X509_V_ERR_PATH_LOOP = NativeStaticallyReferencedJniMethods.x509vErrPathLoop();
/*  91 */   public static final int X509_V_ERR_SUITE_B_INVALID_VERSION = NativeStaticallyReferencedJniMethods.x509vErrSuiteBInvalidVersion();
/*  92 */   public static final int X509_V_ERR_SUITE_B_INVALID_ALGORITHM = NativeStaticallyReferencedJniMethods.x509vErrSuiteBInvalidAlgorithm();
/*  93 */   public static final int X509_V_ERR_SUITE_B_INVALID_CURVE = NativeStaticallyReferencedJniMethods.x509vErrSuiteBInvalidCurve();
/*  94 */   public static final int X509_V_ERR_SUITE_B_INVALID_SIGNATURE_ALGORITHM = NativeStaticallyReferencedJniMethods.x509vErrSuiteBInvalidSignatureAlgorithm();
/*  95 */   public static final int X509_V_ERR_SUITE_B_LOS_NOT_ALLOWED = NativeStaticallyReferencedJniMethods.x509vErrSuiteBLosNotAllowed();
/*  96 */   public static final int X509_V_ERR_SUITE_B_CANNOT_SIGN_P_384_WITH_P_256 = NativeStaticallyReferencedJniMethods.x509vErrSuiteBCannotSignP384WithP256();
/*  97 */   public static final int X509_V_ERR_HOSTNAME_MISMATCH = NativeStaticallyReferencedJniMethods.x509vErrHostnameMismatch();
/*  98 */   public static final int X509_V_ERR_EMAIL_MISMATCH = NativeStaticallyReferencedJniMethods.x509vErrEmailMismatch();
/*  99 */   public static final int X509_V_ERR_IP_ADDRESS_MISMATCH = NativeStaticallyReferencedJniMethods.x509vErrIpAddressMismatch();
/* 100 */   public static final int X509_V_ERR_DANE_NO_MATCH = NativeStaticallyReferencedJniMethods.x509vErrDaneNoMatch();
/*     */   private static final Set<Integer> ERRORS;
/*     */   
/*     */   static
/*     */   {
/* 105 */     Set<Integer> errors = new HashSet();
/* 106 */     errors.add(Integer.valueOf(X509_V_OK));
/* 107 */     errors.add(Integer.valueOf(X509_V_ERR_UNSPECIFIED));
/* 108 */     errors.add(Integer.valueOf(X509_V_ERR_UNABLE_TO_GET_ISSUER_CERT));
/* 109 */     errors.add(Integer.valueOf(X509_V_ERR_UNABLE_TO_GET_CRL));
/* 110 */     errors.add(Integer.valueOf(X509_V_ERR_UNABLE_TO_DECRYPT_CERT_SIGNATURE));
/* 111 */     errors.add(Integer.valueOf(X509_V_ERR_UNABLE_TO_DECRYPT_CRL_SIGNATURE));
/* 112 */     errors.add(Integer.valueOf(X509_V_ERR_UNABLE_TO_DECODE_ISSUER_PUBLIC_KEY));
/* 113 */     errors.add(Integer.valueOf(X509_V_ERR_CERT_SIGNATURE_FAILURE));
/* 114 */     errors.add(Integer.valueOf(X509_V_ERR_CRL_SIGNATURE_FAILURE));
/* 115 */     errors.add(Integer.valueOf(X509_V_ERR_CERT_NOT_YET_VALID));
/* 116 */     errors.add(Integer.valueOf(X509_V_ERR_CERT_HAS_EXPIRED));
/* 117 */     errors.add(Integer.valueOf(X509_V_ERR_CRL_NOT_YET_VALID));
/* 118 */     errors.add(Integer.valueOf(X509_V_ERR_CRL_HAS_EXPIRED));
/* 119 */     errors.add(Integer.valueOf(X509_V_ERR_ERROR_IN_CERT_NOT_BEFORE_FIELD));
/* 120 */     errors.add(Integer.valueOf(X509_V_ERR_ERROR_IN_CERT_NOT_AFTER_FIELD));
/* 121 */     errors.add(Integer.valueOf(X509_V_ERR_ERROR_IN_CRL_LAST_UPDATE_FIELD));
/* 122 */     errors.add(Integer.valueOf(X509_V_ERR_ERROR_IN_CRL_NEXT_UPDATE_FIELD));
/* 123 */     errors.add(Integer.valueOf(X509_V_ERR_OUT_OF_MEM));
/* 124 */     errors.add(Integer.valueOf(X509_V_ERR_DEPTH_ZERO_SELF_SIGNED_CERT));
/* 125 */     errors.add(Integer.valueOf(X509_V_ERR_SELF_SIGNED_CERT_IN_CHAIN));
/* 126 */     errors.add(Integer.valueOf(X509_V_ERR_UNABLE_TO_GET_ISSUER_CERT_LOCALLY));
/* 127 */     errors.add(Integer.valueOf(X509_V_ERR_UNABLE_TO_VERIFY_LEAF_SIGNATURE));
/* 128 */     errors.add(Integer.valueOf(X509_V_ERR_CERT_CHAIN_TOO_LONG));
/* 129 */     errors.add(Integer.valueOf(X509_V_ERR_CERT_REVOKED));
/* 130 */     errors.add(Integer.valueOf(X509_V_ERR_INVALID_CA));
/* 131 */     errors.add(Integer.valueOf(X509_V_ERR_PATH_LENGTH_EXCEEDED));
/* 132 */     errors.add(Integer.valueOf(X509_V_ERR_INVALID_PURPOSE));
/* 133 */     errors.add(Integer.valueOf(X509_V_ERR_CERT_UNTRUSTED));
/* 134 */     errors.add(Integer.valueOf(X509_V_ERR_CERT_REJECTED));
/* 135 */     errors.add(Integer.valueOf(X509_V_ERR_SUBJECT_ISSUER_MISMATCH));
/* 136 */     errors.add(Integer.valueOf(X509_V_ERR_AKID_SKID_MISMATCH));
/* 137 */     errors.add(Integer.valueOf(X509_V_ERR_AKID_ISSUER_SERIAL_MISMATCH));
/* 138 */     errors.add(Integer.valueOf(X509_V_ERR_KEYUSAGE_NO_CERTSIGN));
/* 139 */     errors.add(Integer.valueOf(X509_V_ERR_UNABLE_TO_GET_CRL_ISSUER));
/* 140 */     errors.add(Integer.valueOf(X509_V_ERR_UNHANDLED_CRITICAL_EXTENSION));
/* 141 */     errors.add(Integer.valueOf(X509_V_ERR_KEYUSAGE_NO_CRL_SIGN));
/* 142 */     errors.add(Integer.valueOf(X509_V_ERR_UNHANDLED_CRITICAL_CRL_EXTENSION));
/* 143 */     errors.add(Integer.valueOf(X509_V_ERR_INVALID_NON_CA));
/* 144 */     errors.add(Integer.valueOf(X509_V_ERR_PROXY_PATH_LENGTH_EXCEEDED));
/* 145 */     errors.add(Integer.valueOf(X509_V_ERR_KEYUSAGE_NO_DIGITAL_SIGNATURE));
/* 146 */     errors.add(Integer.valueOf(X509_V_ERR_PROXY_CERTIFICATES_NOT_ALLOWED));
/* 147 */     errors.add(Integer.valueOf(X509_V_ERR_INVALID_EXTENSION));
/* 148 */     errors.add(Integer.valueOf(X509_V_ERR_INVALID_POLICY_EXTENSION));
/* 149 */     errors.add(Integer.valueOf(X509_V_ERR_NO_EXPLICIT_POLICY));
/* 150 */     errors.add(Integer.valueOf(X509_V_ERR_DIFFERENT_CRL_SCOPE));
/* 151 */     errors.add(Integer.valueOf(X509_V_ERR_UNSUPPORTED_EXTENSION_FEATURE));
/* 152 */     errors.add(Integer.valueOf(X509_V_ERR_UNNESTED_RESOURCE));
/* 153 */     errors.add(Integer.valueOf(X509_V_ERR_PERMITTED_VIOLATION));
/* 154 */     errors.add(Integer.valueOf(X509_V_ERR_EXCLUDED_VIOLATION));
/* 155 */     errors.add(Integer.valueOf(X509_V_ERR_SUBTREE_MINMAX));
/* 156 */     errors.add(Integer.valueOf(X509_V_ERR_APPLICATION_VERIFICATION));
/* 157 */     errors.add(Integer.valueOf(X509_V_ERR_UNSUPPORTED_CONSTRAINT_TYPE));
/* 158 */     errors.add(Integer.valueOf(X509_V_ERR_UNSUPPORTED_CONSTRAINT_SYNTAX));
/* 159 */     errors.add(Integer.valueOf(X509_V_ERR_UNSUPPORTED_NAME_SYNTAX));
/* 160 */     errors.add(Integer.valueOf(X509_V_ERR_CRL_PATH_VALIDATION_ERROR));
/* 161 */     errors.add(Integer.valueOf(X509_V_ERR_PATH_LOOP));
/* 162 */     errors.add(Integer.valueOf(X509_V_ERR_SUITE_B_INVALID_VERSION));
/* 163 */     errors.add(Integer.valueOf(X509_V_ERR_SUITE_B_INVALID_ALGORITHM));
/* 164 */     errors.add(Integer.valueOf(X509_V_ERR_SUITE_B_INVALID_CURVE));
/* 165 */     errors.add(Integer.valueOf(X509_V_ERR_SUITE_B_INVALID_SIGNATURE_ALGORITHM));
/* 166 */     errors.add(Integer.valueOf(X509_V_ERR_SUITE_B_LOS_NOT_ALLOWED));
/* 167 */     errors.add(Integer.valueOf(X509_V_ERR_SUITE_B_CANNOT_SIGN_P_384_WITH_P_256));
/* 168 */     errors.add(Integer.valueOf(X509_V_ERR_HOSTNAME_MISMATCH));
/* 169 */     errors.add(Integer.valueOf(X509_V_ERR_EMAIL_MISMATCH));
/* 170 */     errors.add(Integer.valueOf(X509_V_ERR_IP_ADDRESS_MISMATCH));
/* 171 */     errors.add(Integer.valueOf(X509_V_ERR_DANE_NO_MATCH));
/* 172 */     ERRORS = Collections.unmodifiableSet(errors);
/*     */   }
/*     */   
/*     */   public abstract int verify(long paramLong, byte[][] paramArrayOfByte, String paramString);
/*     */   
/*     */   public static boolean isValid(int errorCode)
/*     */   {
/* 179 */     return ERRORS.contains(Integer.valueOf(errorCode));
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\internal\tcnative\CertificateVerifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */