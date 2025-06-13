package com.mysql.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CharsetMapping {
  public static final Map<String, MysqlCharset> CHARSET_NAME_TO_CHARSET;
  
  public static final Map<String, Integer> CHARSET_NAME_TO_COLLATION_INDEX;
  
  public static final MysqlCharset[] COLLATION_INDEX_TO_CHARSET;
  
  public static final String[] COLLATION_INDEX_TO_COLLATION_NAME;
  
  public static final String COLLATION_NOT_DEFINED = "none";
  
  private static final Map<String, String> ERROR_MESSAGE_FILE_TO_MYSQL_CHARSET;
  
  private static final Set<String> ESCAPE_ENCODINGS;
  
  private static final Map<String, List<MysqlCharset>> JAVA_ENCODING_UC_TO_MYSQL_CHARSET;
  
  public static final int MAP_SIZE = 2048;
  
  private static final Set<String> MULTIBYTE_ENCODINGS;
  
  private static final String MYSQL_4_0_CHARSET_NAME_cp1251cias = "cp1251cias";
  
  private static final String MYSQL_4_0_CHARSET_NAME_cp1251csas = "cp1251csas";
  
  private static final String MYSQL_4_0_CHARSET_NAME_croat = "croat";
  
  private static final String MYSQL_4_0_CHARSET_NAME_czech = "czech";
  
  private static final String MYSQL_4_0_CHARSET_NAME_danish = "danish";
  
  private static final String MYSQL_4_0_CHARSET_NAME_dos = "dos";
  
  private static final String MYSQL_4_0_CHARSET_NAME_estonia = "estonia";
  
  private static final String MYSQL_4_0_CHARSET_NAME_euc_kr = "euc_kr";
  
  private static final String MYSQL_4_0_CHARSET_NAME_german1 = "german1";
  
  private static final String MYSQL_4_0_CHARSET_NAME_hungarian = "hungarian";
  
  private static final String MYSQL_4_0_CHARSET_NAME_koi8_ru = "koi8_ru";
  
  private static final String MYSQL_4_0_CHARSET_NAME_koi8_ukr = "koi8_ukr";
  
  private static final String MYSQL_4_0_CHARSET_NAME_latin1_de = "latin1_de";
  
  private static final String MYSQL_4_0_CHARSET_NAME_latvian = "latvian";
  
  private static final String MYSQL_4_0_CHARSET_NAME_latvian1 = "latvian1";
  
  private static final String MYSQL_4_0_CHARSET_NAME_usa7 = "usa7";
  
  private static final String MYSQL_4_0_CHARSET_NAME_win1250 = "win1250";
  
  private static final String MYSQL_4_0_CHARSET_NAME_win1251 = "win1251";
  
  private static final String MYSQL_4_0_CHARSET_NAME_win1251ukr = "win1251ukr";
  
  private static final String MYSQL_CHARSET_NAME_armscii8 = "armscii8";
  
  private static final String MYSQL_CHARSET_NAME_ascii = "ascii";
  
  private static final String MYSQL_CHARSET_NAME_big5 = "big5";
  
  private static final String MYSQL_CHARSET_NAME_binary = "binary";
  
  private static final String MYSQL_CHARSET_NAME_cp1250 = "cp1250";
  
  private static final String MYSQL_CHARSET_NAME_cp1251 = "cp1251";
  
  private static final String MYSQL_CHARSET_NAME_cp1256 = "cp1256";
  
  private static final String MYSQL_CHARSET_NAME_cp1257 = "cp1257";
  
  private static final String MYSQL_CHARSET_NAME_cp850 = "cp850";
  
  private static final String MYSQL_CHARSET_NAME_cp852 = "cp852";
  
  private static final String MYSQL_CHARSET_NAME_cp866 = "cp866";
  
  private static final String MYSQL_CHARSET_NAME_cp932 = "cp932";
  
  private static final String MYSQL_CHARSET_NAME_dec8 = "dec8";
  
  private static final String MYSQL_CHARSET_NAME_eucjpms = "eucjpms";
  
  private static final String MYSQL_CHARSET_NAME_euckr = "euckr";
  
  private static final String MYSQL_CHARSET_NAME_gb18030 = "gb18030";
  
  private static final String MYSQL_CHARSET_NAME_gb2312 = "gb2312";
  
  private static final String MYSQL_CHARSET_NAME_gbk = "gbk";
  
  private static final String MYSQL_CHARSET_NAME_geostd8 = "geostd8";
  
  private static final String MYSQL_CHARSET_NAME_greek = "greek";
  
  private static final String MYSQL_CHARSET_NAME_hebrew = "hebrew";
  
  private static final String MYSQL_CHARSET_NAME_hp8 = "hp8";
  
  private static final String MYSQL_CHARSET_NAME_keybcs2 = "keybcs2";
  
  private static final String MYSQL_CHARSET_NAME_koi8r = "koi8r";
  
  private static final String MYSQL_CHARSET_NAME_koi8u = "koi8u";
  
  private static final String MYSQL_CHARSET_NAME_latin1 = "latin1";
  
  private static final String MYSQL_CHARSET_NAME_latin2 = "latin2";
  
  private static final String MYSQL_CHARSET_NAME_latin5 = "latin5";
  
  private static final String MYSQL_CHARSET_NAME_latin7 = "latin7";
  
  private static final String MYSQL_CHARSET_NAME_macce = "macce";
  
  private static final String MYSQL_CHARSET_NAME_macroman = "macroman";
  
  private static final String MYSQL_CHARSET_NAME_sjis = "sjis";
  
  private static final String MYSQL_CHARSET_NAME_swe7 = "swe7";
  
  private static final String MYSQL_CHARSET_NAME_tis620 = "tis620";
  
  private static final String MYSQL_CHARSET_NAME_ucs2 = "ucs2";
  
  private static final String MYSQL_CHARSET_NAME_ujis = "ujis";
  
  private static final String MYSQL_CHARSET_NAME_utf16 = "utf16";
  
  private static final String MYSQL_CHARSET_NAME_utf16le = "utf16le";
  
  private static final String MYSQL_CHARSET_NAME_utf32 = "utf32";
  
  private static final String MYSQL_CHARSET_NAME_utf8 = "utf8";
  
  private static final String MYSQL_CHARSET_NAME_utf8mb4 = "utf8mb4";
  
  public static final int MYSQL_COLLATION_INDEX_binary = 63;
  
  public static final int MYSQL_COLLATION_INDEX_utf8 = 33;
  
  public static final String NOT_USED = "latin1";
  
  public static final Set<Integer> UTF8MB4_INDEXES;
  
  private static int numberOfEncodingsConfigured;
  
  static {
    MysqlCharset[] arrayOfMysqlCharset = new MysqlCharset[60];
    arrayOfMysqlCharset[0] = new MysqlCharset("usa7", 1, 0, new String[] { "US-ASCII" }, 4, 0);
    arrayOfMysqlCharset[1] = new MysqlCharset("ascii", 1, 0, new String[] { "US-ASCII", "ASCII" });
    arrayOfMysqlCharset[2] = new MysqlCharset("big5", 2, 0, new String[] { "Big5" });
    arrayOfMysqlCharset[3] = new MysqlCharset("gbk", 2, 0, new String[] { "GBK" });
    arrayOfMysqlCharset[4] = new MysqlCharset("sjis", 2, 0, new String[] { "SHIFT_JIS", "Cp943", "WINDOWS-31J" });
    arrayOfMysqlCharset[5] = new MysqlCharset("cp932", 2, 1, new String[] { "WINDOWS-31J" });
    arrayOfMysqlCharset[6] = new MysqlCharset("gb2312", 2, 0, new String[] { "GB2312" });
    arrayOfMysqlCharset[7] = new MysqlCharset("ujis", 3, 0, new String[] { "EUC_JP" });
    arrayOfMysqlCharset[8] = new MysqlCharset("eucjpms", 3, 0, new String[] { "EUC_JP_Solaris" }, 5, 0, 3);
    arrayOfMysqlCharset[9] = new MysqlCharset("gb18030", 4, 0, new String[] { "GB18030" }, 5, 7, 4);
    arrayOfMysqlCharset[10] = new MysqlCharset("euc_kr", 2, 0, new String[] { "EUC_KR" }, 4, 0);
    arrayOfMysqlCharset[11] = new MysqlCharset("euckr", 2, 0, new String[] { "EUC-KR" });
    arrayOfMysqlCharset[12] = new MysqlCharset("latin1", 1, 1, new String[] { "Cp1252", "ISO8859_1" });
    arrayOfMysqlCharset[13] = new MysqlCharset("swe7", 1, 0, new String[] { "Cp1252" });
    arrayOfMysqlCharset[14] = new MysqlCharset("hp8", 1, 0, new String[] { "Cp1252" });
    arrayOfMysqlCharset[15] = new MysqlCharset("dec8", 1, 0, new String[] { "Cp1252" });
    arrayOfMysqlCharset[16] = new MysqlCharset("armscii8", 1, 0, new String[] { "Cp1252" });
    arrayOfMysqlCharset[17] = new MysqlCharset("geostd8", 1, 0, new String[] { "Cp1252" });
    arrayOfMysqlCharset[18] = new MysqlCharset("latin2", 1, 0, new String[] { "ISO8859_2" });
    arrayOfMysqlCharset[19] = new MysqlCharset("czech", 1, 0, new String[] { "ISO8859_2" }, 4, 0);
    arrayOfMysqlCharset[20] = new MysqlCharset("hungarian", 1, 0, new String[] { "ISO8859_2" }, 4, 0);
    arrayOfMysqlCharset[21] = new MysqlCharset("croat", 1, 0, new String[] { "ISO8859_2" }, 4, 0);
    String str2 = "greek";
    arrayOfMysqlCharset[22] = new MysqlCharset("greek", 1, 0, new String[] { "ISO8859_7", "greek" });
    String str1 = "latin7";
    arrayOfMysqlCharset[23] = new MysqlCharset("latin7", 1, 0, new String[] { "ISO-8859-13" });
    arrayOfMysqlCharset[24] = new MysqlCharset("hebrew", 1, 0, new String[] { "ISO8859_8" });
    arrayOfMysqlCharset[25] = new MysqlCharset("latin5", 1, 0, new String[] { "ISO8859_9" });
    arrayOfMysqlCharset[26] = new MysqlCharset("latvian", 1, 0, new String[] { "ISO8859_13" }, 4, 0);
    arrayOfMysqlCharset[27] = new MysqlCharset("latvian1", 1, 0, new String[] { "ISO8859_13" }, 4, 0);
    arrayOfMysqlCharset[28] = new MysqlCharset("estonia", 1, 1, new String[] { "ISO8859_13" }, 4, 0);
    arrayOfMysqlCharset[29] = new MysqlCharset("cp850", 1, 0, new String[] { "Cp850", "Cp437" });
    arrayOfMysqlCharset[30] = new MysqlCharset("dos", 1, 0, new String[] { "Cp850", "Cp437" }, 4, 0);
    arrayOfMysqlCharset[31] = new MysqlCharset("cp852", 1, 0, new String[] { "Cp852" });
    arrayOfMysqlCharset[32] = new MysqlCharset("keybcs2", 1, 0, new String[] { "Cp852" });
    arrayOfMysqlCharset[33] = new MysqlCharset("cp866", 1, 0, new String[] { "Cp866" });
    arrayOfMysqlCharset[34] = new MysqlCharset("koi8_ru", 1, 0, new String[] { "KOI8_R" }, 4, 0);
    arrayOfMysqlCharset[35] = new MysqlCharset("koi8r", 1, 1, new String[] { "KOI8_R" });
    arrayOfMysqlCharset[36] = new MysqlCharset("koi8u", 1, 0, new String[] { "KOI8_R" });
    arrayOfMysqlCharset[37] = new MysqlCharset("koi8_ukr", 1, 0, new String[] { "KOI8_R" }, 4, 0);
    arrayOfMysqlCharset[38] = new MysqlCharset("tis620", 1, 0, new String[] { "TIS620" });
    String str3 = "cp1250";
    arrayOfMysqlCharset[39] = new MysqlCharset("cp1250", 1, 0, new String[] { "Cp1250" });
    arrayOfMysqlCharset[40] = new MysqlCharset("win1250", 1, 0, new String[] { "Cp1250" }, 4, 0);
    arrayOfMysqlCharset[41] = new MysqlCharset("cp1251", 1, 1, new String[] { "Cp1251" });
    arrayOfMysqlCharset[42] = new MysqlCharset("win1251", 1, 0, new String[] { "Cp1251" }, 4, 0);
    arrayOfMysqlCharset[43] = new MysqlCharset("cp1251cias", 1, 0, new String[] { "Cp1251" }, 4, 0);
    arrayOfMysqlCharset[44] = new MysqlCharset("cp1251csas", 1, 0, new String[] { "Cp1251" }, 4, 0);
    arrayOfMysqlCharset[45] = new MysqlCharset("win1251ukr", 1, 0, new String[] { "Cp1251" }, 4, 0);
    arrayOfMysqlCharset[46] = new MysqlCharset("cp1256", 1, 0, new String[] { "Cp1256" });
    arrayOfMysqlCharset[47] = new MysqlCharset("cp1257", 1, 0, new String[] { "Cp1257" });
    arrayOfMysqlCharset[48] = new MysqlCharset("macroman", 1, 0, new String[] { "MacRoman" });
    arrayOfMysqlCharset[49] = new MysqlCharset("macce", 1, 0, new String[] { "MacCentralEurope" });
    arrayOfMysqlCharset[50] = new MysqlCharset("utf8", 3, 1, new String[] { "UTF-8" });
    arrayOfMysqlCharset[51] = new MysqlCharset("utf8mb4", 4, 0, new String[] { "UTF-8" });
    arrayOfMysqlCharset[52] = new MysqlCharset("ucs2", 2, 0, new String[] { "UnicodeBig" });
    arrayOfMysqlCharset[53] = new MysqlCharset("binary", 1, 1, new String[] { "ISO8859_1" });
    arrayOfMysqlCharset[54] = new MysqlCharset("latin1_de", 1, 0, new String[] { "ISO8859_1" }, 4, 0);
    arrayOfMysqlCharset[55] = new MysqlCharset("german1", 1, 0, new String[] { "ISO8859_1" }, 4, 0);
    arrayOfMysqlCharset[56] = new MysqlCharset("danish", 1, 0, new String[] { "ISO8859_1" }, 4, 0);
    arrayOfMysqlCharset[57] = new MysqlCharset("utf16", 4, 0, new String[] { "UTF-16" });
    arrayOfMysqlCharset[58] = new MysqlCharset("utf16le", 4, 0, new String[] { "UTF-16LE" });
    arrayOfMysqlCharset[59] = new MysqlCharset("utf32", 4, 0, new String[] { "UTF-32" });
    HashMap<Object, Object> hashMap3 = new HashMap<Object, Object>();
    HashMap<Object, Object> hashMap2 = new HashMap<Object, Object>();
    HashSet<String> hashSet1 = new HashSet();
    HashSet<String> hashSet2 = new HashSet();
    byte b;
    for (b = 0; b < 60; b++) {
      String str = (arrayOfMysqlCharset[b]).charsetName;
      hashMap3.put(str, arrayOfMysqlCharset[b]);
      numberOfEncodingsConfigured += (arrayOfMysqlCharset[b]).javaEncodingsUc.size();
      for (String str4 : (arrayOfMysqlCharset[b]).javaEncodingsUc) {
        List<MysqlCharset> list = (List)hashMap2.get(str4);
        if (list == null) {
          list = new ArrayList();
          hashMap2.put(str4, list);
        } 
        list.add(arrayOfMysqlCharset[b]);
        if ((arrayOfMysqlCharset[b]).mblen > 1)
          hashSet1.add(str4); 
      } 
      if (str.equals("big5") || str.equals("gbk") || str.equals("sjis"))
        hashSet2.addAll((arrayOfMysqlCharset[b]).javaEncodingsUc); 
    } 
    CHARSET_NAME_TO_CHARSET = Collections.unmodifiableMap(hashMap3);
    JAVA_ENCODING_UC_TO_MYSQL_CHARSET = Collections.unmodifiableMap(hashMap2);
    MULTIBYTE_ENCODINGS = Collections.unmodifiableSet(hashSet1);
    ESCAPE_ENCODINGS = Collections.unmodifiableSet(hashSet2);
    Collation[] arrayOfCollation = new Collation[2048];
    arrayOfCollation[1] = new Collation(1, "big5_chinese_ci", 1, "big5");
    arrayOfCollation[2] = new Collation(2, "latin2_czech_cs", 0, "latin2");
    arrayOfCollation[3] = new Collation(3, "dec8_swedish_ci", 0, "dec8");
    arrayOfCollation[4] = new Collation(4, "cp850_general_ci", 1, "cp850");
    arrayOfCollation[5] = new Collation(5, "latin1_german1_ci", 1, "latin1");
    arrayOfCollation[6] = new Collation(6, "hp8_english_ci", 0, "hp8");
    arrayOfCollation[7] = new Collation(7, "koi8r_general_ci", 0, "koi8r");
    arrayOfCollation[8] = new Collation(8, "latin1_swedish_ci", 0, "latin1");
    arrayOfCollation[9] = new Collation(9, "latin2_general_ci", 1, "latin2");
    arrayOfCollation[10] = new Collation(10, "swe7_swedish_ci", 0, "swe7");
    arrayOfCollation[11] = new Collation(11, "ascii_general_ci", 0, "ascii");
    arrayOfCollation[12] = new Collation(12, "ujis_japanese_ci", 0, "ujis");
    arrayOfCollation[13] = new Collation(13, "sjis_japanese_ci", 0, "sjis");
    arrayOfCollation[14] = new Collation(14, "cp1251_bulgarian_ci", 0, "cp1251");
    arrayOfCollation[15] = new Collation(15, "latin1_danish_ci", 0, "latin1");
    arrayOfCollation[16] = new Collation(16, "hebrew_general_ci", 0, "hebrew");
    arrayOfCollation[17] = new Collation(17, "latin1_german1_ci", 0, "win1251");
    arrayOfCollation[18] = new Collation(18, "tis620_thai_ci", 0, "tis620");
    arrayOfCollation[19] = new Collation(19, "euckr_korean_ci", 0, "euckr");
    arrayOfCollation[20] = new Collation(20, "latin7_estonian_cs", 0, str1);
    arrayOfCollation[21] = new Collation(21, "latin2_hungarian_ci", 0, "latin2");
    arrayOfCollation[22] = new Collation(22, "koi8u_general_ci", 0, "koi8u");
    arrayOfCollation[23] = new Collation(23, "cp1251_ukrainian_ci", 0, "cp1251");
    arrayOfCollation[24] = new Collation(24, "gb2312_chinese_ci", 0, "gb2312");
    arrayOfCollation[25] = new Collation(25, "greek_general_ci", 0, str2);
    arrayOfCollation[26] = new Collation(26, "cp1250_general_ci", 1, str3);
    arrayOfCollation[27] = new Collation(27, "latin2_croatian_ci", 0, "latin2");
    arrayOfCollation[28] = new Collation(28, "gbk_chinese_ci", 1, "gbk");
    arrayOfCollation[29] = new Collation(29, "cp1257_lithuanian_ci", 0, "cp1257");
    arrayOfCollation[30] = new Collation(30, "latin5_turkish_ci", 1, "latin5");
    arrayOfCollation[31] = new Collation(31, "latin1_german2_ci", 0, "latin1");
    arrayOfCollation[32] = new Collation(32, "armscii8_general_ci", 0, "armscii8");
    arrayOfCollation[33] = new Collation(33, "utf8_general_ci", 1, "utf8");
    arrayOfCollation[34] = new Collation(34, "cp1250_czech_cs", 0, str3);
    arrayOfCollation[35] = new Collation(35, "ucs2_general_ci", 1, "ucs2");
    arrayOfCollation[36] = new Collation(36, "cp866_general_ci", 1, "cp866");
    arrayOfCollation[37] = new Collation(37, "keybcs2_general_ci", 1, "keybcs2");
    arrayOfCollation[38] = new Collation(38, "macce_general_ci", 1, "macce");
    arrayOfCollation[39] = new Collation(39, "macroman_general_ci", 1, "macroman");
    arrayOfCollation[40] = new Collation(40, "cp852_general_ci", 1, "cp852");
    arrayOfCollation[41] = new Collation(41, "latin7_general_ci", 1, str1);
    arrayOfCollation[42] = new Collation(42, "latin7_general_cs", 0, str1);
    arrayOfCollation[43] = new Collation(43, "macce_bin", 0, "macce");
    arrayOfCollation[44] = new Collation(44, "cp1250_croatian_ci", 0, str3);
    arrayOfCollation[45] = new Collation(45, "utf8mb4_general_ci", 1, "utf8mb4");
    arrayOfCollation[46] = new Collation(46, "utf8mb4_bin", 0, "utf8mb4");
    arrayOfCollation[47] = new Collation(47, "latin1_bin", 0, "latin1");
    arrayOfCollation[48] = new Collation(48, "latin1_general_ci", 0, "latin1");
    arrayOfCollation[49] = new Collation(49, "latin1_general_cs", 0, "latin1");
    arrayOfCollation[50] = new Collation(50, "cp1251_bin", 0, "cp1251");
    arrayOfCollation[51] = new Collation(51, "cp1251_general_ci", 1, "cp1251");
    arrayOfCollation[52] = new Collation(52, "cp1251_general_cs", 0, "cp1251");
    arrayOfCollation[53] = new Collation(53, "macroman_bin", 0, "macroman");
    arrayOfCollation[54] = new Collation(54, "utf16_general_ci", 1, "utf16");
    arrayOfCollation[55] = new Collation(55, "utf16_bin", 0, "utf16");
    arrayOfCollation[56] = new Collation(56, "utf16le_general_ci", 1, "utf16le");
    arrayOfCollation[57] = new Collation(57, "cp1256_general_ci", 1, "cp1256");
    arrayOfCollation[58] = new Collation(58, "cp1257_bin", 0, "cp1257");
    arrayOfCollation[59] = new Collation(59, "cp1257_general_ci", 1, "cp1257");
    arrayOfCollation[60] = new Collation(60, "utf32_general_ci", 1, "utf32");
    arrayOfCollation[61] = new Collation(61, "utf32_bin", 0, "utf32");
    arrayOfCollation[62] = new Collation(62, "utf16le_bin", 0, "utf16le");
    arrayOfCollation[63] = new Collation(63, "binary", 1, "binary");
    arrayOfCollation[64] = new Collation(64, "armscii8_bin", 0, "armscii8");
    arrayOfCollation[65] = new Collation(65, "ascii_bin", 0, "ascii");
    arrayOfCollation[66] = new Collation(66, "cp1250_bin", 0, str3);
    arrayOfCollation[67] = new Collation(67, "cp1256_bin", 0, "cp1256");
    arrayOfCollation[68] = new Collation(68, "cp866_bin", 0, "cp866");
    arrayOfCollation[69] = new Collation(69, "dec8_bin", 0, "dec8");
    arrayOfCollation[70] = new Collation(70, "greek_bin", 0, str2);
    arrayOfCollation[71] = new Collation(71, "hebrew_bin", 0, "hebrew");
    arrayOfCollation[72] = new Collation(72, "hp8_bin", 0, "hp8");
    arrayOfCollation[73] = new Collation(73, "keybcs2_bin", 0, "keybcs2");
    arrayOfCollation[74] = new Collation(74, "koi8r_bin", 0, "koi8r");
    arrayOfCollation[75] = new Collation(75, "koi8u_bin", 0, "koi8u");
    arrayOfCollation[76] = new Collation(76, "utf8_tolower_ci", 0, "utf8");
    arrayOfCollation[77] = new Collation(77, "latin2_bin", 0, "latin2");
    arrayOfCollation[78] = new Collation(78, "latin5_bin", 0, "latin5");
    arrayOfCollation[79] = new Collation(79, "latin7_bin", 0, str1);
    arrayOfCollation[80] = new Collation(80, "cp850_bin", 0, "cp850");
    arrayOfCollation[81] = new Collation(81, "cp852_bin", 0, "cp852");
    arrayOfCollation[82] = new Collation(82, "swe7_bin", 0, "swe7");
    arrayOfCollation[83] = new Collation(83, "utf8_bin", 0, "utf8");
    arrayOfCollation[84] = new Collation(84, "big5_bin", 0, "big5");
    arrayOfCollation[85] = new Collation(85, "euckr_bin", 0, "euckr");
    arrayOfCollation[86] = new Collation(86, "gb2312_bin", 0, "gb2312");
    arrayOfCollation[87] = new Collation(87, "gbk_bin", 0, "gbk");
    arrayOfCollation[88] = new Collation(88, "sjis_bin", 0, "sjis");
    arrayOfCollation[89] = new Collation(89, "tis620_bin", 0, "tis620");
    arrayOfCollation[90] = new Collation(90, "ucs2_bin", 0, "ucs2");
    arrayOfCollation[91] = new Collation(91, "ujis_bin", 0, "ujis");
    arrayOfCollation[92] = new Collation(92, "geostd8_general_ci", 0, "geostd8");
    arrayOfCollation[93] = new Collation(93, "geostd8_bin", 0, "geostd8");
    arrayOfCollation[94] = new Collation(94, "latin1_spanish_ci", 0, "latin1");
    arrayOfCollation[95] = new Collation(95, "cp932_japanese_ci", 1, "cp932");
    arrayOfCollation[96] = new Collation(96, "cp932_bin", 0, "cp932");
    arrayOfCollation[97] = new Collation(97, "eucjpms_japanese_ci", 1, "eucjpms");
    arrayOfCollation[98] = new Collation(98, "eucjpms_bin", 0, "eucjpms");
    arrayOfCollation[99] = new Collation(99, "cp1250_polish_ci", 0, str3);
    arrayOfCollation[101] = new Collation(101, "utf16_unicode_ci", 0, "utf16");
    arrayOfCollation[102] = new Collation(102, "utf16_icelandic_ci", 0, "utf16");
    arrayOfCollation[103] = new Collation(103, "utf16_latvian_ci", 0, "utf16");
    arrayOfCollation[104] = new Collation(104, "utf16_romanian_ci", 0, "utf16");
    arrayOfCollation[105] = new Collation(105, "utf16_slovenian_ci", 0, "utf16");
    arrayOfCollation[106] = new Collation(106, "utf16_polish_ci", 0, "utf16");
    arrayOfCollation[107] = new Collation(107, "utf16_estonian_ci", 0, "utf16");
    arrayOfCollation[108] = new Collation(108, "utf16_spanish_ci", 0, "utf16");
    arrayOfCollation[109] = new Collation(109, "utf16_swedish_ci", 0, "utf16");
    arrayOfCollation[110] = new Collation(110, "utf16_turkish_ci", 0, "utf16");
    arrayOfCollation[111] = new Collation(111, "utf16_czech_ci", 0, "utf16");
    arrayOfCollation[112] = new Collation(112, "utf16_danish_ci", 0, "utf16");
    arrayOfCollation[113] = new Collation(113, "utf16_lithuanian_ci", 0, "utf16");
    arrayOfCollation[114] = new Collation(114, "utf16_slovak_ci", 0, "utf16");
    arrayOfCollation[115] = new Collation(115, "utf16_spanish2_ci", 0, "utf16");
    arrayOfCollation[116] = new Collation(116, "utf16_roman_ci", 0, "utf16");
    arrayOfCollation[117] = new Collation(117, "utf16_persian_ci", 0, "utf16");
    arrayOfCollation[118] = new Collation(118, "utf16_esperanto_ci", 0, "utf16");
    arrayOfCollation[119] = new Collation(119, "utf16_hungarian_ci", 0, "utf16");
    arrayOfCollation[120] = new Collation(120, "utf16_sinhala_ci", 0, "utf16");
    arrayOfCollation[121] = new Collation(121, "utf16_german2_ci", 0, "utf16");
    arrayOfCollation[122] = new Collation(122, "utf16_croatian_ci", 0, "utf16");
    arrayOfCollation[123] = new Collation(123, "utf16_unicode_520_ci", 0, "utf16");
    arrayOfCollation[124] = new Collation(124, "utf16_vietnamese_ci", 0, "utf16");
    arrayOfCollation[128] = new Collation(128, "ucs2_unicode_ci", 0, "ucs2");
    arrayOfCollation[129] = new Collation(129, "ucs2_icelandic_ci", 0, "ucs2");
    arrayOfCollation[130] = new Collation(130, "ucs2_latvian_ci", 0, "ucs2");
    arrayOfCollation[131] = new Collation(131, "ucs2_romanian_ci", 0, "ucs2");
    arrayOfCollation[132] = new Collation(132, "ucs2_slovenian_ci", 0, "ucs2");
    arrayOfCollation[133] = new Collation(133, "ucs2_polish_ci", 0, "ucs2");
    arrayOfCollation[134] = new Collation(134, "ucs2_estonian_ci", 0, "ucs2");
    arrayOfCollation[135] = new Collation(135, "ucs2_spanish_ci", 0, "ucs2");
    arrayOfCollation[136] = new Collation(136, "ucs2_swedish_ci", 0, "ucs2");
    arrayOfCollation[137] = new Collation(137, "ucs2_turkish_ci", 0, "ucs2");
    arrayOfCollation[138] = new Collation(138, "ucs2_czech_ci", 0, "ucs2");
    arrayOfCollation[139] = new Collation(139, "ucs2_danish_ci", 0, "ucs2");
    arrayOfCollation[140] = new Collation(140, "ucs2_lithuanian_ci", 0, "ucs2");
    arrayOfCollation[141] = new Collation(141, "ucs2_slovak_ci", 0, "ucs2");
    arrayOfCollation[142] = new Collation(142, "ucs2_spanish2_ci", 0, "ucs2");
    arrayOfCollation[143] = new Collation(143, "ucs2_roman_ci", 0, "ucs2");
    arrayOfCollation[144] = new Collation(144, "ucs2_persian_ci", 0, "ucs2");
    arrayOfCollation[145] = new Collation(145, "ucs2_esperanto_ci", 0, "ucs2");
    arrayOfCollation[146] = new Collation(146, "ucs2_hungarian_ci", 0, "ucs2");
    arrayOfCollation[147] = new Collation(147, "ucs2_sinhala_ci", 0, "ucs2");
    arrayOfCollation[148] = new Collation(148, "ucs2_german2_ci", 0, "ucs2");
    arrayOfCollation[149] = new Collation(149, "ucs2_croatian_ci", 0, "ucs2");
    arrayOfCollation[150] = new Collation(150, "ucs2_unicode_520_ci", 0, "ucs2");
    arrayOfCollation[151] = new Collation(151, "ucs2_vietnamese_ci", 0, "ucs2");
    arrayOfCollation[159] = new Collation(159, "ucs2_general_mysql500_ci", 0, "ucs2");
    arrayOfCollation[160] = new Collation(160, "utf32_unicode_ci", 0, "utf32");
    arrayOfCollation[161] = new Collation(161, "utf32_icelandic_ci", 0, "utf32");
    arrayOfCollation[162] = new Collation(162, "utf32_latvian_ci", 0, "utf32");
    arrayOfCollation[163] = new Collation(163, "utf32_romanian_ci", 0, "utf32");
    arrayOfCollation[164] = new Collation(164, "utf32_slovenian_ci", 0, "utf32");
    arrayOfCollation[165] = new Collation(165, "utf32_polish_ci", 0, "utf32");
    arrayOfCollation[166] = new Collation(166, "utf32_estonian_ci", 0, "utf32");
    arrayOfCollation[167] = new Collation(167, "utf32_spanish_ci", 0, "utf32");
    arrayOfCollation[168] = new Collation(168, "utf32_swedish_ci", 0, "utf32");
    arrayOfCollation[169] = new Collation(169, "utf32_turkish_ci", 0, "utf32");
    arrayOfCollation[170] = new Collation(170, "utf32_czech_ci", 0, "utf32");
    arrayOfCollation[171] = new Collation(171, "utf32_danish_ci", 0, "utf32");
    arrayOfCollation[172] = new Collation(172, "utf32_lithuanian_ci", 0, "utf32");
    arrayOfCollation[173] = new Collation(173, "utf32_slovak_ci", 0, "utf32");
    arrayOfCollation[174] = new Collation(174, "utf32_spanish2_ci", 0, "utf32");
    arrayOfCollation[175] = new Collation(175, "utf32_roman_ci", 0, "utf32");
    arrayOfCollation[176] = new Collation(176, "utf32_persian_ci", 0, "utf32");
    arrayOfCollation[177] = new Collation(177, "utf32_esperanto_ci", 0, "utf32");
    arrayOfCollation[178] = new Collation(178, "utf32_hungarian_ci", 0, "utf32");
    arrayOfCollation[179] = new Collation(179, "utf32_sinhala_ci", 0, "utf32");
    arrayOfCollation[180] = new Collation(180, "utf32_german2_ci", 0, "utf32");
    arrayOfCollation[181] = new Collation(181, "utf32_croatian_ci", 0, "utf32");
    arrayOfCollation[182] = new Collation(182, "utf32_unicode_520_ci", 0, "utf32");
    arrayOfCollation[183] = new Collation(183, "utf32_vietnamese_ci", 0, "utf32");
    arrayOfCollation[192] = new Collation(192, "utf8_unicode_ci", 0, "utf8");
    arrayOfCollation[193] = new Collation(193, "utf8_icelandic_ci", 0, "utf8");
    arrayOfCollation[194] = new Collation(194, "utf8_latvian_ci", 0, "utf8");
    arrayOfCollation[195] = new Collation(195, "utf8_romanian_ci", 0, "utf8");
    arrayOfCollation[196] = new Collation(196, "utf8_slovenian_ci", 0, "utf8");
    arrayOfCollation[197] = new Collation(197, "utf8_polish_ci", 0, "utf8");
    arrayOfCollation[198] = new Collation(198, "utf8_estonian_ci", 0, "utf8");
    arrayOfCollation[199] = new Collation(199, "utf8_spanish_ci", 0, "utf8");
    arrayOfCollation[200] = new Collation(200, "utf8_swedish_ci", 0, "utf8");
    arrayOfCollation[201] = new Collation(201, "utf8_turkish_ci", 0, "utf8");
    arrayOfCollation[202] = new Collation(202, "utf8_czech_ci", 0, "utf8");
    arrayOfCollation[203] = new Collation(203, "utf8_danish_ci", 0, "utf8");
    arrayOfCollation[204] = new Collation(204, "utf8_lithuanian_ci", 0, "utf8");
    arrayOfCollation[205] = new Collation(205, "utf8_slovak_ci", 0, "utf8");
    arrayOfCollation[206] = new Collation(206, "utf8_spanish2_ci", 0, "utf8");
    arrayOfCollation[207] = new Collation(207, "utf8_roman_ci", 0, "utf8");
    arrayOfCollation[208] = new Collation(208, "utf8_persian_ci", 0, "utf8");
    arrayOfCollation[209] = new Collation(209, "utf8_esperanto_ci", 0, "utf8");
    arrayOfCollation[210] = new Collation(210, "utf8_hungarian_ci", 0, "utf8");
    arrayOfCollation[211] = new Collation(211, "utf8_sinhala_ci", 0, "utf8");
    arrayOfCollation[212] = new Collation(212, "utf8_german2_ci", 0, "utf8");
    arrayOfCollation[213] = new Collation(213, "utf8_croatian_ci", 0, "utf8");
    arrayOfCollation[214] = new Collation(214, "utf8_unicode_520_ci", 0, "utf8");
    arrayOfCollation[215] = new Collation(215, "utf8_vietnamese_ci", 0, "utf8");
    arrayOfCollation[223] = new Collation(223, "utf8_general_mysql500_ci", 0, "utf8");
    arrayOfCollation[224] = new Collation(224, "utf8mb4_unicode_ci", 0, "utf8mb4");
    arrayOfCollation[225] = new Collation(225, "utf8mb4_icelandic_ci", 0, "utf8mb4");
    arrayOfCollation[226] = new Collation(226, "utf8mb4_latvian_ci", 0, "utf8mb4");
    arrayOfCollation[227] = new Collation(227, "utf8mb4_romanian_ci", 0, "utf8mb4");
    arrayOfCollation[228] = new Collation(228, "utf8mb4_slovenian_ci", 0, "utf8mb4");
    arrayOfCollation[229] = new Collation(229, "utf8mb4_polish_ci", 0, "utf8mb4");
    arrayOfCollation[230] = new Collation(230, "utf8mb4_estonian_ci", 0, "utf8mb4");
    arrayOfCollation[231] = new Collation(231, "utf8mb4_spanish_ci", 0, "utf8mb4");
    arrayOfCollation[232] = new Collation(232, "utf8mb4_swedish_ci", 0, "utf8mb4");
    arrayOfCollation[233] = new Collation(233, "utf8mb4_turkish_ci", 0, "utf8mb4");
    arrayOfCollation[234] = new Collation(234, "utf8mb4_czech_ci", 0, "utf8mb4");
    arrayOfCollation[235] = new Collation(235, "utf8mb4_danish_ci", 0, "utf8mb4");
    arrayOfCollation[236] = new Collation(236, "utf8mb4_lithuanian_ci", 0, "utf8mb4");
    arrayOfCollation[237] = new Collation(237, "utf8mb4_slovak_ci", 0, "utf8mb4");
    arrayOfCollation[238] = new Collation(238, "utf8mb4_spanish2_ci", 0, "utf8mb4");
    arrayOfCollation[239] = new Collation(239, "utf8mb4_roman_ci", 0, "utf8mb4");
    arrayOfCollation[240] = new Collation(240, "utf8mb4_persian_ci", 0, "utf8mb4");
    arrayOfCollation[241] = new Collation(241, "utf8mb4_esperanto_ci", 0, "utf8mb4");
    arrayOfCollation[242] = new Collation(242, "utf8mb4_hungarian_ci", 0, "utf8mb4");
    arrayOfCollation[243] = new Collation(243, "utf8mb4_sinhala_ci", 0, "utf8mb4");
    arrayOfCollation[244] = new Collation(244, "utf8mb4_german2_ci", 0, "utf8mb4");
    arrayOfCollation[245] = new Collation(245, "utf8mb4_croatian_ci", 0, "utf8mb4");
    arrayOfCollation[246] = new Collation(246, "utf8mb4_unicode_520_ci", 0, "utf8mb4");
    arrayOfCollation[247] = new Collation(247, "utf8mb4_vietnamese_ci", 0, "utf8mb4");
    arrayOfCollation[248] = new Collation(248, "gb18030_chinese_ci", 1, "gb18030");
    arrayOfCollation[249] = new Collation(249, "gb18030_bin", 0, "gb18030");
    arrayOfCollation[250] = new Collation(250, "gb18030_unicode_520_ci", 0, "gb18030");
    arrayOfCollation[255] = new Collation(255, "utf8mb4_0900_ai_ci", 0, "utf8mb4");
    arrayOfCollation[256] = new Collation(256, "utf8mb4_de_pb_0900_ai_ci", 0, "utf8mb4");
    arrayOfCollation[257] = new Collation(257, "utf8mb4_is_0900_ai_ci", 0, "utf8mb4");
    arrayOfCollation[258] = new Collation(258, "utf8mb4_lv_0900_ai_ci", 0, "utf8mb4");
    arrayOfCollation[259] = new Collation(259, "utf8mb4_ro_0900_ai_ci", 0, "utf8mb4");
    arrayOfCollation[260] = new Collation(260, "utf8mb4_sl_0900_ai_ci", 0, "utf8mb4");
    arrayOfCollation[261] = new Collation(261, "utf8mb4_pl_0900_ai_ci", 0, "utf8mb4");
    arrayOfCollation[262] = new Collation(262, "utf8mb4_et_0900_ai_ci", 0, "utf8mb4");
    arrayOfCollation[263] = new Collation(263, "utf8mb4_es_0900_ai_ci", 0, "utf8mb4");
    arrayOfCollation[264] = new Collation(264, "utf8mb4_sv_0900_ai_ci", 0, "utf8mb4");
    arrayOfCollation[265] = new Collation(265, "utf8mb4_tr_0900_ai_ci", 0, "utf8mb4");
    arrayOfCollation[266] = new Collation(266, "utf8mb4_cs_0900_ai_ci", 0, "utf8mb4");
    arrayOfCollation[267] = new Collation(267, "utf8mb4_da_0900_ai_ci", 0, "utf8mb4");
    arrayOfCollation[268] = new Collation(268, "utf8mb4_lt_0900_ai_ci", 0, "utf8mb4");
    arrayOfCollation[269] = new Collation(269, "utf8mb4_sk_0900_ai_ci", 0, "utf8mb4");
    arrayOfCollation[270] = new Collation(270, "utf8mb4_es_trad_0900_ai_ci", 0, "utf8mb4");
    arrayOfCollation[271] = new Collation(271, "utf8mb4_la_0900_ai_ci", 0, "utf8mb4");
    arrayOfCollation[273] = new Collation(273, "utf8mb4_eo_0900_ai_ci", 0, "utf8mb4");
    arrayOfCollation[274] = new Collation(274, "utf8mb4_hu_0900_ai_ci", 0, "utf8mb4");
    arrayOfCollation[275] = new Collation(275, "utf8mb4_hr_0900_ai_ci", 0, "utf8mb4");
    arrayOfCollation[277] = new Collation(277, "utf8mb4_vi_0900_ai_ci", 0, "utf8mb4");
    arrayOfCollation[278] = new Collation(278, "utf8mb4_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[279] = new Collation(279, "utf8mb4_de_pb_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[280] = new Collation(280, "utf8mb4_is_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[281] = new Collation(281, "utf8mb4_lv_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[282] = new Collation(282, "utf8mb4_ro_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[283] = new Collation(283, "utf8mb4_sl_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[284] = new Collation(284, "utf8mb4_pl_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[285] = new Collation(285, "utf8mb4_et_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[286] = new Collation(286, "utf8mb4_es_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[287] = new Collation(287, "utf8mb4_sv_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[288] = new Collation(288, "utf8mb4_tr_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[289] = new Collation(289, "utf8mb4_cs_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[290] = new Collation(290, "utf8mb4_da_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[291] = new Collation(291, "utf8mb4_lt_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[292] = new Collation(292, "utf8mb4_sk_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[293] = new Collation(293, "utf8mb4_es_trad_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[294] = new Collation(294, "utf8mb4_la_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[296] = new Collation(296, "utf8mb4_eo_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[297] = new Collation(297, "utf8mb4_hu_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[298] = new Collation(298, "utf8mb4_hr_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[300] = new Collation(300, "utf8mb4_vi_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[303] = new Collation(303, "utf8mb4_ja_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[304] = new Collation(304, "utf8mb4_ja_0900_as_cs_ks", 0, "utf8mb4");
    arrayOfCollation[305] = new Collation(305, "utf8mb4_0900_as_ci", 0, "utf8mb4");
    arrayOfCollation[306] = new Collation(306, "utf8mb4_ru_0900_ai_ci", 0, "utf8mb4");
    arrayOfCollation[307] = new Collation(307, "utf8mb4_ru_0900_as_cs", 0, "utf8mb4");
    arrayOfCollation[326] = new Collation(326, "utf8mb4_test_ci", 0, "utf8mb4");
    arrayOfCollation[327] = new Collation(327, "utf16_test_ci", 0, "utf16");
    arrayOfCollation[328] = new Collation(328, "utf8mb4_test_400_ci", 0, "utf8mb4");
    arrayOfCollation[336] = new Collation(336, "utf8_bengali_standard_ci", 0, "utf8");
    arrayOfCollation[337] = new Collation(337, "utf8_bengali_traditional_ci", 0, "utf8");
    arrayOfCollation[352] = new Collation(352, "utf8_phone_ci", 0, "utf8");
    arrayOfCollation[353] = new Collation(353, "utf8_test_ci", 0, "utf8");
    arrayOfCollation[354] = new Collation(354, "utf8_5624_1", 0, "utf8");
    arrayOfCollation[355] = new Collation(355, "utf8_5624_2", 0, "utf8");
    arrayOfCollation[356] = new Collation(356, "utf8_5624_3", 0, "utf8");
    arrayOfCollation[357] = new Collation(357, "utf8_5624_4", 0, "utf8");
    arrayOfCollation[358] = new Collation(358, "ucs2_test_ci", 0, "ucs2");
    arrayOfCollation[359] = new Collation(359, "ucs2_vn_ci", 0, "ucs2");
    arrayOfCollation[360] = new Collation(360, "ucs2_5624_1", 0, "ucs2");
    arrayOfCollation[368] = new Collation(368, "utf8_5624_5", 0, "utf8");
    arrayOfCollation[391] = new Collation(391, "utf32_test_ci", 0, "utf32");
    arrayOfCollation[2047] = new Collation(2047, "utf8_maxuserid_ci", 0, "utf8");
    COLLATION_INDEX_TO_COLLATION_NAME = new String[2048];
    COLLATION_INDEX_TO_CHARSET = new MysqlCharset[2048];
    TreeMap<Object, Object> treeMap2 = new TreeMap<Object, Object>();
    TreeMap<Object, Object> treeMap1 = new TreeMap<Object, Object>();
    HashSet<Integer> hashSet = new HashSet();
    Collation collation = new Collation(0, "none", 0, "latin1");
    for (b = 1; b < 'ࠀ'; b++) {
      Collation collation1;
      if (arrayOfCollation[b] != null) {
        collation1 = arrayOfCollation[b];
      } else {
        collation1 = collation;
      } 
      COLLATION_INDEX_TO_COLLATION_NAME[b] = collation1.collationName;
      MysqlCharset[] arrayOfMysqlCharset1 = COLLATION_INDEX_TO_CHARSET;
      MysqlCharset mysqlCharset = collation1.mysqlCharset;
      arrayOfMysqlCharset1[b] = mysqlCharset;
      String str = mysqlCharset.charsetName;
      if (!treeMap2.containsKey(str) || ((Integer)treeMap1.get(str)).intValue() < collation1.priority) {
        treeMap2.put(str, Integer.valueOf(b));
        treeMap1.put(str, Integer.valueOf(collation1.priority));
      } 
      if (str.equals("utf8mb4"))
        hashSet.add(Integer.valueOf(b)); 
    } 
    CHARSET_NAME_TO_COLLATION_INDEX = Collections.unmodifiableMap(treeMap2);
    UTF8MB4_INDEXES = Collections.unmodifiableSet(hashSet);
    HashMap<Object, Object> hashMap1 = new HashMap<Object, Object>();
    hashMap1.put("czech", "latin2");
    hashMap1.put("danish", "latin1");
    hashMap1.put("dutch", "latin1");
    hashMap1.put("english", "latin1");
    hashMap1.put("estonian", str1);
    hashMap1.put("french", "latin1");
    hashMap1.put("german", "latin1");
    hashMap1.put(str2, str2);
    hashMap1.put("hungarian", "latin2");
    hashMap1.put("italian", "latin1");
    hashMap1.put("japanese", "ujis");
    hashMap1.put("japanese-sjis", "sjis");
    hashMap1.put("korean", "euckr");
    hashMap1.put("norwegian", "latin1");
    hashMap1.put("norwegian-ny", "latin1");
    hashMap1.put("polish", "latin2");
    hashMap1.put("portuguese", "latin1");
    hashMap1.put("romanian", "latin2");
    hashMap1.put("russian", "koi8r");
    hashMap1.put("serbian", str3);
    hashMap1.put("slovak", "latin2");
    hashMap1.put("spanish", "latin1");
    hashMap1.put("swedish", "latin1");
    hashMap1.put("ukrainian", "koi8u");
    ERROR_MESSAGE_FILE_TO_MYSQL_CHARSET = Collections.unmodifiableMap(hashMap1);
  }
  
  public static final String getCharacterEncodingForErrorMessages(ConnectionImpl paramConnectionImpl) throws SQLException {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: aload_0
    //   3: iconst_5
    //   4: iconst_5
    //   5: iconst_0
    //   6: invokevirtual versionMeetsMinimum : (III)Z
    //   9: ifeq -> 39
    //   12: aload_0
    //   13: ldc_w 'jdbc.local.character_set_results'
    //   16: invokevirtual getServerVariable : (Ljava/lang/String;)Ljava/lang/String;
    //   19: astore_0
    //   20: aload_0
    //   21: ifnull -> 35
    //   24: aload_0
    //   25: invokestatic getJavaEncodingForMysqlCharset : (Ljava/lang/String;)Ljava/lang/String;
    //   28: astore_0
    //   29: aload_0
    //   30: ifnull -> 35
    //   33: aload_0
    //   34: areturn
    //   35: ldc_w 'UTF-8'
    //   38: areturn
    //   39: aload_0
    //   40: ldc_w 'language'
    //   43: invokevirtual getServerVariable : (Ljava/lang/String;)Ljava/lang/String;
    //   46: astore_0
    //   47: aload_0
    //   48: ifnull -> 193
    //   51: aload_0
    //   52: invokevirtual length : ()I
    //   55: ifne -> 61
    //   58: goto -> 193
    //   61: aload_0
    //   62: invokevirtual length : ()I
    //   65: istore_1
    //   66: aload_0
    //   67: ldc_w '/'
    //   70: invokevirtual endsWith : (Ljava/lang/String;)Z
    //   73: ifne -> 88
    //   76: iload_1
    //   77: istore_2
    //   78: aload_0
    //   79: ldc_w '\'
    //   82: invokevirtual endsWith : (Ljava/lang/String;)Z
    //   85: ifeq -> 92
    //   88: iload_1
    //   89: iconst_1
    //   90: isub
    //   91: istore_2
    //   92: iload_2
    //   93: iconst_1
    //   94: isub
    //   95: istore #5
    //   97: aload_0
    //   98: bipush #47
    //   100: iload #5
    //   102: invokevirtual lastIndexOf : (II)I
    //   105: istore #4
    //   107: iload #4
    //   109: istore_1
    //   110: iload #4
    //   112: iconst_m1
    //   113: if_icmpne -> 125
    //   116: aload_0
    //   117: bipush #92
    //   119: iload #5
    //   121: invokevirtual lastIndexOf : (II)I
    //   124: istore_1
    //   125: iload_1
    //   126: iconst_m1
    //   127: if_icmpne -> 135
    //   130: iload_3
    //   131: istore_1
    //   132: goto -> 135
    //   135: iload_1
    //   136: iload_2
    //   137: if_icmpeq -> 193
    //   140: iload_2
    //   141: iload_1
    //   142: if_icmpge -> 148
    //   145: goto -> 193
    //   148: aload_0
    //   149: iload_1
    //   150: iconst_1
    //   151: iadd
    //   152: iload_2
    //   153: invokevirtual substring : (II)Ljava/lang/String;
    //   156: astore_0
    //   157: getstatic com/mysql/jdbc/CharsetMapping.ERROR_MESSAGE_FILE_TO_MYSQL_CHARSET : Ljava/util/Map;
    //   160: aload_0
    //   161: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   166: checkcast java/lang/String
    //   169: astore_0
    //   170: aload_0
    //   171: ifnonnull -> 178
    //   174: ldc_w 'Cp1252'
    //   177: areturn
    //   178: aload_0
    //   179: invokestatic getJavaEncodingForMysqlCharset : (Ljava/lang/String;)Ljava/lang/String;
    //   182: astore_0
    //   183: aload_0
    //   184: ifnonnull -> 191
    //   187: ldc_w 'Cp1252'
    //   190: areturn
    //   191: aload_0
    //   192: areturn
    //   193: ldc_w 'Cp1252'
    //   196: areturn
  }
  
  public static int getCollationIndexForJavaEncoding(String paramString, Connection paramConnection) throws SQLException {
    paramString = getMysqlCharsetForJavaEncoding(paramString, (Connection)paramConnection);
    if (paramString != null) {
      Integer integer = CHARSET_NAME_TO_COLLATION_INDEX.get(paramString);
      if (integer != null)
        return integer.intValue(); 
    } 
    return 0;
  }
  
  public static String getJavaEncodingForCollationIndex(Integer paramInteger) {
    return getJavaEncodingForCollationIndex(paramInteger, null);
  }
  
  public static String getJavaEncodingForCollationIndex(Integer paramInteger, String paramString) {
    return (paramInteger != null && paramInteger.intValue() > 0 && paramInteger.intValue() < 2048) ? COLLATION_INDEX_TO_CHARSET[paramInteger.intValue()].getMatchingJavaEncoding(paramString) : null;
  }
  
  public static String getJavaEncodingForMysqlCharset(String paramString) {
    return getJavaEncodingForMysqlCharset(paramString, null);
  }
  
  public static String getJavaEncodingForMysqlCharset(String paramString1, String paramString2) {
    MysqlCharset mysqlCharset = CHARSET_NAME_TO_CHARSET.get(paramString1);
    paramString1 = paramString2;
    if (mysqlCharset != null)
      paramString1 = mysqlCharset.getMatchingJavaEncoding(paramString2); 
    return paramString1;
  }
  
  public static int getMblen(String paramString) {
    if (paramString != null) {
      MysqlCharset mysqlCharset = CHARSET_NAME_TO_CHARSET.get(paramString);
      if (mysqlCharset != null)
        return mysqlCharset.mblen; 
    } 
    return 0;
  }
  
  public static final String getMysqlCharsetForJavaEncoding(String paramString, Connection paramConnection) throws SQLException {
    try {
      List list = JAVA_ENCODING_UC_TO_MYSQL_CHARSET.get(paramString.toUpperCase(Locale.ENGLISH));
      if (list != null) {
        Object object;
        Iterator<MysqlCharset> iterator = list.iterator();
        list = null;
        while (iterator.hasNext()) {
          MysqlCharset mysqlCharset = iterator.next();
          if (paramConnection == null)
            return mysqlCharset.charsetName; 
          if (object != null) {
            int j = ((MysqlCharset)object).major;
            int i = mysqlCharset.major;
            if (j >= i) {
              int m = ((MysqlCharset)object).minor;
              int k = mysqlCharset.minor;
              if (m >= k) {
                int i1 = ((MysqlCharset)object).subminor;
                int n = mysqlCharset.subminor;
                if (i1 < n || (((MysqlCharset)object).priority < mysqlCharset.priority && j == i && m == k && i1 == n))
                  continue; 
                continue;
              } 
            } 
          } 
          continue;
          if (SYNTHETIC_LOCAL_VARIABLE_8.isOkayForVersion(paramConnection))
            object = SYNTHETIC_LOCAL_VARIABLE_8; 
        } 
        if (object != null)
          return ((MysqlCharset)object).charsetName; 
      } 
      return null;
    } catch (SQLException sQLException) {
      throw sQLException;
    } catch (RuntimeException runtimeException) {
      SQLException sQLException = SQLError.createSQLException(runtimeException.toString(), "S1009", (ExceptionInterceptor)null);
      sQLException.initCause(runtimeException);
      throw sQLException;
    } 
  }
  
  public static String getMysqlCharsetNameForCollationIndex(Integer paramInteger) {
    return (paramInteger != null && paramInteger.intValue() > 0 && paramInteger.intValue() < 2048) ? (COLLATION_INDEX_TO_CHARSET[paramInteger.intValue()]).charsetName : null;
  }
  
  public static final int getNumberOfCharsetsConfigured() {
    return numberOfEncodingsConfigured;
  }
  
  public static final boolean isMultibyteCharset(String paramString) {
    return MULTIBYTE_ENCODINGS.contains(paramString.toUpperCase(Locale.ENGLISH));
  }
  
  public static final boolean requiresEscapeEasternUnicode(String paramString) {
    return ESCAPE_ENCODINGS.contains(paramString.toUpperCase(Locale.ENGLISH));
  }
}
