package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 根据ID删除分类，删除之前要进行判断
     * @param id
     */
    public void remove(Long id){
        //-------------------------------查询当前分类是否关联了菜品，如果已关联，抛出业务异常----------------------

        //构造查询器
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类ID查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        //存储关联的个数
        int count1 = dishService.count(dishLambdaQueryWrapper);

        if(count1 > 0){
            //已关联,抛出异常
            throw new CustomException("当前分类下关联了菜品，不能删除");

        }
        //-------------------------------查询当前分类是否关联了套餐，如果已关联，抛出业务异常----------------------

        //构造查询器
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据套餐ID查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        //存储关联的个数
        int count2 = setmealService.count(setmealLambdaQueryWrapper);

        if(count2 > 0){
            //已关联,抛出异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        //-------------------------------执行到这里表示正常，正常删除分类---------------------------------------
        super.removeById(id);
    }
}
