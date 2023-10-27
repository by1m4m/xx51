/*    */ package com.google.common.io;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
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
/*    */ @GwtIncompatible
/*    */ class MultiReader
/*    */   extends Reader
/*    */ {
/*    */   private final Iterator<? extends CharSource> it;
/*    */   private Reader current;
/*    */   
/*    */   MultiReader(Iterator<? extends CharSource> readers)
/*    */     throws IOException
/*    */   {
/* 36 */     this.it = readers;
/* 37 */     advance();
/*    */   }
/*    */   
/*    */   private void advance() throws IOException
/*    */   {
/* 42 */     close();
/* 43 */     if (this.it.hasNext()) {
/* 44 */       this.current = ((CharSource)this.it.next()).openStream();
/*    */     }
/*    */   }
/*    */   
/*    */   public int read(char[] cbuf, int off, int len) throws IOException
/*    */   {
/* 50 */     if (this.current == null) {
/* 51 */       return -1;
/*    */     }
/* 53 */     int result = this.current.read(cbuf, off, len);
/* 54 */     if (result == -1) {
/* 55 */       advance();
/* 56 */       return read(cbuf, off, len);
/*    */     }
/* 58 */     return result;
/*    */   }
/*    */   
/*    */   public long skip(long n) throws IOException
/*    */   {
/* 63 */     Preconditions.checkArgument(n >= 0L, "n is negative");
/* 64 */     if (n > 0L) {
/* 65 */       while (this.current != null) {
/* 66 */         long result = this.current.skip(n);
/* 67 */         if (result > 0L) {
/* 68 */           return result;
/*    */         }
/* 70 */         advance();
/*    */       }
/*    */     }
/* 73 */     return 0L;
/*    */   }
/*    */   
/*    */   public boolean ready() throws IOException
/*    */   {
/* 78 */     return (this.current != null) && (this.current.ready());
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public void close()
/*    */     throws IOException
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: getfield 9	com/google/common/io/MultiReader:current	Ljava/io/Reader;
/*    */     //   4: ifnull +26 -> 30
/*    */     //   7: aload_0
/*    */     //   8: getfield 9	com/google/common/io/MultiReader:current	Ljava/io/Reader;
/*    */     //   11: invokevirtual 16	java/io/Reader:close	()V
/*    */     //   14: aload_0
/*    */     //   15: aconst_null
/*    */     //   16: putfield 9	com/google/common/io/MultiReader:current	Ljava/io/Reader;
/*    */     //   19: goto +11 -> 30
/*    */     //   22: astore_1
/*    */     //   23: aload_0
/*    */     //   24: aconst_null
/*    */     //   25: putfield 9	com/google/common/io/MultiReader:current	Ljava/io/Reader;
/*    */     //   28: aload_1
/*    */     //   29: athrow
/*    */     //   30: return
/*    */     // Line number table:
/*    */     //   Java source line #83	-> byte code offset #0
/*    */     //   Java source line #85	-> byte code offset #7
/*    */     //   Java source line #87	-> byte code offset #14
/*    */     //   Java source line #88	-> byte code offset #19
/*    */     //   Java source line #87	-> byte code offset #22
/*    */     //   Java source line #88	-> byte code offset #28
/*    */     //   Java source line #90	-> byte code offset #30
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	31	0	this	MultiReader
/*    */     //   22	7	1	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   7	14	22	finally
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\io\MultiReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */