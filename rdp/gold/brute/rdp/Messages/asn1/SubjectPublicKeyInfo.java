/*    */ package rdp.gold.brute.rdp.Messages.asn1;
/*    */ 
/*    */ import rdp.gold.brute.rdp.Messages.common.asn1.BitString;
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
/*    */ public class SubjectPublicKeyInfo
/*    */   extends Sequence
/*    */ {
/* 27 */   public AlgorithmIdentifier algorithm = new AlgorithmIdentifier("algorithm");
/* 28 */   public BitString subjectPublicKey = new BitString("subjectPublicKey");
/*    */   
/*    */   public SubjectPublicKeyInfo(String name) {
/* 31 */     super(name);
/* 32 */     this.tags = new Tag[] { this.algorithm, this.subjectPublicKey };
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\asn1\SubjectPublicKeyInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */