/*    */ package rdp.gold.brute.rdp.Messages;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import rdp.gold.brute.rdp.ByteBuffer;
/*    */ import rdp.gold.brute.rdp.Messages.asn1.TSRequest;
/*    */ import rdp.gold.brute.rdp.Messages.common.asn1.OctetString;
/*    */ 
/*    */ public class ServerNtlmsspPubKeyPlus1 extends rdp.gold.brute.rdp.BaseElement
/*    */ {
/*    */   protected NtlmState ntlmState;
/*    */   
/*    */   public ServerNtlmsspPubKeyPlus1(NtlmState ntlmState)
/*    */   {
/* 14 */     this.ntlmState = ntlmState;
/*    */   }
/*    */   
/*    */   public ByteBuffer proccessPacket(ByteBuffer byteBuffer)
/*    */   {
/* 19 */     TSRequest tsRequest = new TSRequest("TSRequest");
/* 20 */     tsRequest.readTag(byteBuffer);
/*    */     
/* 22 */     ByteBuffer encryptedPubKey = tsRequest.pubKeyAuth.value;
/* 23 */     if ((encryptedPubKey == null) || (encryptedPubKey.length == 0)) {
/* 24 */       throw new RuntimeException("[" + this + "] ERROR: Unexpected message from RDP server. Expected encrypted server public key but got nothing instead. Data: " + byteBuffer);
/*    */     }
/*    */     
/* 27 */     byte[] decryptedPubKey = this.ntlmState.ntlm_DecryptMessage(encryptedPubKey.toByteArray()); int 
/*    */     
/*    */ 
/*    */ 
/* 31 */       tmp86_85 = 0; byte[] tmp86_83 = decryptedPubKey;tmp86_83[tmp86_85] = ((byte)(tmp86_83[tmp86_85] - 1));
/*    */     
/*    */ 
/* 34 */     if (!Arrays.equals(decryptedPubKey, this.ntlmState.subjectPublicKey))
/*    */     {
/*    */ 
/* 37 */       throw new RuntimeException("[" + this + "] ERROR: Unexpected message from RDP server. Expected encrypted server public key but an unknown response. Encryted key after decryption: " + new ByteBuffer(decryptedPubKey).toPlainHexString());
/*    */     }
/* 39 */     byteBuffer.unref();
/*    */     
/* 41 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\ServerNtlmsspPubKeyPlus1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */