package android.develop.ct7liang.accounts.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018-02-22.
 *
 */
@Entity
public class Account implements Serializable{
    @Id
    private Long id;
    private String tag;
    private String account;
    private String password;
    private String remark;
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getTag() {
        return this.tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 103840469)
    public Account(Long id, String tag, String account, String password,
            String remark) {
        this.id = id;
        this.tag = tag;
        this.account = account;
        this.password = password;
        this.remark = remark;
    }
    @Generated(hash = 882125521)
    public Account() {
    }
}
