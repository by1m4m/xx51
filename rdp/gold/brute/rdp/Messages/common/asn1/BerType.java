/*    */ package rdp.gold.brute.rdp.Messages.common.asn1;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BerType
/*    */ {
/*    */   public int tagClass;
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean constructed;
/*    */   
/*    */ 
/*    */ 
/*    */   public int typeOrTagNumber;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public BerType() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public BerType(int tagClass, boolean constructed, int typeOrTagNumber)
/*    */   {
/* 29 */     this.tagClass = tagClass;
/* 30 */     this.constructed = constructed;
/* 31 */     this.typeOrTagNumber = typeOrTagNumber;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 36 */     return 
/* 37 */       "BerType [tagClass=" + Tag.tagClassToString(this.tagClass) + ", constructed=" + this.constructed + ", type or tag number=" + Tag.tagTypeOrNumberToString(this.tagClass, this.typeOrTagNumber) + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\common\asn1\BerType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */