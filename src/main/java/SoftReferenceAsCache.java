import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * Created by vincent on 4/6/16.
 */
public class SoftReferenceAsCache {
    byte[] bytes;
    SoftReferenceAsCache() {
        bytes = new byte[5*1024*1024];
    }
    public String toString() {
        return "test" + bytes.length;
    }
    public static void main(String[] args) throws InterruptedException {
        Object object = new Object();
        WeakReference<Object> ref = new WeakReference<Object>(object);
        object = null;
        if (ref != null) {                 // ref can get collected at any time...
            System.out.println(ref.get()); // Now what?!
            System.gc();                   // Let's assume ref gets collected here.
            System.out.println(ref.get()); // Now what?!
        }

        SoftReferenceAsCache anotherObject = new SoftReferenceAsCache();
        SoftReference<SoftReferenceAsCache> sfRef = new SoftReference<SoftReferenceAsCache>(anotherObject);
        anotherObject = null;
        if (sfRef != null) {
            System.out.println(sfRef.get());
            System.gc();
            System.gc();
            System.gc();
            byte[] tmp = new byte[3*1024*1024];
            tmp[124*1024 -1 ] = '1';
            System.out.println(sfRef.get());
        }
    }
}
