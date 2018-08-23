package android.develop.ct7liang.accounts.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018-02-22.
 *
 */
@Entity
public class User {
    @Id
    private Long id;
    private String password;
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1279343936)
    public User(Long id, String password) {
        this.id = id;
        this.password = password;
    }
    @Generated(hash = 586692638)
    public User() {
    }
}