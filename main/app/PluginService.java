package com.main.app;

import ILil.I丨L.ILil.L丨lLLL;
import ILil.I丨L.ILil.lI丨II;
import ILil.I丨L.I丨L.I11li1;
import ILil.I丨L.I丨L.Lil;
import ILil.I丨L.I丨L.LlLI1;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import dalvik.system.DexFile;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PluginService extends Service {
  private static PluginService mInstance;
  
  private Lil mApkLoader = null;
  
  private Activity mCurrentActivity;
  
  private Set<Activity> mHashActivity = new HashSet<Activity>();
  
  private L丨lLLL mHostProxy;
  
  private String mPluginActivityClassName;
  
  private IL1Iii mPluginBinder = new IL1Iii(this);
  
  private ClassLoader mPluginClassLoader;
  
  private DexFile mPluginDexFile;
  
  private LlLI1 mPluginLoader = null;
  
  private String mPluginPackageName;
  
  private Resources mPluginResource;
  
  private String mPluginServiceClassName;
  
  private Object mPluginServiceInstance;
  
  private Method onLogEvent;
  
  private Method onScriptEvent;
  
  private Method onScriptRun;
  
  private Method onScriptStop;
  
  public static PluginService getInstance() {
    return mInstance;
  }
  
  private void startNotify(Context paramContext, int paramInt1, int paramInt2, String paramString1, String paramString2, Class<?> paramClass) {
    try {
      String str1 = paramContext.getPackageName();
      String str2 = paramContext.getPackageName();
      if (Build.VERSION.SDK_INT >= 26) {
        NotificationChannel notificationChannel = new NotificationChannel();
        this(str1, str2, 1);
        notificationChannel.setSound(null, null);
        ((NotificationManager)getSystemService(ILil.IL1Iii.IL1Iii(new byte[] { 
                13, 10, 65, 94, 85, 95, 0, 4, 65, 94, 
                92, 88 }, "ce5736"))).createNotificationChannel(notificationChannel);
      } 
      NotificationCompat.Builder builder = new NotificationCompat.Builder();
      this((Context)this, str1);
      startForeground(1, builder.setContentTitle(paramString1).setContentText(paramString2).setWhen(System.currentTimeMillis()).setSmallIcon(2131558414).setLargeIcon(BitmapFactory.decodeResource(getResources(), 2131558414)).build());
    } catch (Exception exception) {}
  }
  
  public boolean checkRunEnvIsOk() {
    try {
      L丨lLLL l丨lLLL = this.mHostProxy;
      if (l丨lLLL != null)
        return l丨lLLL.L丨1丨1丨I(); 
    } catch (Exception exception) {}
    return false;
  }
  
  public Lil getApkLoader() {
    return this.mApkLoader;
  }
  
  public String getPluginActivityName() {
    return this.mPluginActivityClassName;
  }
  
  public DexFile getPluginDexFile() {
    return this.mPluginDexFile;
  }
  
  public Resources getPluginResource() {
    return this.mPluginResource;
  }
  
  public Resources getPluginResource(String paramString) {
    try {
      AssetManager assetManager = AssetManager.class.newInstance();
      assetManager.getClass().getMethod(ILil.IL1Iii.IL1Iii(new byte[] { 
              80, 2, 6, 120, 75, 75, 84, 18, 50, 88, 
              76, 80 }, "1fb988"), new Class[] { String.class }).invoke(assetManager, new Object[] { paramString });
      null = I11li1.l1IIi1丨().getResources();
      return new Resources(assetManager, null.getDisplayMetrics(), null.getConfiguration());
    } catch (Exception exception) {
      exception.printStackTrace();
      return null;
    } 
  }
  
  public int getRunEnvType() {
    try {
      L丨lLLL l丨lLLL = this.mHostProxy;
      if (l丨lLLL != null)
        return l丨lLLL.IL1Iii(); 
    } catch (Exception exception) {}
    return -1;
  }
  
  public int getScriptStatus() {
    L丨lLLL l丨lLLL = this.mHostProxy;
    if (l丨lLLL != null)
      try {
        return Integer.parseInt(l丨lLLL.sendCmd(7, 0, 0, ""));
      } catch (Exception exception) {} 
    return -1;
  }
  
  public boolean installPluginApk(String paramString) {
    L丨lLLL l丨lLLL = this.mHostProxy;
    if (l丨lLLL != null)
      try {
        paramString = l丨lLLL.sendCmd(11, 0, 0, paramString);
        boolean bool = ILil.IL1Iii.IL1Iii(new byte[] { 70, 69, 71, 93 }, "2728d5").equals(paramString);
        if (bool)
          return true; 
      } catch (Exception exception) {} 
    return false;
  }
  
  public boolean installScript(String paramString) {
    L丨lLLL l丨lLLL = this.mHostProxy;
    if (l丨lLLL != null)
      try {
        paramString = l丨lLLL.sendCmd(12, 0, 0, paramString);
        boolean bool = ILil.IL1Iii.IL1Iii(new byte[] { 18, 67, 22, 1 }, "f1cdae").equals(paramString);
        if (bool)
          return true; 
      } catch (Exception exception) {} 
    return false;
  }
  
  @Nullable
  public IBinder onBind(Intent paramIntent) {
    return (IBinder)this.mPluginBinder;
  }
  
  public void onCreate() {
    super.onCreate();
    startNotify((Context)this, (int)(System.currentTimeMillis() / 1000L), 2131558414, getResources().getString(2131689501), getResources().getString(2131689501), PluginActivity.class);
    mInstance = this;
    this.mPluginLoader = new LlLI1(App.ILil().getBaseContext(), ILil.IL1Iii.IL1Iii(new byte[] { 92, 14, 23, 21, 73, 9, 65, 6, 13, 15 }, "4ada9e"));
  }
  
  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
    super.onStartCommand(paramIntent, paramInt1, paramInt2);
    if (paramIntent != null)
      paramIntent.getStringExtra(ILil.IL1Iii.IL1Iii(new byte[] { 81, 85, 66, 93, 92, 13 }, "06643c")); 
    return 1;
  }
  
  public void openActivity() {
    try {
      if (this.mPluginActivityClassName != null) {
        Intent intent = new Intent();
        this((Context)this, PluginActivity.class);
        intent.setFlags(268435456);
        PendingIntent.getActivity((Context)this, 0, intent, 134217728).send();
      } 
    } catch (Exception exception) {}
  }
  
  public boolean openRunEnv() {
    try {
      L丨lLLL l丨lLLL = this.mHostProxy;
      if (l丨lLLL != null)
        return l丨lLLL.IL丨丨l(); 
    } catch (Exception exception) {}
    return false;
  }
  
  public boolean pauseScript() {
    L丨lLLL l丨lLLL = this.mHostProxy;
    if (l丨lLLL != null)
      try {
        String str = l丨lLLL.sendCmd(10, 0, 0, "");
        boolean bool = ILil.IL1Iii.IL1Iii(new byte[] { 65, 23, 67, 93 }, "5e68f1").equals(str);
        if (bool)
          return true; 
      } catch (Exception exception) {} 
    return false;
  }
  
  public void recordActivity(Activity paramActivity) {
    this.mHashActivity.add(paramActivity);
  }
  
  public void removeActivity(Activity paramActivity) {
    if (this.mHashActivity.contains(paramActivity))
      this.mHashActivity.remove(paramActivity); 
  }
  
  public boolean resumeScript() {
    L丨lLLL l丨lLLL = this.mHostProxy;
    if (l丨lLLL != null)
      try {
        String str = l丨lLLL.sendCmd(10, 1, 0, "");
        boolean bool = ILil.IL1Iii.IL1Iii(new byte[] { 76, 65, 70, 84 }, "833127").equals(str);
        if (bool)
          return true; 
      } catch (Exception exception) {} 
    return false;
  }
  
  public String sendScriptEvent(int paramInt, String paramString) {
    L丨lLLL l丨lLLL = this.mHostProxy;
    if (l丨lLLL != null)
      try {
        return l丨lLLL.sendCmd(6, paramInt, 0, paramString);
      } catch (Exception exception) {} 
    return null;
  }
  
  public void setCurrentActivity(Activity paramActivity) {
    this.mCurrentActivity = paramActivity;
  }
  
  public Bitmap snapShot() {
    try {
      L丨lLLL l丨lLLL = this.mHostProxy;
      if (l丨lLLL != null)
        return l丨lLLL.l丨Li1LL(0, 0, 0, 0); 
    } catch (Exception exception) {}
    return null;
  }
  
  public boolean startScript() {
    L丨lLLL l丨lLLL = this.mHostProxy;
    if (l丨lLLL != null)
      try {
        String str = l丨lLLL.sendCmd(8, 0, 0, "");
        boolean bool = ILil.IL1Iii.IL1Iii(new byte[] { 18, 71, 70, 92 }, "f53979").equals(str);
        if (bool)
          return true; 
      } catch (Exception exception) {} 
    return false;
  }
  
  public boolean stopScript() {
    L丨lLLL l丨lLLL = this.mHostProxy;
    if (l丨lLLL != null)
      try {
        String str = l丨lLLL.sendCmd(9, 0, 0, "");
        boolean bool = ILil.IL1Iii.IL1Iii(new byte[] { 22, 20, 22, 83 }, "bfc646").equals(str);
        if (bool)
          return true; 
      } catch (Exception exception) {} 
    return false;
  }
  
  public class IL1Iii extends lI丨II.IL1Iii {
    public final PluginService I1I;
    
    public Map<Long, StringBuilder> ILil = new ConcurrentHashMap<Long, StringBuilder>();
    
    public IL1Iii(PluginService this$0) {}
    
    public void lIi丨I(L丨lLLL param1L丨lLLL) throws RemoteException {
      PluginService.access$002(this.I1I, param1L丨lLLL);
    }
    
    public String sendCmd(int param1Int1, int param1Int2, int param1Int3, String param1String) throws RemoteException {
      if (param1Int1 == 1) {
        boolean bool;
        PluginService pluginService = this.I1I;
        LlLI1 llLI1 = pluginService.mPluginLoader;
        if (param1Int2 == 1) {
          bool = true;
        } else {
          bool = false;
        } 
        PluginService.access$302(pluginService, llLI1.Ilil(param1String, bool));
        if (this.I1I.mPluginClassLoader != null)
          try {
            PluginService pluginService1 = this.I1I;
            String[] arrayOfString = I11li1.丨丨LLlI1((Context)pluginService1, pluginService1.mPluginLoader.I丨L());
            if (arrayOfString == null || arrayOfString[0] == null || arrayOfString[1] == null)
              return ILil.IL1Iii.IL1Iii(new byte[] { 84, 3, 93, 21, 87 }, "2b1f2a"); 
            if (arrayOfString[2] != null)
              PluginService.access$502(this.I1I, arrayOfString[2]); 
            try {
              pluginService = this.I1I;
              Lil lil = new Lil();
              this(pluginService.mPluginLoader.I丨L(), this.I1I.mPluginClassLoader);
              PluginService.access$602(pluginService, lil);
              Class<?> clazz = this.I1I.mPluginClassLoader.loadClass(arrayOfString[1]);
              Method method = clazz.getDeclaredMethod(ILil.IL1Iii.IL1Iii(new byte[] { 92, 11, 113, 17, 1, 83, 71, 0 }, "3e2cd2"), new Class[] { Context.class, Context.class });
              PluginService.access$102(this.I1I, clazz.newInstance());
              Object object = this.I1I.mPluginServiceInstance;
              pluginService = this.I1I;
              method.invoke(object, new Object[] { pluginService, PluginService.access$600(pluginService).ILil() });
            } catch (Exception exception) {
              exception.printStackTrace();
            } 
            return ILil.IL1Iii.IL1Iii(new byte[] { 71, 23, 67, 81 }, "3e64d4");
          } catch (Exception exception) {
            exception.printStackTrace();
          }  
      } else if (param1Int1 == 2) {
        try {
          if (this.I1I.mPluginServiceInstance != null)
            this.I1I.mPluginServiceInstance.getClass().getDeclaredMethod(ILil.IL1Iii.IL1Iii(new byte[] { 89, 95, 32, 81, 21, 64, 89, 67, 29 }, "61d4f4"), new Class[0]).invoke(this.I1I.mPluginServiceInstance, new Object[0]); 
        } catch (Exception exception) {
          exception.printStackTrace();
        } 
        try {
          Process.killProcess(Process.myPid());
        } catch (Exception exception) {}
      } else if (param1Int1 == 3) {
        this.I1I.openActivity();
      } else if (param1Int1 == 4) {
        try {
          if (this.I1I.mPluginServiceInstance != null) {
            if (this.I1I.onScriptRun == null) {
              PluginService pluginService = this.I1I;
              PluginService.access$702(pluginService, pluginService.mPluginServiceInstance.getClass().getDeclaredMethod(ILil.IL1Iii.IL1Iii(new byte[] { 
                        90, 95, 100, 84, 20, 13, 69, 69, 101, 66, 
                        8 }, "5177fd"), new Class[0]));
            } 
            this.I1I.onScriptRun.invoke(this.I1I.mPluginServiceInstance, new Object[0]);
            return ILil.IL1Iii.IL1Iii(new byte[] { 76, 64, 69, 3 }, "820f74");
          } 
        } catch (Exception exception) {
          exception.printStackTrace();
        } 
      } else if (param1Int1 == 5) {
        try {
          if (this.I1I.mPluginServiceInstance != null) {
            if (this.I1I.onScriptStop == null) {
              PluginService pluginService = this.I1I;
              PluginService.access$802(pluginService, pluginService.mPluginServiceInstance.getClass().getDeclaredMethod(ILil.IL1Iii.IL1Iii(new byte[] { 
                        94, 8, 99, 6, 23, 90, 65, 18, 99, 17, 
                        10, 67 }, "1f0ee3"), new Class[0]));
            } 
            this.I1I.onScriptStop.invoke(this.I1I.mPluginServiceInstance, new Object[0]);
            return ILil.IL1Iii.IL1Iii(new byte[] { 17, 23, 64, 83 }, "ee56d5");
          } 
        } catch (Exception exception) {
          exception.printStackTrace();
        } 
        this.ILil.clear();
      } else if (param1Int1 == 6) {
        try {
          if (this.I1I.mPluginServiceInstance != null) {
            if (this.I1I.onScriptEvent == null) {
              PluginService pluginService = this.I1I;
              PluginService.access$902(pluginService, pluginService.mPluginServiceInstance.getClass().getDeclaredMethod(ILil.IL1Iii.IL1Iii(new byte[] { 
                        86, 87, 49, 82, 66, 8, 73, 77, 39, 71, 
                        85, 15, 77 }, "99b10a"), new Class[] { int.class, String.class }));
            } 
            return (String)this.I1I.onScriptEvent.invoke(this.I1I.mPluginServiceInstance, new Object[] { Integer.valueOf(param1Int2), exception });
          } 
        } catch (Exception exception1) {
          exception1.printStackTrace();
        } 
      } else if (param1Int1 == 7) {
        Iterator<Activity> iterator = this.I1I.mHashActivity.iterator();
        while (true) {
          if (iterator.hasNext()) {
            Activity activity = iterator.next();
            try {
              activity.finish();
            } catch (Exception exception1) {}
            continue;
          } 
          if (this.I1I.mCurrentActivity != null)
            this.I1I.mCurrentActivity.finish(); 
          return null;
        } 
      } 
      return null;
    }
    
    public void sendLog(int param1Int, String param1String1, String param1String2, long param1Long, boolean param1Boolean) {
      // Byte code:
      //   0: aload_0
      //   1: getfield I1I : Lcom/main/app/PluginService;
      //   4: invokestatic access$100 : (Lcom/main/app/PluginService;)Ljava/lang/Object;
      //   7: ifnonnull -> 11
      //   10: return
      //   11: aload_0
      //   12: getfield I1I : Lcom/main/app/PluginService;
      //   15: invokestatic access$200 : (Lcom/main/app/PluginService;)Ljava/lang/reflect/Method;
      //   18: ifnonnull -> 131
      //   21: aload_0
      //   22: getfield I1I : Lcom/main/app/PluginService;
      //   25: astore #7
      //   27: aload #7
      //   29: aload #7
      //   31: invokestatic access$100 : (Lcom/main/app/PluginService;)Ljava/lang/Object;
      //   34: invokevirtual getClass : ()Ljava/lang/Class;
      //   37: bipush #10
      //   39: newarray byte
      //   41: dup
      //   42: iconst_0
      //   43: bipush #95
      //   45: bastore
      //   46: dup
      //   47: iconst_1
      //   48: bipush #89
      //   50: bastore
      //   51: dup
      //   52: iconst_2
      //   53: bipush #41
      //   55: bastore
      //   56: dup
      //   57: iconst_3
      //   58: bipush #90
      //   60: bastore
      //   61: dup
      //   62: iconst_4
      //   63: iconst_3
      //   64: bastore
      //   65: dup
      //   66: iconst_5
      //   67: bipush #115
      //   69: bastore
      //   70: dup
      //   71: bipush #6
      //   73: bipush #70
      //   75: bastore
      //   76: dup
      //   77: bipush #7
      //   79: bipush #82
      //   81: bastore
      //   82: dup
      //   83: bipush #8
      //   85: bipush #11
      //   87: bastore
      //   88: dup
      //   89: bipush #9
      //   91: bipush #65
      //   93: bastore
      //   94: ldc '07e5d6'
      //   96: invokestatic IL1Iii : ([BLjava/lang/String;)Ljava/lang/String;
      //   99: iconst_3
      //   100: anewarray java/lang/Class
      //   103: dup
      //   104: iconst_0
      //   105: getstatic java/lang/Integer.TYPE : Ljava/lang/Class;
      //   108: aastore
      //   109: dup
      //   110: iconst_1
      //   111: ldc java/lang/String
      //   113: aastore
      //   114: dup
      //   115: iconst_2
      //   116: ldc java/lang/String
      //   118: aastore
      //   119: invokevirtual getDeclaredMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
      //   122: invokestatic access$202 : (Lcom/main/app/PluginService;Ljava/lang/reflect/Method;)Ljava/lang/reflect/Method;
      //   125: pop
      //   126: goto -> 131
      //   129: astore #7
      //   131: aload_0
      //   132: getfield I1I : Lcom/main/app/PluginService;
      //   135: invokestatic access$200 : (Lcom/main/app/PluginService;)Ljava/lang/reflect/Method;
      //   138: ifnonnull -> 142
      //   141: return
      //   142: lload #4
      //   144: lconst_0
      //   145: lcmp
      //   146: ifle -> 275
      //   149: aload_0
      //   150: getfield ILil : Ljava/util/Map;
      //   153: lload #4
      //   155: invokestatic valueOf : (J)Ljava/lang/Long;
      //   158: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
      //   163: checkcast java/lang/StringBuilder
      //   166: astore #8
      //   168: aload #8
      //   170: astore #7
      //   172: aload #8
      //   174: ifnonnull -> 203
      //   177: new java/lang/StringBuilder
      //   180: dup
      //   181: invokespecial <init> : ()V
      //   184: astore #7
      //   186: aload_0
      //   187: getfield ILil : Ljava/util/Map;
      //   190: lload #4
      //   192: invokestatic valueOf : (J)Ljava/lang/Long;
      //   195: aload #7
      //   197: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
      //   202: pop
      //   203: aload #7
      //   205: monitorenter
      //   206: aload_3
      //   207: astore #8
      //   209: aload #7
      //   211: aload_3
      //   212: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   215: pop
      //   216: aload #7
      //   218: monitorexit
      //   219: aload #8
      //   221: astore_3
      //   222: iload #6
      //   224: ifeq -> 275
      //   227: aload #7
      //   229: monitorenter
      //   230: aload #7
      //   232: invokevirtual toString : ()Ljava/lang/String;
      //   235: astore_3
      //   236: aload #7
      //   238: iconst_0
      //   239: invokevirtual setLength : (I)V
      //   242: aload #7
      //   244: monitorexit
      //   245: aload_0
      //   246: getfield ILil : Ljava/util/Map;
      //   249: lload #4
      //   251: invokestatic valueOf : (J)Ljava/lang/Long;
      //   254: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
      //   259: pop
      //   260: goto -> 275
      //   263: astore_2
      //   264: aload #7
      //   266: monitorexit
      //   267: aload_2
      //   268: athrow
      //   269: astore_2
      //   270: aload #7
      //   272: monitorexit
      //   273: aload_2
      //   274: athrow
      //   275: iload #6
      //   277: ifeq -> 317
      //   280: aload_0
      //   281: getfield I1I : Lcom/main/app/PluginService;
      //   284: invokestatic access$200 : (Lcom/main/app/PluginService;)Ljava/lang/reflect/Method;
      //   287: aload_0
      //   288: getfield I1I : Lcom/main/app/PluginService;
      //   291: invokestatic access$100 : (Lcom/main/app/PluginService;)Ljava/lang/Object;
      //   294: iconst_3
      //   295: anewarray java/lang/Object
      //   298: dup
      //   299: iconst_0
      //   300: iload_1
      //   301: invokestatic valueOf : (I)Ljava/lang/Integer;
      //   304: aastore
      //   305: dup
      //   306: iconst_1
      //   307: aload_2
      //   308: aastore
      //   309: dup
      //   310: iconst_2
      //   311: aload_3
      //   312: aastore
      //   313: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
      //   316: pop
      //   317: return
      //   318: astore_2
      //   319: goto -> 317
      // Exception table:
      //   from	to	target	type
      //   11	126	129	java/lang/Exception
      //   209	219	269	finally
      //   230	245	263	finally
      //   264	267	263	finally
      //   270	273	269	finally
      //   280	317	318	java/lang/Exception
    }
  }
}
