package repeackage.com.asus.msa.SupplementaryDID;

import ILil.IL1Iii;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IDidAidlInterface extends IInterface {
  String getAAID() throws RemoteException;
  
  String getOAID() throws RemoteException;
  
  String getUDID() throws RemoteException;
  
  String getVAID() throws RemoteException;
  
  boolean isSupport() throws RemoteException;
  
  public static class Default implements IDidAidlInterface {
    public IBinder asBinder() {
      return null;
    }
    
    public String getAAID() throws RemoteException {
      return null;
    }
    
    public String getOAID() throws RemoteException {
      return null;
    }
    
    public String getUDID() throws RemoteException {
      return null;
    }
    
    public String getVAID() throws RemoteException {
      return null;
    }
    
    public boolean isSupport() throws RemoteException {
      return false;
    }
  }
  
  public static abstract class Stub extends Binder implements IDidAidlInterface {
    private static final String DESCRIPTOR = IL1Iii.IL1Iii(new byte[] { 
          2, 95, 95, 79, 87, 23, 20, 67, 28, 12, 
          69, 5, 79, 99, 71, 17, 70, 8, 4, 93, 
          87, 15, 66, 5, 19, 73, 118, 40, 114, 74, 
          40, 116, 91, 5, 119, 13, 5, 92, 123, 15, 
          66, 1, 19, 86, 83, 2, 83 }, "a02a6d");
    
    public static final int TRANSACTION_getAAID = 5;
    
    public static final int TRANSACTION_getOAID = 3;
    
    public static final int TRANSACTION_getUDID = 2;
    
    public static final int TRANSACTION_getVAID = 4;
    
    public static final int TRANSACTION_isSupport = 1;
    
    public Stub() {
      attachInterface(this, IL1Iii.IL1Iii(new byte[] { 
              90, 11, 88, 27, 80, 22, 76, 23, 27, 88, 
              66, 4, 23, 55, 64, 69, 65, 9, 92, 9, 
              80, 91, 69, 4, 75, 29, 113, 124, 117, 75, 
              112, 32, 92, 81, 112, 12, 93, 8, 124, 91, 
              69, 0, 75, 2, 84, 86, 84 }, "9d551e"));
    }
    
    public static IDidAidlInterface asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface(DESCRIPTOR);
      return (iInterface != null && iInterface instanceof IDidAidlInterface) ? (IDidAidlInterface)iInterface : new Proxy(param1IBinder);
    }
    
    public static IDidAidlInterface getDefaultImpl() {
      return Proxy.sDefaultImpl;
    }
    
    public static boolean setDefaultImpl(IDidAidlInterface param1IDidAidlInterface) {
      if (Proxy.sDefaultImpl == null) {
        if (param1IDidAidlInterface != null) {
          Proxy.sDefaultImpl = param1IDidAidlInterface;
          return true;
        } 
        return false;
      } 
      throw new IllegalStateException(IL1Iii.IL1Iii(new byte[] { 
              69, 3, 76, 38, 84, 83, 87, 19, 84, 22, 
              120, 88, 70, 10, 16, 75, 17, 86, 87, 10, 
              84, 7, 85, 21, 66, 17, 81, 1, 84 }, "6f8b15"));
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
              str1 = getAAID();
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
        str1 = getUDID();
        param1Parcel2.writeNoException();
        param1Parcel2.writeString(str1);
        return true;
      } 
      str1.enforceInterface(str2);
      boolean bool = isSupport();
      param1Parcel2.writeNoException();
      param1Parcel2.writeInt(bool);
      return true;
    }
    
    public static class Proxy implements IDidAidlInterface {
      public static IDidAidlInterface sDefaultImpl;
      
      private IBinder mRemote;
      
      public Proxy(IBinder param2IBinder) {
        this.mRemote = param2IBinder;
      }
      
      public IBinder asBinder() {
        return this.mRemote;
      }
      
      public String getAAID() throws RemoteException {
        Exception exception;
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  1, 86, 9, 29, 3, 68, 23, 74, 74, 94, 
                  17, 86, 76, 106, 17, 67, 18, 91, 7, 84, 
                  1, 93, 22, 86, 16, 64, 32, 122, 38, 25, 
                  43, 125, 13, 87, 35, 94, 6, 85, 45, 93, 
                  22, 82, 16, 95, 5, 80, 7 }, "b9d3b7"));
          try {
            if (!this.mRemote.transact(5, parcel1, parcel2, 0) && IDidAidlInterface.Stub.getDefaultImpl() != null) {
              String str1 = IDidAidlInterface.Stub.getDefaultImpl().getAAID();
              parcel2.recycle();
              parcel1.recycle();
              return str1;
            } 
            parcel2.readException();
            String str = parcel2.readString();
            parcel2.recycle();
            parcel1.recycle();
            return str;
          } finally {}
        } finally {}
        parcel2.recycle();
        parcel1.recycle();
        throw exception;
      }
      
      public String getInterfaceDescriptor() {
        return IL1Iii.IL1Iii(new byte[] { 
              80, 12, 95, 24, 83, 71, 70, 16, 28, 91, 
              65, 85, 29, 48, 71, 70, 66, 88, 86, 14, 
              87, 88, 70, 85, 65, 26, 118, Byte.MAX_VALUE, 118, 26, 
              122, 39, 91, 82, 115, 93, 87, 15, 123, 88, 
              70, 81, 65, 5, 83, 85, 87 }, "3c2624");
      }
      
      public String getOAID() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  2, 90, 89, 28, 89, 70, 20, 70, 26, 95, 
                  75, 84, 79, 102, 65, 66, 72, 89, 4, 88, 
                  81, 92, 76, 84, 19, 76, 112, 123, 124, 27, 
                  40, 113, 93, 86, 121, 92, 5, 89, 125, 92, 
                  76, 80, 19, 83, 85, 81, 93 }, "a54285"));
          if (!this.mRemote.transact(3, parcel1, parcel2, 0) && IDidAidlInterface.Stub.getDefaultImpl() != null)
            return IDidAidlInterface.Stub.getDefaultImpl().getOAID(); 
          parcel2.readException();
          return parcel2.readString();
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public String getUDID() throws RemoteException {
        Exception exception;
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  87, 92, 11, 26, 89, 16, 65, 64, 72, 89, 
                  75, 2, 26, 96, 19, 68, 72, 15, 81, 94, 
                  3, 90, 76, 2, 70, 74, 34, 125, 124, 77, 
                  125, 119, 15, 80, 121, 10, 80, 95, 47, 90, 
                  76, 6, 70, 85, 7, 87, 93 }, "43f48c"));
          try {
            if (!this.mRemote.transact(2, parcel1, parcel2, 0) && IDidAidlInterface.Stub.getDefaultImpl() != null) {
              String str1 = IDidAidlInterface.Stub.getDefaultImpl().getUDID();
              parcel2.recycle();
              parcel1.recycle();
              return str1;
            } 
            parcel2.readException();
            String str = parcel2.readString();
            parcel2.recycle();
            parcel1.recycle();
            return str;
          } finally {}
        } finally {}
        parcel2.recycle();
        parcel1.recycle();
        throw exception;
      }
      
      public String getVAID() throws RemoteException {
        Exception exception;
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  7, 93, 12, 72, 80, 68, 17, 65, 79, 11, 
                  66, 86, 74, 97, 20, 22, 65, 91, 1, 95, 
                  4, 8, 69, 86, 22, 75, 37, 47, 117, 25, 
                  45, 118, 8, 2, 112, 94, 0, 94, 40, 8, 
                  69, 82, 22, 84, 0, 5, 84 }, "d2af17"));
          try {
            if (!this.mRemote.transact(4, parcel2, parcel1, 0) && IDidAidlInterface.Stub.getDefaultImpl() != null) {
              String str1 = IDidAidlInterface.Stub.getDefaultImpl().getVAID();
              parcel1.recycle();
              parcel2.recycle();
              return str1;
            } 
            parcel1.readException();
            String str = parcel1.readString();
            parcel1.recycle();
            parcel2.recycle();
            return str;
          } finally {}
        } finally {}
        parcel1.recycle();
        parcel2.recycle();
        throw exception;
      }
      
      public boolean isSupport() throws RemoteException {
        Exception exception;
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        boolean bool = false;
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  5, 91, 94, 25, 2, 66, 19, 71, 29, 90, 
                  16, 80, 72, 103, 70, 71, 19, 93, 3, 89, 
                  86, 89, 23, 80, 20, 77, 119, 126, 39, 31, 
                  47, 112, 90, 83, 34, 88, 2, 88, 122, 89, 
                  23, 84, 20, 82, 82, 84, 6 }, "f437c1"));
          try {
            if (!this.mRemote.transact(1, parcel2, parcel1, 0) && IDidAidlInterface.Stub.getDefaultImpl() != null) {
              bool = IDidAidlInterface.Stub.getDefaultImpl().isSupport();
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
        throw exception;
      }
    }
  }
  
  public static class Proxy implements IDidAidlInterface {
    public static IDidAidlInterface sDefaultImpl;
    
    private IBinder mRemote;
    
    public Proxy(IBinder param1IBinder) {
      this.mRemote = param1IBinder;
    }
    
    public IBinder asBinder() {
      return this.mRemote;
    }
    
    public String getAAID() throws RemoteException {
      Exception exception;
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                1, 86, 9, 29, 3, 68, 23, 74, 74, 94, 
                17, 86, 76, 106, 17, 67, 18, 91, 7, 84, 
                1, 93, 22, 86, 16, 64, 32, 122, 38, 25, 
                43, 125, 13, 87, 35, 94, 6, 85, 45, 93, 
                22, 82, 16, 95, 5, 80, 7 }, "b9d3b7"));
        try {
          if (!this.mRemote.transact(5, parcel1, parcel2, 0) && IDidAidlInterface.Stub.getDefaultImpl() != null) {
            String str1 = IDidAidlInterface.Stub.getDefaultImpl().getAAID();
            parcel2.recycle();
            parcel1.recycle();
            return str1;
          } 
          parcel2.readException();
          String str = parcel2.readString();
          parcel2.recycle();
          parcel1.recycle();
          return str;
        } finally {}
      } finally {}
      parcel2.recycle();
      parcel1.recycle();
      throw exception;
    }
    
    public String getInterfaceDescriptor() {
      return IL1Iii.IL1Iii(new byte[] { 
            80, 12, 95, 24, 83, 71, 70, 16, 28, 91, 
            65, 85, 29, 48, 71, 70, 66, 88, 86, 14, 
            87, 88, 70, 85, 65, 26, 118, Byte.MAX_VALUE, 118, 26, 
            122, 39, 91, 82, 115, 93, 87, 15, 123, 88, 
            70, 81, 65, 5, 83, 85, 87 }, "3c2624");
    }
    
    public String getOAID() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                2, 90, 89, 28, 89, 70, 20, 70, 26, 95, 
                75, 84, 79, 102, 65, 66, 72, 89, 4, 88, 
                81, 92, 76, 84, 19, 76, 112, 123, 124, 27, 
                40, 113, 93, 86, 121, 92, 5, 89, 125, 92, 
                76, 80, 19, 83, 85, 81, 93 }, "a54285"));
        if (!this.mRemote.transact(3, parcel1, parcel2, 0) && IDidAidlInterface.Stub.getDefaultImpl() != null)
          return IDidAidlInterface.Stub.getDefaultImpl().getOAID(); 
        parcel2.readException();
        return parcel2.readString();
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public String getUDID() throws RemoteException {
      Exception exception;
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                87, 92, 11, 26, 89, 16, 65, 64, 72, 89, 
                75, 2, 26, 96, 19, 68, 72, 15, 81, 94, 
                3, 90, 76, 2, 70, 74, 34, 125, 124, 77, 
                125, 119, 15, 80, 121, 10, 80, 95, 47, 90, 
                76, 6, 70, 85, 7, 87, 93 }, "43f48c"));
        try {
          if (!this.mRemote.transact(2, parcel1, parcel2, 0) && IDidAidlInterface.Stub.getDefaultImpl() != null) {
            String str1 = IDidAidlInterface.Stub.getDefaultImpl().getUDID();
            parcel2.recycle();
            parcel1.recycle();
            return str1;
          } 
          parcel2.readException();
          String str = parcel2.readString();
          parcel2.recycle();
          parcel1.recycle();
          return str;
        } finally {}
      } finally {}
      parcel2.recycle();
      parcel1.recycle();
      throw exception;
    }
    
    public String getVAID() throws RemoteException {
      Exception exception;
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                7, 93, 12, 72, 80, 68, 17, 65, 79, 11, 
                66, 86, 74, 97, 20, 22, 65, 91, 1, 95, 
                4, 8, 69, 86, 22, 75, 37, 47, 117, 25, 
                45, 118, 8, 2, 112, 94, 0, 94, 40, 8, 
                69, 82, 22, 84, 0, 5, 84 }, "d2af17"));
        try {
          if (!this.mRemote.transact(4, parcel2, parcel1, 0) && IDidAidlInterface.Stub.getDefaultImpl() != null) {
            String str1 = IDidAidlInterface.Stub.getDefaultImpl().getVAID();
            parcel1.recycle();
            parcel2.recycle();
            return str1;
          } 
          parcel1.readException();
          String str = parcel1.readString();
          parcel1.recycle();
          parcel2.recycle();
          return str;
        } finally {}
      } finally {}
      parcel1.recycle();
      parcel2.recycle();
      throw exception;
    }
    
    public boolean isSupport() throws RemoteException {
      Exception exception;
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      boolean bool = false;
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                5, 91, 94, 25, 2, 66, 19, 71, 29, 90, 
                16, 80, 72, 103, 70, 71, 19, 93, 3, 89, 
                86, 89, 23, 80, 20, 77, 119, 126, 39, 31, 
                47, 112, 90, 83, 34, 88, 2, 88, 122, 89, 
                23, 84, 20, 82, 82, 84, 6 }, "f437c1"));
        try {
          if (!this.mRemote.transact(1, parcel2, parcel1, 0) && IDidAidlInterface.Stub.getDefaultImpl() != null) {
            bool = IDidAidlInterface.Stub.getDefaultImpl().isSupport();
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
      throw exception;
    }
  }
}
