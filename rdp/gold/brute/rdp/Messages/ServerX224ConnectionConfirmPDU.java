/*     */ package rdp.gold.brute.rdp.Messages;
/*     */ 
/*     */ import org.apache.log4j.Logger;
/*     */ import rdp.gold.brute.rdp.BaseElement;
/*     */ import rdp.gold.brute.rdp.ByteBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerX224ConnectionConfirmPDU
/*     */   extends BaseElement
/*     */ {
/*     */   public static final int X224_TPDU_CONNECTION_REQUEST = 224;
/*     */   public static final int X224_TPDU_CONNECTION_CONFIRM = 208;
/*     */   public static final int X224_TPDU_DISCONNECTION_REQUEST = 128;
/*     */   public static final int X224_TPDU_DISCONNECTION_CONFIRM = 192;
/*     */   public static final int X224_TPDU_EXPEDITED_DATA = 16;
/*     */   public static final int X224_TPDU_DATA_ACKNOWLEDGE = 97;
/*     */   public static final int X224_TPDU_EXPEDITET_ACKNOWLEDGE = 64;
/*     */   public static final int X224_TPDU_REJECT = 81;
/*     */   public static final int X224_TPDU_ERROR = 112;
/*     */   public static final int X224_TPDU_PROTOCOL_IDENTIFIER = 1;
/*     */   public static final int SSL_REQUIRED_BY_SERVER = 1;
/*     */   public static final int SSL_NOT_ALLOWED_BY_SERVER = 2;
/*     */   public static final int SSL_CERT_NOT_ON_SERVER = 3;
/*     */   public static final int INCONSISTENT_FLAGS = 4;
/*     */   public static final int HYBRID_REQUIRED_BY_SERVER = 5;
/*     */   public static final int SSL_WITH_USER_AUTH_REQUIRED_BY_SERVER = 6;
/*  52 */   private final Logger logger = Logger.getLogger(ServerX224ConnectionConfirmPDU.class);
/*     */   
/*     */   public ByteBuffer proccessPacket(ByteBuffer buf)
/*     */   {
/*  56 */     if (buf == null) {
/*  57 */       throw new RuntimeException("Buffer is null");
/*     */     }
/*  59 */     if (this.verbose) {
/*  60 */       this.logger.info("Data received: " + buf + ".");
/*     */     }
/*  62 */     int x224Length = buf.readVariableSignedIntLE();
/*     */     
/*  64 */     int x224Type = buf.readUnsignedByte();
/*  65 */     if (x224Type != 208) {
/*  66 */       throw new RuntimeException("Unexpected type of packet. Expected type: 208 (CONNECTION CONFIRM), actual type: " + x224Type + ", length: " + x224Length + ", buf: " + buf + ".");
/*     */     }
/*     */     
/*     */ 
/*  70 */     buf.skipBytes(2);
/*     */     
/*     */ 
/*     */ 
/*  74 */     buf.skipBytes(2);
/*     */     
/*     */ 
/*  77 */     buf.skipBytes(1);
/*     */     
/*     */ 
/*  80 */     int negType = buf.readUnsignedByte();
/*     */     
/*     */ 
/*  83 */     buf.skipBytes(1);
/*     */     
/*     */ 
/*  86 */     int length = buf.readUnsignedShortLE();
/*     */     
/*  88 */     if (length != 8) {
/*  89 */       throw new RuntimeException("Unexpected length of buffer. Expected value: 8, actual value: " + length + ", RDP NEG buf: " + buf + ".");
/*     */     }
/*     */     
/*  92 */     int protocol = buf.readSignedIntLE();
/*     */     
/*  94 */     if (negType != 2)
/*     */     {
/*     */ 
/*  97 */       int errorCode = protocol;
/*  98 */       String message = "Unknown error.";
/*  99 */       switch (errorCode) {
/*     */       case 1: 
/* 101 */         message = "The server requires that the client support Enhanced RDP Security with either TLS 1.0, 1.1 or 1.2 or CredSSP. If only CredSSP was requested then the server only supports TLS.";
/* 102 */         break;
/*     */       
/*     */       case 2: 
/* 105 */         message = "The server is configured to only use Standard RDP Security mechanisms and does not support any External Security Protocols.";
/* 106 */         break;
/*     */       
/*     */       case 3: 
/* 109 */         message = "The server does not possess a valid authentication certificate and cannot initialize the External Security Protocol Provider.";
/* 110 */         break;
/*     */       
/*     */       case 4: 
/* 113 */         message = "The list of requested security protocols is not consistent with the current security protocol in effect. This error is only possible when the Direct Approach is used and an External Security Protocolis already being used.";
/* 114 */         break;
/*     */       
/*     */       case 5: 
/* 117 */         message = "The server requires that the client support Enhanced RDP Security with CredSSP.";
/* 118 */         break;
/*     */       
/*     */       case 6: 
/* 121 */         message = "The server requires that the client support Enhanced RDP Security  with TLS 1.0, 1.1 or 1.2 and certificate-based client authentication.";
/*     */       }
/*     */       
/*     */       
/* 125 */       throw new RuntimeException("Connection failure: " + message);
/*     */     }
/*     */     
/* 128 */     if ((protocol != 1) && (protocol != 2)) {
/* 129 */       throw new RuntimeException("Unexpected protocol type (nor SSL, nor HYBRID (SSL+CredSSP)): " + protocol + ", RDP NEG buf: " + buf + ".");
/*     */     }
/* 131 */     if (this.verbose) {
/* 132 */       this.logger.info("RDP Negotiation response. Type: " + negType + ", protocol: " + protocol + ".");
/*     */     }
/*     */     
/*     */ 
/* 136 */     return buf;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\ServerX224ConnectionConfirmPDU.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */