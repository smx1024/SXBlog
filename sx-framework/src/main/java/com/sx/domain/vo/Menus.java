package com.sx.domain.vo;

import com.sx.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menus {
    private List<Menu> menus;
    private String checkedKeys;
 }
