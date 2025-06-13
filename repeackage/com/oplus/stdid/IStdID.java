package repeackage.com.oplus.stdid;

import ILil.IL1Iii;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import repeackage.com.heytap.openid.IOpenID;

public interface IStdID extends IOpenID {
  public static abstract class Stub extends IOpenID.Stub {
    private static final String DESCRIPTOR = IL1Iii.IL1Iii(new byte[] { 
          5, 88, 9, 77, 87, 71, 10, 66, 23, 77, 
          75, 67, 2, 94, 0, 77, 113, 100, 18, 83, 
          45, 39 }, "f7dc87");
    
    public static final int TRANSACTION_getSerID = 1;
    
    public Stub() {
      attachInterface(this, IL1Iii.IL1Iii(new byte[] { 
              81, 10, 93, 28, 10, 68, 94, 16, 67, 28, 
              22, 64, 86, 12, 84, 28, 44, 103, 70, 1, 
              121, 118 }, "2e02e4"));
    }
    
    public static IStdID asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface(DESCRIPTOR);
      return (iInterface != null && iInterface instanceof IStdID) ? (IStdID)iInterface : new Proxy(param1IBinder);
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
    
    public static class Proxy implements IStdID {
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
              91, 86, 8, 74, 13, 70, 84, 76, 22, 74, 
              17, 66, 92, 80, 1, 74, 43, 101, 76, 93, 
              44, 32 }, "89edb6");
      }
      
      public String getSerID(String param2String1, String param2String2, String param2String3) throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  7, 13, 15, 72, 9, 67, 8, 23, 17, 72, 
                  21, 71, 0, 11, 6, 72, 47, 96, 16, 6, 
                  43, 34 }, "dbbff3"));
          parcel2.writeString(param2String1);
          parcel2.writeString(param2String2);
          parcel2.writeString(param2String3);
          try {
            if (!this.mRemote.transact(1, parcel2, parcel1, 0) && IStdID.Stub.getDefaultImpl() != null) {
              param2String1 = IStdID.Stub.getDefaultImpl().getSerID(param2String1, param2String2, param2String3);
              parcel1.recycle();
              parcel2.recycle();
              return param2String1;
            } 
            parcel1.readException();
            param2String1 = parcel1.readString();
            parcel1.recycle();
            parcel2.recycle();
            return param2String1;
          } finally {}
        } finally {}
        parcel1.recycle();
        parcel2.recycle();
        throw param2String1;
      }
    }
  }
  
  public static class Proxy implements IStdID {
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
            91, 86, 8, 74, 13, 70, 84, 76, 22, 74, 
            17, 66, 92, 80, 1, 74, 43, 101, 76, 93, 
            44, 32 }, "89edb6");
    }
    
    public String getSerID(String param1String1, String param1String2, String param1String3) throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                7, 13, 15, 72, 9, 67, 8, 23, 17, 72, 
                21, 71, 0, 11, 6, 72, 47, 96, 16, 6, 
                43, 34 }, "dbbff3"));
        parcel2.writeString(param1String1);
        parcel2.writeString(param1String2);
        parcel2.writeString(param1String3);
        try {
          if (!this.mRemote.transact(1, parcel2, parcel1, 0) && IStdID.Stub.getDefaultImpl() != null) {
            param1String1 = IStdID.Stub.getDefaultImpl().getSerID(param1String1, param1String2, param1String3);
            parcel1.recycle();
            parcel2.recycle();
            return param1String1;
          } 
          parcel1.readException();
          param1String1 = parcel1.readString();
          parcel1.recycle();
          parcel2.recycle();
          return param1String1;
        } finally {}
      } finally {}
      parcel1.recycle();
      parcel2.recycle();
      throw param1String1;
    }
  }
}
