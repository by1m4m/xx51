/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.MoreObjects.ToStringHelper;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class PairedStats
/*     */   implements Serializable
/*     */ {
/*     */   private final Stats xStats;
/*     */   private final Stats yStats;
/*     */   private final double sumOfProductsOfDeltas;
/*     */   private static final int BYTES = 88;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   PairedStats(Stats xStats, Stats yStats, double sumOfProductsOfDeltas)
/*     */   {
/*  61 */     this.xStats = xStats;
/*  62 */     this.yStats = yStats;
/*  63 */     this.sumOfProductsOfDeltas = sumOfProductsOfDeltas;
/*     */   }
/*     */   
/*     */   public long count()
/*     */   {
/*  68 */     return this.xStats.count();
/*     */   }
/*     */   
/*     */   public Stats xStats()
/*     */   {
/*  73 */     return this.xStats;
/*     */   }
/*     */   
/*     */   public Stats yStats()
/*     */   {
/*  78 */     return this.yStats;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double populationCovariance()
/*     */   {
/*  96 */     Preconditions.checkState(count() != 0L);
/*  97 */     return this.sumOfProductsOfDeltas / count();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double sampleCovariance()
/*     */   {
/* 114 */     Preconditions.checkState(count() > 1L);
/* 115 */     return this.sumOfProductsOfDeltas / (count() - 1L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double pearsonsCorrelationCoefficient()
/*     */   {
/* 135 */     Preconditions.checkState(count() > 1L);
/* 136 */     if (Double.isNaN(this.sumOfProductsOfDeltas)) {
/* 137 */       return NaN.0D;
/*     */     }
/* 139 */     double xSumOfSquaresOfDeltas = xStats().sumOfSquaresOfDeltas();
/* 140 */     double ySumOfSquaresOfDeltas = yStats().sumOfSquaresOfDeltas();
/* 141 */     Preconditions.checkState(xSumOfSquaresOfDeltas > 0.0D);
/* 142 */     Preconditions.checkState(ySumOfSquaresOfDeltas > 0.0D);
/*     */     
/*     */ 
/*     */ 
/* 146 */     double productOfSumsOfSquaresOfDeltas = ensurePositive(xSumOfSquaresOfDeltas * ySumOfSquaresOfDeltas);
/* 147 */     return ensureInUnitRange(this.sumOfProductsOfDeltas / Math.sqrt(productOfSumsOfSquaresOfDeltas));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LinearTransformation leastSquaresFit()
/*     */   {
/* 182 */     Preconditions.checkState(count() > 1L);
/* 183 */     if (Double.isNaN(this.sumOfProductsOfDeltas)) {
/* 184 */       return LinearTransformation.forNaN();
/*     */     }
/* 186 */     double xSumOfSquaresOfDeltas = this.xStats.sumOfSquaresOfDeltas();
/* 187 */     if (xSumOfSquaresOfDeltas > 0.0D) {
/* 188 */       if (this.yStats.sumOfSquaresOfDeltas() > 0.0D) {
/* 189 */         return 
/* 190 */           LinearTransformation.mapping(this.xStats.mean(), this.yStats.mean()).withSlope(this.sumOfProductsOfDeltas / xSumOfSquaresOfDeltas);
/*     */       }
/* 192 */       return LinearTransformation.horizontal(this.yStats.mean());
/*     */     }
/*     */     
/* 195 */     Preconditions.checkState(this.yStats.sumOfSquaresOfDeltas() > 0.0D);
/* 196 */     return LinearTransformation.vertical(this.xStats.mean());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 217 */     if (obj == null) {
/* 218 */       return false;
/*     */     }
/* 220 */     if (getClass() != obj.getClass()) {
/* 221 */       return false;
/*     */     }
/* 223 */     PairedStats other = (PairedStats)obj;
/* 224 */     if ((this.xStats.equals(other.xStats)) && 
/* 225 */       (this.yStats.equals(other.yStats))) {}
/* 224 */     return 
/*     */     
/*     */ 
/* 227 */       Double.doubleToLongBits(this.sumOfProductsOfDeltas) == Double.doubleToLongBits(other.sumOfProductsOfDeltas);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 238 */     return Objects.hashCode(new Object[] { this.xStats, this.yStats, Double.valueOf(this.sumOfProductsOfDeltas) });
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 243 */     if (count() > 0L) {
/* 244 */       return 
/*     */       
/*     */ 
/*     */ 
/* 248 */         MoreObjects.toStringHelper(this).add("xStats", this.xStats).add("yStats", this.yStats).add("populationCovariance", populationCovariance()).toString();
/*     */     }
/* 250 */     return 
/*     */     
/*     */ 
/* 253 */       MoreObjects.toStringHelper(this).add("xStats", this.xStats).add("yStats", this.yStats).toString();
/*     */   }
/*     */   
/*     */   double sumOfProductsOfDeltas()
/*     */   {
/* 258 */     return this.sumOfProductsOfDeltas;
/*     */   }
/*     */   
/*     */   private static double ensurePositive(double value) {
/* 262 */     if (value > 0.0D) {
/* 263 */       return value;
/*     */     }
/* 265 */     return Double.MIN_VALUE;
/*     */   }
/*     */   
/*     */   private static double ensureInUnitRange(double value)
/*     */   {
/* 270 */     if (value >= 1.0D) {
/* 271 */       return 1.0D;
/*     */     }
/* 273 */     if (value <= -1.0D) {
/* 274 */       return -1.0D;
/*     */     }
/* 276 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] toByteArray()
/*     */   {
/* 291 */     ByteBuffer buffer = ByteBuffer.allocate(88).order(ByteOrder.LITTLE_ENDIAN);
/* 292 */     this.xStats.writeTo(buffer);
/* 293 */     this.yStats.writeTo(buffer);
/* 294 */     buffer.putDouble(this.sumOfProductsOfDeltas);
/* 295 */     return buffer.array();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static PairedStats fromByteArray(byte[] byteArray)
/*     */   {
/* 306 */     Preconditions.checkNotNull(byteArray);
/* 307 */     Preconditions.checkArgument(byteArray.length == 88, "Expected PairedStats.BYTES = %s, got %s", 88, byteArray.length);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 312 */     ByteBuffer buffer = ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN);
/* 313 */     Stats xStats = Stats.readFrom(buffer);
/* 314 */     Stats yStats = Stats.readFrom(buffer);
/* 315 */     double sumOfProductsOfDeltas = buffer.getDouble();
/* 316 */     return new PairedStats(xStats, yStats, sumOfProductsOfDeltas);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\math\PairedStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */