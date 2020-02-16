package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    // null if not found, when updated
    public Meal save(Integer userId, Meal meal){
        return repository.save(userId, meal);
    }

    // false if not found
    public boolean delete(Integer userId, int id){
        return checkNotFoundWithId(repository.delete(userId, id), id);
    }

    // null if not found
    public Meal get(Integer userId, int id){
        return checkNotFoundWithId(repository.get(userId, id), id);
    }

    public Collection<Meal> getAll(Integer userId){
        return repository.getAll(userId);
    }

    public Collection<Meal> getAllBetweenDates(Integer userId, LocalDate startDate, LocalDate endDate){
        return repository.getAllBetweenDates(userId, startDate, endDate);
    }
}