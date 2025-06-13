package repeackage.com.heytap.openid;

import ILil.IL1Iii;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IOpenID extends IInterface {
  String getSerID(String paramString1, String paramString2, String paramString3) throws RemoteException;
  
  public static class Default implements IOpenID {
    public IBinder asBinder() {
      return null;
    }
    
    public String getSerID(String param1String1, String param1String2, String param1String3) throws RemoteException {
      return null;
    }
  }
  
  public static abstract class Stub extends Binder implements IOpenID {
    private static final String DESCRIPTOR = IL1Iii.IL1Iii(new byte[] { 
          90, 13, 11, 79, 11, 92, 64, 22, 7, 17, 
          77, 86, 73, 7, 8, 8, 7, 23, 112, 45, 
          22, 4, 13, 112, 125 }, "9bfac9");
    
    public static final int TRANSACTION_getSerID = 1;
    
    public Stub() {
      attachInterface(this, IL1Iii.IL1Iii(new byte[] { 
              1, 89, 92, 27, 12, 86, 27, 66, 80, 69, 
              74, 92, 18, 83, 95, 92, 0, 29, 43, 121, 
              65, 80, 10, 122, 38 }, "b615d3"));
    }
    
    public static IOpenID asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface(DESCRIPTOR);
      return (iInterface != null && iInterface instanceof IOpenID) ? (IOpenID)iInterface : new Proxy(param1IBinder);
    }
    
    public static IOpenID getDefaultImpl() {
      return Proxy.sDefaultImpl;
    }
    
    public static boolean setDefaultImpl(IOpenID param1IOpenID) {
      if (Proxy.sDefaultImpl == null && param1IOpenID != null) {
        Proxy.sDefaultImpl = param1IOpenID;
        return true;
      } 
      return false;
    }
    
    public IBinder asBinder() {
      return (IBinder)this;
    }
    
    public boolean onTransact(int param1Int1, Parcel param1Parcel1, Parcel param1Parcel2, int param1Int2) throws RemoteException {
      String str2 = DESCRIPTOR;
      if (param1Int1 != 1) {
        if (param1Int1 != 1598968902)
          return super.onTransact(param1Int1, param1Parcel1, param1Parcel2, param1Int2); 
        param1Parcel2.writeString(str2);
        return true;
      } 
      param1Parcel1.enforceInterface(str2);
      String str1 = getSerID(param1Parcel1.readString(), param1Parcel1.readString(), param1Parcel1.readString());
      param1Parcel2.writeNoException();
      param1Parcel2.writeString(str1);
      return true;
    }
    
    public static class Proxy implements IOpenID {
      public static IOpenID sDefaultImpl;
      
      private IBinder mRemote;
      
      public Proxy(IBinder param2IBinder) {
        this.mRemote = param2IBinder;
      }
      
      public IBinder asBinder() {
        return this.mRemote;
      }
      
      public String getInterfaceDescriptor() {
        return IL1Iii.IL1Iii(new byte[] { 
              1, 14, 88, 77, 14, 83, 27, 21, 84, 19, 
              72, 89, 18, 4, 91, 10, 2, 24, 43, 46, 
              69, 6, 8, Byte.MAX_VALUE, 38 }, "ba5cf6");
      }
      
      public String getSerID(String param2String1, String param2String2, String param2String3) throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  81, 87, 11, 79, 88, 3, 75, 76, 7, 17, 
                  30, 9, 66, 93, 8, 8, 84, 72, 123, 119, 
                  22, 4, 94, 47, 118 }, "28fa0f"));
          parcel2.writeString(param2String1);
          parcel2.writeString(param2String2);
          parcel2.writeString(param2String3);
          if (!this.mRemote.transact(1, parcel2, parcel1, 0) && IOpenID.Stub.getDefaultImpl() != null) {
            param2String1 = IOpenID.Stub.getDefaultImpl().getSerID(param2String1, param2String2, param2String3);
            return param2String1;
          } 
          parcel1.readException();
          param2String1 = parcel1.readString();
          return param2String1;
        } finally {
          parcel1.recycle();
          parcel2.recycle();
        } 
      }
    }
  }
  
  public static class Proxy implements IOpenID {
    public static IOpenID sDefaultImpl;
    
    private IBinder mRemote;
    
    public Proxy(IBinder param1IBinder) {
      this.mRemote = param1IBinder;
    }
    
    public IBinder asBinder() {
      return this.mRemote;
    }
    
    public String getInterfaceDescriptor() {
      return IL1Iii.IL1Iii(new byte[] { 
            1, 14, 88, 77, 14, 83, 27, 21, 84, 19, 
            72, 89, 18, 4, 91, 10, 2, 24, 43, 46, 
            69, 6, 8, Byte.MAX_VALUE, 38 }, "ba5cf6");
    }
    
    public String getSerID(String param1String1, String param1String2, String param1String3) throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                81, 87, 11, 79, 88, 3, 75, 76, 7, 17, 
                30, 9, 66, 93, 8, 8, 84, 72, 123, 119, 
                22, 4, 94, 47, 118 }, "28fa0f"));
        parcel2.writeString(param1String1);
        parcel2.writeString(param1String2);
        parcel2.writeString(param1String3);
        if (!this.mRemote.transact(1, parcel2, parcel1, 0) && IOpenID.Stub.getDefaultImpl() != null) {
          param1String1 = IOpenID.Stub.getDefaultImpl().getSerID(param1String1, param1String2, param1String3);
          return param1String1;
        } 
        parcel1.readException();
        param1String1 = parcel1.readString();
        return param1String1;
      } finally {
        parcel1.recycle();
        parcel2.recycle();
      } 
    }
  }
}
