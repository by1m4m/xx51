/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.TreeTraverser;
/*     */ import com.google.common.graph.SuccessorsFunction;
/*     */ import com.google.common.graph.Traverser;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.MappedByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.FileChannel.MapMode;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ @GwtIncompatible
/*     */ public final class Files
/*     */ {
/*     */   private static final int TEMP_DIR_ATTEMPTS = 10000;
/*     */   
/*     */   public static BufferedReader newReader(File file, Charset charset)
/*     */     throws FileNotFoundException
/*     */   {
/*  87 */     Preconditions.checkNotNull(file);
/*  88 */     Preconditions.checkNotNull(charset);
/*  89 */     return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
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
/*     */   public static BufferedWriter newWriter(File file, Charset charset)
/*     */     throws FileNotFoundException
/*     */   {
/* 105 */     Preconditions.checkNotNull(file);
/* 106 */     Preconditions.checkNotNull(charset);
/* 107 */     return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteSource asByteSource(File file)
/*     */   {
/* 116 */     return new FileByteSource(file, null);
/*     */   }
/*     */   
/*     */   private static final class FileByteSource extends ByteSource
/*     */   {
/*     */     private final File file;
/*     */     
/*     */     private FileByteSource(File file) {
/* 124 */       this.file = ((File)Preconditions.checkNotNull(file));
/*     */     }
/*     */     
/*     */     public FileInputStream openStream() throws IOException
/*     */     {
/* 129 */       return new FileInputStream(this.file);
/*     */     }
/*     */     
/*     */     public Optional<Long> sizeIfKnown()
/*     */     {
/* 134 */       if (this.file.isFile()) {
/* 135 */         return Optional.of(Long.valueOf(this.file.length()));
/*     */       }
/* 137 */       return Optional.absent();
/*     */     }
/*     */     
/*     */     public long size()
/*     */       throws IOException
/*     */     {
/* 143 */       if (!this.file.isFile()) {
/* 144 */         throw new FileNotFoundException(this.file.toString());
/*     */       }
/* 146 */       return this.file.length();
/*     */     }
/*     */     
/*     */     public byte[] read() throws IOException
/*     */     {
/* 151 */       Closer closer = Closer.create();
/*     */       try {
/* 153 */         FileInputStream in = (FileInputStream)closer.register(openStream());
/* 154 */         return ByteStreams.toByteArray(in, in.getChannel().size());
/*     */       } catch (Throwable e) {
/* 156 */         throw closer.rethrow(e);
/*     */       } finally {
/* 158 */         closer.close();
/*     */       }
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 164 */       return "Files.asByteSource(" + this.file + ")";
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
/*     */   public static ByteSink asByteSink(File file, FileWriteMode... modes)
/*     */   {
/* 177 */     return new FileByteSink(file, modes, null);
/*     */   }
/*     */   
/*     */   private static final class FileByteSink extends ByteSink
/*     */   {
/*     */     private final File file;
/*     */     private final ImmutableSet<FileWriteMode> modes;
/*     */     
/*     */     private FileByteSink(File file, FileWriteMode... modes) {
/* 186 */       this.file = ((File)Preconditions.checkNotNull(file));
/* 187 */       this.modes = ImmutableSet.copyOf(modes);
/*     */     }
/*     */     
/*     */     public FileOutputStream openStream() throws IOException
/*     */     {
/* 192 */       return new FileOutputStream(this.file, this.modes.contains(FileWriteMode.APPEND));
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 197 */       return "Files.asByteSink(" + this.file + ", " + this.modes + ")";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static CharSource asCharSource(File file, Charset charset)
/*     */   {
/* 208 */     return asByteSource(file).asCharSource(charset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static CharSink asCharSink(File file, Charset charset, FileWriteMode... modes)
/*     */   {
/* 220 */     return asByteSink(file, modes).asCharSink(charset);
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
/*     */   public static byte[] toByteArray(File file)
/*     */     throws IOException
/*     */   {
/* 235 */     return asByteSource(file).read();
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
/*     */   @Deprecated
/*     */   public static String toString(File file, Charset charset)
/*     */     throws IOException
/*     */   {
/* 251 */     return asCharSource(file, charset).read();
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
/*     */   public static void write(byte[] from, File to)
/*     */     throws IOException
/*     */   {
/* 265 */     asByteSink(to, new FileWriteMode[0]).write(from);
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
/*     */   @Deprecated
/*     */   public static void write(CharSequence from, File to, Charset charset)
/*     */     throws IOException
/*     */   {
/* 281 */     asCharSink(to, charset, new FileWriteMode[0]).write(from);
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
/*     */   public static void copy(File from, OutputStream to)
/*     */     throws IOException
/*     */   {
/* 295 */     asByteSource(from).copyTo(to);
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
/*     */   public static void copy(File from, File to)
/*     */     throws IOException
/*     */   {
/* 318 */     Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
/* 319 */     asByteSource(from).copyTo(asByteSink(to, new FileWriteMode[0]));
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
/*     */   @Deprecated
/*     */   public static void copy(File from, Charset charset, Appendable to)
/*     */     throws IOException
/*     */   {
/* 335 */     asCharSource(from, charset).copyTo(to);
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
/*     */   @Deprecated
/*     */   public static void append(CharSequence from, File to, Charset charset)
/*     */     throws IOException
/*     */   {
/* 351 */     asCharSink(to, charset, new FileWriteMode[] { FileWriteMode.APPEND }).write(from);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean equal(File file1, File file2)
/*     */     throws IOException
/*     */   {
/* 360 */     Preconditions.checkNotNull(file1);
/* 361 */     Preconditions.checkNotNull(file2);
/* 362 */     if ((file1 == file2) || (file1.equals(file2))) {
/* 363 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 371 */     long len1 = file1.length();
/* 372 */     long len2 = file2.length();
/* 373 */     if ((len1 != 0L) && (len2 != 0L) && (len1 != len2)) {
/* 374 */       return false;
/*     */     }
/* 376 */     return asByteSource(file1).contentEquals(asByteSource(file2));
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
/*     */   public static File createTempDir()
/*     */   {
/* 399 */     File baseDir = new File(System.getProperty("java.io.tmpdir"));
/* 400 */     String baseName = System.currentTimeMillis() + "-";
/*     */     
/* 402 */     for (int counter = 0; counter < 10000; counter++) {
/* 403 */       File tempDir = new File(baseDir, baseName + counter);
/* 404 */       if (tempDir.mkdir()) {
/* 405 */         return tempDir;
/*     */       }
/*     */     }
/* 408 */     throw new IllegalStateException("Failed to create directory within 10000 attempts (tried " + baseName + "0 to " + baseName + 9999 + ')');
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
/*     */   public static void touch(File file)
/*     */     throws IOException
/*     */   {
/* 427 */     Preconditions.checkNotNull(file);
/* 428 */     if ((!file.createNewFile()) && (!file.setLastModified(System.currentTimeMillis()))) {
/* 429 */       throw new IOException("Unable to update modification time of " + file);
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
/*     */   public static void createParentDirs(File file)
/*     */     throws IOException
/*     */   {
/* 443 */     Preconditions.checkNotNull(file);
/* 444 */     File parent = file.getCanonicalFile().getParentFile();
/* 445 */     if (parent == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 452 */       return;
/*     */     }
/* 454 */     parent.mkdirs();
/* 455 */     if (!parent.isDirectory()) {
/* 456 */       throw new IOException("Unable to create parent directories of " + file);
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
/*     */   public static void move(File from, File to)
/*     */     throws IOException
/*     */   {
/* 473 */     Preconditions.checkNotNull(from);
/* 474 */     Preconditions.checkNotNull(to);
/* 475 */     Preconditions.checkArgument(!from.equals(to), "Source %s and destination %s must be different", from, to);
/*     */     
/* 477 */     if (!from.renameTo(to)) {
/* 478 */       copy(from, to);
/* 479 */       if (!from.delete()) {
/* 480 */         if (!to.delete()) {
/* 481 */           throw new IOException("Unable to delete " + to);
/*     */         }
/* 483 */         throw new IOException("Unable to delete " + from);
/*     */       }
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
/*     */   @Deprecated
/*     */   public static String readFirstLine(File file, Charset charset)
/*     */     throws IOException
/*     */   {
/* 502 */     return asCharSource(file, charset).readFirstLine();
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
/*     */   public static List<String> readLines(File file, Charset charset)
/*     */     throws IOException
/*     */   {
/* 524 */     
/* 525 */       (List)asCharSource(file, charset).readLines(new LineProcessor()
/*     */       {
/* 527 */         final List<String> result = Lists.newArrayList();
/*     */         
/*     */         public boolean processLine(String line)
/*     */         {
/* 531 */           this.result.add(line);
/* 532 */           return true;
/*     */         }
/*     */         
/*     */         public List<String> getResult()
/*     */         {
/* 537 */           return this.result;
/*     */         }
/*     */       });
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readLines(File file, Charset charset, LineProcessor<T> callback)
/*     */     throws IOException
/*     */   {
/* 559 */     return (T)asCharSource(file, charset).readLines(callback);
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readBytes(File file, ByteProcessor<T> processor)
/*     */     throws IOException
/*     */   {
/* 577 */     return (T)asByteSource(file).read(processor);
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
/*     */   @Deprecated
/*     */   public static HashCode hash(File file, HashFunction hashFunction)
/*     */     throws IOException
/*     */   {
/* 593 */     return asByteSource(file).hash(hashFunction);
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
/*     */   public static MappedByteBuffer map(File file)
/*     */     throws IOException
/*     */   {
/* 612 */     Preconditions.checkNotNull(file);
/* 613 */     return map(file, FileChannel.MapMode.READ_ONLY);
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
/*     */   public static MappedByteBuffer map(File file, FileChannel.MapMode mode)
/*     */     throws IOException
/*     */   {
/* 634 */     return mapInternal(file, mode, -1L);
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
/*     */   public static MappedByteBuffer map(File file, FileChannel.MapMode mode, long size)
/*     */     throws IOException
/*     */   {
/* 657 */     Preconditions.checkArgument(size >= 0L, "size (%s) may not be negative", size);
/* 658 */     return mapInternal(file, mode, size);
/*     */   }
/*     */   
/*     */   private static MappedByteBuffer mapInternal(File file, FileChannel.MapMode mode, long size) throws IOException
/*     */   {
/* 663 */     Preconditions.checkNotNull(file);
/* 664 */     Preconditions.checkNotNull(mode);
/*     */     
/* 666 */     Closer closer = Closer.create();
/*     */     try
/*     */     {
/* 669 */       RandomAccessFile raf = (RandomAccessFile)closer.register(new RandomAccessFile(file, mode == FileChannel.MapMode.READ_ONLY ? "r" : "rw"));
/* 670 */       FileChannel channel = (FileChannel)closer.register(raf.getChannel());
/* 671 */       return channel.map(mode, 0L, size == -1L ? channel.size() : size);
/*     */     } catch (Throwable e) {
/* 673 */       throw closer.rethrow(e);
/*     */     } finally {
/* 675 */       closer.close();
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
/*     */ 
/*     */ 
/*     */   public static String simplifyPath(String pathname)
/*     */   {
/* 700 */     Preconditions.checkNotNull(pathname);
/* 701 */     if (pathname.length() == 0) {
/* 702 */       return ".";
/*     */     }
/*     */     
/*     */ 
/* 706 */     Iterable<String> components = Splitter.on('/').omitEmptyStrings().split(pathname);
/* 707 */     List<String> path = new ArrayList();
/*     */     
/*     */ 
/* 710 */     for (String component : components) {
/* 711 */       switch (component) {
/*     */       case ".": 
/*     */         break;
/*     */       case "..": 
/* 715 */         if ((path.size() > 0) && (!((String)path.get(path.size() - 1)).equals(".."))) {
/* 716 */           path.remove(path.size() - 1);
/*     */         } else {
/* 718 */           path.add("..");
/*     */         }
/* 720 */         break;
/*     */       default: 
/* 722 */         path.add(component);
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/* 728 */     String result = Joiner.on('/').join(path);
/* 729 */     if (pathname.charAt(0) == '/') {
/* 730 */       result = "/" + result;
/*     */     }
/*     */     
/* 733 */     while (result.startsWith("/../")) {
/* 734 */       result = result.substring(3);
/*     */     }
/* 736 */     if (result.equals("/..")) {
/* 737 */       result = "/";
/* 738 */     } else if ("".equals(result)) {
/* 739 */       result = ".";
/*     */     }
/*     */     
/* 742 */     return result;
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
/*     */   public static String getFileExtension(String fullName)
/*     */   {
/* 760 */     Preconditions.checkNotNull(fullName);
/* 761 */     String fileName = new File(fullName).getName();
/* 762 */     int dotIndex = fileName.lastIndexOf('.');
/* 763 */     return dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
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
/*     */   public static String getNameWithoutExtension(String file)
/*     */   {
/* 777 */     Preconditions.checkNotNull(file);
/* 778 */     String fileName = new File(file).getName();
/* 779 */     int dotIndex = fileName.lastIndexOf('.');
/* 780 */     return dotIndex == -1 ? fileName : fileName.substring(0, dotIndex);
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
/*     */   static TreeTraverser<File> fileTreeTraverser()
/*     */   {
/* 797 */     return FILE_TREE_TRAVERSER;
/*     */   }
/*     */   
/* 800 */   private static final TreeTraverser<File> FILE_TREE_TRAVERSER = new TreeTraverser()
/*     */   {
/*     */     public Iterable<File> children(File file)
/*     */     {
/* 804 */       return Files.fileTreeChildren(file);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 809 */       return "Files.fileTreeTraverser()";
/*     */     }
/*     */   };
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
/*     */   public static Traverser<File> fileTraverser()
/*     */   {
/* 835 */     return Traverser.forTree(FILE_TREE);
/*     */   }
/*     */   
/* 838 */   private static final SuccessorsFunction<File> FILE_TREE = new SuccessorsFunction()
/*     */   {
/*     */     public Iterable<File> successors(File file)
/*     */     {
/* 842 */       return Files.fileTreeChildren(file);
/*     */     }
/*     */   };
/*     */   
/*     */   private static Iterable<File> fileTreeChildren(File file)
/*     */   {
/* 848 */     if (file.isDirectory()) {
/* 849 */       File[] files = file.listFiles();
/* 850 */       if (files != null) {
/* 851 */         return Collections.unmodifiableList(Arrays.asList(files));
/*     */       }
/*     */     }
/*     */     
/* 855 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Predicate<File> isDirectory()
/*     */   {
/* 864 */     return FilePredicate.IS_DIRECTORY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Predicate<File> isFile()
/*     */   {
/* 873 */     return FilePredicate.IS_FILE;
/*     */   }
/*     */   
/*     */   private static abstract enum FilePredicate implements Predicate<File> {
/* 877 */     IS_DIRECTORY, 
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
/* 889 */     IS_FILE;
/*     */     
/*     */     private FilePredicate() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\io\Files.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */