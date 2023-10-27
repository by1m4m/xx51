/*    */ package rdp.gold.brute.rdp.Messages.common.asn1;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import rdp.gold.brute.rdp.ByteBuffer;
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
/*    */ public class SequenceOf
/*    */   extends Sequence
/*    */ {
/*    */   public Tag type;
/*    */   
/*    */   public SequenceOf(String name)
/*    */   {
/* 36 */     super(name);
/*    */   }
/*    */   
/*    */   protected void parseContent(ByteBuffer buf)
/*    */   {
/* 41 */     ArrayList<Tag> tagList = new ArrayList();
/*    */     
/* 43 */     for (int index = 0; buf.remainderLength() > 0; index++)
/*    */     {
/* 45 */       if ((buf.peekUnsignedByte(0) == 0) && (buf.peekUnsignedByte(1) == 0)) {
/*    */         break;
/*    */       }
/*    */       
/* 49 */       Tag tag = this.type.deepCopy(index);
/*    */       
/* 51 */       tag.readTag(buf);
/* 52 */       tagList.add(tag);
/*    */     }
/*    */     
/* 55 */     this.tags = ((Tag[])tagList.toArray(new Tag[tagList.size()]));
/*    */   }
/*    */   
/*    */   public Tag deepCopy(String suffix)
/*    */   {
/* 60 */     return new SequenceOf(this.name + suffix).copyFrom(this);
/*    */   }
/*    */   
/*    */   public Tag copyFrom(Tag tag)
/*    */   {
/* 65 */     super.copyFrom(tag);
/*    */     
/* 67 */     this.type = ((SequenceOf)tag).type;
/*    */     
/* 69 */     this.tags = new Tag[((Sequence)tag).tags.length];
/* 70 */     for (int i = 0; i < this.tags.length; i++) {
/* 71 */       this.tags[i] = ((Sequence)tag).tags[i].deepCopy("");
/*    */     }
/*    */     
/* 74 */     return this;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 79 */     return super.toString() + ": " + this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\common\asn1\SequenceOf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */