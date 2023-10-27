package io.netty.util;

public abstract interface Constant<T extends Constant<T>>
  extends Comparable<T>
{
  public abstract int id();
  
  public abstract String name();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\Constant.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */