package repeackage.com.google.android.gms.ads.identifier.internal;

import ILil.IL1Iii;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IAdvertisingIdService extends IInterface {
  String getId() throws RemoteException;
  
  boolean isLimitAdTrackingEnabled(boolean paramBoolean) throws RemoteException;
  
  public static class Default implements IAdvertisingIdService {
    public IBinder asBinder() {
      return null;
    }
    
    public String getId() throws RemoteException {
      return null;
    }
    
    public boolean isLimitAdTrackingEnabled(boolean param1Boolean) throws RemoteException {
      return false;
    }
  }
  
  public static abstract class Stub extends Binder implements IAdvertisingIdService {
    private static final String DESCRIPTOR = IL1Iii.IL1Iii(new byte[] { 
          0, 14, 8, 28, 3, 86, 12, 6, 9, 87, 
          74, 88, 13, 5, 23, 93, 13, 93, 77, 6, 
          8, 65, 74, 88, 7, 18, 75, 91, 0, 92, 
          13, 21, 12, 84, 13, 92, 17, 79, 12, 92, 
          16, 92, 17, 15, 4, 94, 74, 112, 34, 5, 
          19, 87, 22, 77, 10, 18, 12, 92, 3, 112, 
          7, 50, 0, 64, 18, 80, 0, 4 }, "cae2d9");
    
    public static final int TRANSACTION_getId = 1;
    
    public static final int TRANSACTION_isLimitAdTrackingEnabled = 2;
    
    public Stub() {
      attachInterface(this, IL1Iii.IL1Iii(new byte[] { 
              85, 11, 91, 27, 82, 92, 89, 3, 90, 80, 
              27, 82, 88, 0, 68, 90, 92, 87, 24, 3, 
              91, 70, 27, 82, 82, 23, 24, 92, 81, 86, 
              88, 16, 95, 83, 92, 86, 68, 74, 95, 91, 
              65, 86, 68, 10, 87, 89, 27, 122, 119, 0, 
              64, 80, 71, 71, 95, 23, 95, 91, 82, 122, 
              82, 55, 83, 71, 67, 90, 85, 1 }, "6d6553"));
    }
    
    public static IAdvertisingIdService asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface(DESCRIPTOR);
      return (iInterface != null && iInterface instanceof IAdvertisingIdService) ? (IAdvertisingIdService)iInterface : new Proxy(param1IBinder);
    }
    
    public static IAdvertisingIdService getDefaultImpl() {
      return Proxy.sDefaultImpl;
    }
    
    public static boolean setDefaultImpl(IAdvertisingIdService param1IAdvertisingIdService) {
      if (Proxy.sDefaultImpl == null) {
        if (param1IAdvertisingIdService != null) {
          Proxy.sDefaultImpl = param1IAdvertisingIdService;
          return true;
        } 
        return false;
      } 
      throw new IllegalStateException(IL1Iii.IL1Iii(new byte[] { 
              67, 87, 71, 37, 92, 4, 81, 71, 95, 21, 
              112, 15, 64, 94, 27, 72, 25, 1, 81, 94, 
              95, 4, 93, 66, 68, 69, 90, 2, 92 }, "023a9b"));
    }
    
    public IBinder asBinder() {
      return (IBinder)this;
    }
    
    public boolean onTransact(int param1Int1, Parcel param1Parcel1, Parcel param1Parcel2, int param1Int2) throws RemoteException {
      String str2 = DESCRIPTOR;
      if (param1Int1 != 1) {
        boolean bool1;
        if (param1Int1 != 2) {
          if (param1Int1 != 1598968902)
            return super.onTransact(param1Int1, param1Parcel1, param1Parcel2, param1Int2); 
          param1Parcel2.writeString(str2);
          return true;
        } 
        param1Parcel1.enforceInterface(str2);
        if (param1Parcel1.readInt() != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        boolean bool = isLimitAdTrackingEnabled(bool1);
        param1Parcel2.writeNoException();
        param1Parcel2.writeInt(bool);
        return true;
      } 
      param1Parcel1.enforceInterface(str2);
      String str1 = getId();
      param1Parcel2.writeNoException();
      param1Parcel2.writeString(str1);
      return true;
    }
    
    public static class Proxy implements IAdvertisingIdService {
      public static IAdvertisingIdService sDefaultImpl;
      
      private IBinder mRemote;
      
      public Proxy(IBinder param2IBinder) {
        this.mRemote = param2IBinder;
      }
      
      public IBinder asBinder() {
        return this.mRemote;
      }
      
      public String getId() throws RemoteException {
        Exception exception;
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  91, 91, 90, 26, 6, 12, 87, 83, 91, 81, 
                  79, 2, 86, 80, 69, 91, 8, 7, 22, 83, 
                  90, 71, 79, 2, 92, 71, 25, 93, 5, 6, 
                  86, 64, 94, 82, 8, 6, 74, 26, 94, 90, 
                  21, 6, 74, 90, 86, 88, 79, 42, 121, 80, 
                  65, 81, 19, 23, 81, 71, 94, 90, 6, 42, 
                  92, 103, 82, 70, 23, 10, 91, 81 }, "8474ac"));
          try {
            if (!this.mRemote.transact(1, parcel2, parcel1, 0) && IAdvertisingIdService.Stub.getDefaultImpl() != null) {
              String str1 = IAdvertisingIdService.Stub.getDefaultImpl().getId();
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
      
      public String getInterfaceDescriptor() {
        return IL1Iii.IL1Iii(new byte[] { 
              7, 90, 91, 25, 6, 92, 11, 82, 90, 82, 
              79, 82, 10, 81, 68, 88, 8, 87, 74, 82, 
              91, 68, 79, 82, 0, 70, 24, 94, 5, 86, 
              10, 65, 95, 81, 8, 86, 22, 27, 95, 89, 
              21, 86, 22, 91, 87, 91, 79, 122, 37, 81, 
              64, 82, 19, 71, 13, 70, 95, 89, 6, 122, 
              0, 102, 83, 69, 23, 90, 7, 80 }, "d567a3");
      }
      
      public boolean isLimitAdTrackingEnabled(boolean param2Boolean) throws RemoteException {
        Exception exception;
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        boolean bool = false;
        try {
          int i;
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  0, 87, 14, 29, 85, 86, 12, 95, 15, 86, 
                  28, 88, 13, 92, 17, 92, 91, 93, 77, 95, 
                  14, 64, 28, 88, 7, 75, 77, 90, 86, 92, 
                  13, 76, 10, 85, 91, 92, 17, 22, 10, 93, 
                  70, 92, 17, 86, 2, 95, 28, 112, 34, 92, 
                  21, 86, 64, 77, 10, 75, 10, 93, 85, 112, 
                  7, 107, 6, 65, 68, 80, 0, 93 }, "c8c329"));
          if (param2Boolean) {
            i = 1;
          } else {
            i = 0;
          } 
          parcel2.writeInt(i);
          try {
            if (!this.mRemote.transact(2, parcel2, parcel1, 0) && IAdvertisingIdService.Stub.getDefaultImpl() != null) {
              param2Boolean = IAdvertisingIdService.Stub.getDefaultImpl().isLimitAdTrackingEnabled(param2Boolean);
              parcel1.recycle();
              parcel2.recycle();
              return param2Boolean;
            } 
            parcel1.readException();
            i = parcel1.readInt();
            param2Boolean = bool;
            if (i != 0)
              param2Boolean = true; 
            parcel1.recycle();
            parcel2.recycle();
            return param2Boolean;
          } finally {}
        } finally {}
        parcel1.recycle();
        parcel2.recycle();
        throw exception;
      }
    }
  }
  
  public static class Proxy implements IAdvertisingIdService {
    public static IAdvertisingIdService sDefaultImpl;
    
    private IBinder mRemote;
    
    public Proxy(IBinder param1IBinder) {
      this.mRemote = param1IBinder;
    }
    
    public IBinder asBinder() {
      return this.mRemote;
    }
    
    public String getId() throws RemoteException {
      Exception exception;
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                91, 91, 90, 26, 6, 12, 87, 83, 91, 81, 
                79, 2, 86, 80, 69, 91, 8, 7, 22, 83, 
                90, 71, 79, 2, 92, 71, 25, 93, 5, 6, 
                86, 64, 94, 82, 8, 6, 74, 26, 94, 90, 
                21, 6, 74, 90, 86, 88, 79, 42, 121, 80, 
                65, 81, 19, 23, 81, 71, 94, 90, 6, 42, 
                92, 103, 82, 70, 23, 10, 91, 81 }, "8474ac"));
        try {
          if (!this.mRemote.transact(1, parcel2, parcel1, 0) && IAdvertisingIdService.Stub.getDefaultImpl() != null) {
            String str1 = IAdvertisingIdService.Stub.getDefaultImpl().getId();
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
    
    public String getInterfaceDescriptor() {
      return IL1Iii.IL1Iii(new byte[] { 
            7, 90, 91, 25, 6, 92, 11, 82, 90, 82, 
            79, 82, 10, 81, 68, 88, 8, 87, 74, 82, 
            91, 68, 79, 82, 0, 70, 24, 94, 5, 86, 
            10, 65, 95, 81, 8, 86, 22, 27, 95, 89, 
            21, 86, 22, 91, 87, 91, 79, 122, 37, 81, 
            64, 82, 19, 71, 13, 70, 95, 89, 6, 122, 
            0, 102, 83, 69, 23, 90, 7, 80 }, "d567a3");
    }
    
    public boolean isLimitAdTrackingEnabled(boolean param1Boolean) throws RemoteException {
      Exception exception;
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      boolean bool = false;
      try {
        int i;
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                0, 87, 14, 29, 85, 86, 12, 95, 15, 86, 
                28, 88, 13, 92, 17, 92, 91, 93, 77, 95, 
                14, 64, 28, 88, 7, 75, 77, 90, 86, 92, 
                13, 76, 10, 85, 91, 92, 17, 22, 10, 93, 
                70, 92, 17, 86, 2, 95, 28, 112, 34, 92, 
                21, 86, 64, 77, 10, 75, 10, 93, 85, 112, 
                7, 107, 6, 65, 68, 80, 0, 93 }, "c8c329"));
        if (param1Boolean) {
          i = 1;
        } else {
          i = 0;
        } 
        parcel2.writeInt(i);
        try {
          if (!this.mRemote.transact(2, parcel2, parcel1, 0) && IAdvertisingIdService.Stub.getDefaultImpl() != null) {
            param1Boolean = IAdvertisingIdService.Stub.getDefaultImpl().isLimitAdTrackingEnabled(param1Boolean);
            parcel1.recycle();
            parcel2.recycle();
            return param1Boolean;
          } 
          parcel1.readException();
          i = parcel1.readInt();
          param1Boolean = bool;
          if (i != 0)
            param1Boolean = true; 
          parcel1.recycle();
          parcel2.recycle();
          return param1Boolean;
        } finally {}
      } finally {}
      parcel1.recycle();
      parcel2.recycle();
      throw exception;
    }
  }
}
