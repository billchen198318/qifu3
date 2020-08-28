/*
 * Copyright 2019-2021 qifu of copyright Chen Xin Nien
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * -----------------------------------------------------------------------
 *
 * author: 	Chen Xin Nien
 * contact: chen.xin.nien@gmail.com
 *
 */
package org.qifu.core.model;

import org.qifu.base.model.BaseUserInfo;
import org.qifu.base.model.YesNo;
import org.qifu.core.entity.TbRole;
import org.qifu.core.entity.TbUserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class User extends BaseUserInfo implements UserDetails {

    private String oid;
    private String username;
    private String password;
    private List<TbUserRole> roles;
    private String onJob;

    public User(String username, String password, String onJob, List<TbUserRole> roles) {
        this.username = username;
        this.password = password;
        this.onJob = onJob;
        this.roles = roles;
        this.setUserId( this.username );
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
        for (TbUserRole userRole : this.roles) {
            auths.add( new SimpleGrantedAuthority(userRole.getRole()) );
        }
        return auths;
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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return (YesNo.YES.equals(this.onJob) ? true : false);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return (YesNo.YES.equals(this.onJob) ? true : false);
    }
    
}