package repeackage.com.coolpad.deviceidsupport;

import ILil.IL1Iii;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IDeviceIdManager extends IInterface {
  String getAAID(String paramString) throws RemoteException;
  
  String getCoolOsVersion() throws RemoteException;
  
  String getIMEI(String paramString) throws RemoteException;
  
  String getOAID(String paramString) throws RemoteException;
  
  String getUDID(String paramString) throws RemoteException;
  
  String getVAID(String paramString) throws RemoteException;
  
  boolean isCoolOs() throws RemoteException;
  
  public static class Default implements IDeviceIdManager {
    public IBinder asBinder() {
      return null;
    }
    
    public String getAAID(String param1String) throws RemoteException {
      return null;
    }
    
    public String getCoolOsVersion() throws RemoteException {
      return null;
    }
    
    public String getIMEI(String param1String) throws RemoteException {
      return null;
    }
    
    public String getOAID(String param1String) throws RemoteException {
      return null;
    }
    
    public String getUDID(String param1String) throws RemoteException {
      return null;
    }
    
    public String getVAID(String param1String) throws RemoteException {
      return null;
    }
    
    public boolean isCoolOs() throws RemoteException {
      return false;
    }
  }
  
  public static abstract class Stub extends Binder implements IDeviceIdManager {
    private static final String DESCRIPTOR = IL1Iii.IL1Iii(new byte[] { 
          83, 87, 8, 28, 0, 11, 95, 84, 21, 83, 
          7, 74, 84, 93, 19, 91, 0, 1, 89, 92, 
          22, 71, 19, 20, 95, 74, 17, 28, 42, 32, 
          85, 78, 12, 81, 6, 45, 84, 117, 4, 92, 
          2, 3, 85, 74 }, "08e2cd");
    
    public static final int TRANSACTION_getAAID = 4;
    
    public static final int TRANSACTION_getCoolOsVersion = 7;
    
    public static final int TRANSACTION_getIMEI = 5;
    
    public static final int TRANSACTION_getOAID = 2;
    
    public static final int TRANSACTION_getUDID = 1;
    
    public static final int TRANSACTION_getVAID = 3;
    
    public static final int TRANSACTION_isCoolOs = 6;
    
    public Stub() {
      attachInterface(this, IL1Iii.IL1Iii(new byte[] { 
              90, 92, 92, 29, 81, 86, 86, 95, 65, 82, 
              86, 23, 93, 86, 71, 90, 81, 92, 80, 87, 
              66, 70, 66, 73, 86, 65, 69, 29, 123, 125, 
              92, 69, 88, 80, 87, 112, 93, 126, 80, 93, 
              83, 94, 92, 65 }, "931329"));
    }
    
    public static IDeviceIdManager asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface(DESCRIPTOR);
      return (iInterface != null && iInterface instanceof IDeviceIdManager) ? (IDeviceIdManager)iInterface : new Proxy(param1IBinder);
    }
    
    public static IDeviceIdManager getDefaultImpl() {
      return Proxy.sDefaultImpl;
    }
    
    public static boolean setDefaultImpl(IDeviceIdManager param1IDeviceIdManager) {
      if (Proxy.sDefaultImpl == null) {
        if (param1IDeviceIdManager != null) {
          Proxy.sDefaultImpl = param1IDeviceIdManager;
          return true;
        } 
        return false;
      } 
      throw new IllegalStateException(IL1Iii.IL1Iii(new byte[] { 
              67, 85, 66, 113, 84, 82, 81, 69, 90, 65, 
              120, 89, 64, 92, 30, 28, 17, 87, 81, 92, 
              90, 80, 85, 20, 68, 71, 95, 86, 84 }, "006514"));
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
          case 7:
            param1Parcel1.enforceInterface(str);
            str1 = getCoolOsVersion();
            param1Parcel2.writeNoException();
            param1Parcel2.writeString(str1);
            return true;
          case 6:
            str1.enforceInterface(str);
            bool = isCoolOs();
            param1Parcel2.writeNoException();
            param1Parcel2.writeInt(bool);
            return true;
          case 5:
            str1.enforceInterface(str);
            str1 = getIMEI(str1.readString());
            param1Parcel2.writeNoException();
            param1Parcel2.writeString(str1);
            return true;
          case 4:
            str1.enforceInterface(str);
            str1 = getAAID(str1.readString());
            param1Parcel2.writeNoException();
            param1Parcel2.writeString(str1);
            return true;
          case 3:
            str1.enforceInterface(str);
            str1 = getVAID(str1.readString());
            param1Parcel2.writeNoException();
            param1Parcel2.writeString(str1);
            return true;
          case 2:
            str1.enforceInterface(str);
            str1 = getOAID(str1.readString());
            param1Parcel2.writeNoException();
            param1Parcel2.writeString(str1);
            return true;
          case 1:
            break;
        } 
        str1.enforceInterface(str);
        String str1 = getUDID(str1.readString());
        param1Parcel2.writeNoException();
        param1Parcel2.writeString(str1);
        return true;
      } 
      param1Parcel2.writeString(str);
      return true;
    }
    
    public static class Proxy implements IDeviceIdManager {
      public static IDeviceIdManager sDefaultImpl;
      
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
                  0, 90, 89, 76, 85, 95, 12, 89, 68, 3, 
                  82, 30, 7, 80, 66, 11, 85, 85, 10, 81, 
                  71, 23, 70, 64, 12, 71, 64, 76, Byte.MAX_VALUE, 116, 
                  6, 67, 93, 1, 83, 121, 7, 120, 85, 12, 
                  87, 87, 6, 71 }, "c54b60"));
          parcel2.writeString(param2String);
          try {
            if (!this.mRemote.transact(4, parcel2, parcel1, 0) && IDeviceIdManager.Stub.getDefaultImpl() != null) {
              param2String = IDeviceIdManager.Stub.getDefaultImpl().getAAID(param2String);
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
      
      public String getCoolOsVersion() throws RemoteException {
        Exception exception;
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  83, 9, 11, 79, 2, 94, 95, 10, 22, 0, 
                  5, 31, 84, 3, 16, 8, 2, 84, 89, 2, 
                  21, 20, 17, 65, 95, 20, 18, 79, 40, 117, 
                  85, 16, 15, 2, 4, 120, 84, 43, 7, 15, 
                  0, 86, 85, 20 }, "0ffaa1"));
          try {
            if (!this.mRemote.transact(7, parcel1, parcel2, 0) && IDeviceIdManager.Stub.getDefaultImpl() != null) {
              String str1 = IDeviceIdManager.Stub.getDefaultImpl().getCoolOsVersion();
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
      
      public String getIMEI(String param2String) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  82, 14, 85, 31, 1, 90, 94, 13, 72, 80, 
                  6, 27, 85, 4, 78, 88, 1, 80, 88, 5, 
                  75, 68, 18, 69, 94, 19, 76, 31, 43, 113, 
                  84, 23, 81, 82, 7, 124, 85, 44, 89, 95, 
                  3, 82, 84, 19 }, "1a81b5"));
          parcel1.writeString(param2String);
          try {
            if (!this.mRemote.transact(5, parcel1, parcel2, 0) && IDeviceIdManager.Stub.getDefaultImpl() != null) {
              param2String = IDeviceIdManager.Stub.getDefaultImpl().getIMEI(param2String);
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
              6, 87, 91, 29, 87, 86, 10, 84, 70, 82, 
              80, 23, 1, 93, 64, 90, 87, 92, 12, 92, 
              69, 70, 68, 73, 10, 74, 66, 29, 125, 125, 
              0, 78, 95, 80, 81, 112, 1, 117, 87, 93, 
              85, 94, 0, 74 }, "e86349");
      }
      
      public String getOAID(String param2String) throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  84, 89, 11, 24, 85, 92, 88, 90, 22, 87, 
                  82, 29, 83, 83, 16, 95, 85, 86, 94, 82, 
                  21, 67, 70, 67, 88, 68, 18, 24, Byte.MAX_VALUE, 119, 
                  82, 64, 15, 85, 83, 122, 83, 123, 7, 88, 
                  87, 84, 82, 68 }, "76f663"));
          parcel2.writeString(param2String);
          try {
            if (!this.mRemote.transact(2, parcel2, parcel1, 0) && IDeviceIdManager.Stub.getDefaultImpl() != null) {
              param2String = IDeviceIdManager.Stub.getDefaultImpl().getOAID(param2String);
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
      
      public String getUDID(String param2String) throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  7, 86, 93, 31, 90, 87, 11, 85, 64, 80, 
                  93, 22, 0, 92, 70, 88, 90, 93, 13, 93, 
                  67, 68, 73, 72, 11, 75, 68, 31, 112, 124, 
                  1, 79, 89, 82, 92, 113, 0, 116, 81, 95, 
                  88, 95, 1, 75 }, "d90198"));
          parcel2.writeString(param2String);
          if (!this.mRemote.transact(1, parcel2, parcel1, 0) && IDeviceIdManager.Stub.getDefaultImpl() != null) {
            param2String = IDeviceIdManager.Stub.getDefaultImpl().getUDID(param2String);
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
      
      public String getVAID(String param2String) throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  2, 9, 15, 25, 84, 14, 14, 10, 18, 86, 
                  83, 79, 5, 3, 20, 94, 84, 4, 8, 2, 
                  17, 66, 71, 17, 14, 20, 22, 25, 126, 37, 
                  4, 16, 11, 84, 82, 40, 5, 43, 3, 89, 
                  86, 6, 4, 20 }, "afb77a"));
          parcel2.writeString(param2String);
          try {
            if (!this.mRemote.transact(3, parcel2, parcel1, 0) && IDeviceIdManager.Stub.getDefaultImpl() != null) {
              param2String = IDeviceIdManager.Stub.getDefaultImpl().getVAID(param2String);
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
      
      public boolean isCoolOs() throws RemoteException {
        Exception exception;
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        boolean bool = false;
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  2, 95, 12, 27, 1, 91, 14, 92, 17, 84, 
                  6, 26, 5, 85, 23, 92, 1, 81, 8, 84, 
                  18, 64, 18, 68, 14, 66, 21, 27, 43, 112, 
                  4, 70, 8, 86, 7, 125, 5, 125, 0, 91, 
                  3, 83, 4, 66 }, "a0a5b4"));
          try {
            if (!this.mRemote.transact(6, parcel1, parcel2, 0) && IDeviceIdManager.Stub.getDefaultImpl() != null) {
              bool = IDeviceIdManager.Stub.getDefaultImpl().isCoolOs();
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
  
  public static class Proxy implements IDeviceIdManager {
    public static IDeviceIdManager sDefaultImpl;
    
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
                0, 90, 89, 76, 85, 95, 12, 89, 68, 3, 
                82, 30, 7, 80, 66, 11, 85, 85, 10, 81, 
                71, 23, 70, 64, 12, 71, 64, 76, Byte.MAX_VALUE, 116, 
                6, 67, 93, 1, 83, 121, 7, 120, 85, 12, 
                87, 87, 6, 71 }, "c54b60"));
        parcel2.writeString(param1String);
        try {
          if (!this.mRemote.transact(4, parcel2, parcel1, 0) && IDeviceIdManager.Stub.getDefaultImpl() != null) {
            param1String = IDeviceIdManager.Stub.getDefaultImpl().getAAID(param1String);
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
    
    public String getCoolOsVersion() throws RemoteException {
      Exception exception;
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                83, 9, 11, 79, 2, 94, 95, 10, 22, 0, 
                5, 31, 84, 3, 16, 8, 2, 84, 89, 2, 
                21, 20, 17, 65, 95, 20, 18, 79, 40, 117, 
                85, 16, 15, 2, 4, 120, 84, 43, 7, 15, 
                0, 86, 85, 20 }, "0ffaa1"));
        try {
          if (!this.mRemote.transact(7, parcel1, parcel2, 0) && IDeviceIdManager.Stub.getDefaultImpl() != null) {
            String str1 = IDeviceIdManager.Stub.getDefaultImpl().getCoolOsVersion();
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
    
    public String getIMEI(String param1String) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                82, 14, 85, 31, 1, 90, 94, 13, 72, 80, 
                6, 27, 85, 4, 78, 88, 1, 80, 88, 5, 
                75, 68, 18, 69, 94, 19, 76, 31, 43, 113, 
                84, 23, 81, 82, 7, 124, 85, 44, 89, 95, 
                3, 82, 84, 19 }, "1a81b5"));
        parcel1.writeString(param1String);
        try {
          if (!this.mRemote.transact(5, parcel1, parcel2, 0) && IDeviceIdManager.Stub.getDefaultImpl() != null) {
            param1String = IDeviceIdManager.Stub.getDefaultImpl().getIMEI(param1String);
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
            6, 87, 91, 29, 87, 86, 10, 84, 70, 82, 
            80, 23, 1, 93, 64, 90, 87, 92, 12, 92, 
            69, 70, 68, 73, 10, 74, 66, 29, 125, 125, 
            0, 78, 95, 80, 81, 112, 1, 117, 87, 93, 
            85, 94, 0, 74 }, "e86349");
    }
    
    public String getOAID(String param1String) throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                84, 89, 11, 24, 85, 92, 88, 90, 22, 87, 
                82, 29, 83, 83, 16, 95, 85, 86, 94, 82, 
                21, 67, 70, 67, 88, 68, 18, 24, Byte.MAX_VALUE, 119, 
                82, 64, 15, 85, 83, 122, 83, 123, 7, 88, 
                87, 84, 82, 68 }, "76f663"));
        parcel2.writeString(param1String);
        try {
          if (!this.mRemote.transact(2, parcel2, parcel1, 0) && IDeviceIdManager.Stub.getDefaultImpl() != null) {
            param1String = IDeviceIdManager.Stub.getDefaultImpl().getOAID(param1String);
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
    
    public String getUDID(String param1String) throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                7, 86, 93, 31, 90, 87, 11, 85, 64, 80, 
                93, 22, 0, 92, 70, 88, 90, 93, 13, 93, 
                67, 68, 73, 72, 11, 75, 68, 31, 112, 124, 
                1, 79, 89, 82, 92, 113, 0, 116, 81, 95, 
                88, 95, 1, 75 }, "d90198"));
        parcel2.writeString(param1String);
        if (!this.mRemote.transact(1, parcel2, parcel1, 0) && IDeviceIdManager.Stub.getDefaultImpl() != null) {
          param1String = IDeviceIdManager.Stub.getDefaultImpl().getUDID(param1String);
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
    
    public String getVAID(String param1String) throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                2, 9, 15, 25, 84, 14, 14, 10, 18, 86, 
                83, 79, 5, 3, 20, 94, 84, 4, 8, 2, 
                17, 66, 71, 17, 14, 20, 22, 25, 126, 37, 
                4, 16, 11, 84, 82, 40, 5, 43, 3, 89, 
                86, 6, 4, 20 }, "afb77a"));
        parcel2.writeString(param1String);
        try {
          if (!this.mRemote.transact(3, parcel2, parcel1, 0) && IDeviceIdManager.Stub.getDefaultImpl() != null) {
            param1String = IDeviceIdManager.Stub.getDefaultImpl().getVAID(param1String);
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
    
    public boolean isCoolOs() throws RemoteException {
      Exception exception;
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      boolean bool = false;
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                2, 95, 12, 27, 1, 91, 14, 92, 17, 84, 
                6, 26, 5, 85, 23, 92, 1, 81, 8, 84, 
                18, 64, 18, 68, 14, 66, 21, 27, 43, 112, 
                4, 70, 8, 86, 7, 125, 5, 125, 0, 91, 
                3, 83, 4, 66 }, "a0a5b4"));
        try {
          if (!this.mRemote.transact(6, parcel1, parcel2, 0) && IDeviceIdManager.Stub.getDefaultImpl() != null) {
            bool = IDeviceIdManager.Stub.getDefaultImpl().isCoolOs();
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
