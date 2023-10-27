/*    */ package org.eclipse.jetty.websocket.api.extensions;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ public abstract interface Frame { public abstract byte[] getMask();
/*    */   
/*    */   public abstract byte getOpCode();
/*    */   
/*    */   public abstract ByteBuffer getPayload();
/*    */   
/*    */   public abstract int getPayloadLength();
/*    */   
/*    */   public abstract Type getType();
/*    */   
/*    */   public abstract boolean hasPayload();
/*    */   
/*    */   public abstract boolean isFin();
/*    */   
/*    */   @Deprecated
/*    */   public abstract boolean isLast();
/*    */   
/*    */   public abstract boolean isMasked();
/*    */   
/*    */   public abstract boolean isRsv1();
/*    */   
/*    */   public abstract boolean isRsv2();
/*    */   
/*    */   public abstract boolean isRsv3();
/*    */   
/* 30 */   public static enum Type { CONTINUATION((byte)0), 
/* 31 */     TEXT((byte)1), 
/* 32 */     BINARY((byte)2), 
/* 33 */     CLOSE((byte)8), 
/* 34 */     PING((byte)9), 
/* 35 */     PONG((byte)10);
/*    */     
/*    */     private byte opcode;
/*    */     
/* 39 */     public static Type from(byte op) { for (Type type : )
/*    */       {
/* 41 */         if (type.opcode == op)
/*    */         {
/* 43 */           return type;
/*    */         }
/*    */       }
/* 46 */       throw new IllegalArgumentException("OpCode " + op + " is not a valid Frame.Type");
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */     private Type(byte code)
/*    */     {
/* 53 */       this.opcode = code;
/*    */     }
/*    */     
/*    */     public byte getOpCode()
/*    */     {
/* 58 */       return this.opcode;
/*    */     }
/*    */     
/*    */     public boolean isControl()
/*    */     {
/* 63 */       return this.opcode >= CLOSE.getOpCode();
/*    */     }
/*    */     
/*    */     public boolean isData()
/*    */     {
/* 68 */       return (this.opcode == TEXT.getOpCode()) || (this.opcode == BINARY.getOpCode());
/*    */     }
/*    */     
/*    */     public boolean isContinuation()
/*    */     {
/* 73 */       return this.opcode == CONTINUATION.getOpCode();
/*    */     }
/*    */     
/*    */ 
/*    */     public String toString()
/*    */     {
/* 79 */       return name();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\extensions\Frame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */