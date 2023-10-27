package io.netty.internal.tcnative;

import java.nio.ByteBuffer;

public final class Buffer
{
  public static native long address(ByteBuffer paramByteBuffer);
  
  public static native long size(ByteBuffer paramByteBuffer);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\internal\tcnative\Buffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */