package android.content;

import ILil.IL1Iii;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IOnPrimaryClipChangedListener extends IInterface {
  void dispatchPrimaryClipChanged() throws RemoteException;
  
  public static class Default implements IOnPrimaryClipChangedListener {
    public IBinder asBinder() {
      return null;
    }
    
    public void dispatchPrimaryClipChanged() throws RemoteException {}
  }
  
  public static abstract class Stub extends Binder implements IOnPrimaryClipChangedListener {
    private static final String DESCRIPTOR = IL1Iii.IL1Iii(new byte[] { 
          87, 91, 0, 71, 86, 91, 82, 27, 7, 90, 
          87, 70, 83, 91, 16, 27, 112, 125, 88, 101, 
          22, 92, 84, 83, 68, 76, 39, 89, 80, 66, 
          117, 93, 5, 91, 94, 87, 82, 121, 13, 70, 
          77, 87, 88, 80, 22 }, "65d592");
    
    public static final int TRANSACTION_dispatchPrimaryClipChanged = 1;
    
    public Stub() {
      attachInterface(this, IL1Iii.IL1Iii(new byte[] { 
              3, 10, 84, 66, 93, 89, 6, 74, 83, 95, 
              92, 68, 7, 10, 68, 30, 123, Byte.MAX_VALUE, 12, 52, 
              66, 89, 95, 81, 16, 29, 115, 92, 91, 64, 
              33, 12, 81, 94, 85, 85, 6, 40, 89, 67, 
              70, 85, 12, 1, 66 }, "bd0020"));
    }
    
    public static IOnPrimaryClipChangedListener asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface(DESCRIPTOR);
      return (iInterface != null && iInterface instanceof IOnPrimaryClipChangedListener) ? (IOnPrimaryClipChangedListener)iInterface : new Proxy(param1IBinder);
    }
    
    public static IOnPrimaryClipChangedListener getDefaultImpl() {
      return Proxy.sDefaultImpl;
    }
    
    public static boolean setDefaultImpl(IOnPrimaryClipChangedListener param1IOnPrimaryClipChangedListener) {
      if (Proxy.sDefaultImpl == null) {
        if (param1IOnPrimaryClipChangedListener != null) {
          Proxy.sDefaultImpl = param1IOnPrimaryClipChangedListener;
          return true;
        } 
        return false;
      } 
      throw new IllegalStateException(IL1Iii.IL1Iii(new byte[] { 
              18, 80, 68, 119, 87, 87, 0, 64, 92, 71, 
              123, 92, 17, 89, 24, 26, 18, 82, 0, 89, 
              92, 86, 86, 17, 21, 66, 89, 80, 87 }, "a50321"));
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
      dispatchPrimaryClipChanged();
      return true;
    }
    
    public static class Proxy implements IOnPrimaryClipChangedListener {
      public static IOnPrimaryClipChangedListener sDefaultImpl;
      
      private IBinder mRemote;
      
      public Proxy(IBinder param2IBinder) {
        this.mRemote = param2IBinder;
      }
      
      public IBinder asBinder() {
        return this.mRemote;
      }
      
      public void dispatchPrimaryClipChanged() throws RemoteException {
        Exception exception;
        Parcel parcel = Parcel.obtain();
        try {
          parcel.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  84, 93, 81, 69, 10, 81, 81, 29, 86, 88, 
                  11, 76, 80, 93, 65, 25, 44, 119, 91, 99, 
                  71, 94, 8, 89, 71, 74, 118, 91, 12, 72, 
                  118, 91, 84, 89, 2, 93, 81, Byte.MAX_VALUE, 92, 68, 
                  17, 93, 91, 86, 71 }, "5357e8"));
          try {
            if (!this.mRemote.transact(1, parcel, null, 1) && IOnPrimaryClipChangedListener.Stub.getDefaultImpl() != null) {
              IOnPrimaryClipChangedListener.Stub.getDefaultImpl().dispatchPrimaryClipChanged();
              parcel.recycle();
              return;
            } 
            parcel.recycle();
            return;
          } finally {}
        } finally {}
        parcel.recycle();
        throw exception;
      }
      
      public String getInterfaceDescriptor() {
        return IL1Iii.IL1Iii(new byte[] { 
              4, 87, 82, 22, 14, 91, 1, 23, 85, 11, 
              15, 70, 0, 87, 66, 74, 40, 125, 11, 105, 
              68, 13, 12, 83, 23, 64, 117, 8, 8, 66, 
              38, 81, 87, 10, 6, 87, 1, 117, 95, 23, 
              21, 87, 11, 92, 68 }, "e96da2");
      }
    }
  }
  
  public static class Proxy implements IOnPrimaryClipChangedListener {
    public static IOnPrimaryClipChangedListener sDefaultImpl;
    
    private IBinder mRemote;
    
    public Proxy(IBinder param1IBinder) {
      this.mRemote = param1IBinder;
    }
    
    public IBinder asBinder() {
      return this.mRemote;
    }
    
    public void dispatchPrimaryClipChanged() throws RemoteException {
      Exception exception;
      Parcel parcel = Parcel.obtain();
      try {
        parcel.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                84, 93, 81, 69, 10, 81, 81, 29, 86, 88, 
                11, 76, 80, 93, 65, 25, 44, 119, 91, 99, 
                71, 94, 8, 89, 71, 74, 118, 91, 12, 72, 
                118, 91, 84, 89, 2, 93, 81, Byte.MAX_VALUE, 92, 68, 
                17, 93, 91, 86, 71 }, "5357e8"));
        try {
          if (!this.mRemote.transact(1, parcel, null, 1) && IOnPrimaryClipChangedListener.Stub.getDefaultImpl() != null) {
            IOnPrimaryClipChangedListener.Stub.getDefaultImpl().dispatchPrimaryClipChanged();
            parcel.recycle();
            return;
          } 
          parcel.recycle();
          return;
        } finally {}
      } finally {}
      parcel.recycle();
      throw exception;
    }
    
    public String getInterfaceDescriptor() {
      return IL1Iii.IL1Iii(new byte[] { 
            4, 87, 82, 22, 14, 91, 1, 23, 85, 11, 
            15, 70, 0, 87, 66, 74, 40, 125, 11, 105, 
            68, 13, 12, 83, 23, 64, 117, 8, 8, 66, 
            38, 81, 87, 10, 6, 87, 1, 117, 95, 23, 
            21, 87, 11, 92, 68 }, "e96da2");
    }
  }
}
