package com.sysapps.utils;
import android.content.Context;
import java.io.IOException;
import java.lang.ClassNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public final class Storage {

   private Storage() {}

   public static void writeObject(Context context, String key, Object object) throws IOException {
      FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(object);
      oos.close();
      fos.close();
   }

   public static Object readObject(Context context, String key) throws IOException,
         ClassNotFoundException {
      FileInputStream fis = context.openFileInput(key);
      ObjectInputStream ois = new ObjectInputStream(fis);
      Object object = ois.readObject();
      return object;
   }
}