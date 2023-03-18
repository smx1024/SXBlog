package com.sx.domain.vo;

import com.sx.domain.entity.Role;
import com.sx.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Userbyid {
    private List<Long> roleIds;
    private List<Role> roles;
    private UserInfoVo userInfoVo;
}
