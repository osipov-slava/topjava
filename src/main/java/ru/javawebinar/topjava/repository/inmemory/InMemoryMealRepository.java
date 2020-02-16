package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(String currentUser, int id) {
        return repository.get(id).getUser().equals(currentUser) && repository.remove(id) != null;
    }

    @Override
    public Meal get(String currentUser, int id) {
        return repository.get(id).getUser().equals(currentUser) ? repository.get(id) : null;
    }

    @Override
    public Collection<Meal> getAll(String currentUser) {
        return repository.values().parallelStream()
                .filter(u -> u.getUser().equals(currentUser))
                .sorted(Comparator.comparing(Meal::getDate))
                .collect(Collectors.toList());
    }
}

