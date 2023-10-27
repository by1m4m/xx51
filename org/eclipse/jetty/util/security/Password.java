/*     */ package org.eclipse.jetty.util.security;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Locale;
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
/*     */ public class Password
/*     */   extends Credential
/*     */ {
/*  58 */   private static final Logger LOG = Log.getLogger(Password.class);
/*     */   
/*     */ 
/*     */ 
/*     */   private static final long serialVersionUID = 5062906681431569445L;
/*     */   
/*     */ 
/*     */   public static final String __OBFUSCATE = "OBF:";
/*     */   
/*     */ 
/*     */   private String _pw;
/*     */   
/*     */ 
/*     */ 
/*     */   public Password(String password)
/*     */   {
/*  74 */     this._pw = password;
/*     */     
/*     */ 
/*  77 */     while ((this._pw != null) && (this._pw.startsWith("OBF:"))) {
/*  78 */       this._pw = deobfuscate(this._pw);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/*  85 */     return this._pw;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toStarString()
/*     */   {
/*  91 */     return "*****************************************************".substring(0, this._pw.length());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean check(Object credentials)
/*     */   {
/*  98 */     if (this == credentials) {
/*  99 */       return true;
/*     */     }
/* 101 */     if ((credentials instanceof Password)) {
/* 102 */       return credentials.equals(this._pw);
/*     */     }
/* 104 */     if ((credentials instanceof String)) {
/* 105 */       return stringEquals(this._pw, (String)credentials);
/*     */     }
/* 107 */     if ((credentials instanceof char[])) {
/* 108 */       return stringEquals(this._pw, new String((char[])credentials));
/*     */     }
/* 110 */     if ((credentials instanceof Credential)) {
/* 111 */       return ((Credential)credentials).check(this._pw);
/*     */     }
/* 113 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 120 */     if (this == o) {
/* 121 */       return true;
/*     */     }
/* 123 */     if (null == o) {
/* 124 */       return false;
/*     */     }
/* 126 */     if ((o instanceof Password)) {
/* 127 */       return stringEquals(this._pw, ((Password)o)._pw);
/*     */     }
/* 129 */     if ((o instanceof String)) {
/* 130 */       return stringEquals(this._pw, (String)o);
/*     */     }
/* 132 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 139 */     return null == this._pw ? super.hashCode() : this._pw.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */   public static String obfuscate(String s)
/*     */   {
/* 145 */     StringBuilder buf = new StringBuilder();
/* 146 */     byte[] b = s.getBytes(StandardCharsets.UTF_8);
/*     */     
/* 148 */     buf.append("OBF:");
/* 149 */     for (int i = 0; i < b.length; i++)
/*     */     {
/* 151 */       byte b1 = b[i];
/* 152 */       byte b2 = b[(b.length - (i + 1))];
/* 153 */       if ((b1 < 0) || (b2 < 0))
/*     */       {
/* 155 */         int i0 = (0xFF & b1) * 256 + (0xFF & b2);
/* 156 */         String x = Integer.toString(i0, 36).toLowerCase(Locale.ENGLISH);
/* 157 */         buf.append("U0000", 0, 5 - x.length());
/* 158 */         buf.append(x);
/*     */       }
/*     */       else
/*     */       {
/* 162 */         int i1 = Byte.MAX_VALUE + b1 + b2;
/* 163 */         int i2 = Byte.MAX_VALUE + b1 - b2;
/* 164 */         int i0 = i1 * 256 + i2;
/* 165 */         String x = Integer.toString(i0, 36).toLowerCase(Locale.ENGLISH);
/*     */         
/* 167 */         int j0 = Integer.parseInt(x, 36);
/* 168 */         int j1 = i0 / 256;
/* 169 */         int j2 = i0 % 256;
/* 170 */         byte bx = (byte)((j1 + j2 - 254) / 2);
/*     */         
/* 172 */         buf.append("000", 0, 4 - x.length());
/* 173 */         buf.append(x);
/*     */       }
/*     */     }
/*     */     
/* 177 */     return buf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public static String deobfuscate(String s)
/*     */   {
/* 183 */     if (s.startsWith("OBF:")) { s = s.substring(4);
/*     */     }
/* 185 */     byte[] b = new byte[s.length() / 2];
/* 186 */     int l = 0;
/* 187 */     for (int i = 0; i < s.length(); i += 4)
/*     */     {
/* 189 */       if (s.charAt(i) == 'U')
/*     */       {
/* 191 */         i++;
/* 192 */         String x = s.substring(i, i + 4);
/* 193 */         int i0 = Integer.parseInt(x, 36);
/* 194 */         byte bx = (byte)(i0 >> 8);
/* 195 */         b[(l++)] = bx;
/*     */       }
/*     */       else
/*     */       {
/* 199 */         String x = s.substring(i, i + 4);
/* 200 */         int i0 = Integer.parseInt(x, 36);
/* 201 */         int i1 = i0 / 256;
/* 202 */         int i2 = i0 % 256;
/* 203 */         byte bx = (byte)((i1 + i2 - 254) / 2);
/* 204 */         b[(l++)] = bx;
/*     */       }
/*     */     }
/*     */     
/* 208 */     return new String(b, 0, l, StandardCharsets.UTF_8);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Password getPassword(String realm, String dft, String promptDft)
/*     */   {
/* 228 */     String passwd = System.getProperty(realm, dft);
/* 229 */     if ((passwd == null) || (passwd.length() == 0))
/*     */     {
/*     */       try
/*     */       {
/* 233 */         System.out.print(realm + ((promptDft != null) && (promptDft.length() > 0) ? " [dft]" : "") + " : ");
/* 234 */         System.out.flush();
/* 235 */         byte[] buf = new byte['Ȁ'];
/* 236 */         int len = System.in.read(buf);
/* 237 */         if (len > 0) passwd = new String(buf, 0, len).trim();
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 241 */         LOG.warn("EXCEPTION ", e);
/*     */       }
/* 243 */       if ((passwd == null) || (passwd.length() == 0)) passwd = promptDft;
/*     */     }
/* 245 */     return new Password(passwd);
/*     */   }
/*     */   
/*     */   public static void main(String[] arg)
/*     */   {
/* 250 */     if ((arg.length != 1) && (arg.length != 2))
/*     */     {
/* 252 */       System.err.println("Usage - java " + Password.class.getName() + " [<user>] <password>");
/* 253 */       System.err.println("If the password is ?, the user will be prompted for the password");
/* 254 */       System.exit(1);
/*     */     }
/* 256 */     String p = arg[1];
/* 257 */     Password pw = new Password(p);
/* 258 */     System.err.println(pw.toString());
/* 259 */     System.err.println(obfuscate(pw.toString()));
/* 260 */     System.err.println(Credential.MD5.digest(p));
/* 261 */     if (arg.length == 2) System.err.println(Credential.Crypt.crypt(arg[0], pw.toString()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\security\Password.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */