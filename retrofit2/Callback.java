package retrofit2;

public interface Callback<T> {
  void onFailure(Call<T> paramCall, Throwable paramThrowable);
  
  void onResponse(Call<T> paramCall, Response<T> paramResponse);
}
