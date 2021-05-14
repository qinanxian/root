package com.vekai.auth.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Table(name = "AUTH_ROLE_PERMIT")
public class RolePermit implements Serializable,Cloneable {
    /** 流水号 */
    @Id
    @GeneratedValue
    private String id ;
    /** 角色ID */
    private String roleId ;
    /** 权限代码 */
    private String permitCode ;
    /** 权限类型 */
    private String permitType ;
    /** 关联类型 */
    private String ownerType ;
    /** 乐观锁 */
    private Integer revision ;
    /** 创建人 */
    private String createdBy ;
    /** 创建时间 */
    private Date createdTime ;
    /** 更新人 */
    private String updatedBy ;
    /** 更新时间 */
    private Date updatedTime ;

    public RolePermit(){

    }

    public RolePermit(String roleId, String permitCode) {
        this.roleId = roleId;
        this.permitCode = permitCode;
    }

    /** 流水号 */
    public String getId(){
        return this.id;
    }
    /** 流水号 */
    public void setId(String id){
        this.id = id;
    }
    /** 角色ID */
    public String getRoleId(){
        return this.roleId;
    }
    /** 角色ID */
    public void setRoleId(String roleId){
        this.roleId = roleId;
    }
    /** 权限代码 */
    public String getPermitCode(){
        return this.permitCode;
    }
    /** 权限代码 */
    public void setPermitCode(String permitCode){
        this.permitCode = permitCode;
    }
    /** 权限类型 */
    public String getPermitType(){
        return this.permitType;
    }
    /** 权限类型 */
    public void setPermitType(String permitType){
        this.permitType = permitType;
    }
    /** 关联类型 */
    public String getOwnerType(){
        return this.ownerType;
    }
    /** 关联类型 */
    public void setOwnerType(String ownerType){
        this.ownerType = ownerType;
    }
    /** 乐观锁 */
    public Integer getRevision(){
        return this.revision;
    }
    /** 乐观锁 */
    public void setRevision(Integer revision){
        this.revision = revision;
    }
    /** 创建人 */
    public String getCreatedBy(){
        return this.createdBy;
    }
    /** 创建人 */
    public void setCreatedBy(String createdBy){
        this.createdBy = createdBy;
    }
    /** 创建时间 */
    public Date getCreatedTime(){
        return this.createdTime;
    }
    /** 创建时间 */
    public void setCreatedTime(Date createdTime){
        this.createdTime = createdTime;
    }
    /** 更新人 */
    public String getUpdatedBy(){
        return this.updatedBy;
    }
    /** 更新人 */
    public void setUpdatedBy(String updatedBy){
        this.updatedBy = updatedBy;
    }
    /** 更新时间 */
    public Date getUpdatedTime(){
        return this.updatedTime;
    }
    /** 更新时间 */
    public void setUpdatedTime(Date updatedTime){
        this.updatedTime = updatedTime;
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof RolePermit) {
            RolePermit thatRolePermit = (RolePermit)that;
            return roleId.equals(thatRolePermit.getRoleId()) && permitCode.equals(thatRolePermit.getPermitCode());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, permitCode);
    }
}
