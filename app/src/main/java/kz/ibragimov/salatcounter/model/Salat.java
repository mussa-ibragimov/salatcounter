package kz.ibragimov.salatcounter.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;

/**
 *
 * Created by musa on 7/18/16.
 */
@Table(name = Salat.ENTITY_NAME)
public class Salat extends Model {
    public static final String ENTITY_NAME = "Salats";
    @Column(name = "Name")
    public String name;
    @Column(name = "Count")
    public int count;
    @Column(name = "Last_Updated")
    public long lastUpdated;

    public static List<Salat> getAll() {
        return new Select()
                .from(Salat.class)
                .execute();
    }

    public static long save(Salat salat, String name, String count) {
        if (validData(name, count)) {
            salat.name = name;
            salat.count = Integer.parseInt(count);
            salat.lastUpdated = new Date().getTime();
            return salat.save();
        } else {
            throw new RuntimeException();
        }
    }

    private static boolean validData(String name, String count) {
        return name !=  null && !name.isEmpty() && count != null && !count.isEmpty();
    }

    public static long delete(Salat salat) {
        long id = salat.getId();
        salat.delete();
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Salat) {
            Salat other = (Salat) obj;
            return this.getId().equals(other.getId());
        }
        return false;
    }
}
