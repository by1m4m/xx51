package net.jpountz.lz4;

@Deprecated
public abstract interface LZ4UnknownSizeDecompressor
{
  public abstract int decompress(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3, int paramInt4);
  
  public abstract int decompress(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\lz4\LZ4UnknownSizeDecompressor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */