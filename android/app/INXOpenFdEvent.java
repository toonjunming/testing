package android.app;

import ILil.IL1Iii;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

public interface INXOpenFdEvent extends IInterface {
  boolean onOpend(ParcelFileDescriptor paramParcelFileDescriptor) throws RemoteException;
  
  public static class Default implements INXOpenFdEvent {
    public IBinder asBinder() {
      return null;
    }
    
    public boolean onOpend(ParcelFileDescriptor param1ParcelFileDescriptor) throws RemoteException {
      return false;
    }
  }
  
  public static abstract class Stub extends Binder implements INXOpenFdEvent {
    private static final String DESCRIPTOR = IL1Iii.IL1Iii(new byte[] { 
          2, 93, 93, 23, 88, 89, 7, 29, 88, 21, 
          71, 30, 42, 125, 97, 42, 71, 85, 13, 117, 
          93, 32, 65, 85, 13, 71 }, "c39e70");
    
    public static final int TRANSACTION_onOpend = 1;
    
    public Stub() {
      attachInterface(this, IL1Iii.IL1Iii(new byte[] { 
              85, 92, 84, 75, 91, 95, 80, 28, 81, 73, 
              68, 24, 125, 124, 104, 118, 68, 83, 90, 116, 
              84, 124, 66, 83, 90, 70 }, "420946"));
    }
    
    public static INXOpenFdEvent asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface(DESCRIPTOR);
      return (iInterface != null && iInterface instanceof INXOpenFdEvent) ? (INXOpenFdEvent)iInterface : new Proxy(param1IBinder);
    }
    
    public static INXOpenFdEvent getDefaultImpl() {
      return Proxy.sDefaultImpl;
    }
    
    public static boolean setDefaultImpl(INXOpenFdEvent param1INXOpenFdEvent) {
      if (Proxy.sDefaultImpl == null) {
        if (param1INXOpenFdEvent != null) {
          Proxy.sDefaultImpl = param1INXOpenFdEvent;
          return true;
        } 
        return false;
      } 
      throw new IllegalStateException(IL1Iii.IL1Iii(new byte[] { 
              18, 83, 76, 112, 3, 3, 0, 67, 84, 64, 
              47, 8, 17, 90, 16, 29, 70, 6, 0, 90, 
              84, 81, 2, 69, 21, 65, 81, 87, 3 }, "a684fe"));
    }
    
    public IBinder asBinder() {
      return (IBinder)this;
    }
    
    public boolean onTransact(int param1Int1, Parcel param1Parcel1, Parcel param1Parcel2, int param1Int2) throws RemoteException {
      String str = DESCRIPTOR;
      if (param1Int1 != 1) {
        if (param1Int1 != 1598968902)
          return super.onTransact(param1Int1, param1Parcel1, param1Parcel2, param1Int2); 
        param1Parcel2.writeString(str);
        return true;
      } 
      param1Parcel1.enforceInterface(str);
      if (param1Parcel1.readInt() != 0) {
        ParcelFileDescriptor parcelFileDescriptor = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(param1Parcel1);
      } else {
        param1Parcel1 = null;
      } 
      boolean bool = onOpend((ParcelFileDescriptor)param1Parcel1);
      param1Parcel2.writeNoException();
      param1Parcel2.writeInt(bool);
      return true;
    }
    
    public static class Proxy implements INXOpenFdEvent {
      public static INXOpenFdEvent sDefaultImpl;
      
      private IBinder mRemote;
      
      public Proxy(IBinder param2IBinder) {
        this.mRemote = param2IBinder;
      }
      
      public IBinder asBinder() {
        return this.mRemote;
      }
      
      public String getInterfaceDescriptor() {
        return IL1Iii.IL1Iii(new byte[] { 
              87, 8, 83, 23, 12, 15, 82, 72, 86, 21, 
              19, 72, Byte.MAX_VALUE, 40, 111, 42, 19, 3, 88, 32, 
              83, 32, 21, 3, 88, 18 }, "6f7ecf");
      }
      
      public boolean onOpend(ParcelFileDescriptor param2ParcelFileDescriptor) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        boolean bool = false;
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  0, 15, 93, 22, 95, 81, 5, 79, 88, 20, 
                  64, 22, 40, 47, 97, 43, 64, 93, 15, 39, 
                  93, 33, 70, 93, 15, 21 }, "aa9d08"));
          if (param2ParcelFileDescriptor != null) {
            parcel1.writeInt(1);
            param2ParcelFileDescriptor.writeToParcel(parcel1, 0);
          } else {
            parcel1.writeInt(0);
          } 
          if (!this.mRemote.transact(1, parcel1, parcel2, 0) && INXOpenFdEvent.Stub.getDefaultImpl() != null) {
            bool = INXOpenFdEvent.Stub.getDefaultImpl().onOpend(param2ParcelFileDescriptor);
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
  
  public static class Proxy implements INXOpenFdEvent {
    public static INXOpenFdEvent sDefaultImpl;
    
    private IBinder mRemote;
    
    public Proxy(IBinder param1IBinder) {
      this.mRemote = param1IBinder;
    }
    
    public IBinder asBinder() {
      return this.mRemote;
    }
    
    public String getInterfaceDescriptor() {
      return IL1Iii.IL1Iii(new byte[] { 
            87, 8, 83, 23, 12, 15, 82, 72, 86, 21, 
            19, 72, Byte.MAX_VALUE, 40, 111, 42, 19, 3, 88, 32, 
            83, 32, 21, 3, 88, 18 }, "6f7ecf");
    }
    
    public boolean onOpend(ParcelFileDescriptor param1ParcelFileDescriptor) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      boolean bool = false;
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                0, 15, 93, 22, 95, 81, 5, 79, 88, 20, 
                64, 22, 40, 47, 97, 43, 64, 93, 15, 39, 
                93, 33, 70, 93, 15, 21 }, "aa9d08"));
        if (param1ParcelFileDescriptor != null) {
          parcel1.writeInt(1);
          param1ParcelFileDescriptor.writeToParcel(parcel1, 0);
        } else {
          parcel1.writeInt(0);
        } 
        if (!this.mRemote.transact(1, parcel1, parcel2, 0) && INXOpenFdEvent.Stub.getDefaultImpl() != null) {
          bool = INXOpenFdEvent.Stub.getDefaultImpl().onOpend(param1ParcelFileDescriptor);
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
