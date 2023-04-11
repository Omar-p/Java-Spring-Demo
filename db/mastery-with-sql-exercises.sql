/**
  3.34, 3.36, extract(), date_part(), BETWEEN inclusive
 */

-- 3.5 Write a query to obtain the length of each customer’s first name (remember to look for string functions in the documentation that can help)
SELECT first_name,
       length(first_name)
FROM customer;

-- 3.6 Write a query to return the initials for each customer
SELECT first_name,
       last_name,
       concat(substr(first_name, 1, 1), substr(last_name, 1, 1))
FROM customer;

-- 3.7 Each film has a rental_rate, which is how much money it costs for a customer to rent out the film. Each film also has a replacement_cost, 
-- which is how much money the film costs to replace. Write a query to figure out how many times each film must be rented out to cover its replacement cost.
SELECT rental_rate,
       replacement_cost	,
       ceil(replacement_cost / rental_rate) "#of rentals to break-even"
FROM film;


-- 3.8 Write a query to list all the films with a ‘G’ rating
SELECT title,
       rating
FROM film
WHERE rating = 'G';


-- 3.9 List all the films longer than 2 hours (note each film has a length in minutes)
SELECT title,
       length
FROM film
WHERE length > 120;

-- 3.10 Write a query to list all the rentals made before June, 2005
SELECT rental_id,
       rental_date
FROM rental
WHERE rental_date < '2005-06-01'::date;

-- 3.11 In Exercise 3.7, you wrote a query to figure out how many times each film must be rented out to cover its replacement cost. 
-- Now write a query to return only those films that must be rented out more than 30 times to cover their replacement cost.
SELECT rental_rate,
       replacement_cost	,
       ceil(replacement_cost / rental_rate) "#of rentals to break-even"
FROM film
WHERE ceil(replacement_cost / rental_rate) > 30;

-- 3.12 Write a query to show all rentals made by the customer with ID 388 in 2005
SELECT rental_id,
       rental_date
FROM rental
WHERE customer_id = 388 AND extract('Year' FROM rental_date) = 2005;

-- 3.13 We’re trying to list all films with a length of an hour or less. Show two different ways to fix our query below that isn’t working (one using the NOT keyword, and one without)
SELECT title,
       length
FROM film
WHERE length <= 60;

SELECT title,
       length
FROM film
WHERE NOt length > 60;


-- 3.15 Write a single query to show all rentals where the return date is greater than the rental date, or the return date is equal to the 
-- rental date, or the return date is less than the rental date. How many rows are returned? Why doesn’t this match the number of rows in the table overall?
-- not match because null value in rental date or return date will make the where clause return unkowun so they will not be in the result
SELECT rental_id,
       rental_date,
       return_date
FROM rental
WHERE return_date > rental_date OR
      return_date = rental_date OR
      return_date < rental_date;


-- 3.16 Write a query to list the rentals that haven’t been returned
SELECT rental_id,
       rental_date,
       return_date
FROM rental
WHERE return_date IS NULL;



-- 3.17 Write a query to list the films that have a rating that is not ‘G’ or ‘PG’
SELECT title,
       rating
FROM film
WHERE rating IS NULL OR
      (rating <> 'G' AND rating <> 'PG');



-- 3.18 Write a query to return the films with a rating of ‘PG’, ‘G’, or ‘PG-13’
SELECT title,
       rating
FROM film
WHERE rating = 'PG-13' OR
      rating = 'G' OR
      rating = 'PG';


-- 3.19 Write a query equivalent to the one below using BETWEEN.


-- 3.20 Write a query to return all film titles that end with the the word “GRAFFITI”
SELECT title
FROM film
WHERE title LIKE '%GRAFFITI';



-- 3.21 In exercise 3.17 you wrote a query to list the films that have a rating that is not ‘G’ or ‘PG’. Re-write this query using NOT IN. Do your results include films with a NULL rating?
SELECT title,
       rating
FROM film
WHERE rating NOT IN ('G', 'PG');



-- 3.22 Write a query the list all the customers with an email address. Order the customers by last name descending
SELECT customer_id,
       email,
       last_name
FROM customer
ORDER BY last_name DESC;


-- 3.23 Write a query to list the country id’s and cities from the city table, first ordered by country id ascending, then by city alphabetically.
SELECT country_id,
       city
FROM city
ORDER BY country_id, city;

-- 3.24 Write a query to list actors ordered by the length of their full name
-- ("[first_name] [last_name]") descending.
SELECT first_name,
       last_name,
       length(first_name || '' || last_name) AS full_name_length
FROM actor
ORDER BY full_name_length DESC ;

-- 3.25 Describe the difference between ORDER BY x, y DESC and ORDER BY x DESC, y DESC
-- (where x and y are columns in some imaginary table you’re querying)
-- A: the first one will order by x ascending and y descending and the second one will order by x descending and y descending




-- 3.27 Write a query to return the 3 most recent payments received
SELECT payment_id,
       amount,
       payment_date
FROM payment
ORDER BY payment_date DESC
LIMIT  3;

-- 3.28 Return the 4 films with the shortest length that are
-- not R rated. For films with the same length, order them alphabetically
SELECT title,
       length,
       rating
FROM film
WHERE rating <> 'R'
ORDER BY length, title
LIMIT 4;

-- 3.29 Write a query to return the last 3 payments made in January, 2007
SELECT payment_id,
       amount,
       payment_date
FROM payment
WHERE date_part('Year', payment_date) = 2007 AND date_part('Month', payment_date) = 1
ORDER BY payment_date DESC
LIMIT  3;

-- 3.30 Can you think of a way you could, as in the previous exercise, return the
-- last 3 payments made in January, 2007 but have those same 3 output rows ordered
-- by date ascending? (Don’t spend too long on this…)
SELECT T.payment_id,
       T.amount,
       T.payment_date

FROM (SELECT payment_id,
             amount,
             payment_date
      FROM payment
      WHERE date_part('Year', payment_date) = 2007 AND date_part('Month', payment_date) = 1
      ORDER BY payment_date DESC
      LIMIT  3
) AS T

ORDER BY T.payment_date;




-- 3.31 Write a query to return all the unique ratings films can have,
-- ordered alphabetically (not including NULL)
SELECT DISTINCT rating
FROM film
WHERE rating IS NOT NULL
ORDER BY rating;

-- 3.32 Write a query to help us quickly see if there is any hour of the day
-- that we have never rented a film out on (maybe the staff always head out for lunch?)
SELECT DISTINCT date_part('Hour', return_date) AS rental_hour
FROM rental
ORDER BY rental_hour;


-- 3.33 Write a query to help quickly check whether the same rental rate is used for each rental duration
-- (for example - is the rental rate always 4.99 when the rental duration is 3?)
SELECT DISTINCT rental_duration,
                rental_rate
FROM film
ORDER BY rental_duration;


-- 3.34 Can you explain why the first query below works, but the second one,
-- which simply adds the DISTINCT keyword, doesn’t? (this is quite challenging)
/*
 In the second query, multiple rows of actors are combined in to a single row due to the use of DISTINCT.
 For example, there are two actors with the first name ADAM (ADAM HOPPER and ADAM GRANT), however after the
 SELECT DISTINCT clause has been processed, there is only one row with first name ADAM. Ordering then by last
 name is undefined - eg. In the case of ADAM, Postgres has no way to know which last name should be used
 (HOPPER or GRANT?). In general, avoid ordering by columns you haven’t selected and you can sidestep complex
 situations like this.
 */
select first_name
from actor
order by last_name;

select distinct first_name
from actor
order by last_name;

-- 3.35 Write a query to return an ordered list of distinct ratings for films in our films table
-- along with their descriptions (you will have to type in the descriptions yourself)
SELECT DISTINCT rating,
                CASE rating
                    WHEN 'R' then 'rating is R'
                    WHEN 'PG-13' then 'rating is PG-13'
                    WHEN 'NC-17' then 'rating is NC-17'
                    WHEN 'G' then 'rating is G'
                    WHEN 'PG' then 'rating is PG'
                    ELSE 'UNKNOWN'
                END AS description
FROM film;

-- 3.36 Write a query to output ‘Returned’ for returned rentals and ‘Not Returned’ for rentals that haven’t
-- been returned. Order the output to show those not returned first.
SELECT rental_id,
       CASE
           WHEN return_date IS NULL THEN 'Not Returned'
           WHEN return_date IS NOT NULL THEN 'Returned'
       END
FROM rental;


-- 3.37 Imagine you were asked to write a query to populate a ‘country picker’ for some internal company
-- dashboard. Write a query to return the countries in alphabetical order, but also with the twist that the
-- first 3 countries in the list must be 1) Australia 2) United Kingdom 3) United States and then normal alphabetical
-- order after that (maybe you want them first because, for example, most of your customers come from these countries)
-- [cannot get the solution]
SELECT country
FROM country
ORDER BY
    CASE country
        WHEN 'Australia' THEN 0
        WHEN 'United Kingdom' THEN 1
        WHEN 'United States' THEN 2
        ELSE 3
    END,
    country;

-- 3.38 We want to give a prize to 5 random customers each month. Write a query that will return 5 random customers
-- each time it is run (you may find a particular math function helpful - make sure to search the documentation!)
-- [cannot get the solution]
-- https://stackoverflow.com/q/8674718/14840351
SELECT first_name,
       last_name,
       email
FROM customer
ORDER BY random()
LIMIT 5;


-- 3.39 Give 3 different solutions to list the rentals made in June, 2005. In one solution, use the date_part function.
-- In another, use the BETWEEN keyword. In the third, don’t use either date_part or BETWEEN.
SELECT rental_id,
       return_date
FROM rental
WHERE date_part('Year', rental_date) = '2005' AND date_part('Month', rental_date) = '06';

SELECT rental_id,
       return_date
FROM rental
WHERE rental_date BETWEEN '2005-06-01' AND '2005-06-30';

SELECT rental_id,
       return_date
FROM rental
WHERE  extract('Year' FROM rental_date) = 2005 AND  extract('Month' FROM rental_date) = 06;


-- 3.40 Return the top 5 films for $ per minute (rental_rate / length) of entertainment[didn't understand the question]
SELECT title,
       length,
       rental_rate,
       rental_rate / length as per_minute
FROM film
WHERE length <> 0
ORDER BY per_minute DESC
LIMIT 5;

-- 3.41 Write a query to list all customers who have a first name
-- containing the letter ‘A’ twice or more
SELECT first_name
FROM customer
WHERE first_name LIKE '%A%A%';

-- 3.42 PostgreSQL supports an interesting variation of DISTINCT called DISTINCT ON. Visit the official documentation page
-- and read about DISTINCT ON. See if you can figure out how you would use it in a query to return the most recent rental
-- for each customer
-- Table(s) to use: rental
SELECT DISTINCT ON (customer_id, rental_date)
FROM rental
ORDER BY customer_id, rental_date DESC;


-- 3.43 Write a query to list all the customers with an email address but
-- not in the format [first_name].[last_name]@sakilacustomer.org
SELECT first_name,
       last_name,
       email
FROM customer
WHERE email <> first_name || '.' || last_name || '@sakilacustomer.org';


-- 4.1 Write a query to return the total count of customers in the customer table and
-- the count of how many customers provided an email address
SELECT count(*) AS "# CUSTOMERS",
       count(email) AS "# CUSTOMERS WHO PROVIDE AN EMAIL"
FROM customer;

-- 4.2 Building on the previous exercise, now return an additional result showing the
-- percentage of customers with an email address (as a helpful hint, if you’re getting 0
-- try multiplying the fraction by 100.0
-- - we’ll examine why this is necessary in an upcoming chapter on data types)
SELECT count(*) AS "# CUSTOMERS",
       count(email) AS "# CUSTOMERS WHO PROVIDE AN EMAIL",
       count(email) * 100.0 / COUNT(*) AS "% of customers with an email"
FROM customer;

-- 4.3 Write a query to return the number of distinct customers who have made payments
-- Table(s) to use: payment
SELECT count(DISTINCT customer_id)  AS "# distinct customers who have made payments"
FROM payment;

-- 4.4 What is the average length of time films are rented out for
-- Table(s) to use: rental
SELECT avg(return_date - rental_date)
FROM rental;


-- 4.5 Write a query to return the sum total of all payment amounts received
-- Table(s) to use: payment
SELECT sum(amount)
FROM payment;

-- 4.6 List the number of films each actor has appeared in and order the results from most popular to least
-- Table(s) to use: film_actor
SELECT  actor_id,
        count(film_id)
FROM film_actor
GROUP BY actor_id
ORDER BY count(film_id) DESC ;
-- 4.7 List the customers who have made over 40 rentals
-- Table(s) to use: rental
SELECT customer_id,
       count(rental_id)
FROM rental
GROUP BY customer_id
HAVING count(rental_id) > 40
ORDER BY count(rental_id) DESC ;

-- 4.8 We want to compare how the staff are performing against each other on a month to month basis. So
-- for each month and year, show for each staff member how many payments they handled, the total amount
-- of money they accepted, and the average payment amount
-- Table(s) to use: payment
SELECT date_part('Year', payment_date) "YEAR",
       date_part('Month', payment_date) "MONTH",
       staff_id,
       count(*) "# PAYMENTS",
       sum(amount) "TOTAL",
       avg(amount) "AVG"
FROM payment
GROUP BY 1,2,3
ORDER BY 1,2,3;


-- 4.9 Write a query to show the number of rentals that were returned within 3 days, the number returned
-- in 3 or more days, and the number never returned (for the logical comparison check you can use the following
-- code snippet to compare against an interval: where return_date - rental_date < interval ‘3 days’)
-- Table(s) to use: rental
SELECT
    case
        when return_date - rental_date < interval '3 days' then 'within 3 days'
        when return_date - rental_date >= interval '3 days' then '3 days or more'
        else 'not returned'
end,
    count(*)
FROM rental
GROUP BY 1
ORDER BY 1;

-- 4.10 Write a query to give counts of the films by their length in groups of 0 - 1hrs, 1 - 2hrs, 2 - 3hrs,
-- and 3hrs+ (note: you might get slightly different numbers if doing inclusive or exclusive grouping -
-- but don’t sweat it!)
-- Table(s) to use: film
SELECT
    case
        when length <= 60 then '0 - 1hrs'
        when length > 60 and length <= 120 then '1 - 2hrs'
        when length > 120 and length <= 180 then '2 - 3hrs'
        when length > 180 then '3hrs+'
        end ,
    count(*)
FROM film
GROUP BY 1;

-- 4.11 Explain why in the following query we obtain two different results for the average
-- film length. Which one is correct?
-- A) avg excludes null column, sum / count -> count(*) will include row with null value in amount;
select
            1.0 * sum(length) / count(*) as avg1,
            1.0 * avg(length) as avg2
from film;

-- 4.12 Write a query to return the average rental duration for each customer in descending order
SELECT customer_id,
       avg(return_date - rental_date)
FROM rental
GROUP BY customer_id
ORDER BY avg(return_date - rental_date) DESC ;

-- 4.13 Return a list of customer where all payments they’ve made have been over $2 (lookup the bool_and
-- aggregate function which will be useful here)
-- Table(s) to use: payment
SELECT customer_id,
       count(*) filter ( where amount > 2 ) "# payment over 2$"
FROM payment
GROUP BY customer_id;

-- 4.14 As a final fun finish to this chapter, run the following query to see a cool way you can generate
-- ascii histogram charts. Look up the repeat function (you’ll find it under ‘String Functions and Operators’)
-- to see how it works and change the output character…and don’t worry, I’ll explain the ::int bit in the next chapter!
--
select rating, repeat('*', (count(*) / 10)::int)
from film
where rating is not null
group by rating;

