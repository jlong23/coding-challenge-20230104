# coding-challenge-20230104
Stellar IT Coding Challenge
# Challenge Request
Coding Challenge:
Problem:
A retailer offers a rewards program to its customers awarding points based on each recorded
purchase as follows:

For every dollar spent over $50 on the transaction, the customer receives one point.
In addition, for every dollar spent over $100, the customer receives another point.
Ex: for a $120 purchase, the customer receives
(120 - 50) x 1 + (120 - 100) x 1 = 90 points

Given a record of every transaction during a three-month period, calculate the reward points
earned for each customer per month and total.
 - Make up a data set to best demonstrate your solution
 - Check solution into GitHub and please share the GitHub URL.

Write a REST API that calculates and returns the reward points in Java using Spring Boot as a
microservice.

Additional Notes:

- All of the code should be provided in GitHub. Check-in all the code into master branch and
squash and re-order all commits if necessary to have clean purposeful commit messages like you
would normally in your job.
- Include a dataset in a table format that shows the customer list, transaction list, monthly total per
customer, and 3 months summary it in, and add an image inside the README.md file with this
data set and results.
- The client would be assessing your Coding Style and your personality when you join as a
developer in their team, not just whether the code runs or logic is correct. So, please follow all of
the Best practices that you know and have implemented in the past while doing this exercise.
- Please pay attention to the way you name your variables, classes, name of your project and
package structure etc. Do not do silly mistakes like declaring variables but not using your
variables anywhere in the code or using something from the Target folder.
- Externalize all the dependencies of your code nicely into property file or resource files in
appropriate locations in package layout.
- Do NOT copy paste from GitHub, Stack Overflow or Leetcode. We run a plagiarism check and
automatically reject all copy past jobs. Taking inspiration from something online is fine but blatant
copy paste or renaming variables after copying and pasting will be very easily flagged by our software.
So, please do it on your own.
- You API should contain clear Documentation (a well formatted README.md and comments in
code, sample Request Response formats, how to invoke your API by any client), a Health Check
Endpoint to check whether the service is running or not by a monitoring tool.
- Write your own Unit Tests and provide instructions on How to run your Tests in your
README.md file
- Show your ability to write well maintainable code by including custom Exception Handling (ability
to identify problems within code easily by throwing custom Errors), custom Logging (different
levels like Verbose etc)
- Additional brownie points if you can containerize your API with Dockerfile and provide instructions
on How to run your dockerized API in REAMDE.md

# Challenge Response

Given the requirements, I've build the following Request Structure to be sent to the API as JSON Body Payload

'''code
POST http://localhost:8080/api/rewards/calculate_period
'

Given a Array of TransactionSummaryVO
**TransactionSummaryVO**
FIELD|TYPE|
-----|:---
transactionId|STRING
transactionDate|ISO-8601 As 'yyyy-MM-dd'T'hh:mm:ss.sssZ'
transactionTotal|DOUBLE 2 Decimal Places expected

'''json
[
{"transactionId": "A", "transactionDate": "2023-10-04T12:00:00.000+0000", "transactionTotal": 120.00 },
{"transactionId": "B", "transactionDate": "2023-10-04T12:00:00.000+0000", "transactionTotal": 2 },
{"transactionId": "C", "transactionDate": "2023-09-04T12:00:00.000+0000", "transactionTotal": 120.00 },
{"transactionId": "D", "transactionDate": "2023-09-04T12:00:00.000+0000", "transactionTotal": 2 },
{"transactionId": "E", "transactionDate": "2023-08-04T12:00:00.000+0000", "transactionTotal": 120.00 },
{"transactionId": "F", "transactionDate": "2023-08-04T12:00:00.000+0000", "transactionTotal": 2 },
{"transactionId": "G", "transactionDate": "2023-07-04T00:00:00.000+0000", "transactionTotal": 120.00 },
{"transactionId": "H", "transactionDate": "2023-07-04T00:00:00.000+0000", "transactionTotal": 2 }
]
'''

With a Response of JSON as follows for that dataset

**RewardsVO**
FIELD|TYPE
-----|:---
periodStart|ISO-8601 As 'yyyy-MM-dd'T'hh:mm:ss.sssZ'
periodEnd|ISO-8601 As 'yyyy-MM-dd'T'hh:mm:ss.sssZ'
rewardsPointsTotal|INTEGER
rewardsPointsPeriods|ARRAY of RewardsPeriodVO

**RewardsPeriodVO**
FIELD|TYPE|
-----|:---
periodStart|ISO-8601 As 'yyyy-MM-dd'T'hh:mm:ss.sssZ'
periodEnd|ISO-8601 As 'yyyy-MM-dd'T'hh:mm:ss.sssZ'
totalTranslationsAmount|DOUBLE
totalTransactions|INTEGER
accumulatedRewards|INTEGER

'''json
{
"periodStart": "2023-07-04T05:00:00.000+00:00",
"periodEnd": "2023-10-05T04:59:59.999+00:00",
"rewardsPointsTotal": 270,
"rewardsPointsPeriods": [
{
"periodStart": "2023-10-01T05:00:00.000+00:00",
"totalTranslationsAmount": 122.0,
"totalTransactions": 2,
"accumulatedRewards": 90
},
{
"periodStart": "2023-09-01T05:00:00.000+00:00",
"totalTranslationsAmount": 122.0,
"totalTransactions": 2,
"accumulatedRewards": 90
},
{
"periodStart": "2023-08-01T05:00:00.000+00:00",
"totalTranslationsAmount": 122.0,
"totalTransactions": 2,
"accumulatedRewards": 90
}
]
}
'''

#Code Rationalization
While not provided in the challenge, I saw a need to ensure we can extend the calculation engine to support windowed 
and recalculation of transaction sets for the windowed total of rewards points.  The Calculation is very strict for 
whole monetary units, for point calculations.  Care was also taken for the window to be Start of the Day 3 months from 
today, a sliding window that would have rewards expire do to the edge conditions of date comparisons.

OpenAPI Annotations have been added to all objects exposed through the api, though I did not have time to enable the 
swagger rendering services.

A Postman Project has been provided in the ./Postman folder with sample data to use.

JUnit Tests are provided for positive and negative testing to ensure accurate rewards calculation.
