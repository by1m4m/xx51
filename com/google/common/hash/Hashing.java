/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.security.Key;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.zip.Adler32;
/*     */ import java.util.zip.CRC32;
/*     */ import java.util.zip.Checksum;
/*     */ import javax.crypto.spec.SecretKeySpec;
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
/*     */ public final class Hashing
/*     */ {
/*     */   public static HashFunction goodFastHash(int minimumBits)
/*     */   {
/*  65 */     int bits = checkPositiveAndMakeMultipleOf32(minimumBits);
/*     */     
/*  67 */     if (bits == 32) {
/*  68 */       return Murmur3_32HashFunction.GOOD_FAST_HASH_32;
/*     */     }
/*  70 */     if (bits <= 128) {
/*  71 */       return Murmur3_128HashFunction.GOOD_FAST_HASH_128;
/*     */     }
/*     */     
/*     */ 
/*  75 */     int hashFunctionsNeeded = (bits + 127) / 128;
/*  76 */     HashFunction[] hashFunctions = new HashFunction[hashFunctionsNeeded];
/*  77 */     hashFunctions[0] = Murmur3_128HashFunction.GOOD_FAST_HASH_128;
/*  78 */     int seed = GOOD_FAST_HASH_SEED;
/*  79 */     for (int i = 1; i < hashFunctionsNeeded; i++) {
/*  80 */       seed += 1500450271;
/*  81 */       hashFunctions[i] = murmur3_128(seed);
/*     */     }
/*  83 */     return new ConcatenatedHashFunction(hashFunctions, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  90 */   static final int GOOD_FAST_HASH_SEED = (int)System.currentTimeMillis();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HashFunction murmur3_32(int seed)
/*     */   {
/* 100 */     return new Murmur3_32HashFunction(seed);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HashFunction murmur3_32()
/*     */   {
/* 111 */     return Murmur3_32HashFunction.MURMUR3_32;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HashFunction murmur3_128(int seed)
/*     */   {
/* 122 */     return new Murmur3_128HashFunction(seed);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HashFunction murmur3_128()
/*     */   {
/* 133 */     return Murmur3_128HashFunction.MURMUR3_128;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HashFunction sipHash24()
/*     */   {
/* 143 */     return SipHashFunction.SIP_HASH_24;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HashFunction sipHash24(long k0, long k1)
/*     */   {
/* 153 */     return new SipHashFunction(2, 4, k0, k1);
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
/*     */   @Deprecated
/*     */   public static HashFunction md5()
/*     */   {
/* 170 */     return Md5Holder.MD5;
/*     */   }
/*     */   
/*     */   private static class Md5Holder {
/* 174 */     static final HashFunction MD5 = new MessageDigestHashFunction("MD5", "Hashing.md5()");
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
/*     */   @Deprecated
/*     */   public static HashFunction sha1()
/*     */   {
/* 191 */     return Sha1Holder.SHA_1;
/*     */   }
/*     */   
/*     */   private static class Sha1Holder {
/* 195 */     static final HashFunction SHA_1 = new MessageDigestHashFunction("SHA-1", "Hashing.sha1()");
/*     */   }
/*     */   
/*     */   public static HashFunction sha256()
/*     */   {
/* 200 */     return Sha256Holder.SHA_256;
/*     */   }
/*     */   
/*     */   private static class Sha256Holder {
/* 204 */     static final HashFunction SHA_256 = new MessageDigestHashFunction("SHA-256", "Hashing.sha256()");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HashFunction sha384()
/*     */   {
/* 214 */     return Sha384Holder.SHA_384;
/*     */   }
/*     */   
/*     */   private static class Sha384Holder {
/* 218 */     static final HashFunction SHA_384 = new MessageDigestHashFunction("SHA-384", "Hashing.sha384()");
/*     */   }
/*     */   
/*     */ 
/*     */   public static HashFunction sha512()
/*     */   {
/* 224 */     return Sha512Holder.SHA_512;
/*     */   }
/*     */   
/*     */   private static class Sha512Holder {
/* 228 */     static final HashFunction SHA_512 = new MessageDigestHashFunction("SHA-512", "Hashing.sha512()");
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
/*     */   public static HashFunction hmacMd5(Key key)
/*     */   {
/* 242 */     return new MacHashFunction("HmacMD5", key, hmacToString("hmacMd5", key));
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
/*     */   public static HashFunction hmacMd5(byte[] key)
/*     */   {
/* 255 */     return hmacMd5(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacMD5"));
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
/*     */   public static HashFunction hmacSha1(Key key)
/*     */   {
/* 268 */     return new MacHashFunction("HmacSHA1", key, hmacToString("hmacSha1", key));
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
/*     */   public static HashFunction hmacSha1(byte[] key)
/*     */   {
/* 281 */     return hmacSha1(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacSHA1"));
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
/*     */   public static HashFunction hmacSha256(Key key)
/*     */   {
/* 294 */     return new MacHashFunction("HmacSHA256", key, hmacToString("hmacSha256", key));
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
/*     */   public static HashFunction hmacSha256(byte[] key)
/*     */   {
/* 307 */     return hmacSha256(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacSHA256"));
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
/*     */   public static HashFunction hmacSha512(Key key)
/*     */   {
/* 320 */     return new MacHashFunction("HmacSHA512", key, hmacToString("hmacSha512", key));
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
/*     */   public static HashFunction hmacSha512(byte[] key)
/*     */   {
/* 333 */     return hmacSha512(new SecretKeySpec((byte[])Preconditions.checkNotNull(key), "HmacSHA512"));
/*     */   }
/*     */   
/*     */   private static String hmacToString(String methodName, Key key) {
/* 337 */     return String.format("Hashing.%s(Key[algorithm=%s, format=%s])", new Object[] { methodName, key
/*     */     
/* 339 */       .getAlgorithm(), key.getFormat() });
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
/*     */   public static HashFunction crc32c()
/*     */   {
/* 353 */     return Crc32cHashFunction.CRC_32_C;
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
/*     */   public static HashFunction crc32()
/*     */   {
/* 369 */     return ChecksumType.CRC_32.hashFunction;
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
/*     */   public static HashFunction adler32()
/*     */   {
/* 385 */     return ChecksumType.ADLER_32.hashFunction;
/*     */   }
/*     */   
/*     */   @Immutable
/*     */   static abstract enum ChecksumType implements ImmutableSupplier<Checksum> {
/* 390 */     CRC_32("Hashing.crc32()"), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 396 */     ADLER_32("Hashing.adler32()");
/*     */     
/*     */ 
/*     */ 
/*     */     public final HashFunction hashFunction;
/*     */     
/*     */ 
/*     */ 
/*     */     private ChecksumType(String toString)
/*     */     {
/* 406 */       this.hashFunction = new ChecksumHashFunction(this, 32, toString);
/*     */     }
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
/*     */   public static HashFunction farmHashFingerprint64()
/*     */   {
/* 429 */     return FarmHashFingerprint64.FARMHASH_FINGERPRINT_64;
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
/*     */   public static int consistentHash(HashCode hashCode, int buckets)
/*     */   {
/* 464 */     return consistentHash(hashCode.padToLong(), buckets);
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
/*     */   public static int consistentHash(long input, int buckets)
/*     */   {
/* 499 */     Preconditions.checkArgument(buckets > 0, "buckets must be positive: %s", buckets);
/* 500 */     LinearCongruentialGenerator generator = new LinearCongruentialGenerator(input);
/* 501 */     int candidate = 0;
/*     */     
/*     */ 
/*     */     for (;;)
/*     */     {
/* 506 */       int next = (int)((candidate + 1) / generator.nextDouble());
/* 507 */       if ((next < 0) || (next >= buckets)) break;
/* 508 */       candidate = next;
/*     */     }
/* 510 */     return candidate;
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
/*     */   public static HashCode combineOrdered(Iterable<HashCode> hashCodes)
/*     */   {
/* 525 */     Iterator<HashCode> iterator = hashCodes.iterator();
/* 526 */     Preconditions.checkArgument(iterator.hasNext(), "Must be at least 1 hash code to combine.");
/* 527 */     int bits = ((HashCode)iterator.next()).bits();
/* 528 */     byte[] resultBytes = new byte[bits / 8];
/* 529 */     for (HashCode hashCode : hashCodes) {
/* 530 */       byte[] nextBytes = hashCode.asBytes();
/* 531 */       Preconditions.checkArgument(nextBytes.length == resultBytes.length, "All hashcodes must have the same bit length.");
/*     */       
/* 533 */       for (int i = 0; i < nextBytes.length; i++) {
/* 534 */         resultBytes[i] = ((byte)(resultBytes[i] * 37 ^ nextBytes[i]));
/*     */       }
/*     */     }
/* 537 */     return HashCode.fromBytesNoCopy(resultBytes);
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
/*     */   public static HashCode combineUnordered(Iterable<HashCode> hashCodes)
/*     */   {
/* 550 */     Iterator<HashCode> iterator = hashCodes.iterator();
/* 551 */     Preconditions.checkArgument(iterator.hasNext(), "Must be at least 1 hash code to combine.");
/* 552 */     byte[] resultBytes = new byte[((HashCode)iterator.next()).bits() / 8];
/* 553 */     for (HashCode hashCode : hashCodes) {
/* 554 */       byte[] nextBytes = hashCode.asBytes();
/* 555 */       Preconditions.checkArgument(nextBytes.length == resultBytes.length, "All hashcodes must have the same bit length.");
/*     */       
/* 557 */       for (int i = 0; i < nextBytes.length; tmp102_100++) {
/* 558 */         int tmp102_100 = i; byte[] tmp102_99 = resultBytes;tmp102_99[tmp102_100] = ((byte)(tmp102_99[tmp102_100] + nextBytes[tmp102_100]));
/*     */       }
/*     */     }
/* 561 */     return HashCode.fromBytesNoCopy(resultBytes);
/*     */   }
/*     */   
/*     */   static int checkPositiveAndMakeMultipleOf32(int bits)
/*     */   {
/* 566 */     Preconditions.checkArgument(bits > 0, "Number of bits must be positive");
/* 567 */     return bits + 31 & 0xFFFFFFE0;
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
/*     */   public static HashFunction concatenating(HashFunction first, HashFunction second, HashFunction... rest)
/*     */   {
/* 583 */     List<HashFunction> list = new ArrayList();
/* 584 */     list.add(first);
/* 585 */     list.add(second);
/* 586 */     list.addAll(Arrays.asList(rest));
/* 587 */     return new ConcatenatedHashFunction((HashFunction[])list.toArray(new HashFunction[0]), null);
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
/*     */   public static HashFunction concatenating(Iterable<HashFunction> hashFunctions)
/*     */   {
/* 601 */     Preconditions.checkNotNull(hashFunctions);
/*     */     
/* 603 */     List<HashFunction> list = new ArrayList();
/* 604 */     for (HashFunction hashFunction : hashFunctions) {
/* 605 */       list.add(hashFunction);
/*     */     }
/* 607 */     Preconditions.checkArgument(list.size() > 0, "number of hash functions (%s) must be > 0", list.size());
/* 608 */     return new ConcatenatedHashFunction((HashFunction[])list.toArray(new HashFunction[0]), null);
/*     */   }
/*     */   
/*     */   private static final class ConcatenatedHashFunction extends AbstractCompositeHashFunction
/*     */   {
/*     */     private ConcatenatedHashFunction(HashFunction... functions) {
/* 614 */       super();
/* 615 */       for (HashFunction function : functions) {
/* 616 */         Preconditions.checkArgument(
/* 617 */           function.bits() % 8 == 0, "the number of bits (%s) in hashFunction (%s) must be divisible by 8", function
/*     */           
/* 619 */           .bits(), function);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     HashCode makeHash(Hasher[] hashers)
/*     */     {
/* 626 */       byte[] bytes = new byte[bits() / 8];
/* 627 */       int i = 0;
/* 628 */       for (Hasher hasher : hashers) {
/* 629 */         HashCode newHash = hasher.hash();
/* 630 */         i += newHash.writeBytesTo(bytes, i, newHash.bits() / 8);
/*     */       }
/* 632 */       return HashCode.fromBytesNoCopy(bytes);
/*     */     }
/*     */     
/*     */     public int bits()
/*     */     {
/* 637 */       int bitSum = 0;
/* 638 */       for (HashFunction function : this.functions) {
/* 639 */         bitSum += function.bits();
/*     */       }
/* 641 */       return bitSum;
/*     */     }
/*     */     
/*     */     public boolean equals(Object object)
/*     */     {
/* 646 */       if ((object instanceof ConcatenatedHashFunction)) {
/* 647 */         ConcatenatedHashFunction other = (ConcatenatedHashFunction)object;
/* 648 */         return Arrays.equals(this.functions, other.functions);
/*     */       }
/* 650 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 655 */       return Arrays.hashCode(this.functions);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class LinearCongruentialGenerator
/*     */   {
/*     */     private long state;
/*     */     
/*     */ 
/*     */     public LinearCongruentialGenerator(long seed)
/*     */     {
/* 667 */       this.state = seed;
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 671 */       this.state = (2862933555777941757L * this.state + 1L);
/* 672 */       return ((int)(this.state >>> 33) + 1) / 2.147483648E9D;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\hash\Hashing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */