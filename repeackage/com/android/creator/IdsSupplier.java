package repeackage.com.android.creator;

import ILil.IL1Iii;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IdsSupplier extends IInterface {
  String getAAID(String paramString) throws RemoteException;
  
  String getOAID() throws RemoteException;
  
  String getUDID(String paramString) throws RemoteException;
  
  String getVAID() throws RemoteException;
  
  boolean isSupported() throws RemoteException;
  
  public static class Default implements IdsSupplier {
    public IBinder asBinder() {
      return null;
    }
    
    public String getAAID(String param1String) throws RemoteException {
      return null;
    }
    
    public String getOAID() throws RemoteException {
      return null;
    }
    
    public String getUDID(String param1String) throws RemoteException {
      return null;
    }
    
    public String getVAID() throws RemoteException {
      return null;
    }
    
    public boolean isSupported() throws RemoteException {
      return false;
    }
  }
  
  public static abstract class Stub extends Binder implements IdsSupplier {
    private static final String DESCRIPTOR = IL1Iii.IL1Iii(new byte[] { 
          2, 93, 11, 27, 2, 91, 5, 64, 9, 92, 
          7, 27, 2, 64, 3, 84, 23, 90, 19, 28, 
          47, 81, 16, 102, 20, 66, 22, 89, 10, 80, 
          19 }, "a2f5c5");
    
    public static final int TRANSACTION_getAAID = 5;
    
    public static final int TRANSACTION_getOAID = 3;
    
    public static final int TRANSACTION_getUDID = 2;
    
    public static final int TRANSACTION_getVAID = 4;
    
    public static final int TRANSACTION_isSupported = 1;
    
    public Stub() {
      attachInterface(this, IL1Iii.IL1Iii(new byte[] { 
              90, 89, 94, 72, 4, 11, 93, 68, 92, 15, 
              1, 75, 90, 68, 86, 7, 17, 10, 75, 24, 
              122, 2, 22, 54, 76, 70, 67, 10, 12, 0, 
              75 }, "963fee"));
    }
    
    public static IdsSupplier asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface(DESCRIPTOR);
      return (iInterface != null && iInterface instanceof IdsSupplier) ? (IdsSupplier)iInterface : new Proxy(param1IBinder);
    }
    
    public static IdsSupplier getDefaultImpl() {
      return Proxy.sDefaultImpl;
    }
    
    public static boolean setDefaultImpl(IdsSupplier param1IdsSupplier) {
      if (Proxy.sDefaultImpl == null) {
        if (param1IdsSupplier != null) {
          Proxy.sDefaultImpl = param1IdsSupplier;
          return true;
        } 
        return false;
      } 
      throw new IllegalStateException(IL1Iii.IL1Iii(new byte[] { 
              68, 81, 23, 39, 7, 5, 86, 65, 15, 23, 
              43, 14, 71, 88, 75, 74, 66, 0, 86, 88, 
              15, 6, 6, 67, 67, 67, 10, 0, 7 }, "74ccbc"));
    }
    
    public IBinder asBinder() {
      return (IBinder)this;
    }
    
    public boolean onTransact(int param1Int1, Parcel param1Parcel1, Parcel param1Parcel2, int param1Int2) throws RemoteException {
      String str1;
      String str2 = DESCRIPTOR;
      if (param1Int1 != 1) {
        if (param1Int1 != 2) {
          if (param1Int1 != 3) {
            if (param1Int1 != 4) {
              if (param1Int1 != 5) {
                if (param1Int1 != 1598968902)
                  return super.onTransact(param1Int1, param1Parcel1, param1Parcel2, param1Int2); 
                param1Parcel2.writeString(str2);
                return true;
              } 
              param1Parcel1.enforceInterface(str2);
              str1 = getAAID(param1Parcel1.readString());
              param1Parcel2.writeNoException();
              param1Parcel2.writeString(str1);
              return true;
            } 
            str1.enforceInterface(str2);
            str1 = getVAID();
            param1Parcel2.writeNoException();
            param1Parcel2.writeString(str1);
            return true;
          } 
          str1.enforceInterface(str2);
          str1 = getOAID();
          param1Parcel2.writeNoException();
          param1Parcel2.writeString(str1);
          return true;
        } 
        str1.enforceInterface(str2);
        str1 = getUDID(str1.readString());
        param1Parcel2.writeNoException();
        param1Parcel2.writeString(str1);
        return true;
      } 
      str1.enforceInterface(str2);
      boolean bool = isSupported();
      param1Parcel2.writeNoException();
      param1Parcel2.writeInt(bool);
      return true;
    }
    
    public static class Proxy implements IdsSupplier {
      public static IdsSupplier sDefaultImpl;
      
      private IBinder mRemote;
      
      public Proxy(IBinder param2IBinder) {
        this.mRemote = param2IBinder;
      }
      
      public IBinder asBinder() {
        return this.mRemote;
      }
      
      public String getAAID(String param2String) throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  86, 95, 11, 79, 81, 10, 81, 66, 9, 8, 
                  84, 74, 86, 66, 3, 0, 68, 11, 71, 30, 
                  47, 5, 67, 55, 64, 64, 22, 13, 89, 1, 
                  71 }, "50fa0d"));
          parcel2.writeString(param2String);
          if (!this.mRemote.transact(5, parcel2, parcel1, 0) && IdsSupplier.Stub.getDefaultImpl() != null) {
            param2String = IdsSupplier.Stub.getDefaultImpl().getAAID(param2String);
            return param2String;
          } 
          parcel1.readException();
          param2String = parcel1.readString();
          return param2String;
        } finally {
          parcel1.recycle();
          parcel2.recycle();
        } 
      }
      
      public String getInterfaceDescriptor() {
        return IL1Iii.IL1Iii(new byte[] { 
              6, 89, 90, 72, 4, 86, 1, 68, 88, 15, 
              1, 22, 6, 68, 82, 7, 17, 87, 23, 24, 
              126, 2, 22, 107, 16, 70, 71, 10, 12, 93, 
              23 }, "e67fe8");
      }
      
      public String getOAID() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  6, 10, 93, 30, 83, 12, 1, 23, 95, 89, 
                  86, 76, 6, 23, 85, 81, 70, 13, 23, 75, 
                  121, 84, 65, 49, 16, 21, 64, 92, 91, 7, 
                  23 }, "ee002b"));
          if (!this.mRemote.transact(3, parcel1, parcel2, 0) && IdsSupplier.Stub.getDefaultImpl() != null)
            return IdsSupplier.Stub.getDefaultImpl().getOAID(); 
          parcel2.readException();
          return parcel2.readString();
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public String getUDID(String param2String) throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  7, 91, 12, 79, 81, 94, 0, 70, 14, 8, 
                  84, 30, 7, 70, 4, 0, 68, 95, 22, 26, 
                  40, 5, 67, 99, 17, 68, 17, 13, 89, 85, 
                  22 }, "d4aa00"));
          parcel2.writeString(param2String);
          if (!this.mRemote.transact(2, parcel2, parcel1, 0) && IdsSupplier.Stub.getDefaultImpl() != null) {
            param2String = IdsSupplier.Stub.getDefaultImpl().getUDID(param2String);
            return param2String;
          } 
          parcel1.readException();
          param2String = parcel1.readString();
          return param2String;
        } finally {
          parcel1.recycle();
          parcel2.recycle();
        } 
      }
      
      public String getVAID() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  90, 13, 9, 30, 82, 89, 93, 16, 11, 89, 
                  87, 25, 90, 16, 1, 81, 71, 88, 75, 76, 
                  45, 84, 64, 100, 76, 18, 20, 92, 90, 82, 
                  75 }, "9bd037"));
          if (!this.mRemote.transact(4, parcel1, parcel2, 0) && IdsSupplier.Stub.getDefaultImpl() != null)
            return IdsSupplier.Stub.getDefaultImpl().getVAID(); 
          parcel2.readException();
          return parcel2.readString();
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public boolean isSupported() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        boolean bool = false;
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  85, 88, 92, 79, 84, 10, 82, 69, 94, 8, 
                  81, 74, 85, 69, 84, 0, 65, 11, 68, 25, 
                  120, 5, 70, 55, 67, 71, 65, 13, 92, 1, 
                  68 }, "671a5d"));
          if (!this.mRemote.transact(1, parcel1, parcel2, 0) && IdsSupplier.Stub.getDefaultImpl() != null) {
            bool = IdsSupplier.Stub.getDefaultImpl().isSupported();
            return bool;
          } 
          parcel2.readException();
          int i = parcel2.readInt();
          if (i != 0)
            bool = true; 
          return bool;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
    }
  }
  
  public static class Proxy implements IdsSupplier {
    public static IdsSupplier sDefaultImpl;
    
    private IBinder mRemote;
    
    public Proxy(IBinder param1IBinder) {
      this.mRemote = param1IBinder;
    }
    
    public IBinder asBinder() {
      return this.mRemote;
    }
    
    public String getAAID(String param1String) throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                86, 95, 11, 79, 81, 10, 81, 66, 9, 8, 
                84, 74, 86, 66, 3, 0, 68, 11, 71, 30, 
                47, 5, 67, 55, 64, 64, 22, 13, 89, 1, 
                71 }, "50fa0d"));
        parcel2.writeString(param1String);
        if (!this.mRemote.transact(5, parcel2, parcel1, 0) && IdsSupplier.Stub.getDefaultImpl() != null) {
          param1String = IdsSupplier.Stub.getDefaultImpl().getAAID(param1String);
          return param1String;
        } 
        parcel1.readException();
        param1String = parcel1.readString();
        return param1String;
      } finally {
        parcel1.recycle();
        parcel2.recycle();
      } 
    }
    
    public String getInterfaceDescriptor() {
      return IL1Iii.IL1Iii(new byte[] { 
            6, 89, 90, 72, 4, 86, 1, 68, 88, 15, 
            1, 22, 6, 68, 82, 7, 17, 87, 23, 24, 
            126, 2, 22, 107, 16, 70, 71, 10, 12, 93, 
            23 }, "e67fe8");
    }
    
    public String getOAID() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                6, 10, 93, 30, 83, 12, 1, 23, 95, 89, 
                86, 76, 6, 23, 85, 81, 70, 13, 23, 75, 
                121, 84, 65, 49, 16, 21, 64, 92, 91, 7, 
                23 }, "ee002b"));
        if (!this.mRemote.transact(3, parcel1, parcel2, 0) && IdsSupplier.Stub.getDefaultImpl() != null)
          return IdsSupplier.Stub.getDefaultImpl().getOAID(); 
        parcel2.readException();
        return parcel2.readString();
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public String getUDID(String param1String) throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                7, 91, 12, 79, 81, 94, 0, 70, 14, 8, 
                84, 30, 7, 70, 4, 0, 68, 95, 22, 26, 
                40, 5, 67, 99, 17, 68, 17, 13, 89, 85, 
                22 }, "d4aa00"));
        parcel2.writeString(param1String);
        if (!this.mRemote.transact(2, parcel2, parcel1, 0) && IdsSupplier.Stub.getDefaultImpl() != null) {
          param1String = IdsSupplier.Stub.getDefaultImpl().getUDID(param1String);
          return param1String;
        } 
        parcel1.readException();
        param1String = parcel1.readString();
        return param1String;
      } finally {
        parcel1.recycle();
        parcel2.recycle();
      } 
    }
    
    public String getVAID() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                90, 13, 9, 30, 82, 89, 93, 16, 11, 89, 
                87, 25, 90, 16, 1, 81, 71, 88, 75, 76, 
                45, 84, 64, 100, 76, 18, 20, 92, 90, 82, 
                75 }, "9bd037"));
        if (!this.mRemote.transact(4, parcel1, parcel2, 0) && IdsSupplier.Stub.getDefaultImpl() != null)
          return IdsSupplier.Stub.getDefaultImpl().getVAID(); 
        parcel2.readException();
        return parcel2.readString();
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public boolean isSupported() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      boolean bool = false;
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                85, 88, 92, 79, 84, 10, 82, 69, 94, 8, 
                81, 74, 85, 69, 84, 0, 65, 11, 68, 25, 
                120, 5, 70, 55, 67, 71, 65, 13, 92, 1, 
                68 }, "671a5d"));
        if (!this.mRemote.transact(1, parcel1, parcel2, 0) && IdsSupplier.Stub.getDefaultImpl() != null) {
          bool = IdsSupplier.Stub.getDefaultImpl().isSupported();
          return bool;
        } 
        parcel2.readException();
        int i = parcel2.readInt();
        if (i != 0)
          bool = true; 
        return bool;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
  }
}
