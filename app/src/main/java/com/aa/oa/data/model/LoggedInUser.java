package com.aa.oa.data.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 5830137046070355393L;
    private Integer userId;

    private String empId;
    /** 部门ID */

    private Long deptId;
    private String deptName;
    /** 部门父ID */
    private Long parentId;

    /** 登录名称 */
    private String loginName;

    /** 用户名称 */
    private String userName;
    private String text;

    /** 用户邮箱 */
    private String email;

    private Date birthday;

    private String address;

    private String officePhone;
    /** 手机号码 */
    private String phonenumber;

    /** 用户性别 */
    private String sex;

    /** 用户头像 */
    private String avatar;

    /** 密码 */
    private String password;
    /** 角色名称 */
    private String rolesName;
    public LoggedInUser(Integer userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
    public LoggedInUser() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRolesName() {
        return rolesName;
    }

    public void setRolesName(String rolesName) {
        this.rolesName = rolesName;
    }

    @Override
    public String toString() {
        return "LoggedInUser{" +
            "userId='" + userId + '\'' +
            ", empId='" + empId + '\'' +
            ", deptId=" + deptId +
            ", deptName='" + deptName + '\'' +
            ", parentId=" + parentId +
            ", loginName='" + loginName + '\'' +
            ", userName='" + userName + '\'' +
            ", text='" + text + '\'' +
            ", email='" + email + '\'' +
            ", birthday=" + birthday +
            ", address='" + address + '\'' +
            ", officePhone='" + officePhone + '\'' +
            ", phonenumber='" + phonenumber + '\'' +
            ", sex='" + sex + '\'' +
            ", avatar='" + avatar + '\'' +
            ", password='" + password + '\'' +
            ", rolesName='" + rolesName + '\'' +
            '}';
    }
}
