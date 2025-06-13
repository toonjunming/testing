package I丨L;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

public final class IL丨丨l extends AbstractList<iI丨LLL1> implements RandomAccess {
  public final iI丨LLL1[] IL1Iii;
  
  public final int[] ILil;
  
  public IL丨丨l(iI丨LLL1[] paramArrayOfiI丨LLL1, int[] paramArrayOfint) {
    this.IL1Iii = paramArrayOfiI丨LLL1;
    this.ILil = paramArrayOfint;
  }
  
  public static int I1I(I1I paramI1I) {
    return (int)(paramI1I.iI1i丨I() / 4L);
  }
  
  public static void IL1Iii(long paramLong, I1I paramI1I, int paramInt1, List<iI丨LLL1> paramList, int paramInt2, int paramInt3, List<Integer> paramList1) {
    // Byte code:
    //   0: iload #5
    //   2: istore #8
    //   4: iload #8
    //   6: iload #6
    //   8: if_icmpge -> 774
    //   11: iload #8
    //   13: istore #9
    //   15: iload #9
    //   17: iload #6
    //   19: if_icmpge -> 55
    //   22: aload #4
    //   24: iload #9
    //   26: invokeinterface get : (I)Ljava/lang/Object;
    //   31: checkcast I丨L/iI丨LLL1
    //   34: invokevirtual size : ()I
    //   37: iload_3
    //   38: if_icmplt -> 47
    //   41: iinc #9, 1
    //   44: goto -> 15
    //   47: new java/lang/AssertionError
    //   50: dup
    //   51: invokespecial <init> : ()V
    //   54: athrow
    //   55: aload #4
    //   57: iload #5
    //   59: invokeinterface get : (I)Ljava/lang/Object;
    //   64: checkcast I丨L/iI丨LLL1
    //   67: astore #13
    //   69: aload #4
    //   71: iload #6
    //   73: iconst_1
    //   74: isub
    //   75: invokeinterface get : (I)Ljava/lang/Object;
    //   80: checkcast I丨L/iI丨LLL1
    //   83: astore #14
    //   85: iconst_m1
    //   86: istore #9
    //   88: iload #8
    //   90: istore #5
    //   92: aload #13
    //   94: astore #12
    //   96: iload_3
    //   97: aload #13
    //   99: invokevirtual size : ()I
    //   102: if_icmpne -> 142
    //   105: aload #7
    //   107: iload #8
    //   109: invokeinterface get : (I)Ljava/lang/Object;
    //   114: checkcast java/lang/Integer
    //   117: invokevirtual intValue : ()I
    //   120: istore #9
    //   122: iload #8
    //   124: iconst_1
    //   125: iadd
    //   126: istore #5
    //   128: aload #4
    //   130: iload #5
    //   132: invokeinterface get : (I)Ljava/lang/Object;
    //   137: checkcast I丨L/iI丨LLL1
    //   140: astore #12
    //   142: aload #12
    //   144: iload_3
    //   145: invokevirtual getByte : (I)B
    //   148: aload #14
    //   150: iload_3
    //   151: invokevirtual getByte : (I)B
    //   154: if_icmpeq -> 534
    //   157: iload #5
    //   159: iconst_1
    //   160: iadd
    //   161: istore #8
    //   163: iconst_1
    //   164: istore #11
    //   166: iload #8
    //   168: iload #6
    //   170: if_icmpge -> 230
    //   173: iload #11
    //   175: istore #10
    //   177: aload #4
    //   179: iload #8
    //   181: iconst_1
    //   182: isub
    //   183: invokeinterface get : (I)Ljava/lang/Object;
    //   188: checkcast I丨L/iI丨LLL1
    //   191: iload_3
    //   192: invokevirtual getByte : (I)B
    //   195: aload #4
    //   197: iload #8
    //   199: invokeinterface get : (I)Ljava/lang/Object;
    //   204: checkcast I丨L/iI丨LLL1
    //   207: iload_3
    //   208: invokevirtual getByte : (I)B
    //   211: if_icmpeq -> 220
    //   214: iload #11
    //   216: iconst_1
    //   217: iadd
    //   218: istore #10
    //   220: iinc #8, 1
    //   223: iload #10
    //   225: istore #11
    //   227: goto -> 166
    //   230: lload_0
    //   231: aload_2
    //   232: invokestatic I1I : (LI丨L/I1I;)I
    //   235: i2l
    //   236: ladd
    //   237: ldc2_w 2
    //   240: ladd
    //   241: iload #11
    //   243: iconst_2
    //   244: imul
    //   245: i2l
    //   246: ladd
    //   247: lstore_0
    //   248: aload_2
    //   249: iload #11
    //   251: invokevirtual iiIIi丨11 : (I)LI丨L/I1I;
    //   254: pop
    //   255: aload_2
    //   256: iload #9
    //   258: invokevirtual iiIIi丨11 : (I)LI丨L/I1I;
    //   261: pop
    //   262: iload #5
    //   264: istore #8
    //   266: iload #8
    //   268: iload #6
    //   270: if_icmpge -> 338
    //   273: aload #4
    //   275: iload #8
    //   277: invokeinterface get : (I)Ljava/lang/Object;
    //   282: checkcast I丨L/iI丨LLL1
    //   285: iload_3
    //   286: invokevirtual getByte : (I)B
    //   289: istore #9
    //   291: iload #8
    //   293: iload #5
    //   295: if_icmpeq -> 321
    //   298: iload #9
    //   300: aload #4
    //   302: iload #8
    //   304: iconst_1
    //   305: isub
    //   306: invokeinterface get : (I)Ljava/lang/Object;
    //   311: checkcast I丨L/iI丨LLL1
    //   314: iload_3
    //   315: invokevirtual getByte : (I)B
    //   318: if_icmpeq -> 332
    //   321: aload_2
    //   322: iload #9
    //   324: sipush #255
    //   327: iand
    //   328: invokevirtual iiIIi丨11 : (I)LI丨L/I1I;
    //   331: pop
    //   332: iinc #8, 1
    //   335: goto -> 266
    //   338: new I丨L/I1I
    //   341: dup
    //   342: invokespecial <init> : ()V
    //   345: astore #12
    //   347: iload #5
    //   349: istore #8
    //   351: iload #8
    //   353: iload #6
    //   355: if_icmpge -> 520
    //   358: aload #4
    //   360: iload #8
    //   362: invokeinterface get : (I)Ljava/lang/Object;
    //   367: checkcast I丨L/iI丨LLL1
    //   370: iload_3
    //   371: invokevirtual getByte : (I)B
    //   374: istore #10
    //   376: iload #8
    //   378: iconst_1
    //   379: iadd
    //   380: istore #9
    //   382: iload #9
    //   384: istore #5
    //   386: iload #5
    //   388: iload #6
    //   390: if_icmpge -> 423
    //   393: iload #10
    //   395: aload #4
    //   397: iload #5
    //   399: invokeinterface get : (I)Ljava/lang/Object;
    //   404: checkcast I丨L/iI丨LLL1
    //   407: iload_3
    //   408: invokevirtual getByte : (I)B
    //   411: if_icmpeq -> 417
    //   414: goto -> 427
    //   417: iinc #5, 1
    //   420: goto -> 386
    //   423: iload #6
    //   425: istore #5
    //   427: iload #9
    //   429: iload #5
    //   431: if_icmpne -> 478
    //   434: iload_3
    //   435: iconst_1
    //   436: iadd
    //   437: aload #4
    //   439: iload #8
    //   441: invokeinterface get : (I)Ljava/lang/Object;
    //   446: checkcast I丨L/iI丨LLL1
    //   449: invokevirtual size : ()I
    //   452: if_icmpne -> 478
    //   455: aload_2
    //   456: aload #7
    //   458: iload #8
    //   460: invokeinterface get : (I)Ljava/lang/Object;
    //   465: checkcast java/lang/Integer
    //   468: invokevirtual intValue : ()I
    //   471: invokevirtual iiIIi丨11 : (I)LI丨L/I1I;
    //   474: pop
    //   475: goto -> 513
    //   478: aload_2
    //   479: aload #12
    //   481: invokestatic I1I : (LI丨L/I1I;)I
    //   484: i2l
    //   485: lload_0
    //   486: ladd
    //   487: ldc2_w -1
    //   490: lmul
    //   491: l2i
    //   492: invokevirtual iiIIi丨11 : (I)LI丨L/I1I;
    //   495: pop
    //   496: lload_0
    //   497: aload #12
    //   499: iload_3
    //   500: iconst_1
    //   501: iadd
    //   502: aload #4
    //   504: iload #8
    //   506: iload #5
    //   508: aload #7
    //   510: invokestatic IL1Iii : (JLI丨L/I1I;ILjava/util/List;IILjava/util/List;)V
    //   513: iload #5
    //   515: istore #8
    //   517: goto -> 351
    //   520: aload_2
    //   521: aload #12
    //   523: aload #12
    //   525: invokevirtual iI1i丨I : ()J
    //   528: invokevirtual write : (LI丨L/I1I;J)V
    //   531: goto -> 773
    //   534: iconst_0
    //   535: istore #8
    //   537: aload #12
    //   539: invokevirtual size : ()I
    //   542: aload #14
    //   544: invokevirtual size : ()I
    //   547: invokestatic min : (II)I
    //   550: istore #11
    //   552: iload_3
    //   553: istore #10
    //   555: iload #10
    //   557: iload #11
    //   559: if_icmpge -> 588
    //   562: aload #12
    //   564: iload #10
    //   566: invokevirtual getByte : (I)B
    //   569: aload #14
    //   571: iload #10
    //   573: invokevirtual getByte : (I)B
    //   576: if_icmpne -> 588
    //   579: iinc #8, 1
    //   582: iinc #10, 1
    //   585: goto -> 555
    //   588: lconst_1
    //   589: lload_0
    //   590: aload_2
    //   591: invokestatic I1I : (LI丨L/I1I;)I
    //   594: i2l
    //   595: ladd
    //   596: ldc2_w 2
    //   599: ladd
    //   600: iload #8
    //   602: i2l
    //   603: ladd
    //   604: ladd
    //   605: lstore_0
    //   606: aload_2
    //   607: iload #8
    //   609: ineg
    //   610: invokevirtual iiIIi丨11 : (I)LI丨L/I1I;
    //   613: pop
    //   614: aload_2
    //   615: iload #9
    //   617: invokevirtual iiIIi丨11 : (I)LI丨L/I1I;
    //   620: pop
    //   621: iload_3
    //   622: istore #9
    //   624: iload_3
    //   625: iload #8
    //   627: iadd
    //   628: istore #10
    //   630: iload #9
    //   632: iload #10
    //   634: if_icmpge -> 659
    //   637: aload_2
    //   638: aload #12
    //   640: iload #9
    //   642: invokevirtual getByte : (I)B
    //   645: sipush #255
    //   648: iand
    //   649: invokevirtual iiIIi丨11 : (I)LI丨L/I1I;
    //   652: pop
    //   653: iinc #9, 1
    //   656: goto -> 624
    //   659: iload #5
    //   661: iconst_1
    //   662: iadd
    //   663: iload #6
    //   665: if_icmpne -> 719
    //   668: iload #10
    //   670: aload #4
    //   672: iload #5
    //   674: invokeinterface get : (I)Ljava/lang/Object;
    //   679: checkcast I丨L/iI丨LLL1
    //   682: invokevirtual size : ()I
    //   685: if_icmpne -> 711
    //   688: aload_2
    //   689: aload #7
    //   691: iload #5
    //   693: invokeinterface get : (I)Ljava/lang/Object;
    //   698: checkcast java/lang/Integer
    //   701: invokevirtual intValue : ()I
    //   704: invokevirtual iiIIi丨11 : (I)LI丨L/I1I;
    //   707: pop
    //   708: goto -> 773
    //   711: new java/lang/AssertionError
    //   714: dup
    //   715: invokespecial <init> : ()V
    //   718: athrow
    //   719: new I丨L/I1I
    //   722: dup
    //   723: invokespecial <init> : ()V
    //   726: astore #12
    //   728: aload_2
    //   729: aload #12
    //   731: invokestatic I1I : (LI丨L/I1I;)I
    //   734: i2l
    //   735: lload_0
    //   736: ladd
    //   737: ldc2_w -1
    //   740: lmul
    //   741: l2i
    //   742: invokevirtual iiIIi丨11 : (I)LI丨L/I1I;
    //   745: pop
    //   746: lload_0
    //   747: aload #12
    //   749: iload #10
    //   751: aload #4
    //   753: iload #5
    //   755: iload #6
    //   757: aload #7
    //   759: invokestatic IL1Iii : (JLI丨L/I1I;ILjava/util/List;IILjava/util/List;)V
    //   762: aload_2
    //   763: aload #12
    //   765: aload #12
    //   767: invokevirtual iI1i丨I : ()J
    //   770: invokevirtual write : (LI丨L/I1I;J)V
    //   773: return
    //   774: new java/lang/AssertionError
    //   777: dup
    //   778: invokespecial <init> : ()V
    //   781: athrow
  }
  
  public static IL丨丨l I丨L(iI丨LLL1... paramVarArgs) {
    int i = paramVarArgs.length;
    boolean bool = false;
    if (i == 0)
      return new IL丨丨l(new iI丨LLL1[0], new int[] { 0, -1 }); 
    ArrayList<Comparable> arrayList = new ArrayList(Arrays.asList((Object[])paramVarArgs));
    Collections.sort(arrayList);
    ArrayList<Integer> arrayList1 = new ArrayList();
    for (i = 0; i < arrayList.size(); i++)
      arrayList1.add(Integer.valueOf(-1)); 
    for (i = 0; i < arrayList.size(); i++)
      arrayList1.set(Collections.binarySearch((List)arrayList, paramVarArgs[i]), Integer.valueOf(i)); 
    if (((iI丨LLL1)arrayList.get(0)).size() != 0) {
      StringBuilder stringBuilder;
      for (i = 0; i < arrayList.size(); i = k) {
        iI丨LLL1 iI丨LLL11 = (iI丨LLL1)arrayList.get(i);
        int k = i + 1;
        int m = k;
        while (m < arrayList.size()) {
          iI丨LLL1 iI丨LLL12 = (iI丨LLL1)arrayList.get(m);
          if (!iI丨LLL12.startsWith(iI丨LLL11))
            break; 
          if (iI丨LLL12.size() != iI丨LLL11.size()) {
            if (((Integer)arrayList1.get(m)).intValue() > ((Integer)arrayList1.get(i)).intValue()) {
              arrayList.remove(m);
              arrayList1.remove(m);
              continue;
            } 
            m++;
            continue;
          } 
          stringBuilder = new StringBuilder();
          stringBuilder.append("duplicate option: ");
          stringBuilder.append(iI丨LLL12);
          throw new IllegalArgumentException(stringBuilder.toString());
        } 
      } 
      I1I i1I = new I1I();
      IL1Iii(0L, i1I, 0, (List)arrayList, 0, arrayList.size(), arrayList1);
      int j = I1I(i1I);
      int[] arrayOfInt = new int[j];
      for (i = bool; i < j; i++)
        arrayOfInt[i] = i1I.readInt(); 
      if (i1I.l丨Li1LL())
        return new IL丨丨l((iI丨LLL1[])stringBuilder.clone(), arrayOfInt); 
      throw new AssertionError();
    } 
    throw new IllegalArgumentException("the empty byte string is not a supported option");
  }
  
  public iI丨LLL1 ILil(int paramInt) {
    return this.IL1Iii[paramInt];
  }
  
  public final int size() {
    return this.IL1Iii.length;
  }
}
