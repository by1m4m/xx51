/*     */ package rdp.gold.brute.rdp.Messages;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import rdp.gold.brute.rdp.BaseElement;
/*     */ import rdp.gold.brute.rdp.ByteBuffer;
/*     */ import rdp.gold.brute.rdp.Messages.asn1.NegoItem;
/*     */ import rdp.gold.brute.rdp.Messages.asn1.SubjectPublicKeyInfo;
/*     */ import rdp.gold.brute.rdp.Messages.asn1.TSRequest;
/*     */ import rdp.gold.brute.rdp.Messages.common.asn1.BitString;
/*     */ import rdp.gold.brute.rdp.Messages.common.asn1.Tag;
/*     */ import rdp.gold.brute.rdp.RdpConstants;
/*     */ import rdp.gold.brute.rdp.ssl.SSLState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClientNtlmsspPubKeyAuth
/*     */   extends BaseElement
/*     */   implements NtlmConstants
/*     */ {
/*     */   private static final int BLOCKS_OFFSET = 88;
/*     */   protected NtlmState ntlmState;
/*     */   protected SSLState sslState;
/*     */   protected String targetDomain;
/*     */   protected String user;
/*     */   protected String password;
/*     */   protected String workstation;
/*     */   protected String serverHostName;
/*     */   
/*     */   public ClientNtlmsspPubKeyAuth(NtlmState ntlmState, SSLState sslState, String serverHostName, String targetDomain, String workstation, String user, String password)
/*     */   {
/*  52 */     this.ntlmState = ntlmState;
/*  53 */     this.sslState = sslState;
/*  54 */     this.serverHostName = serverHostName;
/*  55 */     this.targetDomain = targetDomain;
/*  56 */     this.workstation = workstation;
/*  57 */     this.user = user;
/*  58 */     this.password = password;
/*     */   }
/*     */   
/*     */ 
/*     */   public static ByteBuffer generateAuthenticateMessage(NtlmState ntlmState)
/*     */   {
/*  64 */     int blocksCursor = 88;
/*     */     
/*  66 */     ByteBuffer buf = new ByteBuffer(4096);
/*     */     
/*     */ 
/*  69 */     buf.writeString("NTLMSSP", RdpConstants.CHARSET_8);
/*  70 */     buf.writeByte(0);
/*     */     
/*     */ 
/*  73 */     buf.writeIntLE(3);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  80 */     blocksCursor = writeBlock(buf, ntlmState.lmChallengeResponse, blocksCursor);
/*     */     
/*     */ 
/*  83 */     blocksCursor = writeBlock(buf, ntlmState.ntChallengeResponse, blocksCursor);
/*     */     
/*     */ 
/*  86 */     blocksCursor = writeStringBlock(buf, ntlmState.domain, blocksCursor, RdpConstants.CHARSET_16);
/*     */     
/*     */ 
/*  89 */     blocksCursor = writeStringBlock(buf, ntlmState.user, blocksCursor, RdpConstants.CHARSET_16);
/*     */     
/*     */ 
/*  92 */     blocksCursor = writeStringBlock(buf, ntlmState.workstation, blocksCursor, RdpConstants.CHARSET_16);
/*     */     
/*     */ 
/*  95 */     blocksCursor = writeBlock(buf, ntlmState.encryptedRandomSessionKey, blocksCursor);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 100 */     buf.writeIntLE(-494357963);
/*     */     
/* 102 */     buf.writeBytes(generateVersion());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 108 */     int savedCursorForMIC = buf.cursor;
/*     */     
/* 110 */     buf.writeBytes(new byte[16]);
/*     */     
/* 112 */     if (88 != buf.cursor) {
/* 113 */       throw new RuntimeException("BUG: Actual offset of first byte of allocated blocks is not equal hardcoded offset. Hardcoded offset: 88, actual offset: " + buf.cursor + ". Update hardcoded offset to match actual offset.");
/*     */     }
/*     */     
/* 116 */     buf.cursor = blocksCursor;
/* 117 */     buf.trimAtCursor();
/*     */     
/* 119 */     ntlmState.authenticateMessage = buf.toByteArray();
/*     */     
/*     */ 
/* 122 */     ntlmState.ntlm_compute_message_integrity_check();
/* 123 */     buf.cursor = savedCursorForMIC;
/* 124 */     buf.writeBytes(ntlmState.messageIntegrityCheck);
/* 125 */     buf.rewindCursor();
/*     */     
/* 127 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int writeStringBlock(ByteBuffer buf, String string, int blocksCursor, Charset charset)
/*     */   {
/* 135 */     return writeBlock(buf, string.getBytes(charset), blocksCursor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int writeBlock(ByteBuffer buf, byte[] block, int blocksCursor)
/*     */   {
/* 146 */     buf.writeShortLE(block.length);
/*     */     
/* 148 */     buf.writeShortLE(block.length);
/*     */     
/* 150 */     buf.writeIntLE(blocksCursor);
/*     */     
/*     */ 
/* 153 */     int savedCursor = buf.cursor;
/* 154 */     buf.cursor = blocksCursor;
/* 155 */     buf.writeBytes(block);
/* 156 */     blocksCursor = buf.cursor;
/* 157 */     buf.cursor = savedCursor;
/*     */     
/* 159 */     return blocksCursor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static byte[] generateVersion()
/*     */   {
/* 171 */     return new byte[] { 6, 1, -79, 29, 0, 0, 0, 15 };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteBuffer proccessPacket(ByteBuffer byteBuffer)
/*     */   {
/* 180 */     this.ntlmState.domain = this.targetDomain;
/* 181 */     this.ntlmState.user = this.user;
/* 182 */     this.ntlmState.password = this.password;
/* 183 */     this.ntlmState.workstation = this.workstation;
/* 184 */     this.ntlmState.generateServicePrincipalName(this.serverHostName);
/* 185 */     this.ntlmState.ntlm_construct_authenticate_target_info();
/* 186 */     this.ntlmState.ntlm_generate_timestamp();
/* 187 */     this.ntlmState.ntlm_generate_client_challenge();
/* 188 */     this.ntlmState.ntlm_compute_lm_v2_response();
/* 189 */     this.ntlmState.ntlm_compute_ntlm_v2_response();
/* 190 */     this.ntlmState.ntlm_generate_key_exchange_key();
/* 191 */     this.ntlmState.ntlm_generate_random_session_key();
/* 192 */     this.ntlmState.ntlm_generate_exported_session_key();
/* 193 */     this.ntlmState.ntlm_encrypt_random_session_key();
/* 194 */     this.ntlmState.ntlm_init_rc4_seal_states();
/*     */     
/* 196 */     ByteBuffer authenticateMessage = generateAuthenticateMessage(this.ntlmState);
/* 197 */     ByteBuffer messageSignatureAndEncryptedServerPublicKey = generateMessageSignatureAndEncryptedServerPublicKey(this.ntlmState);
/*     */     
/*     */ 
/* 200 */     ByteBuffer buf = new ByteBuffer(4096, true);
/*     */     
/* 202 */     TSRequest tsRequest = new TSRequest("TSRequest");
/* 203 */     tsRequest.version.value = Long.valueOf(2L);
/* 204 */     NegoItem negoItem = new NegoItem("NegoItem");
/* 205 */     negoItem.negoToken.value = authenticateMessage;
/*     */     
/* 207 */     tsRequest.negoTokens.tags = new Tag[] { negoItem };
/*     */     
/* 209 */     tsRequest.pubKeyAuth.value = messageSignatureAndEncryptedServerPublicKey;
/*     */     
/* 211 */     tsRequest.writeTag(buf);
/*     */     
/*     */ 
/* 214 */     buf.trimAtCursor();
/*     */     
/* 216 */     return buf;
/*     */   }
/*     */   
/*     */   private byte[] getServerPublicKey()
/*     */   {
/* 221 */     ByteBuffer subjectPublicKeyInfo = new ByteBuffer(this.sslState.serverCertificateSubjectPublicKeyInfo);
/*     */     
/*     */ 
/* 224 */     SubjectPublicKeyInfo parser = new SubjectPublicKeyInfo("SubjectPublicKeyInfo");
/* 225 */     parser.readTag(subjectPublicKeyInfo);
/*     */     
/*     */ 
/* 228 */     ByteBuffer subjectPublicKey = new ByteBuffer(subjectPublicKeyInfo.length);
/* 229 */     parser.subjectPublicKey.writeTag(subjectPublicKey);
/*     */     
/* 231 */     subjectPublicKeyInfo.unref();
/* 232 */     subjectPublicKey.trimAtCursor();
/*     */     
/*     */ 
/*     */ 
/* 236 */     subjectPublicKey.trimHeader(5);
/*     */     
/*     */ 
/* 239 */     this.ntlmState.subjectPublicKey = subjectPublicKey.toByteArray();
/*     */     
/* 241 */     return this.ntlmState.subjectPublicKey;
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
/*     */   private ByteBuffer generateMessageSignatureAndEncryptedServerPublicKey(NtlmState ntlmState)
/*     */   {
/* 256 */     return new ByteBuffer(ntlmState.ntlm_EncryptMessage(getServerPublicKey()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\ClientNtlmsspPubKeyAuth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */