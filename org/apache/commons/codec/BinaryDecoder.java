package org.apache.commons.codec;

public abstract interface BinaryDecoder
  extends Decoder
{
  public abstract byte[] decode(byte[] paramArrayOfByte)
    throws DecoderException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\codec\BinaryDecoder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1
 */