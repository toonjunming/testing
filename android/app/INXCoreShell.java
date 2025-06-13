package android.app;

import ILil.IL1Iii;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

public interface INXCoreShell extends IInterface {
  ParcelFileDescriptor openFileNew(String paramString1, String paramString2) throws RemoteException;
  
  String sendCmd(int paramInt1, int paramInt2, int paramInt3, String paramString) throws RemoteException;
  
  public static class Default implements INXCoreShell {
    public IBinder asBinder() {
      return null;
    }
    
    public ParcelFileDescriptor openFileNew(String param1String1, String param1String2) throws RemoteException {
      return null;
    }
    
    public String sendCmd(int param1Int1, int param1Int2, int param1Int3, String param1String) throws RemoteException {
      return null;
    }
  }
  
  public static abstract class Stub extends Binder implements INXCoreShell {
    private static final String DESCRIPTOR = IL1Iii.IL1Iii(new byte[] { 
          82, 90, 84, 69, 9, 90, 87, 26, 81, 71, 
          22, 29, 122, 122, 104, 116, 9, 65, 86, 103, 
          88, 82, 10, 95 }, "3407f3");
    
    public static final int TRANSACTION_openFileNew = 1;
    
    public static final int TRANSACTION_sendCmd = 2;
    
    public Stub() {
      attachInterface(this, IL1Iii.IL1Iii(new byte[] { 
              2, 92, 84, 23, 86, 95, 7, 28, 81, 21, 
              73, 24, 42, 124, 104, 38, 86, 68, 6, 97, 
              88, 0, 85, 90 }, "c20e96"));
    }
    
    public static INXCoreShell asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface(DESCRIPTOR);
      return (iInterface != null && iInterface instanceof INXCoreShell) ? (INXCoreShell)iInterface : new Proxy(param1IBinder);
    }
    
    public static INXCoreShell getDefaultImpl() {
      return Proxy.sDefaultImpl;
    }
    
    public static boolean setDefaultImpl(INXCoreShell param1INXCoreShell) {
      if (Proxy.sDefaultImpl == null) {
        if (param1INXCoreShell != null) {
          Proxy.sDefaultImpl = param1INXCoreShell;
          return true;
        } 
        return false;
      } 
      throw new IllegalStateException(IL1Iii.IL1Iii(new byte[] { 
              66, 86, 21, 37, 84, 83, 80, 70, 13, 21, 
              120, 88, 65, 95, 73, 72, 17, 86, 80, 95, 
              13, 4, 85, 21, 69, 68, 8, 2, 84 }, "13aa15"));
    }
    
    public IBinder asBinder() {
      return (IBinder)this;
    }
    
    public boolean onTransact(int param1Int1, Parcel param1Parcel1, Parcel param1Parcel2, int param1Int2) throws RemoteException {
      String str1;
      String str2 = DESCRIPTOR;
      if (param1Int1 != 1) {
        if (param1Int1 != 2) {
          if (param1Int1 != 1598968902)
            return super.onTransact(param1Int1, param1Parcel1, param1Parcel2, param1Int2); 
          param1Parcel2.writeString(str2);
          return true;
        } 
        param1Parcel1.enforceInterface(str2);
        str1 = sendCmd(param1Parcel1.readInt(), param1Parcel1.readInt(), param1Parcel1.readInt(), param1Parcel1.readString());
        param1Parcel2.writeNoException();
        param1Parcel2.writeString(str1);
        return true;
      } 
      str1.enforceInterface(str2);
      ParcelFileDescriptor parcelFileDescriptor = openFileNew(str1.readString(), str1.readString());
      param1Parcel2.writeNoException();
      if (parcelFileDescriptor != null) {
        param1Parcel2.writeInt(1);
        parcelFileDescriptor.writeToParcel(param1Parcel2, 1);
      } else {
        param1Parcel2.writeInt(0);
      } 
      return true;
    }
    
    public static class Proxy implements INXCoreShell {
      public static INXCoreShell sDefaultImpl;
      
      private IBinder mRemote;
      
      public Proxy(IBinder param2IBinder) {
        this.mRemote = param2IBinder;
      }
      
      public IBinder asBinder() {
        return this.mRemote;
      }
      
      public String getInterfaceDescriptor() {
        return IL1Iii.IL1Iii(new byte[] { 
              88, 12, 81, 20, 11, 15, 93, 76, 84, 22, 
              20, 72, 112, 44, 109, 37, 11, 20, 92, 49, 
              93, 3, 8, 10 }, "9b5fdf");
      }
      
      public ParcelFileDescriptor openFileNew(String param2String1, String param2String2) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  3, 88, 81, 66, 11, 10, 6, 24, 84, 64, 
                  20, 77, 43, 120, 109, 115, 11, 17, 7, 101, 
                  93, 85, 8, 15 }, "b650dc"));
          parcel1.writeString(param2String1);
          parcel1.writeString(param2String2);
          if (!this.mRemote.transact(1, parcel1, parcel2, 0) && INXCoreShell.Stub.getDefaultImpl() != null)
            return INXCoreShell.Stub.getDefaultImpl().openFileNew(param2String1, param2String2); 
          parcel2.readException();
          if (parcel2.readInt() != 0) {
            ParcelFileDescriptor parcelFileDescriptor = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(parcel2);
          } else {
            param2String1 = null;
          } 
          return (ParcelFileDescriptor)param2String1;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public String sendCmd(int param2Int1, int param2Int2, int param2Int3, String param2String) throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  2, 86, 92, 70, 88, 95, 7, 22, 89, 68, 
                  71, 24, 42, 118, 96, 119, 88, 68, 6, 107, 
                  80, 81, 91, 90 }, "c88476"));
          parcel2.writeInt(param2Int1);
          parcel2.writeInt(param2Int2);
          parcel2.writeInt(param2Int3);
          parcel2.writeString(param2String);
          try {
            if (!this.mRemote.transact(2, parcel2, parcel1, 0) && INXCoreShell.Stub.getDefaultImpl() != null) {
              param2String = INXCoreShell.Stub.getDefaultImpl().sendCmd(param2Int1, param2Int2, param2Int3, param2String);
              parcel1.recycle();
              parcel2.recycle();
              return param2String;
            } 
            parcel1.readException();
            param2String = parcel1.readString();
            parcel1.recycle();
            parcel2.recycle();
            return param2String;
          } finally {}
        } finally {}
        parcel1.recycle();
        parcel2.recycle();
        throw param2String;
      }
    }
  }
  
  public static class Proxy implements INXCoreShell {
    public static INXCoreShell sDefaultImpl;
    
    private IBinder mRemote;
    
    public Proxy(IBinder param1IBinder) {
      this.mRemote = param1IBinder;
    }
    
    public IBinder asBinder() {
      return this.mRemote;
    }
    
    public String getInterfaceDescriptor() {
      return IL1Iii.IL1Iii(new byte[] { 
            88, 12, 81, 20, 11, 15, 93, 76, 84, 22, 
            20, 72, 112, 44, 109, 37, 11, 20, 92, 49, 
            93, 3, 8, 10 }, "9b5fdf");
    }
    
    public ParcelFileDescriptor openFileNew(String param1String1, String param1String2) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                3, 88, 81, 66, 11, 10, 6, 24, 84, 64, 
                20, 77, 43, 120, 109, 115, 11, 17, 7, 101, 
                93, 85, 8, 15 }, "b650dc"));
        parcel1.writeString(param1String1);
        parcel1.writeString(param1String2);
        if (!this.mRemote.transact(1, parcel1, parcel2, 0) && INXCoreShell.Stub.getDefaultImpl() != null)
          return INXCoreShell.Stub.getDefaultImpl().openFileNew(param1String1, param1String2); 
        parcel2.readException();
        if (parcel2.readInt() != 0) {
          ParcelFileDescriptor parcelFileDescriptor = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(parcel2);
        } else {
          param1String1 = null;
        } 
        return (ParcelFileDescriptor)param1String1;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public String sendCmd(int param1Int1, int param1Int2, int param1Int3, String param1String) throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                2, 86, 92, 70, 88, 95, 7, 22, 89, 68, 
                71, 24, 42, 118, 96, 119, 88, 68, 6, 107, 
                80, 81, 91, 90 }, "c88476"));
        parcel2.writeInt(param1Int1);
        parcel2.writeInt(param1Int2);
        parcel2.writeInt(param1Int3);
        parcel2.writeString(param1String);
        try {
          if (!this.mRemote.transact(2, parcel2, parcel1, 0) && INXCoreShell.Stub.getDefaultImpl() != null) {
            param1String = INXCoreShell.Stub.getDefaultImpl().sendCmd(param1Int1, param1Int2, param1Int3, param1String);
            parcel1.recycle();
            parcel2.recycle();
            return param1String;
          } 
          parcel1.readException();
          param1String = parcel1.readString();
          parcel1.recycle();
          parcel2.recycle();
          return param1String;
        } finally {}
      } finally {}
      parcel1.recycle();
      parcel2.recycle();
      throw param1String;
    }
  }
}
