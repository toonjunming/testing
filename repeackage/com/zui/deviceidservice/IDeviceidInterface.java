package repeackage.com.zui.deviceidservice;

import ILil.IL1Iii;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IDeviceidInterface extends IInterface {
  String createAAIDForPackageName(String paramString) throws RemoteException;
  
  String getAAID(String paramString) throws RemoteException;
  
  String getOAID() throws RemoteException;
  
  String getUDID() throws RemoteException;
  
  String getVAID(String paramString) throws RemoteException;
  
  boolean isSupport() throws RemoteException;
  
  public static class Default implements IDeviceidInterface {
    public IBinder asBinder() {
      return null;
    }
    
    public String createAAIDForPackageName(String param1String) throws RemoteException {
      return null;
    }
    
    public String getAAID(String param1String) throws RemoteException {
      return null;
    }
    
    public String getOAID() throws RemoteException {
      return null;
    }
    
    public String getUDID() throws RemoteException {
      return null;
    }
    
    public String getVAID(String param1String) throws RemoteException {
      return null;
    }
    
    public boolean isSupport() throws RemoteException {
      return false;
    }
  }
  
  public static abstract class Stub extends Binder implements IDeviceidInterface {
    private static final String DESCRIPTOR = IL1Iii.IL1Iii(new byte[] { 
          84, 95, 15, 26, 74, 70, 94, 30, 6, 81, 
          70, 90, 84, 85, 11, 80, 67, 86, 69, 70, 
          11, 87, 85, 29, 126, 116, 7, 66, 89, 80, 
          82, 89, 6, 125, 94, 71, 82, 66, 4, 85, 
          83, 86 }, "70b403");
    
    public static final int TRANSACTION_createAAIDForPackageName = 6;
    
    public static final int TRANSACTION_getAAID = 5;
    
    public static final int TRANSACTION_getOAID = 1;
    
    public static final int TRANSACTION_getUDID = 2;
    
    public static final int TRANSACTION_getVAID = 4;
    
    public static final int TRANSACTION_isSupport = 3;
    
    public Stub() {
      attachInterface(this, IL1Iii.IL1Iii(new byte[] { 
              0, 94, 89, 30, 72, 66, 10, 31, 80, 85, 
              68, 94, 0, 84, 93, 84, 65, 82, 17, 71, 
              93, 83, 87, 25, 42, 117, 81, 70, 91, 84, 
              6, 88, 80, 121, 92, 67, 6, 67, 82, 81, 
              81, 82 }, "c14027"));
    }
    
    public static IDeviceidInterface asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface(DESCRIPTOR);
      return (iInterface != null && iInterface instanceof IDeviceidInterface) ? (IDeviceidInterface)iInterface : new Proxy(param1IBinder);
    }
    
    public static IDeviceidInterface getDefaultImpl() {
      return Proxy.sDefaultImpl;
    }
    
    public static boolean setDefaultImpl(IDeviceidInterface param1IDeviceidInterface) {
      if (Proxy.sDefaultImpl == null) {
        if (param1IDeviceidInterface != null) {
          Proxy.sDefaultImpl = param1IDeviceidInterface;
          return true;
        } 
        return false;
      } 
      throw new IllegalStateException(IL1Iii.IL1Iii(new byte[] { 
              70, 0, 22, 125, 84, 5, 84, 16, 14, 77, 
              120, 14, 69, 9, 74, 16, 17, 0, 84, 9, 
              14, 92, 85, 67, 65, 18, 11, 90, 84 }, "5eb91c"));
    }
    
    public IBinder asBinder() {
      return (IBinder)this;
    }
    
    public boolean onTransact(int param1Int1, Parcel param1Parcel1, Parcel param1Parcel2, int param1Int2) throws RemoteException {
      String str = DESCRIPTOR;
      if (param1Int1 != 1598968902) {
        boolean bool;
        switch (param1Int1) {
          default:
            return super.onTransact(param1Int1, param1Parcel1, param1Parcel2, param1Int2);
          case 6:
            param1Parcel1.enforceInterface(str);
            str1 = createAAIDForPackageName(param1Parcel1.readString());
            param1Parcel2.writeNoException();
            param1Parcel2.writeString(str1);
            return true;
          case 5:
            str1.enforceInterface(str);
            str1 = getAAID(str1.readString());
            param1Parcel2.writeNoException();
            param1Parcel2.writeString(str1);
            return true;
          case 4:
            str1.enforceInterface(str);
            str1 = getVAID(str1.readString());
            param1Parcel2.writeNoException();
            param1Parcel2.writeString(str1);
            return true;
          case 3:
            str1.enforceInterface(str);
            bool = isSupport();
            param1Parcel2.writeNoException();
            param1Parcel2.writeInt(bool);
            return true;
          case 2:
            str1.enforceInterface(str);
            str1 = getUDID();
            param1Parcel2.writeNoException();
            param1Parcel2.writeString(str1);
            return true;
          case 1:
            break;
        } 
        str1.enforceInterface(str);
        String str1 = getOAID();
        param1Parcel2.writeNoException();
        param1Parcel2.writeString(str1);
        return true;
      } 
      param1Parcel2.writeString(str);
      return true;
    }
    
    public static class Proxy implements IDeviceidInterface {
      public static IDeviceidInterface sDefaultImpl;
      
      private IBinder mRemote;
      
      public Proxy(IBinder param2IBinder) {
        this.mRemote = param2IBinder;
      }
      
      public IBinder asBinder() {
        return this.mRemote;
      }
      
      public String createAAIDForPackageName(String param2String) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  91, 87, 85, 75, 30, 67, 81, 22, 92, 0, 
                  18, 95, 91, 93, 81, 1, 23, 83, 74, 78, 
                  81, 6, 1, 24, 113, 124, 93, 19, 13, 85, 
                  93, 81, 92, 44, 10, 66, 93, 74, 94, 4, 
                  7, 83 }, "888ed6"));
          parcel1.writeString(param2String);
          try {
            if (!this.mRemote.transact(6, parcel1, parcel2, 0) && IDeviceidInterface.Stub.getDefaultImpl() != null) {
              param2String = IDeviceidInterface.Stub.getDefaultImpl().createAAIDForPackageName(param2String);
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
      
      public String getAAID(String param2String) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  2, 12, 85, 72, 77, 68, 8, 77, 92, 3, 
                  65, 88, 2, 6, 81, 2, 68, 84, 19, 21, 
                  81, 5, 82, 31, 40, 39, 93, 16, 94, 82, 
                  4, 10, 92, 47, 89, 69, 4, 17, 94, 7, 
                  84, 84 }, "ac8f71"));
          parcel1.writeString(param2String);
          try {
            if (!this.mRemote.transact(5, parcel1, parcel2, 0) && IDeviceidInterface.Stub.getDefaultImpl() != null) {
              param2String = IDeviceidInterface.Stub.getDefaultImpl().getAAID(param2String);
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
              80, 89, 85, 75, 67, 70, 90, 24, 92, 0, 
              79, 90, 80, 83, 81, 1, 74, 86, 65, 64, 
              81, 6, 92, 29, 122, 114, 93, 19, 80, 80, 
              86, 95, 92, 44, 87, 71, 86, 68, 94, 4, 
              90, 86 }, "368e93");
      }
      
      public String getOAID() throws RemoteException {
        Exception exception;
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  86, 90, 11, 28, 78, 70, 92, 27, 2, 87, 
                  66, 90, 86, 80, 15, 86, 71, 86, 71, 67, 
                  15, 81, 81, 29, 124, 113, 3, 68, 93, 80, 
                  80, 92, 2, 123, 90, 71, 80, 71, 0, 83, 
                  87, 86 }, "55f243"));
          try {
            if (!this.mRemote.transact(1, parcel1, parcel2, 0) && IDeviceidInterface.Stub.getDefaultImpl() != null) {
              String str1 = IDeviceidInterface.Stub.getDefaultImpl().getOAID();
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
      
      public String getUDID() throws RemoteException {
        Exception exception;
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  90, 93, 12, 23, 28, 66, 80, 28, 5, 92, 
                  16, 94, 90, 87, 8, 93, 21, 82, 75, 68, 
                  8, 90, 3, 25, 112, 118, 4, 79, 15, 84, 
                  92, 91, 5, 112, 8, 67, 92, 64, 7, 88, 
                  5, 82 }, "92a9f7"));
          try {
            if (!this.mRemote.transact(2, parcel1, parcel2, 0) && IDeviceidInterface.Stub.getDefaultImpl() != null) {
              String str1 = IDeviceidInterface.Stub.getDefaultImpl().getUDID();
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
      
      public String getVAID(String param2String) throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  90, 14, 85, 23, 28, 65, 80, 79, 92, 92, 
                  16, 93, 90, 4, 81, 93, 21, 81, 75, 23, 
                  81, 90, 3, 26, 112, 37, 93, 79, 15, 87, 
                  92, 8, 92, 112, 8, 64, 92, 19, 94, 88, 
                  5, 81 }, "9a89f4"));
          parcel2.writeString(param2String);
          try {
            if (!this.mRemote.transact(4, parcel2, parcel1, 0) && IDeviceidInterface.Stub.getDefaultImpl() != null) {
              param2String = IDeviceidInterface.Stub.getDefaultImpl().getVAID(param2String);
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
      
      public boolean isSupport() throws RemoteException {
        Exception exception;
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        boolean bool = false;
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  81, 93, 94, 26, 79, 19, 91, 28, 87, 81, 
                  67, 15, 81, 87, 90, 80, 70, 3, 64, 68, 
                  90, 87, 80, 72, 123, 118, 86, 66, 92, 5, 
                  87, 91, 87, 125, 91, 18, 87, 64, 85, 85, 
                  86, 3 }, "22345f"));
          try {
            if (!this.mRemote.transact(3, parcel1, parcel2, 0) && IDeviceidInterface.Stub.getDefaultImpl() != null) {
              bool = IDeviceidInterface.Stub.getDefaultImpl().isSupport();
              parcel2.recycle();
              parcel1.recycle();
              return bool;
            } 
            parcel2.readException();
            int i = parcel2.readInt();
            if (i != 0)
              bool = true; 
            parcel2.recycle();
            parcel1.recycle();
            return bool;
          } finally {}
        } finally {}
        parcel2.recycle();
        parcel1.recycle();
        throw exception;
      }
    }
  }
  
  public static class Proxy implements IDeviceidInterface {
    public static IDeviceidInterface sDefaultImpl;
    
    private IBinder mRemote;
    
    public Proxy(IBinder param1IBinder) {
      this.mRemote = param1IBinder;
    }
    
    public IBinder asBinder() {
      return this.mRemote;
    }
    
    public String createAAIDForPackageName(String param1String) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                91, 87, 85, 75, 30, 67, 81, 22, 92, 0, 
                18, 95, 91, 93, 81, 1, 23, 83, 74, 78, 
                81, 6, 1, 24, 113, 124, 93, 19, 13, 85, 
                93, 81, 92, 44, 10, 66, 93, 74, 94, 4, 
                7, 83 }, "888ed6"));
        parcel1.writeString(param1String);
        try {
          if (!this.mRemote.transact(6, parcel1, parcel2, 0) && IDeviceidInterface.Stub.getDefaultImpl() != null) {
            param1String = IDeviceidInterface.Stub.getDefaultImpl().createAAIDForPackageName(param1String);
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
    
    public String getAAID(String param1String) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                2, 12, 85, 72, 77, 68, 8, 77, 92, 3, 
                65, 88, 2, 6, 81, 2, 68, 84, 19, 21, 
                81, 5, 82, 31, 40, 39, 93, 16, 94, 82, 
                4, 10, 92, 47, 89, 69, 4, 17, 94, 7, 
                84, 84 }, "ac8f71"));
        parcel1.writeString(param1String);
        try {
          if (!this.mRemote.transact(5, parcel1, parcel2, 0) && IDeviceidInterface.Stub.getDefaultImpl() != null) {
            param1String = IDeviceidInterface.Stub.getDefaultImpl().getAAID(param1String);
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
            80, 89, 85, 75, 67, 70, 90, 24, 92, 0, 
            79, 90, 80, 83, 81, 1, 74, 86, 65, 64, 
            81, 6, 92, 29, 122, 114, 93, 19, 80, 80, 
            86, 95, 92, 44, 87, 71, 86, 68, 94, 4, 
            90, 86 }, "368e93");
    }
    
    public String getOAID() throws RemoteException {
      Exception exception;
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                86, 90, 11, 28, 78, 70, 92, 27, 2, 87, 
                66, 90, 86, 80, 15, 86, 71, 86, 71, 67, 
                15, 81, 81, 29, 124, 113, 3, 68, 93, 80, 
                80, 92, 2, 123, 90, 71, 80, 71, 0, 83, 
                87, 86 }, "55f243"));
        try {
          if (!this.mRemote.transact(1, parcel1, parcel2, 0) && IDeviceidInterface.Stub.getDefaultImpl() != null) {
            String str1 = IDeviceidInterface.Stub.getDefaultImpl().getOAID();
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
    
    public String getUDID() throws RemoteException {
      Exception exception;
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                90, 93, 12, 23, 28, 66, 80, 28, 5, 92, 
                16, 94, 90, 87, 8, 93, 21, 82, 75, 68, 
                8, 90, 3, 25, 112, 118, 4, 79, 15, 84, 
                92, 91, 5, 112, 8, 67, 92, 64, 7, 88, 
                5, 82 }, "92a9f7"));
        try {
          if (!this.mRemote.transact(2, parcel1, parcel2, 0) && IDeviceidInterface.Stub.getDefaultImpl() != null) {
            String str1 = IDeviceidInterface.Stub.getDefaultImpl().getUDID();
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
    
    public String getVAID(String param1String) throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                90, 14, 85, 23, 28, 65, 80, 79, 92, 92, 
                16, 93, 90, 4, 81, 93, 21, 81, 75, 23, 
                81, 90, 3, 26, 112, 37, 93, 79, 15, 87, 
                92, 8, 92, 112, 8, 64, 92, 19, 94, 88, 
                5, 81 }, "9a89f4"));
        parcel2.writeString(param1String);
        try {
          if (!this.mRemote.transact(4, parcel2, parcel1, 0) && IDeviceidInterface.Stub.getDefaultImpl() != null) {
            param1String = IDeviceidInterface.Stub.getDefaultImpl().getVAID(param1String);
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
    
    public boolean isSupport() throws RemoteException {
      Exception exception;
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      boolean bool = false;
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                81, 93, 94, 26, 79, 19, 91, 28, 87, 81, 
                67, 15, 81, 87, 90, 80, 70, 3, 64, 68, 
                90, 87, 80, 72, 123, 118, 86, 66, 92, 5, 
                87, 91, 87, 125, 91, 18, 87, 64, 85, 85, 
                86, 3 }, "22345f"));
        try {
          if (!this.mRemote.transact(3, parcel1, parcel2, 0) && IDeviceidInterface.Stub.getDefaultImpl() != null) {
            bool = IDeviceidInterface.Stub.getDefaultImpl().isSupport();
            parcel2.recycle();
            parcel1.recycle();
            return bool;
          } 
          parcel2.readException();
          int i = parcel2.readInt();
          if (i != 0)
            bool = true; 
          parcel2.recycle();
          parcel1.recycle();
          return bool;
        } finally {}
      } finally {}
      parcel2.recycle();
      parcel1.recycle();
      throw exception;
    }
  }
}
