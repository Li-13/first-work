package com.zyy.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@TableName("sys_user")
public class User implements UserDetails {
    private static final long serialVersionUID = 242146703513492331L;

    @TableId(type = IdType.AUTO)
    private Integer userId;  // 用户id

    private String username;  // 账号

    private String password;  // 密码

    private String nickName;  // 昵称

    private String sex;  // 性别

    private String phone;  // 手机号

    private Date birthday;  // 出生日期

    private Integer state;  // 状态，0正常，1冻结

    private Date createTime;  // 注册时间

    private Date updateTime;  // 修改时间

    @TableField(exist = false)
    private List<Authorities> authorities;  // 权限

    @TableField(exist = false)
    private List<Role> roles;  // 角色

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setAuthorities(List<Authorities> authorities) {
        this.authorities = authorities;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // 账户是否未过期
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.state != 1;  // 账户是否未锁定
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // 凭证(密码)是否未过期
    }

    @Override
    public boolean isEnabled() {
        return true;  // 用户是否启用
    }
}
