package io.netty.util.internal;

public abstract interface PriorityQueueNode
{
  public static final int INDEX_NOT_IN_QUEUE = -1;
  
  public abstract int priorityQueueIndex(DefaultPriorityQueue<?> paramDefaultPriorityQueue);
  
  public abstract void priorityQueueIndex(DefaultPriorityQueue<?> paramDefaultPriorityQueue, int paramInt);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\PriorityQueueNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */