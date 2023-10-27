/*    */ package com.sun.jna.platform.mac;
/*    */ 
/*    */ import com.sun.jna.Memory;
/*    */ import java.nio.Buffer;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.CharBuffer;
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class XAttrUtil
/*    */ {
/*    */   public static List<String> listXAttr(String path)
/*    */   {
/* 26 */     long bufferLength = XAttr.INSTANCE.listxattr(path, null, 0L, 0);
/*    */     
/* 28 */     if (bufferLength < 0L) {
/* 29 */       return null;
/*    */     }
/* 31 */     if (bufferLength == 0L) {
/* 32 */       return new ArrayList(0);
/*    */     }
/* 34 */     Memory valueBuffer = new Memory(bufferLength);
/* 35 */     long valueLength = XAttr.INSTANCE.listxattr(path, valueBuffer, bufferLength, 0);
/*    */     
/* 37 */     if (valueLength < 0L) {
/* 38 */       return null;
/*    */     }
/* 40 */     return decodeStringSequence(valueBuffer.getByteBuffer(0L, valueLength));
/*    */   }
/*    */   
/*    */   public static String getXAttr(String path, String name)
/*    */   {
/* 45 */     long bufferLength = XAttr.INSTANCE.getxattr(path, name, null, 0L, 0, 0);
/*    */     
/* 47 */     if (bufferLength < 0L) {
/* 48 */       return null;
/*    */     }
/* 50 */     Memory valueBuffer = new Memory(bufferLength);
/* 51 */     long valueLength = XAttr.INSTANCE.getxattr(path, name, valueBuffer, bufferLength, 0, 0);
/*    */     
/* 53 */     if (valueLength < 0L) {
/* 54 */       return null;
/*    */     }
/* 56 */     return decodeString(valueBuffer.getByteBuffer(0L, valueLength - 1L));
/*    */   }
/*    */   
/*    */   public static int setXAttr(String path, String name, String value) {
/* 60 */     Memory valueBuffer = encodeString(value);
/* 61 */     return XAttr.INSTANCE.setxattr(path, name, valueBuffer, valueBuffer.size(), 0, 0);
/*    */   }
/*    */   
/*    */   public static int removeXAttr(String path, String name) {
/* 65 */     return XAttr.INSTANCE.removexattr(path, name, 0);
/*    */   }
/*    */   
/*    */   protected static Memory encodeString(String s)
/*    */   {
/* 70 */     byte[] bb = s.getBytes(Charset.forName("UTF-8"));
/* 71 */     Memory valueBuffer = new Memory(bb.length + 1);
/* 72 */     valueBuffer.write(0L, bb, 0, bb.length);
/* 73 */     valueBuffer.setByte(valueBuffer.size() - 1L, (byte)0);
/* 74 */     return valueBuffer;
/*    */   }
/*    */   
/*    */   protected static String decodeString(ByteBuffer bb) {
/* 78 */     return Charset.forName("UTF-8").decode(bb).toString();
/*    */   }
/*    */   
/*    */   protected static List<String> decodeStringSequence(ByteBuffer bb) {
/* 82 */     List<String> names = new ArrayList();
/*    */     
/* 84 */     bb.mark();
/* 85 */     while (bb.hasRemaining()) {
/* 86 */       if (bb.get() == 0) {
/* 87 */         ByteBuffer nameBuffer = (ByteBuffer)bb.duplicate().limit(bb.position() - 1).reset();
/* 88 */         if (nameBuffer.hasRemaining()) {
/* 89 */           names.add(decodeString(nameBuffer));
/*    */         }
/* 91 */         bb.mark();
/*    */       }
/*    */     }
/*    */     
/* 95 */     return names;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\platform\mac\XAttrUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */