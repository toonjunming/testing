package android.app;

import ILil.IL1Iii;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IApplicationThread extends IInterface {
  public static class Default implements IApplicationThread {
    public IBinder asBinder() {
      return null;
    }
  }
  
  public static abstract class Stub extends Binder implements IApplicationThread {
    private static final String DESCRIPTOR = IL1Iii.IL1Iii(new byte[] { 
          83, 89, 6, 20, 88, 80, 86, 25, 3, 22, 
          71, 23, 123, 118, 18, 22, 91, 80, 81, 86, 
          22, 15, 88, 87, 102, 95, 16, 3, 86, 93 }, "27bf79");
    
    public Stub() {
      attachInterface(this, IL1Iii.IL1Iii(new byte[] { 
              88, 95, 87, 19, 89, 81, 93, 31, 82, 17, 
              70, 22, 112, 112, 67, 17, 90, 81, 90, 80, 
              71, 8, 89, 86, 109, 89, 65, 4, 87, 92 }, "913a68"));
    }
    
    public static IApplicationThread asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface(DESCRIPTOR);
      return (iInterface != null && iInterface instanceof IApplicationThread) ? (IApplicationThread)iInterface : new Proxy(param1IBinder);
    }
    
    public static IApplicationThread getDefaultImpl() {
      return Proxy.sDefaultImpl;
    }
    
    public static boolean setDefaultImpl(IApplicationThread param1IApplicationThread) {
      if (Proxy.sDefaultImpl == null) {
        if (param1IApplicationThread != null) {
          Proxy.sDefaultImpl = param1IApplicationThread;
          return true;
        } 
        return false;
      } 
      throw new IllegalStateException(IL1Iii.IL1Iii(new byte[] { 
              71, 92, 17, 32, 93, 85, 85, 76, 9, 16, 
              113, 94, 68, 85, 77, 77, 24, 80, 85, 85, 
              9, 1, 92, 19, 64, 78, 12, 7, 93 }, "49ed83"));
    }
    
    public IBinder asBinder() {
      return (IBinder)this;
    }
    
    public boolean onTransact(int param1Int1, Parcel param1Parcel1, Parcel param1Parcel2, int param1Int2) throws RemoteException {
      String str = DESCRIPTOR;
      if (param1Int1 != 1598968902)
        return super.onTransact(param1Int1, param1Parcel1, param1Parcel2, param1Int2); 
      param1Parcel2.writeString(str);
      return true;
    }
    
    public static class Proxy implements IApplicationThread {
      public static IApplicationThread sDefaultImpl;
      
      private IBinder mRemote;
      
      public Proxy(IBinder param2IBinder) {
        this.mRemote = param2IBinder;
      }
      
      public IBinder asBinder() {
        return this.mRemote;
      }
      
      public String getInterfaceDescriptor() {
        return IL1Iii.IL1Iii(new byte[] { 
              81, 87, 87, 75, 86, 89, 84, 23, 82, 73, 
              73, 30, 121, 120, 67, 73, 85, 89, 83, 88, 
              71, 80, 86, 94, 100, 81, 65, 92, 88, 84 }, "093990");
      }
    }
  }
  
  public static class Proxy implements IApplicationThread {
    public static IApplicationThread sDefaultImpl;
    
    private IBinder mRemote;
    
    public Proxy(IBinder param1IBinder) {
      this.mRemote = param1IBinder;
    }
    
    public IBinder asBinder() {
      return this.mRemote;
    }
    
    public String getInterfaceDescriptor() {
      return IL1Iii.IL1Iii(new byte[] { 
            81, 87, 87, 75, 86, 89, 84, 23, 82, 73, 
            73, 30, 121, 120, 67, 73, 85, 89, 83, 88, 
            71, 80, 86, 94, 100, 81, 65, 92, 88, 84 }, "093990");
    }
  }
}
