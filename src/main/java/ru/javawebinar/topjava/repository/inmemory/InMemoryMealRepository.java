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

    //           UserId
    private Map<Integer, Set<Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    static class ComparatorByDateTime implements Comparator<Meal> {
        public int compare(Meal a,Meal b) {
            return b.getDateTime().compareTo(a.getDateTime());
        }
    }

    {
        MealsUtil.MEALS.forEach(meal -> save(2, meal));
        MealsUtil.MEALS.forEach(meal -> {
            save(3, new Meal(meal.getDateTime(), meal.getDescription(), meal.getCalories()));
        });
    }

    @Override
    public Meal save(Integer userId, Meal meal) {
        if (!repository.containsKey(userId))
            repository.put(userId, new TreeSet<Meal>(new ComparatorByDateTime()));

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        } else {
            // если update
            delete(userId, meal.getId());
        }
        repository.get(userId).add(meal);
        return meal;
    }

    @Override
    public boolean delete(Integer userId, int id) {
        boolean result = false;
        if (!repository.containsKey(userId)) return result;
        for (Meal m: repository.get(userId)) {
            if (id==m.getId()) {
                repository.get(userId).remove(m);
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public Meal get(Integer userId, int id) {
        if (!repository.containsKey(userId)) return null;

        return repository.get(userId).stream()
                .filter(p->p.getId()==id)
                .findFirst().get();
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        if (!repository.containsKey(userId)) return new ArrayList<>();
        return new ArrayList<>(repository.get(userId));
    }

    @Override
    public Collection<Meal> getAllBetweenDates(Integer userId, LocalDate startDate, LocalDate endDate) {
        if (!repository.containsKey(userId)) return new ArrayList<>();
        return repository.get(userId).parallelStream()
                .filter(meal -> meal.getDate().compareTo(startDate) >=0 && meal.getDate().compareTo(endDate) <=0)
                .sorted((m1,m2) -> -m1.getDate().compareTo(m2.getDate()))
                .collect(Collectors.toList());
    }
}

