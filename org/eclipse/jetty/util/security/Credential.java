/*     */ package org.eclipse.jetty.util.security;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.MessageDigest;
/*     */ import java.util.ServiceLoader;
/*     */ import org.eclipse.jetty.util.TypeUtil;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
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
/*     */ public abstract class Credential
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7760551052768181572L;
/*  44 */   private static final Logger LOG = Log.getLogger(Credential.class);
/*  45 */   private static final ServiceLoader<CredentialProvider> CREDENTIAL_PROVIDER_LOADER = ServiceLoader.load(CredentialProvider.class);
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
/*     */   public abstract boolean check(Object paramObject);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Credential getCredential(String credential)
/*     */   {
/*  68 */     if (credential.startsWith("CRYPT:"))
/*  69 */       return new Crypt(credential);
/*  70 */     if (credential.startsWith("MD5:")) {
/*  71 */       return new MD5(credential);
/*     */     }
/*  73 */     for (CredentialProvider cp : CREDENTIAL_PROVIDER_LOADER)
/*     */     {
/*  75 */       if (credential.startsWith(cp.getPrefix()))
/*     */       {
/*  77 */         Credential credentialObj = cp.getCredential(credential);
/*  78 */         if (credentialObj != null)
/*     */         {
/*  80 */           return credentialObj;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  85 */     return new Password(credential);
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
/*     */   protected static boolean stringEquals(String known, String unknown)
/*     */   {
/*  99 */     boolean sameObject = known == unknown;
/* 100 */     if (sameObject)
/* 101 */       return true;
/* 102 */     if ((known == null) || (unknown == null))
/* 103 */       return false;
/* 104 */     boolean result = true;
/* 105 */     int l1 = known.length();
/* 106 */     int l2 = unknown.length();
/* 107 */     for (int i = 0; i < l2; i++)
/* 108 */       result &= known.charAt(i % l1) == unknown.charAt(i);
/* 109 */     return (result) && (l1 == l2);
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
/*     */   protected static boolean byteEquals(byte[] known, byte[] unknown)
/*     */   {
/* 122 */     if (known == unknown)
/* 123 */       return true;
/* 124 */     if ((known == null) || (unknown == null))
/* 125 */       return false;
/* 126 */     boolean result = true;
/* 127 */     int l1 = known.length;
/* 128 */     int l2 = unknown.length;
/* 129 */     for (int i = 0; i < l2; i++)
/* 130 */       result &= known[(i % l1)] == unknown[i];
/* 131 */     return (result) && (l1 == l2);
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Crypt
/*     */     extends Credential
/*     */   {
/*     */     private static final long serialVersionUID = -2027792997664744210L;
/*     */     
/*     */     private static final String __TYPE = "CRYPT:";
/*     */     
/*     */     private final String _cooked;
/*     */     
/*     */     Crypt(String cooked)
/*     */     {
/* 146 */       this._cooked = (cooked.startsWith("CRYPT:") ? cooked.substring("CRYPT:".length()) : cooked);
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean check(Object credentials)
/*     */     {
/* 152 */       if ((credentials instanceof char[]))
/* 153 */         credentials = new String((char[])credentials);
/* 154 */       if ((!(credentials instanceof String)) && (!(credentials instanceof Password)))
/* 155 */         Credential.LOG.warn("Can't check " + credentials.getClass() + " against CRYPT", new Object[0]);
/* 156 */       return stringEquals(this._cooked, UnixCrypt.crypt(credentials.toString(), this._cooked));
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean equals(Object credential)
/*     */     {
/* 162 */       if (!(credential instanceof Crypt))
/* 163 */         return false;
/* 164 */       Crypt c = (Crypt)credential;
/* 165 */       return stringEquals(this._cooked, c._cooked);
/*     */     }
/*     */     
/*     */     public static String crypt(String user, String pw)
/*     */     {
/* 170 */       return "CRYPT:" + UnixCrypt.crypt(pw, user);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class MD5
/*     */     extends Credential
/*     */   {
/*     */     private static final long serialVersionUID = 5533846540822684240L;
/*     */     
/*     */     private static final String __TYPE = "MD5:";
/* 181 */     private static final Object __md5Lock = new Object();
/*     */     
/*     */     private static MessageDigest __md;
/*     */     private final byte[] _digest;
/*     */     
/*     */     MD5(String digest)
/*     */     {
/* 188 */       digest = digest.startsWith("MD5:") ? digest.substring("MD5:".length()) : digest;
/* 189 */       this._digest = TypeUtil.parseBytes(digest, 16);
/*     */     }
/*     */     
/*     */     public byte[] getDigest()
/*     */     {
/* 194 */       return this._digest;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean check(Object credentials)
/*     */     {
/*     */       try
/*     */       {
/* 202 */         if ((credentials instanceof char[]))
/* 203 */           credentials = new String((char[])credentials);
/* 204 */         if (((credentials instanceof Password)) || ((credentials instanceof String)))
/*     */         {
/*     */           byte[] digest;
/* 207 */           synchronized (__md5Lock)
/*     */           {
/* 209 */             if (__md == null)
/* 210 */               __md = MessageDigest.getInstance("MD5");
/* 211 */             __md.reset();
/* 212 */             __md.update(credentials.toString().getBytes(StandardCharsets.ISO_8859_1));
/* 213 */             digest = __md.digest(); }
/*     */           byte[] digest;
/* 215 */           return byteEquals(this._digest, digest);
/*     */         }
/* 217 */         if ((credentials instanceof MD5))
/*     */         {
/* 219 */           return equals(credentials);
/*     */         }
/* 221 */         if ((credentials instanceof Credential))
/*     */         {
/*     */ 
/*     */ 
/* 225 */           return ((Credential)credentials).check(this);
/*     */         }
/*     */         
/*     */ 
/* 229 */         Credential.LOG.warn("Can't check " + credentials.getClass() + " against MD5", new Object[0]);
/* 230 */         return false;
/*     */ 
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 235 */         Credential.LOG.warn(e); }
/* 236 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public boolean equals(Object obj)
/*     */     {
/* 243 */       if ((obj instanceof MD5))
/* 244 */         return byteEquals(this._digest, ((MD5)obj)._digest);
/* 245 */       return false;
/*     */     }
/*     */     
/*     */     public static String digest(String password)
/*     */     {
/*     */       try
/*     */       {
/*     */         byte[] digest;
/* 253 */         synchronized (__md5Lock)
/*     */         {
/* 255 */           if (__md == null)
/*     */           {
/*     */             try
/*     */             {
/* 259 */               __md = MessageDigest.getInstance("MD5");
/*     */             }
/*     */             catch (Exception e)
/*     */             {
/* 263 */               Credential.LOG.warn(e);
/* 264 */               return null;
/*     */             }
/*     */           }
/*     */           
/* 268 */           __md.reset();
/* 269 */           __md.update(password.getBytes(StandardCharsets.ISO_8859_1));
/* 270 */           digest = __md.digest();
/*     */         }
/*     */         byte[] digest;
/* 273 */         return "MD5:" + TypeUtil.toString(digest, 16);
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 277 */         Credential.LOG.warn(e); }
/* 278 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\security\Credential.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */