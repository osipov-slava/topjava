package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
//              UserId       MealId
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(2, meal));
        MealsUtil.MEALS.forEach(meal -> {
            save(3, new Meal(meal.getDateTime(), meal.getDescription(), meal.getCalories()));
        });
    }

    @Override
    public Meal save(Integer userId, Meal meal) {
        if (!repository.containsKey(userId)) repository.put(userId, new HashMap<>());

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.get(userId).put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.get(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(Integer userId, int id) {
        if (!repository.containsKey(userId)) return false;
        return repository.get(userId).remove(id) != null;
    }

    @Override
    public Meal get(Integer userId, int id) {
        if (!repository.containsKey(userId)) return null;
        return repository.get(userId).get(id);
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        if (!repository.containsKey(userId)) return new ArrayList<>();
        return repository.get(userId).values().parallelStream()
                .sorted((m1,m2) -> -m1.getDate().compareTo(m2.getDate()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getAllBetweenDates(Integer userId, LocalDate startDate, LocalDate endDate) {
        if (!repository.containsKey(userId)) return new ArrayList<>();
        return repository.get(userId).values().parallelStream()
                .filter(meal -> meal.getDate().compareTo(startDate) >=0 && meal.getDate().compareTo(endDate) <=0)
                .sorted((m1,m2) -> -m1.getDate().compareTo(m2.getDate()))
                .collect(Collectors.toList());
    }
}

