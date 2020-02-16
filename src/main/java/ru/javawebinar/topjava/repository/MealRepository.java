package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealRepository {
    // null if not found, when updated
    Meal save(Meal meal);

    // false if not found
    boolean delete(String currentUser, int id);

    // null if not found
    Meal get(String currentUser, int id);

    Collection<Meal> getAll(String currentUser);
}
