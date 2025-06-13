package android.app;

import ILil.IL1Iii;
import ILil.I丨L.ILil.I11L;
import ILil.I丨L.ILil.L11丨丨丨1;
import ILil.I丨L.ILil.llliI;
import ILil.I丨L.ILil.丨丨;
import ILil.I丨L.ILil.丨丨LLlI1;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INXCoreNative extends IInterface {
  void closeFile(long paramLong) throws RemoteException;
  
  I11L obtainAccessibityActionProxy() throws RemoteException;
  
  IAccessibServiceProxy obtainAccessibityService() throws RemoteException;
  
  Bitmap obtainBitmap(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) throws RemoteException;
  
  Bundle obtainData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) throws RemoteException;
  
  llliI obtainDataTask() throws RemoteException;
  
  INXCoreNative obtainScriptProxy(int paramInt) throws RemoteException;
  
  丨丨LLlI1 obtainUiObjectProxy(int paramInt) throws RemoteException;
  
  丨丨 obtainUiSelectorProxy(int paramInt) throws RemoteException;
  
  long openFile(String paramString, int paramInt) throws RemoteException;
  
  boolean openFileNew(String paramString1, String paramString2, INXOpenFdEvent paramINXOpenFdEvent) throws RemoteException;
  
  long readFile(long paramLong, byte[] paramArrayOfbyte) throws RemoteException;
  
  String sendCmd(int paramInt1, int paramInt2, int paramInt3, String paramString) throws RemoteException;
  
  boolean sendKeyEvent(int paramInt1, int paramInt2) throws RemoteException;
  
  void sendLog(int paramInt, String paramString1, String paramString2, long paramLong, boolean paramBoolean) throws RemoteException;
  
  void sendTaskResultBundle(int paramInt, long paramLong, Bundle paramBundle) throws RemoteException;
  
  boolean sendTouchEvent(int paramInt1, int paramInt2, int paramInt3, int paramInt4) throws RemoteException;
  
  void setTsEventListener(int paramInt, L11丨丨丨1 paramL11丨丨丨1) throws RemoteException;
  
  long writeFile(long paramLong, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws RemoteException;
  
  public static class Default implements INXCoreNative {
    public IBinder asBinder() {
      return null;
    }
    
    public void closeFile(long param1Long) throws RemoteException {}
    
    public I11L obtainAccessibityActionProxy() throws RemoteException {
      return null;
    }
    
    public IAccessibServiceProxy obtainAccessibityService() throws RemoteException {
      return null;
    }
    
    public Bitmap obtainBitmap(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5) throws RemoteException {
      return null;
    }
    
    public Bundle obtainData(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5) throws RemoteException {
      return null;
    }
    
    public llliI obtainDataTask() throws RemoteException {
      return null;
    }
    
    public INXCoreNative obtainScriptProxy(int param1Int) throws RemoteException {
      return null;
    }
    
    public 丨丨LLlI1 obtainUiObjectProxy(int param1Int) throws RemoteException {
      return null;
    }
    
    public 丨丨 obtainUiSelectorProxy(int param1Int) throws RemoteException {
      return null;
    }
    
    public long openFile(String param1String, int param1Int) throws RemoteException {
      return 0L;
    }
    
    public boolean openFileNew(String param1String1, String param1String2, INXOpenFdEvent param1INXOpenFdEvent) throws RemoteException {
      return false;
    }
    
    public long readFile(long param1Long, byte[] param1ArrayOfbyte) throws RemoteException {
      return 0L;
    }
    
    public String sendCmd(int param1Int1, int param1Int2, int param1Int3, String param1String) throws RemoteException {
      return null;
    }
    
    public boolean sendKeyEvent(int param1Int1, int param1Int2) throws RemoteException {
      return false;
    }
    
    public void sendLog(int param1Int, String param1String1, String param1String2, long param1Long, boolean param1Boolean) throws RemoteException {}
    
    public void sendTaskResultBundle(int param1Int, long param1Long, Bundle param1Bundle) throws RemoteException {}
    
    public boolean sendTouchEvent(int param1Int1, int param1Int2, int param1Int3, int param1Int4) throws RemoteException {
      return false;
    }
    
    public void setTsEventListener(int param1Int, L11丨丨丨1 param1L11丨丨丨1) throws RemoteException {}
    
    public long writeFile(long param1Long, byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws RemoteException {
      return 0L;
    }
  }
  
  public static abstract class Stub extends Binder implements INXCoreNative {
    private static final String DESCRIPTOR = IL1Iii.IL1Iii(new byte[] { 
          89, 8, 5, 67, 95, 12, 92, 72, 0, 65, 
          64, 75, 113, 40, 57, 114, 95, 23, 93, 40, 
          0, 69, 89, 19, 93 }, "8fa10e");
    
    public static final int TRANSACTION_closeFile = 4;
    
    public static final int TRANSACTION_obtainAccessibityActionProxy = 13;
    
    public static final int TRANSACTION_obtainAccessibityService = 14;
    
    public static final int TRANSACTION_obtainBitmap = 19;
    
    public static final int TRANSACTION_obtainData = 18;
    
    public static final int TRANSACTION_obtainDataTask = 11;
    
    public static final int TRANSACTION_obtainScriptProxy = 12;
    
    public static final int TRANSACTION_obtainUiObjectProxy = 10;
    
    public static final int TRANSACTION_obtainUiSelectorProxy = 9;
    
    public static final int TRANSACTION_openFile = 1;
    
    public static final int TRANSACTION_openFileNew = 5;
    
    public static final int TRANSACTION_readFile = 3;
    
    public static final int TRANSACTION_sendCmd = 6;
    
    public static final int TRANSACTION_sendKeyEvent = 17;
    
    public static final int TRANSACTION_sendLog = 8;
    
    public static final int TRANSACTION_sendTaskResultBundle = 7;
    
    public static final int TRANSACTION_sendTouchEvent = 16;
    
    public static final int TRANSACTION_setTsEventListener = 15;
    
    public static final int TRANSACTION_writeFile = 2;
    
    public Stub() {
      attachInterface(this, IL1Iii.IL1Iii(new byte[] { 
              7, 11, 93, 19, 90, 89, 2, 75, 88, 17, 
              69, 30, 47, 43, 97, 34, 90, 66, 3, 43, 
              88, 21, 92, 70, 3 }, "fe9a50"));
    }
    
    public static INXCoreNative asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface(DESCRIPTOR);
      return (iInterface != null && iInterface instanceof INXCoreNative) ? (INXCoreNative)iInterface : new Proxy(param1IBinder);
    }
    
    public static INXCoreNative getDefaultImpl() {
      return Proxy.sDefaultImpl;
    }
    
    public static boolean setDefaultImpl(INXCoreNative param1INXCoreNative) {
      if (Proxy.sDefaultImpl == null) {
        if (param1INXCoreNative != null) {
          Proxy.sDefaultImpl = param1INXCoreNative;
          return true;
        } 
        return false;
      } 
      throw new IllegalStateException(IL1Iii.IL1Iii(new byte[] { 
              69, 0, 64, 32, 1, 86, 87, 16, 88, 16, 
              45, 93, 70, 9, 28, 77, 68, 83, 87, 9, 
              88, 1, 0, 16, 66, 18, 93, 7, 1 }, "6e4dd0"));
    }
    
    public IBinder asBinder() {
      return (IBinder)this;
    }
    
    public boolean onTransact(int param1Int1, Parcel param1Parcel1, Parcel param1Parcel2, int param1Int2) throws RemoteException {
      String str = DESCRIPTOR;
      if (param1Int1 != 1598968902) {
        boolean bool2;
        int j;
        boolean bool1;
        int i;
        Bitmap bitmap;
        Bundle bundle1;
        IBinder iBinder2;
        llliI llliI1;
        IBinder iBinder1;
        String str1;
        byte[] arrayOfByte;
        boolean bool;
        I11L i11L;
        INXCoreNative iNXCoreNative;
        llliI llliI2;
        丨丨 丨丨;
        String str2;
        Bundle bundle2;
        丨丨LLlI1 丨丨LLlI1;
        String str3;
        IBinder iBinder3 = null;
        IBinder iBinder4 = null;
        IBinder iBinder6 = null;
        IAccessibServiceProxy iAccessibServiceProxy = null;
        IBinder iBinder5 = null;
        String str4 = null;
        String str5 = null;
        Bundle bundle3 = null;
        switch (param1Int1) {
          default:
            return super.onTransact(param1Int1, param1Parcel1, param1Parcel2, param1Int2);
          case 19:
            param1Parcel1.enforceInterface(str);
            bitmap = obtainBitmap(param1Parcel1.readInt(), param1Parcel1.readInt(), param1Parcel1.readInt(), param1Parcel1.readInt(), param1Parcel1.readInt());
            param1Parcel2.writeNoException();
            if (bitmap != null) {
              param1Parcel2.writeInt(1);
              bitmap.writeToParcel(param1Parcel2, 1);
            } else {
              param1Parcel2.writeInt(0);
            } 
            return true;
          case 18:
            bitmap.enforceInterface(str);
            bundle1 = obtainData(bitmap.readInt(), bitmap.readInt(), bitmap.readInt(), bitmap.readInt(), bitmap.readInt());
            param1Parcel2.writeNoException();
            if (bundle1 != null) {
              param1Parcel2.writeInt(1);
              bundle1.writeToParcel(param1Parcel2, 1);
            } else {
              param1Parcel2.writeInt(0);
            } 
            return true;
          case 17:
            bundle1.enforceInterface(str);
            bool2 = sendKeyEvent(bundle1.readInt(), bundle1.readInt());
            param1Parcel2.writeNoException();
            param1Parcel2.writeInt(bool2);
            return true;
          case 16:
            bundle1.enforceInterface(str);
            bool2 = sendTouchEvent(bundle1.readInt(), bundle1.readInt(), bundle1.readInt(), bundle1.readInt());
            param1Parcel2.writeNoException();
            param1Parcel2.writeInt(bool2);
            return true;
          case 15:
            bundle1.enforceInterface(str);
            setTsEventListener(bundle1.readInt(), L11丨丨丨1.IL1Iii.ILil(bundle1.readStrongBinder()));
            param1Parcel2.writeNoException();
            return true;
          case 14:
            bundle1.enforceInterface(str);
            iAccessibServiceProxy = obtainAccessibityService();
            param1Parcel2.writeNoException();
            bundle1 = bundle3;
            if (iAccessibServiceProxy != null)
              iBinder2 = iAccessibServiceProxy.asBinder(); 
            param1Parcel2.writeStrongBinder(iBinder2);
            return true;
          case 13:
            iBinder2.enforceInterface(str);
            i11L = obtainAccessibityActionProxy();
            param1Parcel2.writeNoException();
            iBinder2 = iBinder3;
            if (i11L != null)
              iBinder2 = i11L.asBinder(); 
            param1Parcel2.writeStrongBinder(iBinder2);
            return true;
          case 12:
            iBinder2.enforceInterface(str);
            iNXCoreNative = obtainScriptProxy(iBinder2.readInt());
            param1Parcel2.writeNoException();
            iBinder2 = iBinder4;
            if (iNXCoreNative != null)
              iBinder2 = iNXCoreNative.asBinder(); 
            param1Parcel2.writeStrongBinder(iBinder2);
            return true;
          case 11:
            iBinder2.enforceInterface(str);
            llliI2 = obtainDataTask();
            param1Parcel2.writeNoException();
            iBinder2 = iBinder6;
            if (llliI2 != null)
              iBinder2 = llliI2.asBinder(); 
            param1Parcel2.writeStrongBinder(iBinder2);
            return true;
          case 10:
            iBinder2.enforceInterface(str);
            丨丨LLlI1 = obtainUiObjectProxy(iBinder2.readInt());
            param1Parcel2.writeNoException();
            llliI1 = llliI2;
            if (丨丨LLlI1 != null)
              iBinder1 = 丨丨LLlI1.asBinder(); 
            param1Parcel2.writeStrongBinder(iBinder1);
            return true;
          case 9:
            iBinder1.enforceInterface(str);
            丨丨 = obtainUiSelectorProxy(iBinder1.readInt());
            param1Parcel2.writeNoException();
            iBinder1 = iBinder5;
            if (丨丨 != null)
              iBinder1 = 丨丨.asBinder(); 
            param1Parcel2.writeStrongBinder(iBinder1);
            return true;
          case 8:
            iBinder1.enforceInterface(str);
            j = iBinder1.readInt();
            str2 = iBinder1.readString();
            str3 = iBinder1.readString();
            l = iBinder1.readLong();
            if (iBinder1.readInt() != 0) {
              bool = true;
            } else {
              bool = false;
            } 
            sendLog(j, str2, str3, l, bool);
            param1Parcel2.writeNoException();
            return true;
          case 7:
            iBinder1.enforceInterface(str);
            j = iBinder1.readInt();
            l = iBinder1.readLong();
            str2 = str4;
            if (iBinder1.readInt() != 0)
              bundle2 = (Bundle)Bundle.CREATOR.createFromParcel((Parcel)iBinder1); 
            sendTaskResultBundle(j, l, bundle2);
            param1Parcel2.writeNoException();
            return true;
          case 6:
            iBinder1.enforceInterface(str);
            str1 = sendCmd(iBinder1.readInt(), iBinder1.readInt(), iBinder1.readInt(), iBinder1.readString());
            param1Parcel2.writeNoException();
            param1Parcel2.writeString(str1);
            return true;
          case 5:
            str1.enforceInterface(str);
            bool1 = openFileNew(str1.readString(), str1.readString(), INXOpenFdEvent.Stub.asInterface(str1.readStrongBinder()));
            param1Parcel2.writeNoException();
            param1Parcel2.writeInt(bool1);
            return true;
          case 4:
            str1.enforceInterface(str);
            closeFile(str1.readLong());
            param1Parcel2.writeNoException();
            return true;
          case 3:
            str1.enforceInterface(str);
            l = str1.readLong();
            i = str1.readInt();
            if (i < 0) {
              str1 = str5;
            } else {
              arrayOfByte = new byte[i];
            } 
            l = readFile(l, arrayOfByte);
            param1Parcel2.writeNoException();
            param1Parcel2.writeLong(l);
            param1Parcel2.writeByteArray(arrayOfByte);
            return true;
          case 2:
            arrayOfByte.enforceInterface(str);
            l = writeFile(arrayOfByte.readLong(), arrayOfByte.createByteArray(), arrayOfByte.readInt(), arrayOfByte.readInt());
            param1Parcel2.writeNoException();
            param1Parcel2.writeLong(l);
            return true;
          case 1:
            break;
        } 
        arrayOfByte.enforceInterface(str);
        long l = openFile(arrayOfByte.readString(), arrayOfByte.readInt());
        param1Parcel2.writeNoException();
        param1Parcel2.writeLong(l);
        return true;
      } 
      param1Parcel2.writeString(str);
      return true;
    }
    
    public static class Proxy implements INXCoreNative {
      public static INXCoreNative sDefaultImpl;
      
      private IBinder mRemote;
      
      public Proxy(IBinder param2IBinder) {
        this.mRemote = param2IBinder;
      }
      
      public IBinder asBinder() {
        return this.mRemote;
      }
      
      public void closeFile(long param2Long) throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  86, 89, 6, 71, 9, 95, 83, 25, 3, 69, 
                  22, 24, 126, 121, 58, 118, 9, 68, 82, 121, 
                  3, 65, 15, 64, 82 }, "77b5f6"));
          parcel2.writeLong(param2Long);
          if (!this.mRemote.transact(4, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
            INXCoreNative.Stub.getDefaultImpl().closeFile(param2Long);
            return;
          } 
          parcel1.readException();
          return;
        } finally {
          parcel1.recycle();
          parcel2.recycle();
        } 
      }
      
      public String getInterfaceDescriptor() {
        return IL1Iii.IL1Iii(new byte[] { 
              81, 88, 93, 22, 86, 91, 84, 24, 88, 20, 
              73, 28, 121, 120, 97, 39, 86, 64, 85, 120, 
              88, 16, 80, 68, 85 }, "069d92");
      }
      
      public I11L obtainAccessibityActionProxy() throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  82, 91, 84, 17, 95, 94, 87, 27, 81, 19, 
                  64, 25, 122, 123, 104, 32, 95, 69, 86, 123, 
                  81, 23, 89, 65, 86 }, "350c07"));
          if (!this.mRemote.transact(13, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null)
            return INXCoreNative.Stub.getDefaultImpl().obtainAccessibityActionProxy(); 
          parcel1.readException();
          return I11L.IL1Iii.ILil(parcel1.readStrongBinder());
        } finally {
          parcel1.recycle();
          parcel2.recycle();
        } 
      }
      
      public IAccessibServiceProxy obtainAccessibityService() throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  3, 88, 85, 22, 94, 8, 6, 24, 80, 20, 
                  65, 79, 43, 120, 105, 39, 94, 19, 7, 120, 
                  80, 16, 88, 23, 7 }, "b61d1a"));
          if (!this.mRemote.transact(14, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null)
            return INXCoreNative.Stub.getDefaultImpl().obtainAccessibityService(); 
          parcel1.readException();
          return IAccessibServiceProxy.Stub.asInterface(parcel1.readStrongBinder());
        } finally {
          parcel1.recycle();
          parcel2.recycle();
        } 
      }
      
      public Bitmap obtainBitmap(int param2Int1, int param2Int2, int param2Int3, int param2Int4, int param2Int5) throws RemoteException {
        Exception exception;
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  87, 94, 7, 16, 95, 94, 82, 30, 2, 18, 
                  64, 25, Byte.MAX_VALUE, 126, 59, 33, 95, 69, 83, 126, 
                  2, 22, 89, 65, 83 }, "60cb07"));
          parcel1.writeInt(param2Int1);
          parcel1.writeInt(param2Int2);
          parcel1.writeInt(param2Int3);
          parcel1.writeInt(param2Int4);
          parcel1.writeInt(param2Int5);
          try {
            Bitmap bitmap;
            if (!this.mRemote.transact(19, parcel1, parcel2, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
              bitmap = INXCoreNative.Stub.getDefaultImpl().obtainBitmap(param2Int1, param2Int2, param2Int3, param2Int4, param2Int5);
              parcel2.recycle();
              parcel1.recycle();
              return bitmap;
            } 
            parcel2.readException();
            if (parcel2.readInt() != 0) {
              bitmap = (Bitmap)Bitmap.CREATOR.createFromParcel(parcel2);
            } else {
              bitmap = null;
            } 
            parcel2.recycle();
            parcel1.recycle();
            return bitmap;
          } finally {}
        } finally {}
        parcel2.recycle();
        parcel1.recycle();
        throw exception;
      }
      
      public Bundle obtainData(int param2Int1, int param2Int2, int param2Int3, int param2Int4, int param2Int5) throws RemoteException {
        Exception exception;
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  83, 15, 5, 16, 12, 94, 86, 79, 0, 18, 
                  19, 25, 123, 47, 57, 33, 12, 69, 87, 47, 
                  0, 22, 10, 65, 87 }, "2aabc7"));
          parcel1.writeInt(param2Int1);
          parcel1.writeInt(param2Int2);
          parcel1.writeInt(param2Int3);
          parcel1.writeInt(param2Int4);
          parcel1.writeInt(param2Int5);
          try {
            Bundle bundle;
            if (!this.mRemote.transact(18, parcel1, parcel2, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
              bundle = INXCoreNative.Stub.getDefaultImpl().obtainData(param2Int1, param2Int2, param2Int3, param2Int4, param2Int5);
              parcel2.recycle();
              parcel1.recycle();
              return bundle;
            } 
            parcel2.readException();
            if (parcel2.readInt() != 0) {
              bundle = (Bundle)Bundle.CREATOR.createFromParcel(parcel2);
            } else {
              bundle = null;
            } 
            parcel2.recycle();
            parcel1.recycle();
            return bundle;
          } finally {}
        } finally {}
        parcel2.recycle();
        parcel1.recycle();
        throw exception;
      }
      
      public llliI obtainDataTask() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  83, 90, 84, 65, 87, 11, 86, 26, 81, 67, 
                  72, 76, 123, 122, 104, 112, 87, 16, 87, 122, 
                  81, 71, 81, 20, 87 }, "24038b"));
          if (!this.mRemote.transact(11, parcel1, parcel2, 0) && INXCoreNative.Stub.getDefaultImpl() != null)
            return INXCoreNative.Stub.getDefaultImpl().obtainDataTask(); 
          parcel2.readException();
          return llliI.IL1Iii.ILil(parcel2.readStrongBinder());
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public INXCoreNative obtainScriptProxy(int param2Int) throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  85, 90, 86, 23, 89, 11, 80, 26, 83, 21, 
                  70, 76, 125, 122, 106, 38, 89, 16, 81, 122, 
                  83, 17, 95, 20, 81 }, "442e6b"));
          parcel2.writeInt(param2Int);
          if (!this.mRemote.transact(12, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null)
            return INXCoreNative.Stub.getDefaultImpl().obtainScriptProxy(param2Int); 
          parcel1.readException();
          return INXCoreNative.Stub.asInterface(parcel1.readStrongBinder());
        } finally {
          parcel1.recycle();
          parcel2.recycle();
        } 
      }
      
      public 丨丨LLlI1 obtainUiObjectProxy(int param2Int) throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  84, 86, 0, 66, 89, 15, 81, 22, 5, 64, 
                  70, 72, 124, 118, 60, 115, 89, 20, 80, 118, 
                  5, 68, 95, 16, 80 }, "58d06f"));
          parcel2.writeInt(param2Int);
          if (!this.mRemote.transact(10, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null)
            return INXCoreNative.Stub.getDefaultImpl().obtainUiObjectProxy(param2Int); 
          parcel1.readException();
          return 丨丨LLlI1.IL1Iii.ILil(parcel1.readStrongBinder());
        } finally {
          parcel1.recycle();
          parcel2.recycle();
        } 
      }
      
      public 丨丨 obtainUiSelectorProxy(int param2Int) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  83, 93, 0, 17, 12, 12, 86, 29, 5, 19, 
                  19, 75, 123, 125, 60, 32, 12, 23, 87, 125, 
                  5, 23, 10, 19, 87 }, "23dcce"));
          parcel1.writeInt(param2Int);
          if (!this.mRemote.transact(9, parcel1, parcel2, 0) && INXCoreNative.Stub.getDefaultImpl() != null)
            return INXCoreNative.Stub.getDefaultImpl().obtainUiSelectorProxy(param2Int); 
          parcel2.readException();
          return 丨丨.IL1Iii.ILil(parcel2.readStrongBinder());
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public long openFile(String param2String, int param2Int) throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  85, 90, 80, 68, 93, 90, 80, 26, 85, 70, 
                  66, 29, 125, 122, 108, 117, 93, 65, 81, 122, 
                  85, 66, 91, 69, 81 }, "444623"));
          parcel2.writeString(param2String);
          parcel2.writeInt(param2Int);
          if (!this.mRemote.transact(1, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null)
            return INXCoreNative.Stub.getDefaultImpl().openFile(param2String, param2Int); 
          parcel1.readException();
          return parcel1.readLong();
        } finally {
          parcel1.recycle();
          parcel2.recycle();
        } 
      }
      
      public boolean openFileNew(String param2String1, String param2String2, INXOpenFdEvent param2INXOpenFdEvent) throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        boolean bool = false;
        try {
          IBinder iBinder;
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  81, 15, 82, 16, 87, 8, 84, 79, 87, 18, 
                  72, 79, 121, 47, 110, 33, 87, 19, 85, 47, 
                  87, 22, 81, 23, 85 }, "0a6b8a"));
          parcel2.writeString(param2String1);
          parcel2.writeString(param2String2);
          if (param2INXOpenFdEvent != null) {
            iBinder = param2INXOpenFdEvent.asBinder();
          } else {
            iBinder = null;
          } 
          parcel2.writeStrongBinder(iBinder);
          try {
            if (!this.mRemote.transact(5, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
              bool = INXCoreNative.Stub.getDefaultImpl().openFileNew(param2String1, param2String2, param2INXOpenFdEvent);
              parcel1.recycle();
              parcel2.recycle();
              return bool;
            } 
            parcel1.readException();
            int i = parcel1.readInt();
            if (i != 0)
              bool = true; 
            parcel1.recycle();
            parcel2.recycle();
            return bool;
          } finally {}
        } finally {}
        parcel1.recycle();
        parcel2.recycle();
        throw param2String1;
      }
      
      public long readFile(long param2Long, byte[] param2ArrayOfbyte) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  5, 11, 86, 23, 9, 13, 0, 75, 83, 21, 
                  22, 74, 45, 43, 106, 38, 9, 22, 1, 43, 
                  83, 17, 15, 18, 1 }, "de2efd"));
          parcel1.writeLong(param2Long);
          if (param2ArrayOfbyte == null) {
            parcel1.writeInt(-1);
          } else {
            parcel1.writeInt(param2ArrayOfbyte.length);
          } 
          try {
            if (!this.mRemote.transact(3, parcel1, parcel2, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
              param2Long = INXCoreNative.Stub.getDefaultImpl().readFile(param2Long, param2ArrayOfbyte);
              parcel2.recycle();
              parcel1.recycle();
              return param2Long;
            } 
            parcel2.readException();
            param2Long = parcel2.readLong();
            parcel2.readByteArray(param2ArrayOfbyte);
            parcel2.recycle();
            parcel1.recycle();
            return param2Long;
          } finally {}
        } finally {}
        parcel2.recycle();
        parcel1.recycle();
        throw param2ArrayOfbyte;
      }
      
      public String sendCmd(int param2Int1, int param2Int2, int param2Int3, String param2String) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  89, 92, 87, 69, 93, 12, 92, 28, 82, 71, 
                  66, 75, 113, 124, 107, 116, 93, 23, 93, 124, 
                  82, 67, 91, 19, 93 }, "82372e"));
          parcel1.writeInt(param2Int1);
          parcel1.writeInt(param2Int2);
          parcel1.writeInt(param2Int3);
          parcel1.writeString(param2String);
          if (!this.mRemote.transact(6, parcel1, parcel2, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
            param2String = INXCoreNative.Stub.getDefaultImpl().sendCmd(param2Int1, param2Int2, param2Int3, param2String);
            return param2String;
          } 
          parcel2.readException();
          param2String = parcel2.readString();
          return param2String;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public boolean sendKeyEvent(int param2Int1, int param2Int2) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        boolean bool = false;
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  3, 12, 2, 23, 91, 80, 6, 76, 7, 21, 
                  68, 23, 43, 44, 62, 38, 91, 75, 7, 44, 
                  7, 17, 93, 79, 7 }, "bbfe49"));
          parcel1.writeInt(param2Int1);
          parcel1.writeInt(param2Int2);
          if (!this.mRemote.transact(17, parcel1, parcel2, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
            bool = INXCoreNative.Stub.getDefaultImpl().sendKeyEvent(param2Int1, param2Int2);
            return bool;
          } 
          parcel2.readException();
          param2Int1 = parcel2.readInt();
          if (param2Int1 != 0)
            bool = true; 
          return bool;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public void sendLog(int param2Int, String param2String1, String param2String2, long param2Long, boolean param2Boolean) throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        boolean bool = true;
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  0, 90, 1, 69, 9, 15, 5, 26, 4, 71, 
                  22, 72, 40, 122, 61, 116, 9, 20, 4, 122, 
                  4, 67, 15, 16, 4 }, "a4e7ff"));
          parcel2.writeInt(param2Int);
          parcel2.writeString(param2String1);
          parcel2.writeString(param2String2);
          parcel2.writeLong(param2Long);
          if (!param2Boolean)
            bool = false; 
          parcel2.writeInt(bool);
          try {
            if (!this.mRemote.transact(8, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
              INXCoreNative.Stub.getDefaultImpl().sendLog(param2Int, param2String1, param2String2, param2Long, param2Boolean);
              parcel1.recycle();
              parcel2.recycle();
              return;
            } 
            parcel1.readException();
            parcel1.recycle();
            parcel2.recycle();
            return;
          } finally {}
        } finally {}
        parcel1.recycle();
        parcel2.recycle();
        throw param2String1;
      }
      
      public void sendTaskResultBundle(int param2Int, long param2Long, Bundle param2Bundle) throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  2, 8, 6, 66, 92, 80, 7, 72, 3, 64, 
                  67, 23, 42, 40, 58, 115, 92, 75, 6, 40, 
                  3, 68, 90, 79, 6 }, "cfb039"));
          parcel2.writeInt(param2Int);
          parcel2.writeLong(param2Long);
          if (param2Bundle != null) {
            parcel2.writeInt(1);
            param2Bundle.writeToParcel(parcel2, 0);
          } else {
            parcel2.writeInt(0);
          } 
          try {
            if (!this.mRemote.transact(7, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
              INXCoreNative.Stub.getDefaultImpl().sendTaskResultBundle(param2Int, param2Long, param2Bundle);
              parcel1.recycle();
              parcel2.recycle();
              return;
            } 
            parcel1.readException();
            parcel1.recycle();
            parcel2.recycle();
            return;
          } finally {}
        } finally {}
        parcel1.recycle();
        parcel2.recycle();
        throw param2Bundle;
      }
      
      public boolean sendTouchEvent(int param2Int1, int param2Int2, int param2Int3, int param2Int4) throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        boolean bool = false;
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  87, 95, 82, 67, 92, 8, 82, 31, 87, 65, 
                  67, 79, Byte.MAX_VALUE, Byte.MAX_VALUE, 110, 114, 92, 19, 83, Byte.MAX_VALUE, 
                  87, 69, 90, 23, 83 }, "61613a"));
          parcel2.writeInt(param2Int1);
          parcel2.writeInt(param2Int2);
          parcel2.writeInt(param2Int3);
          parcel2.writeInt(param2Int4);
          if (!this.mRemote.transact(16, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
            bool = INXCoreNative.Stub.getDefaultImpl().sendTouchEvent(param2Int1, param2Int2, param2Int3, param2Int4);
            return bool;
          } 
          parcel1.readException();
          param2Int1 = parcel1.readInt();
          if (param2Int1 != 0)
            bool = true; 
          return bool;
        } finally {
          parcel1.recycle();
          parcel2.recycle();
        } 
      }
      
      public void setTsEventListener(int param2Int, L11丨丨丨1 param2L11丨丨丨1) throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          IBinder iBinder;
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  7, 8, 7, 16, 11, 8, 2, 72, 2, 18, 
                  20, 79, 47, 40, 59, 33, 11, 19, 3, 40, 
                  2, 22, 13, 23, 3 }, "ffcbda"));
          parcel2.writeInt(param2Int);
          if (param2L11丨丨丨1 != null) {
            iBinder = param2L11丨丨丨1.asBinder();
          } else {
            iBinder = null;
          } 
          parcel2.writeStrongBinder(iBinder);
          try {
            if (!this.mRemote.transact(15, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
              INXCoreNative.Stub.getDefaultImpl().setTsEventListener(param2Int, param2L11丨丨丨1);
              parcel1.recycle();
              parcel2.recycle();
              return;
            } 
            parcel1.readException();
            parcel1.recycle();
            parcel2.recycle();
            return;
          } finally {}
        } finally {}
        parcel1.recycle();
        parcel2.recycle();
        throw param2L11丨丨丨1;
      }
      
      public long writeFile(long param2Long, byte[] param2ArrayOfbyte, int param2Int1, int param2Int2) throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  83, 90, 7, 16, 11, 90, 86, 26, 2, 18, 
                  20, 29, 123, 122, 59, 33, 11, 65, 87, 122, 
                  2, 22, 13, 69, 87 }, "24cbd3"));
          parcel2.writeLong(param2Long);
          parcel2.writeByteArray(param2ArrayOfbyte);
          parcel2.writeInt(param2Int1);
          parcel2.writeInt(param2Int2);
          try {
            if (!this.mRemote.transact(2, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
              param2Long = INXCoreNative.Stub.getDefaultImpl().writeFile(param2Long, param2ArrayOfbyte, param2Int1, param2Int2);
              parcel1.recycle();
              parcel2.recycle();
              return param2Long;
            } 
            parcel1.readException();
            param2Long = parcel1.readLong();
            parcel1.recycle();
            parcel2.recycle();
            return param2Long;
          } finally {}
        } finally {}
        parcel1.recycle();
        parcel2.recycle();
        throw param2ArrayOfbyte;
      }
    }
  }
  
  public static class Proxy implements INXCoreNative {
    public static INXCoreNative sDefaultImpl;
    
    private IBinder mRemote;
    
    public Proxy(IBinder param1IBinder) {
      this.mRemote = param1IBinder;
    }
    
    public IBinder asBinder() {
      return this.mRemote;
    }
    
    public void closeFile(long param1Long) throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                86, 89, 6, 71, 9, 95, 83, 25, 3, 69, 
                22, 24, 126, 121, 58, 118, 9, 68, 82, 121, 
                3, 65, 15, 64, 82 }, "77b5f6"));
        parcel2.writeLong(param1Long);
        if (!this.mRemote.transact(4, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
          INXCoreNative.Stub.getDefaultImpl().closeFile(param1Long);
          return;
        } 
        parcel1.readException();
        return;
      } finally {
        parcel1.recycle();
        parcel2.recycle();
      } 
    }
    
    public String getInterfaceDescriptor() {
      return IL1Iii.IL1Iii(new byte[] { 
            81, 88, 93, 22, 86, 91, 84, 24, 88, 20, 
            73, 28, 121, 120, 97, 39, 86, 64, 85, 120, 
            88, 16, 80, 68, 85 }, "069d92");
    }
    
    public I11L obtainAccessibityActionProxy() throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                82, 91, 84, 17, 95, 94, 87, 27, 81, 19, 
                64, 25, 122, 123, 104, 32, 95, 69, 86, 123, 
                81, 23, 89, 65, 86 }, "350c07"));
        if (!this.mRemote.transact(13, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null)
          return INXCoreNative.Stub.getDefaultImpl().obtainAccessibityActionProxy(); 
        parcel1.readException();
        return I11L.IL1Iii.ILil(parcel1.readStrongBinder());
      } finally {
        parcel1.recycle();
        parcel2.recycle();
      } 
    }
    
    public IAccessibServiceProxy obtainAccessibityService() throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                3, 88, 85, 22, 94, 8, 6, 24, 80, 20, 
                65, 79, 43, 120, 105, 39, 94, 19, 7, 120, 
                80, 16, 88, 23, 7 }, "b61d1a"));
        if (!this.mRemote.transact(14, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null)
          return INXCoreNative.Stub.getDefaultImpl().obtainAccessibityService(); 
        parcel1.readException();
        return IAccessibServiceProxy.Stub.asInterface(parcel1.readStrongBinder());
      } finally {
        parcel1.recycle();
        parcel2.recycle();
      } 
    }
    
    public Bitmap obtainBitmap(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5) throws RemoteException {
      Exception exception;
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                87, 94, 7, 16, 95, 94, 82, 30, 2, 18, 
                64, 25, Byte.MAX_VALUE, 126, 59, 33, 95, 69, 83, 126, 
                2, 22, 89, 65, 83 }, "60cb07"));
        parcel1.writeInt(param1Int1);
        parcel1.writeInt(param1Int2);
        parcel1.writeInt(param1Int3);
        parcel1.writeInt(param1Int4);
        parcel1.writeInt(param1Int5);
        try {
          Bitmap bitmap;
          if (!this.mRemote.transact(19, parcel1, parcel2, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
            bitmap = INXCoreNative.Stub.getDefaultImpl().obtainBitmap(param1Int1, param1Int2, param1Int3, param1Int4, param1Int5);
            parcel2.recycle();
            parcel1.recycle();
            return bitmap;
          } 
          parcel2.readException();
          if (parcel2.readInt() != 0) {
            bitmap = (Bitmap)Bitmap.CREATOR.createFromParcel(parcel2);
          } else {
            bitmap = null;
          } 
          parcel2.recycle();
          parcel1.recycle();
          return bitmap;
        } finally {}
      } finally {}
      parcel2.recycle();
      parcel1.recycle();
      throw exception;
    }
    
    public Bundle obtainData(int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5) throws RemoteException {
      Exception exception;
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                83, 15, 5, 16, 12, 94, 86, 79, 0, 18, 
                19, 25, 123, 47, 57, 33, 12, 69, 87, 47, 
                0, 22, 10, 65, 87 }, "2aabc7"));
        parcel1.writeInt(param1Int1);
        parcel1.writeInt(param1Int2);
        parcel1.writeInt(param1Int3);
        parcel1.writeInt(param1Int4);
        parcel1.writeInt(param1Int5);
        try {
          Bundle bundle;
          if (!this.mRemote.transact(18, parcel1, parcel2, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
            bundle = INXCoreNative.Stub.getDefaultImpl().obtainData(param1Int1, param1Int2, param1Int3, param1Int4, param1Int5);
            parcel2.recycle();
            parcel1.recycle();
            return bundle;
          } 
          parcel2.readException();
          if (parcel2.readInt() != 0) {
            bundle = (Bundle)Bundle.CREATOR.createFromParcel(parcel2);
          } else {
            bundle = null;
          } 
          parcel2.recycle();
          parcel1.recycle();
          return bundle;
        } finally {}
      } finally {}
      parcel2.recycle();
      parcel1.recycle();
      throw exception;
    }
    
    public llliI obtainDataTask() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                83, 90, 84, 65, 87, 11, 86, 26, 81, 67, 
                72, 76, 123, 122, 104, 112, 87, 16, 87, 122, 
                81, 71, 81, 20, 87 }, "24038b"));
        if (!this.mRemote.transact(11, parcel1, parcel2, 0) && INXCoreNative.Stub.getDefaultImpl() != null)
          return INXCoreNative.Stub.getDefaultImpl().obtainDataTask(); 
        parcel2.readException();
        return llliI.IL1Iii.ILil(parcel2.readStrongBinder());
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public INXCoreNative obtainScriptProxy(int param1Int) throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                85, 90, 86, 23, 89, 11, 80, 26, 83, 21, 
                70, 76, 125, 122, 106, 38, 89, 16, 81, 122, 
                83, 17, 95, 20, 81 }, "442e6b"));
        parcel2.writeInt(param1Int);
        if (!this.mRemote.transact(12, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null)
          return INXCoreNative.Stub.getDefaultImpl().obtainScriptProxy(param1Int); 
        parcel1.readException();
        return INXCoreNative.Stub.asInterface(parcel1.readStrongBinder());
      } finally {
        parcel1.recycle();
        parcel2.recycle();
      } 
    }
    
    public 丨丨LLlI1 obtainUiObjectProxy(int param1Int) throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                84, 86, 0, 66, 89, 15, 81, 22, 5, 64, 
                70, 72, 124, 118, 60, 115, 89, 20, 80, 118, 
                5, 68, 95, 16, 80 }, "58d06f"));
        parcel2.writeInt(param1Int);
        if (!this.mRemote.transact(10, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null)
          return INXCoreNative.Stub.getDefaultImpl().obtainUiObjectProxy(param1Int); 
        parcel1.readException();
        return 丨丨LLlI1.IL1Iii.ILil(parcel1.readStrongBinder());
      } finally {
        parcel1.recycle();
        parcel2.recycle();
      } 
    }
    
    public 丨丨 obtainUiSelectorProxy(int param1Int) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                83, 93, 0, 17, 12, 12, 86, 29, 5, 19, 
                19, 75, 123, 125, 60, 32, 12, 23, 87, 125, 
                5, 23, 10, 19, 87 }, "23dcce"));
        parcel1.writeInt(param1Int);
        if (!this.mRemote.transact(9, parcel1, parcel2, 0) && INXCoreNative.Stub.getDefaultImpl() != null)
          return INXCoreNative.Stub.getDefaultImpl().obtainUiSelectorProxy(param1Int); 
        parcel2.readException();
        return 丨丨.IL1Iii.ILil(parcel2.readStrongBinder());
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public long openFile(String param1String, int param1Int) throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                85, 90, 80, 68, 93, 90, 80, 26, 85, 70, 
                66, 29, 125, 122, 108, 117, 93, 65, 81, 122, 
                85, 66, 91, 69, 81 }, "444623"));
        parcel2.writeString(param1String);
        parcel2.writeInt(param1Int);
        if (!this.mRemote.transact(1, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null)
          return INXCoreNative.Stub.getDefaultImpl().openFile(param1String, param1Int); 
        parcel1.readException();
        return parcel1.readLong();
      } finally {
        parcel1.recycle();
        parcel2.recycle();
      } 
    }
    
    public boolean openFileNew(String param1String1, String param1String2, INXOpenFdEvent param1INXOpenFdEvent) throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      boolean bool = false;
      try {
        IBinder iBinder;
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                81, 15, 82, 16, 87, 8, 84, 79, 87, 18, 
                72, 79, 121, 47, 110, 33, 87, 19, 85, 47, 
                87, 22, 81, 23, 85 }, "0a6b8a"));
        parcel2.writeString(param1String1);
        parcel2.writeString(param1String2);
        if (param1INXOpenFdEvent != null) {
          iBinder = param1INXOpenFdEvent.asBinder();
        } else {
          iBinder = null;
        } 
        parcel2.writeStrongBinder(iBinder);
        try {
          if (!this.mRemote.transact(5, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
            bool = INXCoreNative.Stub.getDefaultImpl().openFileNew(param1String1, param1String2, param1INXOpenFdEvent);
            parcel1.recycle();
            parcel2.recycle();
            return bool;
          } 
          parcel1.readException();
          int i = parcel1.readInt();
          if (i != 0)
            bool = true; 
          parcel1.recycle();
          parcel2.recycle();
          return bool;
        } finally {}
      } finally {}
      parcel1.recycle();
      parcel2.recycle();
      throw param1String1;
    }
    
    public long readFile(long param1Long, byte[] param1ArrayOfbyte) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                5, 11, 86, 23, 9, 13, 0, 75, 83, 21, 
                22, 74, 45, 43, 106, 38, 9, 22, 1, 43, 
                83, 17, 15, 18, 1 }, "de2efd"));
        parcel1.writeLong(param1Long);
        if (param1ArrayOfbyte == null) {
          parcel1.writeInt(-1);
        } else {
          parcel1.writeInt(param1ArrayOfbyte.length);
        } 
        try {
          if (!this.mRemote.transact(3, parcel1, parcel2, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
            param1Long = INXCoreNative.Stub.getDefaultImpl().readFile(param1Long, param1ArrayOfbyte);
            parcel2.recycle();
            parcel1.recycle();
            return param1Long;
          } 
          parcel2.readException();
          param1Long = parcel2.readLong();
          parcel2.readByteArray(param1ArrayOfbyte);
          parcel2.recycle();
          parcel1.recycle();
          return param1Long;
        } finally {}
      } finally {}
      parcel2.recycle();
      parcel1.recycle();
      throw param1ArrayOfbyte;
    }
    
    public String sendCmd(int param1Int1, int param1Int2, int param1Int3, String param1String) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                89, 92, 87, 69, 93, 12, 92, 28, 82, 71, 
                66, 75, 113, 124, 107, 116, 93, 23, 93, 124, 
                82, 67, 91, 19, 93 }, "82372e"));
        parcel1.writeInt(param1Int1);
        parcel1.writeInt(param1Int2);
        parcel1.writeInt(param1Int3);
        parcel1.writeString(param1String);
        if (!this.mRemote.transact(6, parcel1, parcel2, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
          param1String = INXCoreNative.Stub.getDefaultImpl().sendCmd(param1Int1, param1Int2, param1Int3, param1String);
          return param1String;
        } 
        parcel2.readException();
        param1String = parcel2.readString();
        return param1String;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public boolean sendKeyEvent(int param1Int1, int param1Int2) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      boolean bool = false;
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                3, 12, 2, 23, 91, 80, 6, 76, 7, 21, 
                68, 23, 43, 44, 62, 38, 91, 75, 7, 44, 
                7, 17, 93, 79, 7 }, "bbfe49"));
        parcel1.writeInt(param1Int1);
        parcel1.writeInt(param1Int2);
        if (!this.mRemote.transact(17, parcel1, parcel2, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
          bool = INXCoreNative.Stub.getDefaultImpl().sendKeyEvent(param1Int1, param1Int2);
          return bool;
        } 
        parcel2.readException();
        param1Int1 = parcel2.readInt();
        if (param1Int1 != 0)
          bool = true; 
        return bool;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public void sendLog(int param1Int, String param1String1, String param1String2, long param1Long, boolean param1Boolean) throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      boolean bool = true;
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                0, 90, 1, 69, 9, 15, 5, 26, 4, 71, 
                22, 72, 40, 122, 61, 116, 9, 20, 4, 122, 
                4, 67, 15, 16, 4 }, "a4e7ff"));
        parcel2.writeInt(param1Int);
        parcel2.writeString(param1String1);
        parcel2.writeString(param1String2);
        parcel2.writeLong(param1Long);
        if (!param1Boolean)
          bool = false; 
        parcel2.writeInt(bool);
        try {
          if (!this.mRemote.transact(8, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
            INXCoreNative.Stub.getDefaultImpl().sendLog(param1Int, param1String1, param1String2, param1Long, param1Boolean);
            parcel1.recycle();
            parcel2.recycle();
            return;
          } 
          parcel1.readException();
          parcel1.recycle();
          parcel2.recycle();
          return;
        } finally {}
      } finally {}
      parcel1.recycle();
      parcel2.recycle();
      throw param1String1;
    }
    
    public void sendTaskResultBundle(int param1Int, long param1Long, Bundle param1Bundle) throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                2, 8, 6, 66, 92, 80, 7, 72, 3, 64, 
                67, 23, 42, 40, 58, 115, 92, 75, 6, 40, 
                3, 68, 90, 79, 6 }, "cfb039"));
        parcel2.writeInt(param1Int);
        parcel2.writeLong(param1Long);
        if (param1Bundle != null) {
          parcel2.writeInt(1);
          param1Bundle.writeToParcel(parcel2, 0);
        } else {
          parcel2.writeInt(0);
        } 
        try {
          if (!this.mRemote.transact(7, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
            INXCoreNative.Stub.getDefaultImpl().sendTaskResultBundle(param1Int, param1Long, param1Bundle);
            parcel1.recycle();
            parcel2.recycle();
            return;
          } 
          parcel1.readException();
          parcel1.recycle();
          parcel2.recycle();
          return;
        } finally {}
      } finally {}
      parcel1.recycle();
      parcel2.recycle();
      throw param1Bundle;
    }
    
    public boolean sendTouchEvent(int param1Int1, int param1Int2, int param1Int3, int param1Int4) throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      boolean bool = false;
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                87, 95, 82, 67, 92, 8, 82, 31, 87, 65, 
                67, 79, Byte.MAX_VALUE, Byte.MAX_VALUE, 110, 114, 92, 19, 83, Byte.MAX_VALUE, 
                87, 69, 90, 23, 83 }, "61613a"));
        parcel2.writeInt(param1Int1);
        parcel2.writeInt(param1Int2);
        parcel2.writeInt(param1Int3);
        parcel2.writeInt(param1Int4);
        if (!this.mRemote.transact(16, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
          bool = INXCoreNative.Stub.getDefaultImpl().sendTouchEvent(param1Int1, param1Int2, param1Int3, param1Int4);
          return bool;
        } 
        parcel1.readException();
        param1Int1 = parcel1.readInt();
        if (param1Int1 != 0)
          bool = true; 
        return bool;
      } finally {
        parcel1.recycle();
        parcel2.recycle();
      } 
    }
    
    public void setTsEventListener(int param1Int, L11丨丨丨1 param1L11丨丨丨1) throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        IBinder iBinder;
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                7, 8, 7, 16, 11, 8, 2, 72, 2, 18, 
                20, 79, 47, 40, 59, 33, 11, 19, 3, 40, 
                2, 22, 13, 23, 3 }, "ffcbda"));
        parcel2.writeInt(param1Int);
        if (param1L11丨丨丨1 != null) {
          iBinder = param1L11丨丨丨1.asBinder();
        } else {
          iBinder = null;
        } 
        parcel2.writeStrongBinder(iBinder);
        try {
          if (!this.mRemote.transact(15, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
            INXCoreNative.Stub.getDefaultImpl().setTsEventListener(param1Int, param1L11丨丨丨1);
            parcel1.recycle();
            parcel2.recycle();
            return;
          } 
          parcel1.readException();
          parcel1.recycle();
          parcel2.recycle();
          return;
        } finally {}
      } finally {}
      parcel1.recycle();
      parcel2.recycle();
      throw param1L11丨丨丨1;
    }
    
    public long writeFile(long param1Long, byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                83, 90, 7, 16, 11, 90, 86, 26, 2, 18, 
                20, 29, 123, 122, 59, 33, 11, 65, 87, 122, 
                2, 22, 13, 69, 87 }, "24cbd3"));
        parcel2.writeLong(param1Long);
        parcel2.writeByteArray(param1ArrayOfbyte);
        parcel2.writeInt(param1Int1);
        parcel2.writeInt(param1Int2);
        try {
          if (!this.mRemote.transact(2, parcel2, parcel1, 0) && INXCoreNative.Stub.getDefaultImpl() != null) {
            param1Long = INXCoreNative.Stub.getDefaultImpl().writeFile(param1Long, param1ArrayOfbyte, param1Int1, param1Int2);
            parcel1.recycle();
            parcel2.recycle();
            return param1Long;
          } 
          parcel1.readException();
          param1Long = parcel1.readLong();
          parcel1.recycle();
          parcel2.recycle();
          return param1Long;
        } finally {}
      } finally {}
      parcel1.recycle();
      parcel2.recycle();
      throw param1ArrayOfbyte;
    }
  }
}
