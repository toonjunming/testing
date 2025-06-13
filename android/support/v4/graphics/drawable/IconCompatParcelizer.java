package android.support.v4.graphics.drawable;

import androidx.annotation.RestrictTo;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.graphics.drawable.IconCompatParcelizer;
import androidx.versionedparcelable.VersionedParcel;

@RestrictTo({RestrictTo.Scope.LIBRARY})
public final class IconCompatParcelizer extends IconCompatParcelizer {
  public static IconCompat read(VersionedParcel paramVersionedParcel) {
    return IconCompatParcelizer.read(paramVersionedParcel);
  }
  
  public static void write(IconCompat paramIconCompat, VersionedParcel paramVersionedParcel) {
    IconCompatParcelizer.write(paramIconCompat, paramVersionedParcel);
  }
}
