/*    */ package rdp.gold.brute.rdp.Messages.asn1;
/*    */ 
/*    */ import rdp.gold.brute.rdp.Messages.common.asn1.Any;
/*    */ import rdp.gold.brute.rdp.Messages.common.asn1.ObjectID;
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
/*    */ public class AlgorithmIdentifier
/*    */   extends Sequence
/*    */ {
/* 28 */   public ObjectID algorithm = new ObjectID("algorithm");
/* 29 */   public Any parameters = new Any("parameters") {};
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public AlgorithmIdentifier(String name)
/*    */   {
/* 36 */     super(name);
/* 37 */     this.tags = new Tag[] { this.algorithm, this.parameters };
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\asn1\AlgorithmIdentifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */