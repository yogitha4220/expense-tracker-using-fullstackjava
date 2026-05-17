package com.example.expense_tracker;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseRepository repository;

    public ExpenseController(ExpenseRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Expense> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Expense create(@RequestBody Expense expense) {
        if (expense.getDate() == null) {
            expense.setDate(java.time.LocalDate.now());
        }
        return repository.save(expense);
    }

    // 🔹 NEW: update existing expense
    @PutMapping("/{id}")
    public Expense update(@PathVariable Long id, @RequestBody Expense updated) {
        return repository.findById(id)
                .map(expense -> {
                    expense.setDescription(updated.getDescription());
                    expense.setAmount(updated.getAmount());
                    expense.setCategory(updated.getCategory());
                    expense.setDate(updated.getDate());
                    return repository.save(expense);
                })
                .orElseThrow(() -> new RuntimeException("Expense not found"));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
