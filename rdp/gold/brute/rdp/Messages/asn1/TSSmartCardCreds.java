/*    */ package rdp.gold.brute.rdp.Messages.asn1;
/*    */ 
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
/*    */ 
/*    */ public class TSSmartCardCreds
/*    */   extends Sequence
/*    */ {
/* 49 */   public OctetString pin = new OctetString("pin") {};
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 56 */   public TSCspDataDetail cspData = new TSCspDataDetail("cspData") {};
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 63 */   public OctetString userHint = new OctetString("userHint") {};
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 71 */   public OctetString domainHint = new OctetString("domainHint") {};
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public TSSmartCardCreds(String name)
/*    */   {
/* 81 */     super(name);
/* 82 */     this.tags = new Tag[] { this.pin, this.cspData, this.userHint, this.domainHint };
/*    */   }
/*    */   
/*    */   public Tag deepCopy(String suffix)
/*    */   {
/* 87 */     return new TSSmartCardCreds(this.name + suffix).copyFrom(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\asn1\TSSmartCardCreds.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */