package android.app;

import ILil.IL1Iii;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.accessibility.AccessibilityNodeInfo;
import java.util.List;

public interface IAccessibServiceProxy extends IInterface {
  AccessibilityNodeInfo getRootInActiveWindow() throws RemoteException;
  
  List<AccessibilityNodeInfo> getWindows() throws RemoteException;
  
  public static class Default implements IAccessibServiceProxy {
    public IBinder asBinder() {
      return null;
    }
    
    public AccessibilityNodeInfo getRootInActiveWindow() throws RemoteException {
      return null;
    }
    
    public List<AccessibilityNodeInfo> getWindows() throws RemoteException {
      return null;
    }
  }
  
  public static abstract class Stub extends Binder implements IAccessibServiceProxy {
    private static final String DESCRIPTOR = IL1Iii.IL1Iii(new byte[] { 
          89, 15, 86, 74, 14, 8, 92, 79, 83, 72, 
          17, 79, 113, 32, 81, 91, 4, 18, 75, 8, 
          80, 107, 4, 19, 78, 8, 81, 93, 49, 19, 
          87, 25, 75 }, "8a28aa");
    
    public static final int TRANSACTION_getRootInActiveWindow = 2;
    
    public static final int TRANSACTION_getWindows = 1;
    
    public Stub() {
      attachInterface(this, IL1Iii.IL1Iii(new byte[] { 
              86, 91, 83, 68, 93, 94, 83, 27, 86, 70, 
              66, 25, 126, 116, 84, 85, 87, 68, 68, 92, 
              85, 101, 87, 69, 65, 92, 84, 83, 98, 69, 
              88, 77, 78 }, "757627"));
    }
    
    public static IAccessibServiceProxy asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface(DESCRIPTOR);
      return (iInterface != null && iInterface instanceof IAccessibServiceProxy) ? (IAccessibServiceProxy)iInterface : new Proxy(param1IBinder);
    }
    
    public static IAccessibServiceProxy getDefaultImpl() {
      return Proxy.sDefaultImpl;
    }
    
    public static boolean setDefaultImpl(IAccessibServiceProxy param1IAccessibServiceProxy) {
      if (Proxy.sDefaultImpl == null) {
        if (param1IAccessibServiceProxy != null) {
          Proxy.sDefaultImpl = param1IAccessibServiceProxy;
          return true;
        } 
        return false;
      } 
      throw new IllegalStateException(IL1Iii.IL1Iii(new byte[] { 
              74, 81, 17, 116, 4, 7, 88, 65, 9, 68, 
              40, 12, 73, 88, 77, 25, 65, 2, 88, 88, 
              9, 85, 5, 65, 77, 67, 12, 83, 4 }, "94e0aa"));
    }
    
    public IBinder asBinder() {
      return (IBinder)this;
    }
    
    public boolean onTransact(int param1Int1, Parcel param1Parcel1, Parcel param1Parcel2, int param1Int2) throws RemoteException {
      AccessibilityNodeInfo accessibilityNodeInfo;
      String str = DESCRIPTOR;
      if (param1Int1 != 1) {
        if (param1Int1 != 2) {
          if (param1Int1 != 1598968902)
            return super.onTransact(param1Int1, param1Parcel1, param1Parcel2, param1Int2); 
          param1Parcel2.writeString(str);
          return true;
        } 
        param1Parcel1.enforceInterface(str);
        accessibilityNodeInfo = getRootInActiveWindow();
        param1Parcel2.writeNoException();
        if (accessibilityNodeInfo != null) {
          param1Parcel2.writeInt(1);
          accessibilityNodeInfo.writeToParcel(param1Parcel2, 1);
        } else {
          param1Parcel2.writeInt(0);
        } 
        return true;
      } 
      accessibilityNodeInfo.enforceInterface(str);
      List<AccessibilityNodeInfo> list = getWindows();
      param1Parcel2.writeNoException();
      param1Parcel2.writeTypedList(list);
      return true;
    }
    
    public static class Proxy implements IAccessibServiceProxy {
      public static IAccessibServiceProxy sDefaultImpl;
      
      private IBinder mRemote;
      
      public Proxy(IBinder param2IBinder) {
        this.mRemote = param2IBinder;
      }
      
      public IBinder asBinder() {
        return this.mRemote;
      }
      
      public String getInterfaceDescriptor() {
        return IL1Iii.IL1Iii(new byte[] { 
              7, 91, 1, 16, 92, 93, 2, 27, 4, 18, 
              67, 26, 47, 116, 6, 1, 86, 71, 21, 92, 
              7, 49, 86, 70, 16, 92, 6, 7, 99, 70, 
              9, 77, 28 }, "f5eb34");
      }
      
      public AccessibilityNodeInfo getRootInActiveWindow() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          AccessibilityNodeInfo accessibilityNodeInfo;
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  0, 8, 6, 65, 87, 80, 5, 72, 3, 67, 
                  72, 23, 40, 39, 1, 80, 93, 74, 18, 15, 
                  0, 96, 93, 75, 23, 15, 1, 86, 104, 75, 
                  14, 30, 27 }, "afb389"));
          if (!this.mRemote.transact(2, parcel1, parcel2, 0) && IAccessibServiceProxy.Stub.getDefaultImpl() != null) {
            accessibilityNodeInfo = IAccessibServiceProxy.Stub.getDefaultImpl().getRootInActiveWindow();
            return accessibilityNodeInfo;
          } 
          parcel2.readException();
          if (parcel2.readInt() != 0) {
            accessibilityNodeInfo = (AccessibilityNodeInfo)AccessibilityNodeInfo.CREATOR.createFromParcel(parcel2);
          } else {
            accessibilityNodeInfo = null;
          } 
          return accessibilityNodeInfo;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public List<AccessibilityNodeInfo> getWindows() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                  88, 94, 80, 17, 14, 8, 93, 30, 85, 19, 
                  17, 79, 112, 113, 87, 0, 4, 18, 74, 89, 
                  86, 48, 4, 19, 79, 89, 87, 6, 49, 19, 
                  86, 72, 77 }, "904caa"));
          if (!this.mRemote.transact(1, parcel1, parcel2, 0) && IAccessibServiceProxy.Stub.getDefaultImpl() != null)
            return IAccessibServiceProxy.Stub.getDefaultImpl().getWindows(); 
          parcel2.readException();
          return parcel2.createTypedArrayList(AccessibilityNodeInfo.CREATOR);
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
    }
  }
  
  public static class Proxy implements IAccessibServiceProxy {
    public static IAccessibServiceProxy sDefaultImpl;
    
    private IBinder mRemote;
    
    public Proxy(IBinder param1IBinder) {
      this.mRemote = param1IBinder;
    }
    
    public IBinder asBinder() {
      return this.mRemote;
    }
    
    public String getInterfaceDescriptor() {
      return IL1Iii.IL1Iii(new byte[] { 
            7, 91, 1, 16, 92, 93, 2, 27, 4, 18, 
            67, 26, 47, 116, 6, 1, 86, 71, 21, 92, 
            7, 49, 86, 70, 16, 92, 6, 7, 99, 70, 
            9, 77, 28 }, "f5eb34");
    }
    
    public AccessibilityNodeInfo getRootInActiveWindow() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        AccessibilityNodeInfo accessibilityNodeInfo;
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                0, 8, 6, 65, 87, 80, 5, 72, 3, 67, 
                72, 23, 40, 39, 1, 80, 93, 74, 18, 15, 
                0, 96, 93, 75, 23, 15, 1, 86, 104, 75, 
                14, 30, 27 }, "afb389"));
        if (!this.mRemote.transact(2, parcel1, parcel2, 0) && IAccessibServiceProxy.Stub.getDefaultImpl() != null) {
          accessibilityNodeInfo = IAccessibServiceProxy.Stub.getDefaultImpl().getRootInActiveWindow();
          return accessibilityNodeInfo;
        } 
        parcel2.readException();
        if (parcel2.readInt() != 0) {
          accessibilityNodeInfo = (AccessibilityNodeInfo)AccessibilityNodeInfo.CREATOR.createFromParcel(parcel2);
        } else {
          accessibilityNodeInfo = null;
        } 
        return accessibilityNodeInfo;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public List<AccessibilityNodeInfo> getWindows() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken(IL1Iii.IL1Iii(new byte[] { 
                88, 94, 80, 17, 14, 8, 93, 30, 85, 19, 
                17, 79, 112, 113, 87, 0, 4, 18, 74, 89, 
                86, 48, 4, 19, 79, 89, 87, 6, 49, 19, 
                86, 72, 77 }, "904caa"));
        if (!this.mRemote.transact(1, parcel1, parcel2, 0) && IAccessibServiceProxy.Stub.getDefaultImpl() != null)
          return IAccessibServiceProxy.Stub.getDefaultImpl().getWindows(); 
        parcel2.readException();
        return parcel2.createTypedArrayList(AccessibilityNodeInfo.CREATOR);
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
  }
}
