/*     */ package rdp.gold.brute.rdp.Messages;
/*     */ 
/*     */ import org.apache.log4j.Logger;
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
/*     */ public class NegoFlags
/*     */ {
/*     */   public static final int NTLMSSP_NEGOTIATE_56 = Integer.MIN_VALUE;
/*     */   public static final int NTLMSSP_NEGOTIATE_KEY_EXCH = 1073741824;
/*     */   public static final int NTLMSSP_NEGOTIATE_128 = 536870912;
/*     */   public static final int NTLMSSP_NEGOTIATE_VERSION = 33554432;
/*     */   public static final int NTLMSSP_NEGOTIATE_TARGET_INFO = 8388608;
/*     */   public static final int NTLMSSP_REQUEST_NON_NT_SESSION_KEY = 4194304;
/*     */   public static final int NTLMSSP_NEGOTIATE_IDENTIFY = 1048576;
/*     */   public static final int NTLMSSP_NEGOTIATE_EXTENDED_SESSION_SECURITY = 524288;
/*     */   public static final int NTLMSSP_TARGET_TYPE_SERVER = 131072;
/*     */   public static final int NTLMSSP_TARGET_TYPE_DOMAIN = 65536;
/*     */   public static final int NTLMSSP_NEGOTIATE_ALWAYS_SIGN = 32768;
/*     */   public static final int NTLMSSP_NEGOTIATE_OEM_WORKSTATION_SUPPLIED = 8192;
/*     */   public static final int NTLMSSP_NEGOTIATE_OEM_DOMAIN_SUPPLIED = 4096;
/*     */   public static final int NTLMSSP_NEGOTIATE_ANONYMOUS = 2048;
/*     */   public static final int NTLMSSP_NEGOTIATE_NTLM = 512;
/*     */   public static final int NTLMSSP_NEGOTIATE_LM_KEY = 128;
/*     */   public static final int NTLMSSP_NEGOTIATE_DATAGRAM = 64;
/*     */   public static final int NTLMSSP_NEGOTIATE_SEAL = 32;
/*     */   public static final int NTLMSSP_NEGOTIATE_SIGN = 16;
/*     */   public static final int NTLMSSP_REQUEST_TARGET = 4;
/*     */   public static final int NTLMSSP_NEGOTIATE_OEM = 2;
/*     */   public static final int NTLMSSP_NEGOTIATE_UNICODE = 1;
/* 199 */   private static final Logger logger = Logger.getLogger(NegoFlags.class);
/*     */   public int value;
/*     */   
/*     */   public NegoFlags(int value) {
/* 203 */     this.value = value;
/*     */   }
/*     */   
/*     */   public NegoFlags() {
/* 207 */     this.value = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 216 */     NegoFlags flags = new NegoFlags(-502758729);
/* 217 */     logger.info("Negotiation flags: " + flags);
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 223 */     return String.format("NegoFlags [value=0x%04x (%s)]", new Object[] { Integer.valueOf(this.value), flagsToSting() });
/*     */   }
/*     */   
/*     */   public String flagsToSting()
/*     */   {
/* 228 */     String str = "";
/*     */     
/* 230 */     if (NEGOTIATE_56())
/* 231 */       str = str + "NEGOTIATE_56 ";
/* 232 */     if (NEGOTIATE_KEY_EXCH())
/* 233 */       str = str + "NEGOTIATE_KEY_EXCH ";
/* 234 */     if (NEGOTIATE_128())
/* 235 */       str = str + "NEGOTIATE_128 ";
/* 236 */     if (NEGOTIATE_VERSION())
/* 237 */       str = str + "NEGOTIATE_VERSION ";
/* 238 */     if (NEGOTIATE_TARGET_INFO())
/* 239 */       str = str + "NEGOTIATE_TARGET_INFO ";
/* 240 */     if (REQUEST_NON_NT_SESSION_KEY())
/* 241 */       str = str + "REQUEST_NON_NT_SESSION_KEY ";
/* 242 */     if (NEGOTIATE_IDENTIFY())
/* 243 */       str = str + "NEGOTIATE_IDENTIFY ";
/* 244 */     if (NEGOTIATE_EXTENDED_SESSION_SECURITY())
/* 245 */       str = str + "NEGOTIATE_EXTENDED_SESSION_SECURITY ";
/* 246 */     if (TARGET_TYPE_SERVER())
/* 247 */       str = str + "TARGET_TYPE_SERVER ";
/* 248 */     if (TARGET_TYPE_DOMAIN())
/* 249 */       str = str + "TARGET_TYPE_DOMAIN ";
/* 250 */     if (NEGOTIATE_ALWAYS_SIGN())
/* 251 */       str = str + "NEGOTIATE_ALWAYS_SIGN ";
/* 252 */     if (NEGOTIATE_OEM_WORKSTATION_SUPPLIED())
/* 253 */       str = str + "NEGOTIATE_OEM_WORKSTATION_SUPPLIED ";
/* 254 */     if (NEGOTIATE_OEM_DOMAIN_SUPPLIED())
/* 255 */       str = str + "NEGOTIATE_OEM_DOMAIN_SUPPLIED ";
/* 256 */     if (NEGOTIATE_ANONYMOUS())
/* 257 */       str = str + "NEGOTIATE_ANONYMOUS ";
/* 258 */     if (NEGOTIATE_NTLM())
/* 259 */       str = str + "NEGOTIATE_NTLM ";
/* 260 */     if (NEGOTIATE_LM_KEY())
/* 261 */       str = str + "NEGOTIATE_LM_KEY ";
/* 262 */     if (NEGOTIATE_DATAGRAM())
/* 263 */       str = str + "NEGOTIATE_DATAGRAM ";
/* 264 */     if (NEGOTIATE_SEAL())
/* 265 */       str = str + "NEGOTIATE_SEAL ";
/* 266 */     if (NEGOTIATE_SIGN())
/* 267 */       str = str + "NEGOTIATE_SIGN ";
/* 268 */     if (REQUEST_TARGET())
/* 269 */       str = str + "REQUEST_TARGET ";
/* 270 */     if (NEGOTIATE_OEM())
/* 271 */       str = str + "NEGOTIATE_OEM ";
/* 272 */     if (NEGOTIATE_UNICODE()) {
/* 273 */       str = str + "NEGOTIATE_UNICODE ";
/*     */     }
/* 275 */     return str;
/*     */   }
/*     */   
/*     */   public boolean NEGOTIATE_56() {
/* 279 */     return (this.value & 0x80000000) != 0;
/*     */   }
/*     */   
/*     */   public boolean NEGOTIATE_KEY_EXCH() {
/* 283 */     return (this.value & 0x40000000) != 0;
/*     */   }
/*     */   
/*     */   public boolean NEGOTIATE_128() {
/* 287 */     return (this.value & 0x20000000) != 0;
/*     */   }
/*     */   
/*     */   public boolean NEGOTIATE_VERSION() {
/* 291 */     return (this.value & 0x2000000) != 0;
/*     */   }
/*     */   
/*     */   public boolean NEGOTIATE_TARGET_INFO() {
/* 295 */     return (this.value & 0x800000) != 0;
/*     */   }
/*     */   
/*     */   public boolean REQUEST_NON_NT_SESSION_KEY() {
/* 299 */     return (this.value & 0x400000) != 0;
/*     */   }
/*     */   
/*     */   public boolean NEGOTIATE_IDENTIFY() {
/* 303 */     return (this.value & 0x100000) != 0;
/*     */   }
/*     */   
/*     */   public boolean NEGOTIATE_EXTENDED_SESSION_SECURITY() {
/* 307 */     return (this.value & 0x80000) != 0;
/*     */   }
/*     */   
/*     */   public boolean TARGET_TYPE_SERVER() {
/* 311 */     return (this.value & 0x20000) != 0;
/*     */   }
/*     */   
/*     */   public boolean TARGET_TYPE_DOMAIN() {
/* 315 */     return (this.value & 0x10000) != 0;
/*     */   }
/*     */   
/*     */   public boolean NEGOTIATE_ALWAYS_SIGN() {
/* 319 */     return (this.value & 0x8000) != 0;
/*     */   }
/*     */   
/*     */   public boolean NEGOTIATE_OEM_WORKSTATION_SUPPLIED() {
/* 323 */     return (this.value & 0x2000) != 0;
/*     */   }
/*     */   
/*     */   public boolean NEGOTIATE_OEM_DOMAIN_SUPPLIED() {
/* 327 */     return (this.value & 0x1000) != 0;
/*     */   }
/*     */   
/*     */   public boolean NEGOTIATE_ANONYMOUS() {
/* 331 */     return (this.value & 0x800) != 0;
/*     */   }
/*     */   
/*     */   public boolean NEGOTIATE_NTLM() {
/* 335 */     return (this.value & 0x200) != 0;
/*     */   }
/*     */   
/*     */   public boolean NEGOTIATE_LM_KEY() {
/* 339 */     return (this.value & 0x80) != 0;
/*     */   }
/*     */   
/*     */   public boolean NEGOTIATE_DATAGRAM() {
/* 343 */     return (this.value & 0x40) != 0;
/*     */   }
/*     */   
/*     */   public boolean NEGOTIATE_SEAL() {
/* 347 */     return (this.value & 0x20) != 0;
/*     */   }
/*     */   
/*     */   public boolean NEGOTIATE_SIGN() {
/* 351 */     return (this.value & 0x10) != 0;
/*     */   }
/*     */   
/*     */   public boolean REQUEST_TARGET() {
/* 355 */     return (this.value & 0x4) != 0;
/*     */   }
/*     */   
/*     */   public boolean NEGOTIATE_OEM() {
/* 359 */     return (this.value & 0x2) != 0;
/*     */   }
/*     */   
/*     */   public boolean NEGOTIATE_UNICODE() {
/* 363 */     return (this.value & 0x1) != 0;
/*     */   }
/*     */   
/*     */   public NegoFlags set_NEGOTIATE_56() {
/* 367 */     this.value |= 0x80000000;
/* 368 */     return this;
/*     */   }
/*     */   
/*     */   public NegoFlags set_NEGOTIATE_KEY_EXCH() {
/* 372 */     this.value |= 0x40000000;
/* 373 */     return this;
/*     */   }
/*     */   
/*     */   public NegoFlags set_NEGOTIATE_128() {
/* 377 */     this.value |= 0x20000000;
/* 378 */     return this;
/*     */   }
/*     */   
/*     */   public NegoFlags set_NEGOTIATE_VERSION() {
/* 382 */     this.value |= 0x2000000;
/* 383 */     return this;
/*     */   }
/*     */   
/*     */   public NegoFlags set_NEGOTIATE_TARGET_INFO() {
/* 387 */     this.value |= 0x800000;
/* 388 */     return this;
/*     */   }
/*     */   
/*     */   public NegoFlags set_REQUEST_NON_NT_SESSION_KEY() {
/* 392 */     this.value |= 0x400000;
/* 393 */     return this;
/*     */   }
/*     */   
/*     */   public NegoFlags set_NEGOTIATE_IDENTIFY() {
/* 397 */     this.value |= 0x100000;
/* 398 */     return this;
/*     */   }
/*     */   
/*     */   public NegoFlags set_NEGOTIATE_EXTENDED_SESSION_SECURITY() {
/* 402 */     this.value |= 0x80000;
/* 403 */     return this;
/*     */   }
/*     */   
/*     */   public NegoFlags set_TARGET_TYPE_SERVER() {
/* 407 */     this.value |= 0x20000;
/* 408 */     return this;
/*     */   }
/*     */   
/*     */   public NegoFlags set_TARGET_TYPE_DOMAIN() {
/* 412 */     this.value |= 0x10000;
/* 413 */     return this;
/*     */   }
/*     */   
/*     */   public NegoFlags set_NEGOTIATE_ALWAYS_SIGN() {
/* 417 */     this.value |= 0x8000;
/* 418 */     return this;
/*     */   }
/*     */   
/*     */   public NegoFlags set_NEGOTIATE_OEM_WORKSTATION_SUPPLIED() {
/* 422 */     this.value |= 0x2000;
/* 423 */     return this;
/*     */   }
/*     */   
/*     */   public NegoFlags set_NEGOTIATE_OEM_DOMAIN_SUPPLIED() {
/* 427 */     this.value |= 0x1000;
/* 428 */     return this;
/*     */   }
/*     */   
/*     */   public NegoFlags set_NEGOTIATE_ANONYMOUS() {
/* 432 */     this.value |= 0x800;
/* 433 */     return this;
/*     */   }
/*     */   
/*     */   public NegoFlags set_NEGOTIATE_NTLM() {
/* 437 */     this.value |= 0x200;
/* 438 */     return this;
/*     */   }
/*     */   
/*     */   public NegoFlags set_NEGOTIATE_LM_KEY() {
/* 442 */     this.value |= 0x80;
/* 443 */     return this;
/*     */   }
/*     */   
/*     */   public NegoFlags set_NEGOTIATE_DATAGRAM() {
/* 447 */     this.value |= 0x40;
/* 448 */     return this;
/*     */   }
/*     */   
/*     */   public NegoFlags set_NEGOTIATE_SEAL() {
/* 452 */     this.value |= 0x20;
/* 453 */     return this;
/*     */   }
/*     */   
/*     */   public NegoFlags set_NEGOTIATE_SIGN() {
/* 457 */     this.value |= 0x10;
/* 458 */     return this;
/*     */   }
/*     */   
/*     */   public NegoFlags set_REQUEST_TARGET() {
/* 462 */     this.value |= 0x4;
/* 463 */     return this;
/*     */   }
/*     */   
/*     */   public NegoFlags set_NEGOTIATE_OEM() {
/* 467 */     this.value |= 0x2;
/* 468 */     return this;
/*     */   }
/*     */   
/*     */   public NegoFlags set_NEGOTIATE_UNICODE() {
/* 472 */     this.value |= 0x1;
/* 473 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\NegoFlags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */