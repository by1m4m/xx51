package org.apache.commons.codec;

public abstract interface BinaryEncoder
  extends Encoder
{
  public abstract byte[] encode(byte[] paramArrayOfByte)
    throws EncoderException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\codec\BinaryEncoder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */