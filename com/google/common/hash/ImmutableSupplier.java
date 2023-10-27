package com.google.common.hash;

import com.google.common.base.Supplier;
import com.google.errorprone.annotations.Immutable;

@Immutable
abstract interface ImmutableSupplier<T>
  extends Supplier<T>
{}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\hash\ImmutableSupplier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */