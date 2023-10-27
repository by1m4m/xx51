/*    */ package rdp.gold.brute.rdp.Messages.asn1;
/*    */ 
/*    */ import rdp.gold.brute.rdp.Messages.common.asn1.Asn1Integer;
/*    */ import rdp.gold.brute.rdp.Messages.common.asn1.OctetString;
/*    */ import rdp.gold.brute.rdp.Messages.common.asn1.Sequence;
/*    */ import rdp.gold.brute.rdp.Messages.common.asn1.Tag;
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
/*    */ public class TSCspDataDetail
/*    */   extends Sequence
/*    */ {
/* 49 */   public Asn1Integer keySpec = new Asn1Integer("keySpec") {};
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 56 */   public OctetString cardName = new OctetString("cardName") {};
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 64 */   public OctetString readerName = new OctetString("readerName") {};
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 72 */   public OctetString containerName = new OctetString("containerName") {};
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 80 */   public OctetString cspName = new OctetString("cspName") {};
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public TSCspDataDetail(String name)
/*    */   {
/* 90 */     super(name);
/* 91 */     this.tags = new Tag[] { this.keySpec, this.cardName, this.readerName, this.containerName, this.cspName };
/*    */   }
/*    */   
/*    */   public Tag deepCopy(String suffix)
/*    */   {
/* 96 */     return new TSCspDataDetail(this.name + suffix).copyFrom(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\asn1\TSCspDataDetail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */