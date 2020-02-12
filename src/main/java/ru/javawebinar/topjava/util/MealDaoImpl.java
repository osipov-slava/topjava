package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.List;

public interface MealDaoImpl {
    void add(Meal meal);
    void delete(int userId);
    void update(Meal meal);
    List<MealTo> getAll();
    Meal getById(int mealId);
}
