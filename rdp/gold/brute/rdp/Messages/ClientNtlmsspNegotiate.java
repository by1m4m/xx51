/*    */ package rdp.gold.brute.rdp.Messages;
/*    */ 
/*    */ import rdp.gold.brute.rdp.BaseElement;
/*    */ import rdp.gold.brute.rdp.ByteBuffer;
/*    */ import rdp.gold.brute.rdp.Messages.asn1.NegoItem;
/*    */ import rdp.gold.brute.rdp.Messages.asn1.TSRequest;
/*    */ import rdp.gold.brute.rdp.Messages.common.asn1.Tag;
/*    */ import rdp.gold.brute.rdp.RdpConstants;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClientNtlmsspNegotiate
/*    */   extends BaseElement
/*    */ {
/* 33 */   public NegoFlags clientConfigFlags = new NegoFlags().set_NEGOTIATE_56().set_NEGOTIATE_KEY_EXCH().set_NEGOTIATE_128().set_NEGOTIATE_VERSION()
/* 34 */     .set_NEGOTIATE_EXTENDED_SESSION_SECURITY().set_NEGOTIATE_ALWAYS_SIGN().set_NEGOTIATE_NTLM().set_NEGOTIATE_LM_KEY().set_NEGOTIATE_SEAL()
/* 35 */     .set_NEGOTIATE_SIGN().set_REQUEST_TARGET().set_NEGOTIATE_OEM().set_NEGOTIATE_UNICODE();
/*    */   protected NtlmState ntlmState;
/*    */   
/*    */   public ClientNtlmsspNegotiate(NtlmState state)
/*    */   {
/* 40 */     this.ntlmState = state;
/*    */   }
/*    */   
/*    */   public ByteBuffer proccessPacket(ByteBuffer byteBuffer)
/*    */   {
/* 45 */     ByteBuffer negoToken = generateNegotiateMessage();
/* 46 */     this.ntlmState.negotiateMessage = negoToken.toByteArray();
/*    */     
/*    */ 
/* 49 */     ByteBuffer buf = new ByteBuffer(1024, true);
/*    */     
/* 51 */     TSRequest tsRequest = new TSRequest("TSRequest");
/* 52 */     tsRequest.version.value = Long.valueOf(2L);
/* 53 */     NegoItem negoItem = new NegoItem("NegoItem");
/* 54 */     negoItem.negoToken.value = negoToken;
/* 55 */     tsRequest.negoTokens.tags = new Tag[] { negoItem };
/*    */     
/* 57 */     tsRequest.writeTag(buf);
/*    */     
/*    */ 
/* 60 */     buf.trimAtCursor();
/*    */     
/* 62 */     return buf;
/*    */   }
/*    */   
/*    */   private ByteBuffer generateNegotiateMessage() {
/* 66 */     ByteBuffer buf = new ByteBuffer(1024);
/*    */     
/*    */ 
/* 69 */     buf.writeString("NTLMSSP", RdpConstants.CHARSET_8);
/* 70 */     buf.writeByte(0);
/*    */     
/*    */ 
/* 73 */     buf.writeIntLE(1);
/*    */     
/* 75 */     buf.writeIntLE(this.clientConfigFlags.value);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 83 */     buf.writeShortLE(0);
/* 84 */     buf.writeShortLE(0);
/* 85 */     buf.writeIntLE(0);
/*    */     
/*    */ 
/* 88 */     buf.writeShortLE(0);
/* 89 */     buf.writeShortLE(0);
/* 90 */     buf.writeIntLE(0);
/*    */     
/*    */ 
/* 93 */     buf.writeBytes(new byte[] { 6, 1, -79, 29, 0, 0, 0, 15 });
/*    */     
/*    */ 
/* 96 */     buf.trimAtCursor();
/*    */     
/* 98 */     return buf;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\ClientNtlmsspNegotiate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */