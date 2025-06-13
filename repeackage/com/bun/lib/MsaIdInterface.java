package repeackage.com.bun.lib;

import ILil.IL1Iii;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface MsaIdInterface extends IInterface {
  String getAAID() throws RemoteException;
  
  String getOAID() throws RemoteException;
  
  String getVAID() throws RemoteException;
  
  boolean isDataArrived() throws RemoteException;
  
  boolean isSupported() throws RemoteException;
  
  void shutDown() throws RemoteException;
  
  public static class Default implements MsaIdInterface {
    public IBinder asBinder() {
      return null;
    }
    
    public String getAAID() throws RemoteException {
      return null;
    }
    
    public String getOAID() throws RemoteException {
      return null;
    }
    
    public String getVAID() throws RemoteException {
      return null;
    }
    
    public boolean isDataArrived() throws RemoteException {
      return false;
    }
    
    public boolean isSupported() throws RemoteException {
      return false;
    }
    
    public void shutDown() throws RemoteException {}
  }
  
  public static abstract class Stub extends Binder implements MsaIdInterface {
    private static final String DESCRIPTOR = IL1Iii.IL1Iii(new byte[] { 
          87, 87, 84, 28, 83, 71, 90, 22, 85, 91, 
          83, 28, 121, 75, 88, 123, 85, 123, 90, 76, 
          92, 64, 87, 83, 87, 93 }, "489212");
    
    public static final int TRANSACTION_getAAID = 5;
    
    public static final int TRANSACTION_getOAID = 3;
    
    public static final int TRANSACTION_getVAID = 4;
    
    public static final int TRANSACTION_isDataArrived = 2;
    
    public static final int TRANSACTION_isSupported = 1;
    
    public static final int TRANSACTION_shutDown = 6;
    
    public Stub() {
      attachInterface(this, IL1Iii.IL1Iii(new byte[] { 
              2, 86, 9, 75, 84, 64, 15, 23, 8, 12, 
              84, 27, 44, 74, 5, 44, 82, 124, 15, 77, 
              1, 23, 80, 84, 2, 92 }, "a9de65"));
    }
    
    public static MsaIdInterface asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface(DESCRIPTOR);
      return (iInterface != null && iInterface instanceof MsaIdInterface) ? (MsaIdInterface)iInterface : new Proxy(param1IBinder);
    }
    
    public static MsaIdInterface getDefaultImpl() {
      return Proxy.sDefaultImpl;
    }
    
    public static boolean setDefaultImpl(MsaIdInterface param1MsaIdInterface) {
      if (Proxy.sDefaultImpl == null) {
        if (param1MsaIdInterface != null) {
          Proxy.sDefaultImpl = param1MsaIdInterface;
          return true;
        } 
        return false;
      } 
      throw new IllegalStateException(IL1Iii.IL1Iii(new byte[] { 
              64, 7, 71, 113, 6, 2, 82, 23, 95, 65, 
              42, 9, 67, 14, 27, 28, 67, 7, 82, 14, 
              95, 80, 7, 68, 71, 21, 90, 86, 6 }, "3b35cd"));
    }
    
    public IBinder asBinder() {
      return (IBinder)this;
    }
    
    public boolean onTransact(int param1Int1, Parcel param1Parcel1, Parcel param1Parcel2, int param1Int2) throws RemoteException {
      String str = DESCRIPTOR;
      if (param1Int1 != 1598968902) {
        String str1;
        switch (param1Int1) {
          default:
            return super.onTransact(param1Int1, param1Parcel1, param1Parcel2, param1Int2);
          case 6:
            param1Parcel1.enforceInterface(str);
            shutDown();
            param1Parcel2.writeNoException();
            return true;
          case 5:
            param1Parcel1.enforceInterface(str);
            str1 = getAAID();
            param1Parcel2.writeNoException();
            param1Parcel2.writeString(str1);
            return true;
          case 4:
            str1.enforceInterface(str);
            str1 = getVAID();
            param1Parcel2.writeNoException();
            param1Parcel2.writeString(str1);
            return true;
          case 3:
            str1.enforceInterface(str);
            str1 = getOAID();
            param1Parcel2.writeNoException();
            param1Parcel2.writeString(str1);
            return true;
          case 2:
            str1.enforceInterface(str);
            bool = isDataArrived();
            param1Parcel2.writeNoException();
            param1Parcel2.writeInt(bool);
            return true;
          case 1:
            break;
        } 
        str1.enforceInterface(str);
        boolean bool = isSupported();
        param1Parcel2.writeNoException();
        param1Parcel2.writeInt(bool);
        return true;
      } 
      param1Parcel2.writeString(str);
      return true;
    }
    
    public static class Proxy implements MsaIdInterface {
      public static MsaIdInterface sDefaultImpl;
      
      private IBinder mRemote;
      
      public Proxy(IBinder param2IBinder) {
        this.mRemote = param2IBinder;
      }
      
      public IBinder asBinder() {
        return this.mRemote;
      }
      
      public String getAAID() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  84, 92, 8, 74, 6, 65, 89, 29, 9, 13, 
                  6, 26, 122, 64, 4, 45, 0, 125, 89, 71, 
                  0, 22, 2, 85, 84, 86 }, "73edd4"));
          if (!this.mRemote.transact(5, parcel1, parcel2, 0) && MsaIdInterface.Stub.getDefaultImpl() != null)
            return MsaIdInterface.Stub.getDefaultImpl().getAAID(); 
          parcel2.readException();
          return parcel2.readString();
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public String getInterfaceDescriptor() {
        return IL1Iii.IL1Iii(new byte[] { 
              83, 90, 84, 30, 87, 17, 94, 27, 85, 89, 
              87, 74, 125, 70, 88, 121, 81, 45, 94, 65, 
              92, 66, 83, 5, 83, 80 }, "05905d");
      }
      
      public String getOAID() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  84, 93, 95, 27, 7, 17, 89, 28, 94, 92, 
                  7, 74, 122, 65, 83, 124, 1, 45, 89, 70, 
                  87, 71, 3, 5, 84, 87 }, "7225ed"));
          if (!this.mRemote.transact(3, parcel1, parcel2, 0) && MsaIdInterface.Stub.getDefaultImpl() != null)
            return MsaIdInterface.Stub.getDefaultImpl().getOAID(); 
          parcel2.readException();
          return parcel2.readString();
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public String getVAID() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  84, 91, 94, 23, 80, 69, 89, 26, 95, 80, 
                  80, 30, 122, 71, 82, 112, 86, 121, 89, 64, 
                  86, 75, 84, 81, 84, 81 }, "743920"));
          if (!this.mRemote.transact(4, parcel1, parcel2, 0) && MsaIdInterface.Stub.getDefaultImpl() != null)
            return MsaIdInterface.Stub.getDefaultImpl().getVAID(); 
          parcel2.readException();
          return parcel2.readString();
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public boolean isDataArrived() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        boolean bool = false;
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  91, 92, 8, 24, 1, 65, 86, 29, 9, 95, 
                  1, 26, 117, 64, 4, Byte.MAX_VALUE, 7, 125, 86, 71, 
                  0, 68, 5, 85, 91, 86 }, "83e6c4"));
          if (!this.mRemote.transact(2, parcel1, parcel2, 0) && MsaIdInterface.Stub.getDefaultImpl() != null) {
            bool = MsaIdInterface.Stub.getDefaultImpl().isDataArrived();
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
      
      public boolean isSupported() throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        boolean bool = false;
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  91, 13, 85, 77, 0, 19, 86, 76, 84, 10, 
                  0, 72, 117, 17, 89, 42, 6, 47, 86, 22, 
                  93, 17, 4, 7, 91, 7 }, "8b8cbf"));
          if (!this.mRemote.transact(1, parcel2, parcel1, 0) && MsaIdInterface.Stub.getDefaultImpl() != null) {
            bool = MsaIdInterface.Stub.getDefaultImpl().isSupported();
            return bool;
          } 
          parcel1.readException();
          int i = parcel1.readInt();
          if (i != 0)
            bool = true; 
          return bool;
        } finally {
          parcel1.recycle();
          parcel2.recycle();
        } 
      }
      
      public void shutDown() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  87, 13, 95, 25, 6, 64, 90, 76, 94, 94, 
                  6, 27, 121, 17, 83, 126, 0, 124, 90, 22, 
                  87, 69, 2, 84, 87, 7 }, "4b27d5"));
          if (!this.mRemote.transact(6, parcel1, parcel2, 0) && MsaIdInterface.Stub.getDefaultImpl() != null) {
            MsaIdInterface.Stub.getDefaultImpl().shutDown();
            return;
          } 
          parcel2.readException();
          return;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
    }
  }
  
  public static class Proxy implements MsaIdInterface {
    public static MsaIdInterface sDefaultImpl;
    
    private IBinder mRemote;
    
    public Proxy(IBinder param1IBinder) {
      this.mRemote = param1IBinder;
    }
    
    public IBinder asBinder() {
      return this.mRemote;
    }
    
    public String getAAID() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                84, 92, 8, 74, 6, 65, 89, 29, 9, 13, 
                6, 26, 122, 64, 4, 45, 0, 125, 89, 71, 
                0, 22, 2, 85, 84, 86 }, "73edd4"));
        if (!this.mRemote.transact(5, parcel1, parcel2, 0) && MsaIdInterface.Stub.getDefaultImpl() != null)
          return MsaIdInterface.Stub.getDefaultImpl().getAAID(); 
        parcel2.readException();
        return parcel2.readString();
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public String getInterfaceDescriptor() {
      return IL1Iii.IL1Iii(new byte[] { 
            83, 90, 84, 30, 87, 17, 94, 27, 85, 89, 
            87, 74, 125, 70, 88, 121, 81, 45, 94, 65, 
            92, 66, 83, 5, 83, 80 }, "05905d");
    }
    
    public String getOAID() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                84, 93, 95, 27, 7, 17, 89, 28, 94, 92, 
                7, 74, 122, 65, 83, 124, 1, 45, 89, 70, 
                87, 71, 3, 5, 84, 87 }, "7225ed"));
        if (!this.mRemote.transact(3, parcel1, parcel2, 0) && MsaIdInterface.Stub.getDefaultImpl() != null)
          return MsaIdInterface.Stub.getDefaultImpl().getOAID(); 
        parcel2.readException();
        return parcel2.readString();
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public String getVAID() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                84, 91, 94, 23, 80, 69, 89, 26, 95, 80, 
                80, 30, 122, 71, 82, 112, 86, 121, 89, 64, 
                86, 75, 84, 81, 84, 81 }, "743920"));
        if (!this.mRemote.transact(4, parcel1, parcel2, 0) && MsaIdInterface.Stub.getDefaultImpl() != null)
          return MsaIdInterface.Stub.getDefaultImpl().getVAID(); 
        parcel2.readException();
        return parcel2.readString();
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public boolean isDataArrived() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      boolean bool = false;
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                91, 92, 8, 24, 1, 65, 86, 29, 9, 95, 
                1, 26, 117, 64, 4, Byte.MAX_VALUE, 7, 125, 86, 71, 
                0, 68, 5, 85, 91, 86 }, "83e6c4"));
        if (!this.mRemote.transact(2, parcel1, parcel2, 0) && MsaIdInterface.Stub.getDefaultImpl() != null) {
          bool = MsaIdInterface.Stub.getDefaultImpl().isDataArrived();
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
    
    public boolean isSupported() throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      boolean bool = false;
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                91, 13, 85, 77, 0, 19, 86, 76, 84, 10, 
                0, 72, 117, 17, 89, 42, 6, 47, 86, 22, 
                93, 17, 4, 7, 91, 7 }, "8b8cbf"));
        if (!this.mRemote.transact(1, parcel2, parcel1, 0) && MsaIdInterface.Stub.getDefaultImpl() != null) {
          bool = MsaIdInterface.Stub.getDefaultImpl().isSupported();
          return bool;
        } 
        parcel1.readException();
        int i = parcel1.readInt();
        if (i != 0)
          bool = true; 
        return bool;
      } finally {
        parcel1.recycle();
        parcel2.recycle();
      } 
    }
    
    public void shutDown() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                87, 13, 95, 25, 6, 64, 90, 76, 94, 94, 
                6, 27, 121, 17, 83, 126, 0, 124, 90, 22, 
                87, 69, 2, 84, 87, 7 }, "4b27d5"));
        if (!this.mRemote.transact(6, parcel1, parcel2, 0) && MsaIdInterface.Stub.getDefaultImpl() != null) {
          MsaIdInterface.Stub.getDefaultImpl().shutDown();
          return;
        } 
        parcel2.readException();
        return;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
  }
}
