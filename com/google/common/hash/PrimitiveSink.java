package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

@Beta
@CanIgnoreReturnValue
public abstract interface PrimitiveSink
{
  public abstract PrimitiveSink putByte(byte paramByte);
  
  public abstract PrimitiveSink putBytes(byte[] paramArrayOfByte);
  
  public abstract PrimitiveSink putBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  public abstract PrimitiveSink putBytes(ByteBuffer paramByteBuffer);
  
  public abstract PrimitiveSink putShort(short paramShort);
  
  public abstract PrimitiveSink putInt(int paramInt);
  
  public abstract PrimitiveSink putLong(long paramLong);
  
  public abstract PrimitiveSink putFloat(float paramFloat);
  
  public abstract PrimitiveSink putDouble(double paramDouble);
  
  public abstract PrimitiveSink putBoolean(boolean paramBoolean);
  
  public abstract PrimitiveSink putChar(char paramChar);
  
  public abstract PrimitiveSink putUnencodedChars(CharSequence paramCharSequence);
  
  public abstract PrimitiveSink putString(CharSequence paramCharSequence, Charset paramCharset);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\hash\PrimitiveSink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */