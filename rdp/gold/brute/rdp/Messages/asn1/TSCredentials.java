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
/*    */ public class TSCredentials
/*    */   extends Sequence
/*    */ {
/* 37 */   public Asn1Integer credType = new Asn1Integer("credType") {};
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 44 */   public OctetString credentials = new OctetString("credentials") {};
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public TSCredentials(String name)
/*    */   {
/* 53 */     super(name);
/* 54 */     this.tags = new Tag[] { this.credType, this.credentials };
/*    */   }
/*    */   
/*    */   public Tag deepCopy(String suffix)
/*    */   {
/* 59 */     return new TSCredentials(this.name + suffix).copyFrom(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\asn1\TSCredentials.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */