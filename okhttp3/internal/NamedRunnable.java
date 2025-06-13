package okhttp3.internal;

public abstract class NamedRunnable implements Runnable {
  public final String name;
  
  public NamedRunnable(String paramString, Object... paramVarArgs) {
    this.name = Util.format(paramString, paramVarArgs);
  }
  
  public abstract void execute();
  
  public final void run() {
    String str = Thread.currentThread().getName();
    Thread.currentThread().setName(this.name);
    try {
      execute();
      return;
    } finally {
      Thread.currentThread().setName(str);
    } 
  }
}
