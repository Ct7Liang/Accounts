package android.develop.ct7liang.accounts.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018-02-22.
 *
 */
@Entity
public class Query {
    @Id
    private Long id;
    private String queryPassword;
    public String getQueryPassword() {
        return this.queryPassword;
    }
    public void setQueryPassword(String queryPassword) {
        this.queryPassword = queryPassword;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 335109719)
    public Query(Long id, String queryPassword) {
        this.id = id;
        this.queryPassword = queryPassword;
    }
    @Generated(hash = 1837957505)
    public Query() {
    }
}
