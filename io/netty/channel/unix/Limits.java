/*    */ package io.netty.channel.unix;
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
/*    */ public final class Limits
/*    */ {
/* 24 */   public static final int IOV_MAX = ;
/* 25 */   public static final int UIO_MAX_IOV = LimitsStaticallyReferencedJniMethods.uioMaxIov();
/* 26 */   public static final long SSIZE_MAX = LimitsStaticallyReferencedJniMethods.ssizeMax();
/*    */   
/* 28 */   public static final int SIZEOF_JLONG = LimitsStaticallyReferencedJniMethods.sizeOfjlong();
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\unix\Limits.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */