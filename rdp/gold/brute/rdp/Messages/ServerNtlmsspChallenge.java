/*     */ package rdp.gold.brute.rdp.Messages;
/*     */ 
/*     */ import org.apache.log4j.Logger;
/*     */ import rdp.gold.brute.rdp.BaseElement;
/*     */ import rdp.gold.brute.rdp.ByteBuffer;
/*     */ import rdp.gold.brute.rdp.ConstantTimeComparator;
/*     */ import rdp.gold.brute.rdp.Messages.asn1.NegoItem;
/*     */ import rdp.gold.brute.rdp.Messages.asn1.TSRequest;
/*     */ import rdp.gold.brute.rdp.Messages.common.asn1.OctetString;
/*     */ import rdp.gold.brute.rdp.RdpConstants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerNtlmsspChallenge
/*     */   extends BaseElement
/*     */   implements NtlmConstants
/*     */ {
/*  28 */   private final Logger logger = Logger.getLogger(ServerNtlmsspChallenge.class);
/*     */   protected NtlmState ntlmState;
/*     */   
/*     */   public ServerNtlmsspChallenge(NtlmState state) {
/*  32 */     this.ntlmState = state;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String readStringByDescription(ByteBuffer buf)
/*     */   {
/*  43 */     ByteBuffer block = readBlockByDescription(buf);
/*  44 */     String value = block.readString(block.length, RdpConstants.CHARSET_16);
/*  45 */     block.unref();
/*     */     
/*  47 */     return value;
/*     */   }
/*     */   
/*     */   public static ByteBuffer readBlockByDescription(ByteBuffer buf) {
/*  51 */     int blockLength = buf.readUnsignedShortLE();
/*  52 */     int allocatedSpace = buf.readUnsignedShortLE();
/*  53 */     int offset = buf.readSignedIntLE();
/*     */     
/*  55 */     if (allocatedSpace < blockLength) {
/*  56 */       blockLength = allocatedSpace;
/*     */     }
/*  58 */     if ((offset > buf.length) || (offset < 0) || (offset + allocatedSpace > buf.length)) {
/*  59 */       throw new RuntimeException("ERROR: NTLM block is too long. Allocated space: " + allocatedSpace + ", block offset: " + offset + ", data: " + buf + ".");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  64 */     int storedCursor = buf.cursor;
/*  65 */     buf.cursor = offset;
/*     */     
/*     */ 
/*  68 */     ByteBuffer value = buf.readBytes(blockLength);
/*     */     
/*     */ 
/*  71 */     buf.cursor = storedCursor;
/*     */     
/*  73 */     return value;
/*     */   }
/*     */   
/*     */   public ByteBuffer proccessPacket(ByteBuffer byteBuffer)
/*     */   {
/*  78 */     if (byteBuffer == null) {
/*  79 */       throw new RuntimeException("Buffer is null");
/*     */     }
/*  81 */     if (this.verbose) {
/*  82 */       this.logger.info("Data received: " + byteBuffer + ".");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  87 */     TSRequest request = new TSRequest("TSRequest");
/*  88 */     request.readTag(byteBuffer);
/*     */     
/*  90 */     ByteBuffer negoToken = ((NegoItem)request.negoTokens.tags[0]).negoToken.value;
/*  91 */     this.ntlmState.challengeMessage = negoToken.toByteArray();
/*     */     
/*  93 */     parseNtlmChallenge(negoToken);
/*     */     
/*  95 */     negoToken.unref();
/*  96 */     byteBuffer.unref();
/*     */     
/*  98 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void parseNtlmChallenge(ByteBuffer buf)
/*     */   {
/* 104 */     String signature = buf.readVariableString(RdpConstants.CHARSET_8);
/* 105 */     if (!ConstantTimeComparator.compareStrings(signature, "NTLMSSP")) {
/* 106 */       throw new RuntimeException("Unexpected NTLM message singature: \"" + signature + "\". Expected signature: \"" + "NTLMSSP" + "\". Data: " + buf + ".");
/*     */     }
/*     */     
/* 109 */     int messageType = buf.readSignedIntLE();
/* 110 */     if (messageType != 2) {
/* 111 */       throw new RuntimeException("Unexpected NTLM message type: " + messageType + ". Expected type: CHALLENGE (" + 2 + "). Data: " + buf + ".");
/*     */     }
/*     */     
/*     */ 
/* 115 */     this.ntlmState.serverTargetName = readStringByDescription(buf);
/*     */     
/*     */ 
/* 118 */     this.ntlmState.negotiatedFlags = new NegoFlags(buf.readSignedIntLE());
/* 119 */     if (this.verbose) {
/* 120 */       this.logger.info("Server negotiate flags: " + this.ntlmState.negotiatedFlags + ".");
/*     */     }
/*     */     
/* 123 */     ByteBuffer challenge = buf.readBytes(8);
/* 124 */     this.ntlmState.serverChallenge = challenge.toByteArray();
/* 125 */     if (this.verbose)
/* 126 */       this.logger.info("Server challenge: " + challenge + ".");
/* 127 */     challenge.unref();
/*     */     
/*     */ 
/* 130 */     buf.skipBytes(8);
/*     */     
/*     */ 
/* 133 */     ByteBuffer targetInfo = readBlockByDescription(buf);
/*     */     
/*     */ 
/* 136 */     this.ntlmState.serverTargetInfo = targetInfo.toByteArray();
/*     */     
/*     */ 
/* 139 */     parseTargetInfo(targetInfo);
/* 140 */     targetInfo.unref();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 146 */     buf.unref();
/*     */   }
/*     */   
/*     */ 
/*     */   public void parseTargetInfo(ByteBuffer buf)
/*     */   {
/* 152 */     while (buf.remainderLength() > 0) {
/* 153 */       int type = buf.readUnsignedShortLE();
/* 154 */       int length = buf.readUnsignedShortLE();
/*     */       
/* 156 */       if (type == 0) {
/*     */         break;
/*     */       }
/*     */       
/* 160 */       ByteBuffer data = buf.readBytes(length);
/* 161 */       parseAttribute(data, type, length);
/* 162 */       data.unref();
/*     */     }
/*     */   }
/*     */   
/*     */   public void parseAttribute(ByteBuffer buf, int type, int length) {
/* 167 */     switch (type) {
/*     */     case 2: 
/* 169 */       this.ntlmState.serverNetbiosDomainName = buf.readString(length, RdpConstants.CHARSET_16);
/* 170 */       break;
/*     */     case 1: 
/* 172 */       this.ntlmState.serverNetbiosComputerName = buf.readString(length, RdpConstants.CHARSET_16);
/* 173 */       break;
/*     */     case 4: 
/* 175 */       this.ntlmState.serverDnsDomainName = buf.readString(length, RdpConstants.CHARSET_16);
/* 176 */       break;
/*     */     case 3: 
/* 178 */       this.ntlmState.serverDnsComputerName = buf.readString(length, RdpConstants.CHARSET_16);
/* 179 */       break;
/*     */     case 5: 
/* 181 */       this.ntlmState.serverDnsTreeName = buf.readString(length, RdpConstants.CHARSET_16);
/* 182 */       break;
/*     */     
/*     */     case 7: 
/* 185 */       ByteBuffer tmp = buf.readBytes(length);
/* 186 */       this.ntlmState.serverTimestamp = tmp.toByteArray();
/*     */       
/* 188 */       tmp.unref();
/* 189 */       break;
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NtlmState getNtlmState()
/*     */   {
/* 200 */     return this.ntlmState;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\ServerNtlmsspChallenge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */