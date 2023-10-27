package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.Immutable;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

@Immutable
@Beta
public abstract interface HashFunction
{
  public abstract Hasher newHasher();
  
  public abstract Hasher newHasher(int paramInt);
  
  public abstract HashCode hashInt(int paramInt);
  
  public abstract HashCode hashLong(long paramLong);
  
  public abstract HashCode hashBytes(byte[] paramArrayOfByte);
  
  public abstract HashCode hashBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  public abstract HashCode hashBytes(ByteBuffer paramByteBuffer);
  
  public abstract HashCode hashUnencodedChars(CharSequence paramCharSequence);
  
  public abstract HashCode hashString(CharSequence paramCharSequence, Charset paramCharset);
  
  public abstract <T> HashCode hashObject(T paramT, Funnel<? super T> paramFunnel);
  
  public abstract int bits();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\hash\HashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */