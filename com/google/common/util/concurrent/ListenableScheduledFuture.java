package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.concurrent.ScheduledFuture;

@Beta
@GwtCompatible
public abstract interface ListenableScheduledFuture<V>
  extends ScheduledFuture<V>, ListenableFuture<V>
{}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\ListenableScheduledFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */