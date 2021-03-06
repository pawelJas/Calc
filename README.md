Given Task:

Write a simple scientific calculator app with the following features:
The calculator can do calculations in both infix and postfix (RPN) notation.
The calculator supports parentheses in infix notation.
The calculator supports addition, subtraction, multiplication, division, logarithms (log and ln), trigonometric functions (sin, cos, tan, ctan).
The calculator should work with the known constants of Archimedes’ constant  (Pi) and Euler’s number e (e).
The calculator can solve simple linear equations with a single variable (namely, x or y), for simplicity, only addition, subtraction, multiplication and division are allowed.
The calculator should have a language parser.
Do not use any library that can accomplish any of the listed requirements.
The calculator should handle all error cases properly (by carefully indicating the errors to the user).
Write tests.

Approach:

Javafx has been used to implement the challenge. I have used IntelliJ and IntelliJ's build in files to organise, build and run the project.

As agreed the UI interface is very basic. I have put the main focus on the functionality, language parser and the error reporting.

All expected functionalities have been implemented as various Expressions.
Expressions are built in the form of a tree with a root expression representing the calculator input.
The Expression building and evaluation is done in phases:
- First the SymbolParser decomposes the String into Symbol
- Secondly the RpnParser or InfixParser builds the Expression tree
- Lastly the expressions are evaluated.


Expressions are not evaluated until the parent expression requests it.
The above help in analyzing the errors and setting the error priorities. 
There are 3 main group of errors based on the language parsing layers:
- Parsing Error
- Expression building error
- Expression evaluation error


Examples:

input:   (3+(4-1))*5

output: 30

input:   2 * x + 0.5 = 1

output: x = 0.25

input:   2 * x + 1 = 2*(1-x)

output: x = 0.25

input: Log(10)

output: 1

input: Log10

output: 1

input: Log100(10)

output: 0.5

input: sin(pi) or sinpi

output: 0

input: sin(1.5pi) or sin(1.5*pi)

output: -1
