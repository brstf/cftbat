# Clojure for the Brave and True: Chapter 3 Exercises

A gentle introduction to Clojure syntax, data structures, functions (including anonymous functions), and a discussion on sequences.

## The Exercises

1. Use the `str`, `vector`, `list`, `hash-map`, and `hash-set` functions
2. Write a function that takes a number and adds 100 to it.
3. Write a function, `dec-maker` that creates a function that subtracts a number from it's input:

        (def dec9 (dec-maker 9))
        (dec9 10)
        ; => 1

4. Write a function `mapset`, that works exactly like map but returns a set.
5. Create a function that's similar to `symmetrize-body-parts` but works for an alien with with 5 arms, legs, etc.
6. Generalize the function in Exercise 5 to work with any given number of symmetry.