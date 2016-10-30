package kz.ibragimov.salatcounter.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

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
}
