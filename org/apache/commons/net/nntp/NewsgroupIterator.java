/*    */ package org.apache.commons.net.nntp;
/*    */ 
/*    */ import java.util.Iterator;
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
/*    */ class NewsgroupIterator
/*    */   implements Iterator<NewsgroupInfo>, Iterable<NewsgroupInfo>
/*    */ {
/*    */   private final Iterator<String> stringIterator;
/*    */   
/*    */   public NewsgroupIterator(Iterable<String> iterableString)
/*    */   {
/* 32 */     this.stringIterator = iterableString.iterator();
/*    */   }
/*    */   
/*    */   public boolean hasNext()
/*    */   {
/* 37 */     return this.stringIterator.hasNext();
/*    */   }
/*    */   
/*    */   public NewsgroupInfo next()
/*    */   {
/* 42 */     String line = (String)this.stringIterator.next();
/* 43 */     return NNTPClient.__parseNewsgroupListEntry(line);
/*    */   }
/*    */   
/*    */   public void remove()
/*    */   {
/* 48 */     this.stringIterator.remove();
/*    */   }
/*    */   
/*    */   public Iterator<NewsgroupInfo> iterator()
/*    */   {
/* 53 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\nntp\NewsgroupIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */