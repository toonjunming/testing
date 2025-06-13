package android.view;

import ILil.IL1Iii;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IRotationWatcher extends IInterface {
  void onRotationChanged(int paramInt) throws RemoteException;
  
  public static class Default implements IRotationWatcher {
    public IBinder asBinder() {
      return null;
    }
    
    public void onRotationChanged(int param1Int) throws RemoteException {}
  }
  
  public static abstract class Stub extends Binder implements IRotationWatcher {
    private static final String DESCRIPTOR = IL1Iii.IL1Iii(new byte[] { 
          84, 8, 2, 64, 91, 81, 81, 72, 16, 91, 
          81, 79, 27, 47, 52, 93, 64, 89, 65, 15, 
          9, 92, 99, 89, 65, 5, 14, 87, 70 }, "5ff248");
    
    public static final int TRANSACTION_onRotationChanged = 1;
    
    public Stub() {
      attachInterface(this, IL1Iii.IL1Iii(new byte[] { 
              89, 15, 92, 74, 91, 10, 92, 79, 78, 81, 
              81, 20, 22, 40, 106, 87, 64, 2, 76, 8, 
              87, 86, 99, 2, 76, 2, 80, 93, 70 }, "8a884c"));
    }
    
    public static IRotationWatcher asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface(DESCRIPTOR);
      return (iInterface != null && iInterface instanceof IRotationWatcher) ? (IRotationWatcher)iInterface : new Proxy(param1IBinder);
    }
    
    public static IRotationWatcher getDefaultImpl() {
      return Proxy.sDefaultImpl;
    }
    
    public static boolean setDefaultImpl(IRotationWatcher param1IRotationWatcher) {
      if (Proxy.sDefaultImpl == null) {
        if (param1IRotationWatcher != null) {
          Proxy.sDefaultImpl = param1IRotationWatcher;
          return true;
        } 
        return false;
      } 
      throw new IllegalStateException(IL1Iii.IL1Iii(new byte[] { 
              18, 92, 67, 118, 3, 5, 0, 76, 91, 70, 
              47, 14, 17, 85, 31, 27, 70, 0, 0, 85, 
              91, 87, 2, 67, 21, 78, 94, 81, 3 }, "a972fc"));
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
      onRotationChanged(param1Parcel1.readInt());
      param1Parcel2.writeNoException();
      return true;
    }
    
    public static class Proxy implements IRotationWatcher {
      public static IRotationWatcher sDefaultImpl;
      
      private IBinder mRemote;
      
      public Proxy(IBinder param2IBinder) {
        this.mRemote = param2IBinder;
      }
      
      public IBinder asBinder() {
        return this.mRemote;
      }
      
      public String getInterfaceDescriptor() {
        return IL1Iii.IL1Iii(new byte[] { 
              83, 11, 0, 65, 13, 88, 86, 75, 18, 90, 
              7, 70, 28, 44, 54, 92, 22, 80, 70, 12, 
              11, 93, 53, 80, 70, 6, 12, 86, 16 }, "2ed3b1");
      }
      
      public void onRotationChanged(int param2Int) throws RemoteException {
        Parcel parcel2 = Parcel.obtain();
        Parcel parcel1 = Parcel.obtain();
        try {
          parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  5, 86, 93, 16, 95, 94, 0, 22, 79, 11, 
                  85, 64, 74, 113, 107, 13, 68, 86, 16, 81, 
                  86, 12, 103, 86, 16, 91, 81, 7, 66 }, "d89b07"));
          parcel2.writeInt(param2Int);
          if (!this.mRemote.transact(1, parcel2, parcel1, 0) && IRotationWatcher.Stub.getDefaultImpl() != null) {
            IRotationWatcher.Stub.getDefaultImpl().onRotationChanged(param2Int);
            return;
          } 
          parcel1.readException();
          return;
        } finally {
          parcel1.recycle();
          parcel2.recycle();
        } 
      }
    }
  }
  
  public static class Proxy implements IRotationWatcher {
    public static IRotationWatcher sDefaultImpl;
    
    private IBinder mRemote;
    
    public Proxy(IBinder param1IBinder) {
      this.mRemote = param1IBinder;
    }
    
    public IBinder asBinder() {
      return this.mRemote;
    }
    
    public String getInterfaceDescriptor() {
      return IL1Iii.IL1Iii(new byte[] { 
            83, 11, 0, 65, 13, 88, 86, 75, 18, 90, 
            7, 70, 28, 44, 54, 92, 22, 80, 70, 12, 
            11, 93, 53, 80, 70, 6, 12, 86, 16 }, "2ed3b1");
    }
    
    public void onRotationChanged(int param1Int) throws RemoteException {
      Parcel parcel2 = Parcel.obtain();
      Parcel parcel1 = Parcel.obtain();
      try {
        parcel2.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                5, 86, 93, 16, 95, 94, 0, 22, 79, 11, 
                85, 64, 74, 113, 107, 13, 68, 86, 16, 81, 
                86, 12, 103, 86, 16, 91, 81, 7, 66 }, "d89b07"));
        parcel2.writeInt(param1Int);
        if (!this.mRemote.transact(1, parcel2, parcel1, 0) && IRotationWatcher.Stub.getDefaultImpl() != null) {
          IRotationWatcher.Stub.getDefaultImpl().onRotationChanged(param1Int);
          return;
        } 
        parcel1.readException();
        return;
      } finally {
        parcel1.recycle();
        parcel2.recycle();
      } 
    }
  }
}
