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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NegoItem
/*    */   extends Sequence
/*    */ {
/* 55 */   public OctetString negoToken = new OctetString("negoToken") {};
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public NegoItem(String name)
/*    */   {
/* 64 */     super(name);
/* 65 */     this.tags = new Tag[] { this.negoToken };
/*    */   }
/*    */   
/*    */   public Tag deepCopy(String suffix)
/*    */   {
/* 70 */     return new NegoItem(this.name + suffix).copyFrom(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\asn1\NegoItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */