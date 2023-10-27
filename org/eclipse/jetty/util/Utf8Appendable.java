/*     */ package org.eclipse.jetty.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Utf8Appendable
/*     */ {
/*  54 */   protected static final Logger LOG = Log.getLogger(Utf8Appendable.class);
/*     */   public static final char REPLACEMENT = '�';
/*  56 */   public static final byte[] REPLACEMENT_UTF8 = { -17, -65, -67 };
/*     */   
/*     */   private static final int UTF8_ACCEPT = 0;
/*     */   private static final int UTF8_REJECT = 12;
/*     */   protected final Appendable _appendable;
/*  61 */   protected int _state = 0;
/*     */   
/*  63 */   private static final byte[] BYTE_TABLE = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 3, 11, 6, 6, 6, 5, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  77 */   private static final byte[] TRANS_TABLE = { 0, 12, 24, 36, 60, 96, 84, 12, 12, 12, 48, 72, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 0, 12, 12, 12, 12, 12, 0, 12, 0, 12, 12, 12, 24, 12, 12, 12, 12, 12, 24, 12, 24, 12, 12, 12, 12, 12, 12, 12, 12, 12, 24, 12, 12, 12, 12, 12, 24, 12, 12, 12, 12, 12, 12, 12, 24, 12, 12, 12, 12, 12, 12, 12, 12, 12, 36, 12, 36, 12, 12, 12, 36, 12, 12, 12, 12, 12, 36, 12, 36, 12, 12, 12, 36, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int _codep;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Utf8Appendable(Appendable appendable)
/*     */   {
/*  92 */     this._appendable = appendable;
/*     */   }
/*     */   
/*     */   public abstract int length();
/*     */   
/*     */   protected void reset()
/*     */   {
/*  99 */     this._state = 0;
/*     */   }
/*     */   
/*     */   private void checkCharAppend()
/*     */     throws IOException
/*     */   {
/* 105 */     if (this._state != 0)
/*     */     {
/* 107 */       this._appendable.append(65533);
/* 108 */       int state = this._state;
/* 109 */       this._state = 0;
/* 110 */       throw new NotUtf8Exception("char appended in state " + state);
/*     */     }
/*     */   }
/*     */   
/*     */   public void append(char c)
/*     */   {
/*     */     try
/*     */     {
/* 118 */       checkCharAppend();
/* 119 */       this._appendable.append(c);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 123 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void append(String s)
/*     */   {
/*     */     try
/*     */     {
/* 131 */       checkCharAppend();
/* 132 */       this._appendable.append(s);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 136 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void append(String s, int offset, int length)
/*     */   {
/*     */     try
/*     */     {
/* 144 */       checkCharAppend();
/* 145 */       this._appendable.append(s, offset, offset + length);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 149 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void append(byte b)
/*     */   {
/*     */     try
/*     */     {
/* 158 */       appendByte(b);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 162 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void append(ByteBuffer buf)
/*     */   {
/*     */     try
/*     */     {
/* 170 */       while (buf.remaining() > 0)
/*     */       {
/* 172 */         appendByte(buf.get());
/*     */       }
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 177 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void append(byte[] b)
/*     */   {
/* 183 */     append(b, 0, b.length);
/*     */   }
/*     */   
/*     */   public void append(byte[] b, int offset, int length)
/*     */   {
/*     */     try
/*     */     {
/* 190 */       int end = offset + length;
/* 191 */       for (int i = offset; i < end; i++) {
/* 192 */         appendByte(b[i]);
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 196 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean append(byte[] b, int offset, int length, int maxChars)
/*     */   {
/*     */     try
/*     */     {
/* 204 */       int end = offset + length;
/* 205 */       for (int i = offset; i < end; i++)
/*     */       {
/* 207 */         if (length() > maxChars)
/* 208 */           return false;
/* 209 */         appendByte(b[i]);
/*     */       }
/* 211 */       return true;
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 215 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void appendByte(byte b)
/*     */     throws IOException
/*     */   {
/* 222 */     if ((b > 0) && (this._state == 0))
/*     */     {
/* 224 */       this._appendable.append((char)(b & 0xFF));
/*     */     }
/*     */     else
/*     */     {
/* 228 */       int i = b & 0xFF;
/* 229 */       int type = BYTE_TABLE[i];
/* 230 */       this._codep = (this._state == 0 ? 255 >> type & i : i & 0x3F | this._codep << 6);
/* 231 */       int next = TRANS_TABLE[(this._state + type)];
/*     */       
/* 233 */       switch (next)
/*     */       {
/*     */       case 0: 
/* 236 */         this._state = next;
/* 237 */         if (this._codep < 55296)
/*     */         {
/* 239 */           this._appendable.append((char)this._codep);
/*     */         }
/*     */         else
/*     */         {
/* 243 */           for (char c : Character.toChars(this._codep))
/* 244 */             this._appendable.append(c);
/*     */         }
/* 246 */         break;
/*     */       
/*     */       case 12: 
/* 249 */         String reason = "byte " + TypeUtil.toHexString(b) + " in state " + this._state / 12;
/* 250 */         this._codep = 0;
/* 251 */         this._state = 0;
/* 252 */         this._appendable.append(65533);
/* 253 */         throw new NotUtf8Exception(reason);
/*     */       
/*     */       default: 
/* 256 */         this._state = next;
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isUtf8SequenceComplete()
/*     */   {
/* 264 */     return this._state == 0;
/*     */   }
/*     */   
/*     */   public static class NotUtf8Exception
/*     */     extends IllegalArgumentException
/*     */   {
/*     */     public NotUtf8Exception(String reason)
/*     */     {
/* 272 */       super();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void checkState()
/*     */   {
/* 278 */     if (!isUtf8SequenceComplete())
/*     */     {
/* 280 */       this._codep = 0;
/* 281 */       this._state = 0;
/*     */       try
/*     */       {
/* 284 */         this._appendable.append(65533);
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 288 */         throw new RuntimeException(e);
/*     */       }
/* 290 */       throw new NotUtf8Exception("incomplete UTF8 sequence");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract String getPartialString();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String takePartialString()
/*     */   {
/* 307 */     String partial = getPartialString();
/* 308 */     int save = this._state;
/* 309 */     reset();
/* 310 */     this._state = save;
/* 311 */     return partial;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toReplacedString()
/*     */   {
/* 317 */     if (!isUtf8SequenceComplete())
/*     */     {
/* 319 */       this._codep = 0;
/* 320 */       this._state = 0;
/*     */       try
/*     */       {
/* 323 */         this._appendable.append(65533);
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 327 */         throw new RuntimeException(e);
/*     */       }
/* 329 */       Throwable th = new NotUtf8Exception("incomplete UTF8 sequence");
/* 330 */       LOG.warn(th.toString(), new Object[0]);
/* 331 */       LOG.debug(th);
/*     */     }
/* 333 */     return this._appendable.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\Utf8Appendable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */