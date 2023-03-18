package com.sx.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.sx.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TreeSelect {

    private List<Menu> children;
    //菜单ID@TableId
    private Long id;
    //父菜单ID
    private Long parentId;
    String menuName;
    String label;
}
