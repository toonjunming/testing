package repeackage.com.samsung.android.deviceidservice;

import ILil.IL1Iii;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IDeviceIdService extends IInterface {
  String getAAID(String paramString) throws RemoteException;
  
  String getOAID() throws RemoteException;
  
  String getVAID(String paramString) throws RemoteException;
  
  public static class Default implements IDeviceIdService {
    public IBinder asBinder() {
      return null;
    }
    
    public String getAAID(String param1String) throws RemoteException {
      return null;
    }
    
    public String getOAID() throws RemoteException {
      return null;
    }
    
    public String getVAID(String param1String) throws RemoteException {
      return null;
    }
  }
  
  public static abstract class Stub extends Binder implements IDeviceIdService {
    private static final String DESCRIPTOR = IL1Iii.IL1Iii(new byte[] { 
          5, 86, 85, 25, 22, 3, 11, 74, 77, 89, 
          2, 76, 7, 87, 92, 69, 10, 11, 2, 23, 
          92, 82, 19, 11, 5, 92, 81, 83, 22, 7, 
          20, 79, 81, 84, 0, 76, 47, 125, 93, 65, 
          12, 1, 3, 112, 92, 100, 0, 16, 16, 80, 
          91, 82 }, "f987eb");
    
    public static final int TRANSACTION_getAAID = 3;
    
    public static final int TRANSACTION_getOAID = 1;
    
    public static final int TRANSACTION_getVAID = 2;
    
    public Stub() {
      attachInterface(this, IL1Iii.IL1Iii(new byte[] { 
              1, 87, 12, 25, 75, 86, 15, 75, 20, 89, 
              95, 25, 3, 86, 5, 69, 87, 94, 6, 22, 
              5, 82, 78, 94, 1, 93, 8, 83, 75, 82, 
              16, 78, 8, 84, 93, 25, 43, 124, 4, 65, 
              81, 84, 7, 113, 5, 100, 93, 69, 20, 81, 
              2, 82 }, "b8a787"));
    }
    
    public static IDeviceIdService asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface(DESCRIPTOR);
      return (iInterface != null && iInterface instanceof IDeviceIdService) ? (IDeviceIdService)iInterface : new Proxy(param1IBinder);
    }
    
    public static IDeviceIdService getDefaultImpl() {
      return Proxy.sDefaultImpl;
    }
    
    public static boolean setDefaultImpl(IDeviceIdService param1IDeviceIdService) {
      if (Proxy.sDefaultImpl == null) {
        if (param1IDeviceIdService != null) {
          Proxy.sDefaultImpl = param1IDeviceIdService;
          return true;
        } 
        return false;
      } 
      throw new IllegalStateException(IL1Iii.IL1Iii(new byte[] { 
              66, 81, 23, 37, 86, 84, 80, 65, 15, 21, 
              122, 95, 65, 88, 75, 72, 19, 81, 80, 88, 
              15, 4, 87, 18, 69, 67, 10, 2, 86 }, "14ca32"));
    }
    
    public IBinder asBinder() {
      return (IBinder)this;
    }
    
    public boolean onTransact(int param1Int1, Parcel param1Parcel1, Parcel param1Parcel2, int param1Int2) throws RemoteException {
      String str2 = DESCRIPTOR;
      if (param1Int1 != 1) {
        if (param1Int1 != 2) {
          if (param1Int1 != 3) {
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
        str1 = getVAID(str1.readString());
        param1Parcel2.writeNoException();
        param1Parcel2.writeString(str1);
        return true;
      } 
      str1.enforceInterface(str2);
      String str1 = getOAID();
      param1Parcel2.writeNoException();
      param1Parcel2.writeString(str1);
      return true;
    }
    
    public static class Proxy implements IDeviceIdService {
      public static IDeviceIdService sDefaultImpl;
      
      private IBinder mRemote;
      
      public Proxy(IBinder param2IBinder) {
        this.mRemote = param2IBinder;
      }
      
      public IBinder asBinder() {
        return this.mRemote;
      }
      
      public String getAAID(String param2String) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  87, 94, 8, 24, 67, 85, 89, 66, 16, 88, 
                  87, 26, 85, 95, 1, 68, 95, 93, 80, 31, 
                  1, 83, 70, 93, 87, 84, 12, 82, 67, 81, 
                  70, 71, 12, 85, 85, 26, 125, 117, 0, 64, 
                  89, 87, 81, 120, 1, 101, 85, 70, 66, 88, 
                  6, 83 }, "41e604"));
          parcel1.writeString(param2String);
          try {
            if (!this.mRemote.transact(3, parcel1, parcel2, 0) && IDeviceIdService.Stub.getDefaultImpl() != null) {
              param2String = IDeviceIdService.Stub.getDefaultImpl().getAAID(param2String);
              parcel2.recycle();
              parcel1.recycle();
              return param2String;
            } 
            parcel2.readException();
            param2String = parcel2.readString();
            parcel2.recycle();
            parcel1.recycle();
            return param2String;
          } finally {}
        } finally {}
        parcel2.recycle();
        parcel1.recycle();
        throw param2String;
      }
      
      public String getInterfaceDescriptor() {
        return IL1Iii.IL1Iii(new byte[] { 
              1, 9, 9, 25, 64, 82, 15, 21, 17, 89, 
              84, 29, 3, 8, 0, 69, 92, 90, 6, 72, 
              0, 82, 69, 90, 1, 3, 13, 83, 64, 86, 
              16, 16, 13, 84, 86, 29, 43, 34, 1, 65, 
              90, 80, 7, 47, 0, 100, 86, 65, 20, 15, 
              7, 82 }, "bfd733");
      }
      
      public String getOAID() throws RemoteException {
        Exception exception;
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  7, 90, 93, 74, 21, 83, 9, 70, 69, 10, 
                  1, 28, 5, 91, 84, 22, 9, 91, 0, 27, 
                  84, 1, 16, 91, 7, 80, 89, 0, 21, 87, 
                  22, 67, 89, 7, 3, 28, 45, 113, 85, 18, 
                  15, 81, 1, 124, 84, 55, 3, 64, 18, 92, 
                  83, 1 }, "d50df2"));
          try {
            if (!this.mRemote.transact(1, parcel2, parcel1, 0) && IDeviceIdService.Stub.getDefaultImpl() != null) {
              String str1 = IDeviceIdService.Stub.getDefaultImpl().getOAID();
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
      
      public String getVAID(String param2String) throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  2, 92, 12, 29, 67, 4, 12, 64, 20, 93, 
                  87, 75, 0, 93, 5, 65, 95, 12, 5, 29, 
                  5, 86, 70, 12, 2, 86, 8, 87, 67, 0, 
                  19, 69, 8, 80, 85, 75, 40, 119, 4, 69, 
                  89, 6, 4, 122, 5, 96, 85, 23, 23, 90, 
                  2, 86 }, "a3a30e"));
          parcel2.writeString(param2String);
          try {
            if (!this.mRemote.transact(2, parcel2, parcel1, 0) && IDeviceIdService.Stub.getDefaultImpl() != null) {
              param2String = IDeviceIdService.Stub.getDefaultImpl().getVAID(param2String);
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
  
  public static class Proxy implements IDeviceIdService {
    public static IDeviceIdService sDefaultImpl;
    
    private IBinder mRemote;
    
    public Proxy(IBinder param1IBinder) {
      this.mRemote = param1IBinder;
    }
    
    public IBinder asBinder() {
      return this.mRemote;
    }
    
    public String getAAID(String param1String) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                87, 94, 8, 24, 67, 85, 89, 66, 16, 88, 
                87, 26, 85, 95, 1, 68, 95, 93, 80, 31, 
                1, 83, 70, 93, 87, 84, 12, 82, 67, 81, 
                70, 71, 12, 85, 85, 26, 125, 117, 0, 64, 
                89, 87, 81, 120, 1, 101, 85, 70, 66, 88, 
                6, 83 }, "41e604"));
        parcel1.writeString(param1String);
        try {
          if (!this.mRemote.transact(3, parcel1, parcel2, 0) && IDeviceIdService.Stub.getDefaultImpl() != null) {
            param1String = IDeviceIdService.Stub.getDefaultImpl().getAAID(param1String);
            parcel2.recycle();
            parcel1.recycle();
            return param1String;
          } 
          parcel2.readException();
          param1String = parcel2.readString();
          parcel2.recycle();
          parcel1.recycle();
          return param1String;
        } finally {}
      } finally {}
      parcel2.recycle();
      parcel1.recycle();
      throw param1String;
    }
    
    public String getInterfaceDescriptor() {
      return IL1Iii.IL1Iii(new byte[] { 
            1, 9, 9, 25, 64, 82, 15, 21, 17, 89, 
            84, 29, 3, 8, 0, 69, 92, 90, 6, 72, 
            0, 82, 69, 90, 1, 3, 13, 83, 64, 86, 
            16, 16, 13, 84, 86, 29, 43, 34, 1, 65, 
            90, 80, 7, 47, 0, 100, 86, 65, 20, 15, 
            7, 82 }, "bfd733");
    }
    
    public String getOAID() throws RemoteException {
      Exception exception;
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                7, 90, 93, 74, 21, 83, 9, 70, 69, 10, 
                1, 28, 5, 91, 84, 22, 9, 91, 0, 27, 
                84, 1, 16, 91, 7, 80, 89, 0, 21, 87, 
                22, 67, 89, 7, 3, 28, 45, 113, 85, 18, 
                15, 81, 1, 124, 84, 55, 3, 64, 18, 92, 
                83, 1 }, "d50df2"));
        try {
          if (!this.mRemote.transact(1, parcel2, parcel1, 0) && IDeviceIdService.Stub.getDefaultImpl() != null) {
            String str1 = IDeviceIdService.Stub.getDefaultImpl().getOAID();
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
    
    public String getVAID(String param1String) throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                2, 92, 12, 29, 67, 4, 12, 64, 20, 93, 
                87, 75, 0, 93, 5, 65, 95, 12, 5, 29, 
                5, 86, 70, 12, 2, 86, 8, 87, 67, 0, 
                19, 69, 8, 80, 85, 75, 40, 119, 4, 69, 
                89, 6, 4, 122, 5, 96, 85, 23, 23, 90, 
                2, 86 }, "a3a30e"));
        parcel2.writeString(param1String);
        try {
          if (!this.mRemote.transact(2, parcel2, parcel1, 0) && IDeviceIdService.Stub.getDefaultImpl() != null) {
            param1String = IDeviceIdService.Stub.getDefaultImpl().getVAID(param1String);
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
