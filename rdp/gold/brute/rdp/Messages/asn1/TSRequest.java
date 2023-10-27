/*     */ package rdp.gold.brute.rdp.Messages.asn1;
/*     */ 
/*     */ import rdp.gold.brute.rdp.ByteBuffer;
/*     */ import rdp.gold.brute.rdp.Messages.common.asn1.Asn1Integer;
/*     */ import rdp.gold.brute.rdp.Messages.common.asn1.OctetString;
/*     */ import rdp.gold.brute.rdp.Messages.common.asn1.Sequence;
/*     */ import rdp.gold.brute.rdp.Messages.common.asn1.Tag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TSRequest
/*     */   extends Sequence
/*     */ {
/*  79 */   public Asn1Integer version = new Asn1Integer("version") {};
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  86 */   public NegoData negoTokens = new NegoData("negoTokens") {};
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  94 */   public OctetString authInfo = new OctetString("authInfo") {};
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */   public OctetString pubKeyAuth = new OctetString("pubKeyAuth") {};
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TSRequest(String name)
/*     */   {
/* 112 */     super(name);
/* 113 */     this.tags = new Tag[] { this.version, this.negoTokens, this.authInfo, this.pubKeyAuth };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 122 */     byte[] packet = { 48, -126, 1, 2, -96, 3, 2, 1, 3, -95, -127, -6, 48, -127, -9, 48, -127, -12, -96, -127, -15, 4, -127, -18, 78, 84, 76, 77, 83, 83, 80, 0, 2, 0, 0, 0, 30, 0, 30, 0, 56, 0, 0, 0, 53, -126, -118, -30, 82, -66, -125, -47, -8, Byte.MIN_VALUE, 22, 106, 0, 0, 0, 0, 0, 0, 0, 0, -104, 0, -104, 0, 86, 0, 0, 0, 6, 3, -41, 36, 0, 0, 0, 15, 87, 0, 73, 0, 78, 0, 45, 0, 76, 0, 79, 0, 52, 0, 49, 0, 57, 0, 66, 0, 50, 0, 76, 0, 83, 0, 82, 0, 48, 0, 2, 0, 30, 0, 87, 0, 73, 0, 78, 0, 45, 0, 76, 0, 79, 0, 52, 0, 49, 0, 57, 0, 66, 0, 50, 0, 76, 0, 83, 0, 82, 0, 48, 0, 1, 0, 30, 0, 87, 0, 73, 0, 78, 0, 45, 0, 76, 0, 79, 0, 52, 0, 49, 0, 57, 0, 66, 0, 50, 0, 76, 0, 83, 0, 82, 0, 48, 0, 4, 0, 30, 0, 87, 0, 73, 0, 78, 0, 45, 0, 76, 0, 79, 0, 52, 0, 49, 0, 57, 0, 66, 0, 50, 0, 76, 0, 83, 0, 82, 0, 48, 0, 3, 0, 30, 0, 87, 0, 73, 0, 78, 0, 45, 0, 76, 0, 79, 0, 52, 0, 49, 0, 57, 0, 66, 0, 50, 0, 76, 0, 83, 0, 82, 0, 48, 0, 7, 0, 8, 0, -103, 79, 2, -40, -12, -81, -50, 1, 0, 0, 0, 0 };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 175 */     TSRequest request = new TSRequest("TSRequest");
/*     */     
/*     */ 
/*     */ 
/* 179 */     ByteBuffer toReadBuf = new ByteBuffer(packet);
/* 180 */     request.readTag(toReadBuf);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 188 */     ByteBuffer toWriteBuf = new ByteBuffer(packet.length + 100, true);
/* 189 */     request.writeTag(toWriteBuf);
/* 190 */     toWriteBuf.trimAtCursor();
/*     */     
/* 192 */     if (!toReadBuf.equals(toWriteBuf)) {
/* 193 */       throw new RuntimeException("Data written to buffer is not equal to data read from buffer. \nExpected: " + toReadBuf + "\nActual: " + toWriteBuf + ".");
/*     */     }
/*     */   }
/*     */   
/*     */   public Tag deepCopy(String suffix) {
/* 198 */     return new TSRequest(this.name + suffix).copyFrom(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\asn1\TSRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */