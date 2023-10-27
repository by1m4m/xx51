/*    */ package rdp.gold.brute.utils;
/*    */ 
/*    */ import net.jpountz.lz4.LZ4Compressor;
/*    */ import net.jpountz.lz4.LZ4Factory;
/*    */ import org.apache.log4j.Logger;
/*    */ 
/*    */ public final class DataTransformer
/*    */ {
/*  9 */   private static LZ4Factory factory = ;
/* 10 */   private final Logger logger = Logger.getLogger(getClass());
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static byte[] compressBytes(byte[] packet)
/*    */     throws Exception
/*    */   {
/* 18 */     int decompressedLength = packet.length;
/* 19 */     LZ4Compressor compressor = factory.fastCompressor();
/* 20 */     int maxCompressedLength = compressor.maxCompressedLength(decompressedLength);
/* 21 */     byte[] compressed = new byte[maxCompressedLength];
/* 22 */     compressor.compress(packet, 0, decompressedLength, compressed, 0, maxCompressedLength);
/*    */     
/* 24 */     return compressed;
/*    */   }
/*    */   
/*    */   public static byte[] decompressBytes(byte[] packet, int packetLength) throws Exception
/*    */   {
/* 29 */     net.jpountz.lz4.LZ4FastDecompressor decompressor = factory.fastDecompressor();
/* 30 */     byte[] decompressedPacket = new byte[packetLength];
/* 31 */     decompressor.decompress(packet, 0, decompressedPacket, 0, packetLength);
/*    */     
/* 33 */     return decompressedPacket;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\utils\DataTransformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */